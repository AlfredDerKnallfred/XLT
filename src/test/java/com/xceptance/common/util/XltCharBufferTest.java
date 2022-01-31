package com.xceptance.common.util;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class XltCharBufferTest
{
    @Test
    public void create()
    {
        final char[] src = "Test".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);
        Assert.assertEquals(4, b.length());
        Assert.assertEquals(String.valueOf(src), b.toString());
    }

    @Test
    public void string()
    {
        {
            final char[] src = "Test".toCharArray();
            final XltCharBuffer b = new XltCharBuffer(src);
            Assert.assertEquals(String.valueOf(src), b.toString());
        }

        {
            final char[] src = "TestFoobarRally".toCharArray();
            final XltCharBuffer b = new XltCharBuffer(src);
            Assert.assertEquals(String.valueOf(src), b.toString());

            Assert.assertEquals("Test", b.viewByLength(0, 4).toString());
            Assert.assertEquals("Foobar", b.viewByLength(4, 6).toString());
            Assert.assertEquals("Rally", b.viewByLength(10, 5).toString());
        }
    }

    @Test
    public void put()
    {
        final char[] src = "Test".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);

        b.put(1, 'ä'); Assert.assertEquals("Täst", b.toString());
        b.put(0, '1'); Assert.assertEquals("1äst", b.toString());
        b.put(3, '3'); Assert.assertEquals("1äs3", b.toString());
        b.put(2, '2'); Assert.assertEquals("1ä23", b.toString());
    }

    @Test
    public void putWithView()
    {
        final char[] src = "TestFoo".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);
        final XltCharBuffer b1 = b.viewByLength(0, 4);
        final XltCharBuffer b2 = b.viewByLength(4, 3);

        b1.put(1, 'ä');
        Assert.assertEquals("Täst", b1.toString());
        Assert.assertEquals("Foo", b2.toString());

        b2.put(1, '1');
        Assert.assertEquals("Täst", b1.toString());
        Assert.assertEquals("F1o", b2.toString());

        Assert.assertEquals("TästF1o", b.toString());
    }

    @Test
    public void get()
    {
        final char[] src = "TestFo2".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);
        final XltCharBuffer b1 = b.viewByLength(0, 4);
        final XltCharBuffer b2 = b.viewByLength(4, 3);

        Assert.assertEquals('T', b.get(0));
        Assert.assertEquals('o', b.get(5));
        Assert.assertEquals('2', b.get(6));

        Assert.assertEquals('T', b1.get(0));
        Assert.assertEquals('e', b1.get(1));
        Assert.assertEquals('s', b1.get(2));
        Assert.assertEquals('t', b1.get(3));

        Assert.assertEquals('F', b2.get(0));
        Assert.assertEquals('o', b2.get(1));
        Assert.assertEquals('2', b2.get(2));
    }

    @Test
    public void viewByLength()
    {
        var x = XltCharBuffer.valueOf("0123456");

        assertEquals(XltCharBuffer.valueOf(""), x.viewByLength(0, 0));
        assertEquals(XltCharBuffer.valueOf("0"), x.viewByLength(0, 1));
        assertEquals(XltCharBuffer.valueOf("01"), x.viewByLength(0, 2));
        assertEquals(XltCharBuffer.valueOf("012"), x.viewByLength(0, 3));
        assertEquals(XltCharBuffer.valueOf("0123"), x.viewByLength(0, 4));
        assertEquals(XltCharBuffer.valueOf("01234"), x.viewByLength(0, 5));
        assertEquals(XltCharBuffer.valueOf("012345"), x.viewByLength(0, 6));
        assertEquals(XltCharBuffer.valueOf("0123456"), x.viewByLength(0, 7));

        assertEquals(XltCharBuffer.valueOf("1"), x.viewByLength(1, 1));
        assertEquals(XltCharBuffer.valueOf("123456"), x.viewByLength(1, 6));
        assertEquals(XltCharBuffer.valueOf("6"), x.viewByLength(6, 1));
    }

    @Test
    public void viewByLength_viewByLength()
    {
        var x = XltCharBuffer.valueOf("0123456");

        assertEquals(XltCharBuffer.valueOf(""), x.viewByLength(0, 1).viewByLength(0, 0));
        
        assertEquals(XltCharBuffer.valueOf("0"), x.viewByLength(0, 2).viewByLength(0, 1));
        assertEquals(XltCharBuffer.valueOf("1"), x.viewByLength(0, 2)/*01*/.viewByLength(1, 1));

        assertEquals(XltCharBuffer.valueOf("012"), x.viewByLength(0, 3).viewByLength(0, 3));
        assertEquals(XltCharBuffer.valueOf("01"), x.viewByLength(0, 3).viewByLength(0, 2));
        assertEquals(XltCharBuffer.valueOf("0"), x.viewByLength(0, 3).viewByLength(0, 1));

        assertEquals(XltCharBuffer.valueOf( "1"), x.viewByLength(1, 3)/*123*/.viewByLength(0, 1));
        assertEquals(XltCharBuffer.valueOf( "2"), x.viewByLength(1, 3)/*123*/.viewByLength(1, 1));
        assertEquals(XltCharBuffer.valueOf( "3"), x.viewByLength(1, 3)/*123*/.viewByLength(2, 1));
        assertEquals(XltCharBuffer.valueOf("23"), x.viewByLength(1, 3)/*123*/.viewByLength(1, 2));
        assertEquals(XltCharBuffer.valueOf("123"), x.viewByLength(1, 3)/*123*/.viewByLength(0, 3));

        assertEquals(XltCharBuffer.valueOf("45"), x.viewByLength(4, 3)/*456*/.viewByLength(0, 2));
        
        assertEquals(XltCharBuffer.valueOf("b"), XltCharBuffer.valueOf("abc").viewByLength(1, 1).viewByLength(0, 1));
    }

    
    @Test
    public void viewFromTo()
    {
        final char[] src = "TestFo2".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);

        // Assert.assertEquals("", b.viewFromTo(0, 0).toString());
        Assert.assertEquals("T", b.viewFromTo(0, 1).toString());
        Assert.assertEquals("TestFo2", b.viewFromTo(0, 7).toString());
        Assert.assertEquals("2", b.viewFromTo(6, 7).toString());
        // Assert.assertEquals("", b.viewFromTo(6, 6).toString());

        Assert.assertEquals("est", b.viewFromTo(1, 4).toString());
        Assert.assertEquals("Fo2", b.viewFromTo(4, 7).toString());

    }

    @Test
    public void peakAhead()
    {
        final char[] src = "TestFo2".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);

        Assert.assertEquals('T', b.peakAhead(0));
        Assert.assertEquals('e', b.peakAhead(1));
        Assert.assertEquals('s', b.peakAhead(2));
        Assert.assertEquals('t', b.peakAhead(3));
        Assert.assertEquals('F', b.peakAhead(4));
        Assert.assertEquals('o', b.peakAhead(5));
        Assert.assertEquals('2', b.peakAhead(6));
        Assert.assertEquals(0, b.peakAhead(7));
        Assert.assertEquals(0, b.peakAhead(8));
    }

    //    @Test
    //    public void viewOfViews()
    //    {
    //        final char[] src = "TestFo2".toCharArray();
    //        final XltCharBuffer b = new XltCharBuffer(src);
    //        
    //        Assert.assertEquals("", b.viewFromTo(0, 0).toString());
    //
    //    }
    //    
    @Test
    public void length()
    {
        Assert.assertEquals(0, new XltCharBuffer("".toCharArray()).length());
        Assert.assertEquals(1, new XltCharBuffer("T".toCharArray()).length());
        Assert.assertEquals(2, new XltCharBuffer("TA".toCharArray()).length());

        Assert.assertEquals(0, new XltCharBuffer("TA".toCharArray()).viewByLength(0, 0).length());
        Assert.assertEquals(1, new XltCharBuffer("TA".toCharArray()).viewByLength(0, 1).length());
        Assert.assertEquals(1, new XltCharBuffer("TA".toCharArray()).viewByLength(1, 1).length());
        Assert.assertEquals(2, new XltCharBuffer("TA".toCharArray()).viewByLength(0, 2).length());
    }

    @Test
    public void empty()
    {
        final char[] src = "TestFo2".toCharArray();
        final XltCharBuffer b = new XltCharBuffer(src);

        Assert.assertEquals(0, b.viewByLength(0, 0).length());
        Assert.assertEquals("", b.viewByLength(0, 0).toString());

        Assert.assertEquals(0, XltCharBuffer.empty().length());
        Assert.assertEquals("", XltCharBuffer.empty().toString());
    }

    @Test
    public void indexOf_char()
    {
        Assert.assertEquals(-1, XltCharBuffer.valueOf("").indexOf('a'));
        Assert.assertEquals(-1, XltCharBuffer.valueOf("a").indexOf('b'));
        Assert.assertEquals(0, XltCharBuffer.valueOf("b").indexOf('b'));
        Assert.assertEquals(0, XltCharBuffer.valueOf("ba").indexOf('b'));
        Assert.assertEquals(0, XltCharBuffer.valueOf("bab").indexOf('b'));
        Assert.assertEquals(0, XltCharBuffer.valueOf("abc").indexOf('a'));
        Assert.assertEquals(1, XltCharBuffer.valueOf("abc").indexOf('b'));
        Assert.assertEquals(2, XltCharBuffer.valueOf("abc").indexOf('c'));
        Assert.assertEquals(-1, XltCharBuffer.valueOf("abc").indexOf('d'));
    }

    @Test
    public void hashCode_test()
    {
        Assert.assertEquals(new String("").hashCode(), XltCharBuffer.valueOf("").hashCode());
        Assert.assertEquals(new String(" ").hashCode(), XltCharBuffer.valueOf(" ").hashCode());
        Assert.assertEquals(new String("  ").hashCode(), XltCharBuffer.valueOf("  ").hashCode());
        Assert.assertEquals(new String("Foobar").hashCode(), XltCharBuffer.valueOf("Foobar").hashCode());
        Assert.assertEquals(new String("Das ist ein Test.").hashCode(), XltCharBuffer.valueOf("Das ist ein Test.").hashCode());
        Assert.assertEquals(new String("ist").hashCode(), XltCharBuffer.valueOf("Das ist ein Test.").substring(4, 7).hashCode());

        // longer strings fall back to vector
        for (int i = 99; i < 110; i ++)
        {
            final String s = RandomStringUtils.random(i, "0123456789");
            Assert.assertEquals(s.hashCode(), XltCharBuffer.valueOf(s).hashCode());
        }
    }

    @Test
    public void hashCodeVectored_test()
    {
        Assert.assertEquals(new String("").hashCode(), XltCharBuffer.valueOf("").hashCodeVectored());
        Assert.assertEquals(new String(" ").hashCode(), XltCharBuffer.valueOf(" ").hashCodeVectored());
        Assert.assertEquals(new String("  ").hashCode(), XltCharBuffer.valueOf("  ").hashCodeVectored());
        Assert.assertEquals(new String("Foobar").hashCode(), XltCharBuffer.valueOf("Foobar").hashCodeVectored());
        Assert.assertEquals(new String("Das ist ein Test.").hashCode(), XltCharBuffer.valueOf("Das ist ein Test.").hashCodeVectored());
        Assert.assertEquals(new String("ist").hashCode(), XltCharBuffer.valueOf("Das ist ein Test.").substring(4, 7).hashCodeVectored());

        Assert.assertEquals(new String("0").hashCode(), XltCharBuffer.valueOf("0").hashCodeVectored());
        Assert.assertEquals(new String("01").hashCode(), XltCharBuffer.valueOf("01").hashCodeVectored());
        Assert.assertEquals(new String("012").hashCode(), XltCharBuffer.valueOf("012").hashCodeVectored());
        Assert.assertEquals(new String("0123").hashCode(), XltCharBuffer.valueOf("0123").hashCodeVectored());
        Assert.assertEquals(new String("01234").hashCode(), XltCharBuffer.valueOf("01234").hashCodeVectored());
        Assert.assertEquals(new String("012345").hashCode(), XltCharBuffer.valueOf("012345").hashCodeVectored());
    }


    @Test
    public void compare()
    {
        {
            // basics
            assertEquals(0, XltCharBuffer.valueOf("").compareTo(XltCharBuffer.valueOf("")));
            assertEquals(-1, XltCharBuffer.valueOf("a").compareTo(XltCharBuffer.valueOf("b")));
            assertEquals(1, XltCharBuffer.valueOf("b").compareTo(XltCharBuffer.valueOf("a")));
        }
        {
            XltCharBuffer a = XltCharBuffer.valueOf("abcd").substring(0, 1);
            XltCharBuffer b = XltCharBuffer.valueOf("abcd").substring(1, 2);

            assertEquals(0, a.compareTo(a));
            assertEquals(-1, a.compareTo(b));
            assertEquals(1, b.compareTo(a));
        }
        {
            XltCharBuffer x = XltCharBuffer.valueOf("1234abcd1234").substring(4, 8);
            XltCharBuffer a = x.substring(0, 1);
            XltCharBuffer b = x.substring(1, 2);

            assertEquals(0, a.compareTo(a));
            assertEquals(-1, a.compareTo(b));
            assertEquals(1, b.compareTo(a));
        }
    }

    @Test
    public void substring_to()
    {
        {
            var x1 = XltCharBuffer.valueOf("a").substring(0);
            assertEquals(XltCharBuffer.valueOf("a"), x1);

            var x2 = XltCharBuffer.valueOf("abc").viewByLength(0, 1).substring(0);
            assertEquals(XltCharBuffer.valueOf("a"), x2);

            var x3 = XltCharBuffer.valueOf("abc").viewByLength(1, 1);
            x3 = x3.substring(0);
            assertEquals(XltCharBuffer.valueOf("b"), x3);

            var x4 = XltCharBuffer.valueOf("bca").viewByLength(2, 1).substring(0);
            assertEquals(XltCharBuffer.valueOf("a"), x4);
        }

        {
            var x1 = XltCharBuffer.valueOf("abc").substring(0);
            assertEquals(XltCharBuffer.valueOf("abc"), x1);

            var x2 = XltCharBuffer.valueOf("abc123").viewByLength(0, 3).substring(0);
            assertEquals(XltCharBuffer.valueOf("abc"), x2);

            var x3 = XltCharBuffer.valueOf("abc123").viewByLength(3, 3).substring(0);
            assertEquals(XltCharBuffer.valueOf("123"), x3);

            var x4 = XltCharBuffer.valueOf("abc123").viewByLength(2, 3).substring(0);
            assertEquals(XltCharBuffer.valueOf("c12"), x4);
        }

        {
            var x1 = XltCharBuffer.valueOf("abc").substring(1);
            assertEquals(XltCharBuffer.valueOf("bc"), x1);

            var x2 = XltCharBuffer.valueOf("abc123").viewByLength(0, 3).substring(1);
            assertEquals(XltCharBuffer.valueOf("bc"), x2);

            var x3 = XltCharBuffer.valueOf("abc123").viewByLength(3, 3).substring(1);
            assertEquals(XltCharBuffer.valueOf("23"), x3);

            var x4 = XltCharBuffer.valueOf("abc123").viewByLength(2, 3).substring(1);
            assertEquals(XltCharBuffer.valueOf("12"), x4);
        }

    }
}
