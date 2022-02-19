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
package com.xceptance.xlt.api.report;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.xceptance.xlt.api.engine.Data;
import com.xceptance.xlt.api.engine.TransactionData;
import com.xceptance.xlt.report.PostprocessedDataContainer;

/**
 * The {@link AbstractReportProvider} class provides common functionality of a typical report provider.
 * 
 * @author Jörg Werner (Xceptance Software Technologies GmbH)
 */
public abstract class AbstractReportProvider implements ReportProvider
{
    /**
     * The report provider's configuration.
     */
    private ReportProviderConfiguration configuration;

    /**
     * locking
     */
    public final AtomicBoolean locked = new AtomicBoolean(false);
    
    /**
     * Returns the report provider's configuration. Use the configuration object to get access to general as well as
     * provider-specific properties stored in the global configuration file.
     * 
     * @return the report provider configuration
     */
    public ReportProviderConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfiguration(final ReportProviderConfiguration config)
    {
        configuration = config;
    }
    
    @Override
    public boolean lock()
    {
        return locked.compareAndSet(false, true);
    }

    @Override
    public void unlock()
    {
        locked.set(false);
    }
    
    public void processAll(final PostprocessedDataContainer dataContainer)
    {
        final List<Data> data = dataContainer.data;
        int size = data.size();
        int sampleFactor = dataContainer.sampleFactor;
        int droppedLines = dataContainer.droppedLines;

        int p = 1;
        for (; p < size; p = p + 1)
        {
            final Data d = data.get(p);
            processDataRecord(d);
        }
        
        if (droppedLines > 0)
        {
            for (int i = 0; i < size; i++)
            {
                final Data d = data.get(i);
            
                if (!(d instanceof TransactionData))
                {
                    for (int y = 1; y < sampleFactor; y++)
                    {
                        processDataRecord(d);
                    }
                    droppedLines--;
                    
                    if (droppedLines == 0)
                    {
                        break;
                    }
                }
            }
        }
    }
}
