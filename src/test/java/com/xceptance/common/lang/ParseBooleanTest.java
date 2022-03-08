/*
 * Copyright (c) 2005-2022 Xceptance Software Technologies GmbH
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
package com.xceptance.common.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParseBooleanTest 
{
    @Test
    public void normal()
    {
        assertTrue(ParseBoolean.parse(XltCharBuffer.valueOf("true")));
        assertFalse(ParseBoolean.parse(XltCharBuffer.valueOf("false")));
        assertFalse(ParseBoolean.parse(XltCharBuffer.valueOf("trueish")));
        assertFalse(ParseBoolean.parse(XltCharBuffer.valueOf("wahr")));
        assertFalse(ParseBoolean.parse(XltCharBuffer.valueOf("")));

        assertTrue(ParseBoolean.parse(XltCharBuffer.valueOf("TRUE")));
        assertTrue(ParseBoolean.parse(XltCharBuffer.valueOf("truE")));
        assertTrue(ParseBoolean.parse(XltCharBuffer.valueOf("True")));
        assertTrue(ParseBoolean.parse(XltCharBuffer.valueOf("tRue")));
        assertTrue(ParseBoolean.parse(XltCharBuffer.valueOf("trUe")));
    }

    @Test(expected = NullPointerException.class)
    public void nullNPE()
    {
        assertTrue(ParseBoolean.parse(null));
    }
}
