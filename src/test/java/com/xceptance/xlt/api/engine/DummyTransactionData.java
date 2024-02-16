/*
 * Copyright (c) 2005-2023 Xceptance Software Technologies GmbH
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
package com.xceptance.xlt.api.engine;

import com.xceptance.xlt.api.util.XltCharBufferUtil;

/**
 * This class provides a dummy implementation of {@link TransactionData} but makes {@link #parseRemainingValues(String[])} public
 * to allow modifications for testing purposes.
 * <p>
 * The class provides the convenience method {@link #getDefault()} which gives a new instance of this class for each
 * invocation.
 * </p>
 *
 * @author Sebastian Oerding
 */
public class DummyTransactionData extends TransactionData
{
    /**
     * Returns a freshly instantiated DummyTransactionData with the following values:
     * <ul>
     * <li>&quot;T&quot; as type code</li>
     * <li>&quot;transactionName&quot; as name</li>
     * <li>&quot;5000&quot; as time</li>
     * <li>failed set to <code>true</code></li>
     * <li>&quot;a (user: 'testUser', output: '1234567890')&quot; as stacktrace</li>
     * <li>&quot;007&quot; as agent name</li>
     * </ul>
     *
     * @return a new instance for each invocation with some hard coded values as described above
     */
    public static DummyTransactionData getDefault()
    {
        final DummyTransactionData returnValue = new DummyTransactionData();
        final String stackTrace = "a (user: 'testUser', output: '1234567890')";
        returnValue.setupRemainingValues(XltCharBufferUtil.toList(new String[]
            {
                "T", "transactionName", "5000", "1", "true", stackTrace
            }));
        returnValue.setAgentName("007");
        return returnValue;
    }
}
