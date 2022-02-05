package com.xceptance.common.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.common.lang.OpenStringBuilder;

public class XltCharBufferTest
{
    @Test
    public void empty_const()
    {
        assertEquals(0, XltCharBuffer.EMPTY.length());
        assertArrayEquals(new char[0], XltCharBuffer.EMPTY.toCharArray());
        assertSame(XltCharBuffer.EMPTY, XltCharBuffer.EMPTY);
        assertEquals(0, XltCharBuffer.EMPTY.hashCode());
    }

    @Test
    public void empty()
    {
        assertEquals(0, XltCharBuffer.empty().length());
        assertArrayEquals(new char[0], XltCharBuffer.empty().toCharArray());
        assertEquals(XltCharBuffer.empty(), XltCharBuffer.empty());
        assertSame(XltCharBuffer.empty(), XltCharBuffer.empty());
        assertEquals(0, XltCharBuffer.empty().hashCode());
    }

    @Test
    public void ctr_chararray()
    {
        {
            var c = new char[] {};
            var x = new XltCharBuffer((char[]) null);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            var c = new char[] {};
            var x = new XltCharBuffer(c);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            var c = "a".toCharArray();
            var x = new XltCharBuffer(c);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            var c = "jhjashdj sjdh".toCharArray();
            var x = new XltCharBuffer(c);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            // ensure that we don't have a copy of the array for speed
            var c = "012345".toCharArray();
            var x = new XltCharBuffer(c);
            c[0] = '9';
            assertArrayEquals("912345".toCharArray(), x.toCharArray());
        }        

        // no futher edge cases
    }

    @Test
    public void ctr_openstringbuilder()
    {
        {
            var s = "";
            var os = new OpenStringBuilder(10).append(s);
            var x = new XltCharBuffer(os);
            assertEquals(s.length(), x.length());
            assertArrayEquals(s.toCharArray(), x.toCharArray());
        }
        {
            var s = "012345";
            var os = new OpenStringBuilder(10).ensureCapacity(100).append(s);
            var x = new XltCharBuffer(os);
            assertEquals(s.length(), x.length());
            assertArrayEquals(s.toCharArray(), x.toCharArray());
        }
        {
            var s = "012345";
            var os = new OpenStringBuilder(6).append(s);
            var x = new XltCharBuffer(os);
            assertEquals(s.length(), x.length());
            assertArrayEquals(s.toCharArray(), x.toCharArray());
        }
    }

    @Test
    public void ctr_chararray_from_length()
    {
        {
            // null, we will correct the passed numbers, only
            // edge case handling we have
            var x = new XltCharBuffer(null, 1, 8);
            assertEquals(0, x.length());
            assertArrayEquals("".toCharArray(), x.toCharArray());
        }
        {
            var s = "".toCharArray();
            var x = new XltCharBuffer(s, 0, 0);
            assertEquals(s.length, x.length());
            assertArrayEquals(s, x.toCharArray());
        }
        {
            var s = "0123456789".toCharArray();
            var x = new XltCharBuffer(s, 0, 10);
            assertEquals(s.length, x.length());
            assertArrayEquals(s, x.toCharArray());
        }
        {
            var s = "0123456789".toCharArray();
            var x = new XltCharBuffer(s, 0, 9);
            assertEquals("012345678".length(), x.length());
            assertArrayEquals("012345678".toCharArray(), x.toCharArray());
        }
        {
            var s = "0123456789".toCharArray();
            var x = new XltCharBuffer(s, 1, 8);
            assertEquals("12345678".length(), x.length());
            assertArrayEquals("12345678".toCharArray(), x.toCharArray());
        }
        {
            var s = "0123456789".toCharArray();
            var x = new XltCharBuffer(s, 1, 0);
            assertEquals(0, x.length());
            assertArrayEquals("".toCharArray(), x.toCharArray());
        }
    }

    @Test
    public void valueof_openstringbuilder()
    {
        {
            var s = "";
            var os = new OpenStringBuilder(10).append(s);
            var x = XltCharBuffer.valueOf(os);
            assertEquals(s.length(), x.length());
            assertArrayEquals(s.toCharArray(), x.toCharArray());
        }
        {
            var s = "012345";
            var os = new OpenStringBuilder(10).ensureCapacity(100).append(s);
            var x = XltCharBuffer.valueOf(os);
            assertEquals(s.length(), x.length());
            assertArrayEquals(s.toCharArray(), x.toCharArray());
        }
        {
            var s = "012345";
            var os = new OpenStringBuilder(6).append(s);
            var x = XltCharBuffer.valueOf(os);
            assertEquals(s.length(), x.length());
            assertArrayEquals(s.toCharArray(), x.toCharArray());
        }   
    }

