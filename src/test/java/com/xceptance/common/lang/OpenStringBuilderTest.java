package com.xceptance.common.lang;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This tests only the methods of the OpenStringBulder than have been changed for XLT
 * @author rschwietzke
 *
 */
public class OpenStringBuilderTest
{
    @Test
    public void fastAppend()
    {
        var f = new Object() 
        {
            public void test(final int length, final String initial, final String toAppend)
            {
                var osb = new OpenStringBuilder(length);
                osb.append(initial);
                osb.fastAppend(toAppend);
                
                assertEquals(initial.length() + toAppend.length(), osb.length());
                assertEquals(initial + toAppend, osb.toString());
            }
        };
        
        f.test(1, "", "");
        f.test(10, "", "");
        f.test(10, "01234", "01234");
        f.test(1, "01", "");
        f.test(2, "01", "23");
        f.test(4, "01", "23");
        f.test(10, "01", "23");
        f.test(10, "01", "234567890123456");
    }
}
