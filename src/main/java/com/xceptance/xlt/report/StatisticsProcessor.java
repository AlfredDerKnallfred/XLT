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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.threadly.concurrent.wrapper.KeyDistributedExecutor;

import com.xceptance.common.util.SimpleArrayList;
import com.xceptance.common.util.concurrent.DaemonThreadFactory;
import com.xceptance.xlt.api.engine.Data;
import com.xceptance.xlt.api.report.ReportProvider;

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
    private long maximumTime;

    /**
     * Creation time of first data record.
     */
    private long minimumTime;

    /**
     * The configured report providers. An array for less overhead.
     */
    private final List<ReportProvider> reportProviders;

    /**
     * The thread pool for processing the statistics in providers in parallel
     * Just per provider, because they are not synchronized
     */
    private final KeyDistributedExecutor statisticsMaintenanceExecutor;

    /**
     * Constructor.
     *
     * @param reportProviders
     *            the configured report providers
     * @param dispatcher
     *            the dispatcher that coordinates result processing
     */
    public StatisticsProcessor(final List<ReportProvider> reportProviders, final int threadCount)
    {
        this.reportProviders = reportProviders;
        statisticsMaintenanceExecutor = new KeyDistributedExecutor(Executors.newFixedThreadPool(threadCount, new DaemonThreadFactory(c -> "Providers-" + c)));

        maximumTime = 0;
        minimumTime = Long.MAX_VALUE;
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
    public void process(final SimpleArrayList<Data> data)
    {
        final List<Future<?>> tasks = new ArrayList<>();

        /**
         * Capture the time stats independently because each provider
         * runs a loop too and we only have to do it once
         * 
         * Ensures that we only run this once due to the use of the
         * Threadly lib and the usage of this as sync key.
         */
        tasks.add(statisticsMaintenanceExecutor.submit(this, () -> 
        {
            maintainStatistics(data);
        }));

        /**
         * Create a task for each report provider and the full data set
         */
        for (int i = 0; i < reportProviders.size(); i++)
        {
            final ReportProvider reportProvider = reportProviders.get(i);

            tasks.add(statisticsMaintenanceExecutor.submit(reportProvider, () ->
            {
                try
                {
                    final int size = data.size();
                    
                    for (int d = 0; d < size; d++)
                    {
                        reportProvider.processDataRecord(data.get(d));
                    }
                }
                catch (final Throwable t)
                {
                    LOG.error("Failed to process data record, discarding full chunk", t);
                }
            }));
        }

        // ok, we have to wait till the last provider is fed
        for (int i = 0; i < tasks.size(); i++)
        {
            try
            {
                tasks.get(i).get();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Maintain our statistics
     *
     * @param data
     *            the data records
     */
    private void maintainStatistics(final List<Data> data)
    {
        long min = minimumTime;
        long max = maximumTime;

        // process the data
        final int size = data.size();
        for (int i = 0; i < size; i++)
        {
            // maintain statistics
            final long time = data.get(i).getTime();

            min = Math.min(min, time);
            max = Math.max(max, time);
        }

        minimumTime = min;
        maximumTime = max;
    }
}