    @Test
    public void valueof_chararray()
    {
        {
            var c = new char[] {};
            var x = XltCharBuffer.valueOf((char[]) null);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            var c = new char[] {};
            var x = XltCharBuffer.valueOf(c);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            var c = "a".toCharArray();
            var x = XltCharBuffer.valueOf(c);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            var c = "jhjashdj sjdh".toCharArray();
            var x = XltCharBuffer.valueOf(c);
            assertEquals(c.length, x.length());
            assertArrayEquals(c, x.toCharArray());
        }
        {
            // ensure that we don't have a copy of the array for speed
            var c = "012345".toCharArray();
            var x = XltCharBuffer.valueOf(c);
            c[0] = '9';
            assertArrayEquals("912345".toCharArray(), x.toCharArray());
        }

    }

    @Test
    public void valueof_string()
    {
        var f = new Consumer<String>() 
        {
            @Override
            public void accept(String s)
            {
                var x = XltCharBuffer.valueOf(s);
                assertEquals(s.length(), x.length());
                assertArrayEquals(s.toCharArray(), x.toCharArray());
            }

        };

        f.accept("");
        f.accept("9q389328932");
    }

    @Test
    public void valueof_string_string()
    {
        var f = new BiConsumer<String, String>() 
        {
            @Override
            public void accept(String s1, String s2)
            {
                var x = XltCharBuffer.valueOf(s1, s2);
                var e = s1 + s2;
                assertEquals(e.length(), x.length());
                assertTrue(x.equals(XltCharBuffer.valueOf(e)));
                assertArrayEquals(e.toCharArray(), x.toCharArray());
            }
        };

        f.accept("", "");
        f.accept("", "s");
        f.accept("s", "");
        f.accept("12345", "1234asdfasfd");
    }

    @Test
    public void valueof_string_string_string()
    {
        var f = new Object() 
        {
            public void accept(String s1, String s2, String s3)
            {
                var x = XltCharBuffer.valueOf(s1, s2, s3);
                var e = s1 + s2 + s3;
                assertEquals(e.length(), x.length());
                assertTrue(x.equals(XltCharBuffer.valueOf(e)));
                assertArrayEquals(e.toCharArray(), x.toCharArray());
            }
        };

        f.accept("", "", "");
        f.accept("", "s", "s");
        f.accept("s", "", "");
        f.accept("12345", "1234asdfasfd", "098765");
    }

    @Test
    public void valueof_string_vargs()
    {
        var f = new Object() 
        {
            public void accept(String s1, String s2, String s3, String... vargs)
            {
                var x = XltCharBuffer.valueOf(s1, s2, s3, vargs);
                var e = s1 + s2 + s3;
                for (String s : vargs)
                {
                    e += s;
                }
                assertEquals(e.length(), x.length());
                assertTrue(x.equals(XltCharBuffer.valueOf(e)));
                assertArrayEquals(e.toCharArray(), x.toCharArray());
            }
        };

        f.accept("", "", "", "");
        f.accept("", "s", "s", "");
        f.accept("s", "", "", "");
        f.accept("12345", "1234asdfasfd", "098765", "sds");
        f.accept("12345", "1234asdfasfd", "098765", "sds", "aa");
    }

    @Test
    public void valueof_buffer_char()
    {
        var f = new Object() 
        {
            public void accept(XltCharBuffer x, char c)
            {
                var r = XltCharBuffer.valueOf(x, c);
                var e = x.toString() + c;
                assertEquals(e.length(), r.length());
                assertArrayEquals(e.toCharArray(), r.toCharArray());
            }
        };

        f.accept(XltCharBuffer.valueOf(""), 'a');
        f.accept(XltCharBuffer.valueOf("Tes"), 't');
        f.accept(XltCharBuffer.valueOf("0123456").viewByLength(2, 2), 't');
    }

    @Test
    public void valueof_buffer_buffer()
    {
        var f = new Object() 
        {
            public void accept(XltCharBuffer x1, XltCharBuffer x2)
            {
                var r = XltCharBuffer.valueOf(x1, x2);
                var e = x1.toString() + x2.toString();
                assertEquals(e.length(), r.length());
                assertArrayEquals(e.toCharArray(), r.toCharArray());
            }
        };

        f.accept(XltCharBuffer.valueOf(""), XltCharBuffer.valueOf(""));
        f.accept(XltCharBuffer.valueOf("Tes"), XltCharBuffer.valueOf("t"));
        f.accept(
                 XltCharBuffer.valueOf("0123456").viewByLength(2, 2),
                 XltCharBuffer.valueOf("0123456").viewByLength(2, 2));
    }

