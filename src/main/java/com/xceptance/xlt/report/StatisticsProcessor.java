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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xceptance.xlt.api.engine.Data;
import com.xceptance.xlt.api.report.ReportProvider;
import com.xceptance.xlt.report.DataParserThread.PostprocessedDataContainer;

/**
 * Processes parsed data records. Processing means passing a data record to all configured report providers. Since data
 * processing is not thread-safe (yet), there will be only one data processor.
 */
class StatisticsProcessor
{
    /**
     * Class logger.
     */
    private static final Log LOG = LogFactory.getLog(StatisticsProcessor.class);

    /**
     * Creation time of last data record.
     */
    private long maximumTime = 0;

    /**
     * Creation time of first data record.
     */
    private long minimumTime = Long.MAX_VALUE;

    /**
     * The configured report providers. An array for less overhead.
     */
    private final List<ReportProvider> reportProviders;

    /**
     * Constructor.
     *
     * @param reportProviders
     *            the configured report providers
     */
    public StatisticsProcessor(final List<ReportProvider> reportProviders)
    {
        // filter the list and take only the provider that really need runtime parsed data
        this.reportProviders = reportProviders.stream().filter(p -> p.wantsDataRecords()).collect(Collectors.toList());
    }

    /**
     * Returns the maximum time.
     *
     * @return maximum time
     */
    public final long getMaximumTime()
    {
        return maximumTime;
    }

    /**
     * Returns the minimum time.
     *
     * @return minimum time
     */
    public final long getMinimumTime()
    {
        return (minimumTime == Long.MAX_VALUE) ? 0 : minimumTime;
    }

    /**
     * Take the post processed data and put it into the statitics machinery to
     * capture the final data points. 
     * 
     * @param data a chunk of post processed data for final statitics gathering
     */
    public void process(final PostprocessedDataContainer dataContainer)
    {
        // get your own list
        final Deque<ReportProvider> providerList = new ArrayDeque<>(reportProviders);

        /**
         * Create a task for each report provider and the full data set
         */
        final List<Data> data = dataContainer.getData();
        while (providerList.isEmpty() == false)
        {
            ReportProvider provider = null;
        
            int attemptsBeforeYielding = providerList.size();
            do 
            {
                provider = providerList.pollFirst();
                final boolean wasLocked = provider.lock();
                
                if (wasLocked == false)
                {
                    providerList.addLast(provider);
                    provider = null;
                    
                    if (attemptsBeforeYielding == 0)
                    {
                        attemptsBeforeYielding = 9;
                        Thread.yield(); 
                    }
                    else
                    {
                        attemptsBeforeYielding--;
                    }
                }
            }
            while (provider == null);
                
            try
            {
                provider.processAll(data);
            }
            catch (final Throwable t)
            {
                LOG.error("Failed to process data record, discarding full chunk", t);
            }
            finally
            {
                provider.unlock();
                attemptsBeforeYielding = 3; 
            }
        }

        // get the max and min
        synchronized (this)
        {
            minimumTime = Math.min(minimumTime, dataContainer.getMinimumTime());
            maximumTime = Math.max(maximumTime, dataContainer.getMaximumTime());
        }
    }

    //    /**
    //     * Maintain our statistics
    //     *
    //     * @param data
    //     *            the data records
    //     */
    //    private void maintainStatistics(final List<Data> data)
    //    {
    //        long min = minimumTime;
    //        long max = maximumTime;
    //
    //        // process the data
    //        final int size = data.size();
    //        for (int i = 0; i < size; i++)
    //        {
    //            // maintain statistics
    //            final long time = data.get(i).getTime();
    //
    //            min = Math.min(min, time);
    //            max = Math.max(max, time);
    //        }
    //
    //        minimumTime = min;
    //        maximumTime = max;
    //    }
}
