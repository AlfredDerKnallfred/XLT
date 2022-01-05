package com.xceptance.common.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xceptance.common.util.XltCharBuffer;

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