    @Test
    public void valueof_buffer_buffer_buffer()
    {
        var f = new Object() 
        {
            public void accept(XltCharBuffer x1, XltCharBuffer x2, XltCharBuffer x3)
            {
                var r = XltCharBuffer.valueOf(x1, x2, x3);
                var e = x1.toString() + x2.toString() + x3.toString();
                assertEquals(e.length(), r.length());
                assertArrayEquals(e.toCharArray(), r.toCharArray());
            }
        };

        f.accept(XltCharBuffer.valueOf(""), XltCharBuffer.valueOf(""), XltCharBuffer.valueOf(""));
        f.accept(XltCharBuffer.valueOf("Tes"), XltCharBuffer.valueOf("t"), XltCharBuffer.valueOf("0987"));
        f.accept(
                 XltCharBuffer.valueOf("0123456").viewByLength(2, 2),
                 XltCharBuffer.valueOf("0123456").viewByLength(2, 2),
                 XltCharBuffer.valueOf("821821").viewByLength(2, 4));
    }

    @Test
    public void put()
    {
        {
            var b = XltCharBuffer.valueOf("Test");

            b.put(1, 'ä'); Assert.assertEquals("Täst", b.toString());
            b.put(0, '1'); Assert.assertEquals("1äst", b.toString());
            b.put(3, '3'); Assert.assertEquals("1äs3", b.toString());
            b.put(2, '2'); Assert.assertEquals("1ä23", b.toString());
        }
        {
            var b = XltCharBuffer.valueOf("0123456789").viewFromTo(3, 7);

            b.put(1, 'a'); Assert.assertEquals("3a56", b.toString());
            b.put(0, 'b'); Assert.assertEquals("ba56", b.toString());
            b.put(3, 'c'); Assert.assertEquals("ba5c", b.toString());
            b.put(2, 'd'); Assert.assertEquals("badc", b.toString());
        }
    }

    @Test
    public void charAt()
    {
        var b = XltCharBuffer.valueOf("0123456789");
        var b1 = b.viewByLength(0, 4); // 0123
        var b2 = b.viewByLength(3, 3); // 345
        var b3 = b2.viewByLength(1, 2); // 45

        Assert.assertEquals('0', b.charAt(0));
        Assert.assertEquals('5', b.charAt(5));
        Assert.assertEquals('6', b.charAt(6));

        Assert.assertEquals('0', b1.charAt(0));
        Assert.assertEquals('1', b1.charAt(1));
        Assert.assertEquals('2', b1.charAt(2));
        Assert.assertEquals('3', b1.charAt(3));

        Assert.assertEquals('3', b2.charAt(0));
        Assert.assertEquals('4', b2.charAt(1));
        Assert.assertEquals('5', b2.charAt(2));

        Assert.assertEquals('4', b3.charAt(0));
        Assert.assertEquals('5', b3.charAt(1));
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
        var b = XltCharBuffer.valueOf("TestFo2");

        Assert.assertEquals("", b.viewFromTo(0, 0).toString());
        Assert.assertEquals("T", b.viewFromTo(0, 1).toString());
        Assert.assertEquals("TestFo2", b.viewFromTo(0, 7).toString());
        Assert.assertEquals("2", b.viewFromTo(6, 7).toString());
        Assert.assertEquals("", b.viewFromTo(6, 6).toString());

        Assert.assertEquals("est", b.viewFromTo(1, 4).toString());
        Assert.assertEquals("Fo2", b.viewFromTo(4, 7).toString());
    }
    
    @Test
    public void viewFromTo_viewFromTo()
    {
        var b = XltCharBuffer.valueOf("TA0123456789A").viewFromTo(2, 12);

        Assert.assertEquals("0123456789", b.toString());
        Assert.assertEquals("", b.viewFromTo(0, 0).toString());
        Assert.assertEquals("0", b.viewFromTo(0, 1).toString());
        Assert.assertEquals("0123456", b.viewFromTo(0, 7).toString());
        Assert.assertEquals("6", b.viewFromTo(6, 7).toString());
        Assert.assertEquals("", b.viewFromTo(6, 6).toString());

        Assert.assertEquals("123", b.viewFromTo(1, 4).toString());
        Assert.assertEquals("456", b.viewFromTo(4, 7).toString());
    }

    @Test
    public void peakAhead()
    {
        {
            var b = XltCharBuffer.valueOf("012345");

            Assert.assertEquals('0', b.peakAhead(0));
            Assert.assertEquals('1', b.peakAhead(1));
            Assert.assertEquals('2', b.peakAhead(2));
            Assert.assertEquals('3', b.peakAhead(3));
            Assert.assertEquals('4', b.peakAhead(4));
            Assert.assertEquals('5', b.peakAhead(5));
            Assert.assertEquals(0, b.peakAhead(6));
            Assert.assertEquals(0, b.peakAhead(7));
        }
        {
            var b = XltCharBuffer.valueOf("TA01234X").viewFromTo(2, 7);

            Assert.assertEquals('0', b.peakAhead(0));
            Assert.assertEquals('1', b.peakAhead(1));
            Assert.assertEquals('2', b.peakAhead(2));
            Assert.assertEquals('3', b.peakAhead(3));
            Assert.assertEquals('4', b.peakAhead(4));
            Assert.assertEquals(0, b.peakAhead(5));
            Assert.assertEquals(0, b.peakAhead(6));
        }
    }

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
