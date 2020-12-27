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

import java.lang.reflect.Constructor;
import java.util.Map;

import com.xceptance.common.lang.OpenStringBuilder;
import com.xceptance.common.util.XltCharBuffer;
import com.xceptance.xlt.api.engine.Data;

/**
 * Data classes hold processor for certain data types, such as Request, Transaction, Action, and
 * more. This is indicated in the logs by the first column of the record (a line), such as
 * A, T, R, C, and more. This can be later extended. The column is not limited to a single character
 * and can hold more, in case we run out of options sooner or later.
 */
public class DataRecordFactory
{
    /**
     * The registered data handlers per Data(Record) type. 
     */
    private final Constructor<? extends Data> ctrs[];
    private final int offset;

    /**
     * Setup this factory based on the config
     * 
     * @param dataClasses the data classes to support
     */
    public DataRecordFactory(final Map<String, Class<? extends Data>> dataClasses)
    {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (final Map.Entry<String, Class<? extends Data>> entry : dataClasses.entrySet())
        {
            char c = entry.getKey().charAt(0);

            min = Math.min(min, c);
            max = Math.max(max, c);
        }

        offset = min;
        ctrs = new Constructor[max - offset + 1];

        for (final Map.Entry<String, Class<? extends Data>> entry : dataClasses.entrySet())
        {
            final int typeCode = entry.getKey().charAt(0);
            Constructor<? extends Data> clazz;
            try
            {
                clazz = entry.getValue().getConstructor();
                registerStatisticsClass(clazz, typeCode);
            }
            catch (NoSuchMethodException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (SecurityException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Register a new handler under its type code
     * 
     * @param c
     * @param typeCode
     */
    private void registerStatisticsClass(final Constructor<? extends Data> c, final int typeCode)
    {
        ctrs[typeCode - offset] = c;
    }

    /**
     * Parse the the line into a suitable data record
     * @param s the csv line to parse
     * @return the parsed csv line as fitting data object
     * @throws Exception
     */
    public Data createStatistics(final XltCharBuffer src) throws Exception
    {
        // create the statistics object
        final Constructor<? extends Data> c = ctrs[src.get(0) - offset];
        final Data data = c.newInstance();

        data.fromCSV(src);

        return data;
    }
}
