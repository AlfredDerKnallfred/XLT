/*
 * Copyright (c) 2005-2020 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.report;

import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xceptance.common.util.SimpleArrayList;
import com.xceptance.xlt.api.engine.ActionData;
import com.xceptance.xlt.api.engine.Data;
import com.xceptance.xlt.api.engine.PageLoadTimingData;
import com.xceptance.xlt.api.engine.RequestData;
import com.xceptance.xlt.api.engine.TransactionData;
import com.xceptance.xlt.report.mergerules.RequestProcessingRule;
import com.xceptance.xlt.report.mergerules.RequestProcessingRuleResult;

/**
 * Parses lines to data records and performs any data record preprocessing that can be done in parallel. Preprocessing
 * also includes executing request merge rules.
 */
class DataRecordParser implements Runnable
{
    /**
     * Class logger.
     */
    private static final Log LOG = LogFactory.getLog(DataRecordParser.class);

    /**
     * Pattern used to rename the name of Web driver timers generated by FF add-on.
     */
    private static final Pattern WD_TIMER_NAME_PATTERN = Pattern.compile("page_\\d+");

    /**
     * The data record factory.
     */
    private final DataRecordFactory dataRecordFactory;

    /**
     * The dispatcher that coordinates result processing.
     */
    private final Dispatcher dispatcher;

    /**
     * The start time of the report period. Data records generated outside this window will be ignored.
     */
    private final long fromTime;

    /**
     * The request processing rules.
     */
    private final List<RequestProcessingRule> requestProcessingRules;

    /**
     * The end time of the report period. Data records generated outside this window will be ignored.
     */
    private final long toTime;

    /**
     * Whether to automatically remove any indexes from the request name (i.e. "HomePage.1.27" -> "HomePage").
     */
    private final boolean removeIndexesFromRequestNames;

    /**
     * Constructor.
     *
     * @param dataRecordFactory
     *            the data record factory
     * @param fromTime
     *            the start time
     * @param toTime
     *            the end time
     * @param requestProcessingRules
     *            the request processing rules
     * @param dispatcher
     *            the dispatcher that coordinates result processing
     * @param removeIndexesFromRequestNames
     *            whether to automatically remove any indexes from request names
     */
    public DataRecordParser(final DataRecordFactory dataRecordFactory, final long fromTime, final long toTime,
                            final List<RequestProcessingRule> requestProcessingRules, final Dispatcher dispatcher,
                            boolean removeIndexesFromRequestNames)
    {
        this.dataRecordFactory = dataRecordFactory;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.requestProcessingRules = requestProcessingRules;
        this.dispatcher = dispatcher;
        this.removeIndexesFromRequestNames = removeIndexesFromRequestNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // get a chunk of lines
                final LineChunk lineChunk = dispatcher.getNextLineChunk();
                final List<String> lines = lineChunk.getLines();

                // parse the chunk of lines and preprocess the results
                final List<Data> dataRecordChunk = new SimpleArrayList<>(lines.size());

                int lineNumber = lineChunk.getBaseLineNumber();

                final int size = lines.size();
                for (int i = 0; i < size; i++)
                {
                    final Data data = parseLine(lines.get(i), lineNumber, lineChunk);
                    if (data != null)
                    {
                        final Data preprocessedData = preprocessDataRecord(data);
                        if (preprocessedData != null)
                        {
                            dataRecordChunk.add(preprocessedData);
                        }
                    }

                    lineNumber++;
                }

                // deliver the chunk of parsed data records
                dispatcher.addNewParsedDataRecordChunk(dataRecordChunk);
            }
            catch (final InterruptedException e)
            {
                break;
            }
        }
    }

    /**
     * Parses the given line to a data record.
     *
     * @param line
     *            the line to parse
     * @param lineNumber
     *            the number of the line in its file (for logging purposes)
     * @param lineChunk
     *            the line chunk the line belongs to
     * @return the parsed data record, or <code>null</code> if the line could not be parsed or the data record's
     *         timestamp was outside the configured time period
     */
    private Data parseLine(final String line, final int lineNumber, final LineChunk lineChunk)
    {
        try
        {
            // parse the data record
            final Data dataRecord = dataRecordFactory.createStatistics(line);

            // skip the data record if it was not generated in the given time period
            final long time = dataRecord.getTime();
            if (time < fromTime || time > toTime)
            {
                return null;
            }

            // set general fields
            dataRecord.setAgentName(lineChunk.getAgentName());
            dataRecord.setTransactionName(lineChunk.getTestCaseName());

            // set special fields / special handling
            if (dataRecord instanceof TransactionData)
            {
                final TransactionData td = (TransactionData) dataRecord;
                td.setTestUserNumber(lineChunk.getUserNumber());
            }
            else if (lineChunk.getCollectActionNames() && dataRecord instanceof ActionData)
            {
                // store the action name/time for later use
                lineChunk.getActionNames().put(dataRecord.getTime(), dataRecord.getName());
            }
            else if (lineChunk.getAdjustTimerNames() && (dataRecord instanceof RequestData || dataRecord instanceof PageLoadTimingData))
            {
                // rename web driver requests/custom timers using the previously stored action names
                final Entry<Long, String> entry = lineChunk.getActionNames().floorEntry(time);
                final String actionName = (entry != null) ? entry.getValue() : "UnknownAction";

                final Matcher m = WD_TIMER_NAME_PATTERN.matcher(dataRecord.getName());
                dataRecord.setName(m.replaceFirst(actionName));
            }

            return dataRecord;
        }
        catch (final Exception ex)
        {
            final String msg = String.format("Failed to parse data record at line %,d in file '%s': %s", lineNumber, lineChunk.getFile(),
                                             ex);
            LOG.error(msg);
            System.out.println(msg);

            return null;
        }
    }

    /**
     * Preprocesses the passed data record.
     *
     * @param data
     *            the data record
     * @return the preprocessed data record, or <code>null</code> if the data record is to be discarded
     */
    private Data preprocessDataRecord(final Data data)
    {
        if (data instanceof RequestData)
        {
            return processRequestData((RequestData) data);
        }
        else
        {
            return data;
        }
    }

    /**
     * Processes a request according to the configured request processing rules. Currently, this means renaming or
     * discarding requests.
     *
     * @param requestData
     *            the request data record
     * @return the processed request data record, or <code>null</code> if the data record is to be discarded
     */
    private RequestData processRequestData(RequestData requestData)
    {
        // fix up the name first (Product.1.2 -> Product) if so configured
        if (removeIndexesFromRequestNames)
        {
            // @TODO Chance for more performance here
            String requestName = requestData.getName();

            final int firstDotPos = requestName.indexOf(".");
            if (firstDotPos > 0)
            {
                requestName = requestName.substring(0, firstDotPos);
                requestData.setName(requestName);
            }
        }

        // remember the original name so we can restore it in case request processing fails
        final String originalName = requestData.getName();

        // execute all processing rules one after the other until processing is complete
        final int size = requestProcessingRules.size();
        for (int i = 0; i < size; i++)
        {
            final RequestProcessingRule requestProcessingRule = requestProcessingRules.get(i);

            try
            {
                final RequestProcessingRuleResult result = requestProcessingRule.process(requestData);

                requestData = result.requestData;

                if (result.stopRequestProcessing)
                {
                    break;
                }
            }
            catch (final Throwable t)
            {
                final String msg = String.format("Failed to apply request merge rule: %s\n%s", requestProcessingRule, t);
                LOG.error(msg);
                System.out.println(msg);

                // restore the request's original name
                requestData.setName(originalName);

                break;
            }
        }

        return requestData;
    }
}
