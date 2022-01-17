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
package com.xceptance.common.util;

import java.util.Arrays;
import java.util.List;

import com.xceptance.common.lang.OpenStringBuilder;

/**
 * This class does not implement the CharBuffer of the JDK, but uses the idea of a shared
 * character array with views. This is also a very unsafe implementation with as little
 * as possible boundary checks to achieve the maximum speed possible. To enhance use, we
 * implement CharSequence and hence can also do regex with it now.
 * 
 * @author rschwietzke
 *
 */
public class XltCharBuffer implements CharSequence, Comparable<XltCharBuffer>
{
    /**
     * An empty static XltCharBuffer
     */
    public static final XltCharBuffer EMPTY = new XltCharBuffer(new char[0]);
    
    /**
     * Just a new line
     */
    public static final XltCharBuffer NEWLINE = XltCharBuffer.valueOf("\n");

    /**
     * The internal buffer, it is shared!
     */
    private final char[] src;
    
    /**
     * Because we are here dealing with the view of an array, we need a start
     * and a length.
     */
    private final int from;
    
    /**
     * The length of the view of the buffer
     */
    private final int length;
    
    /**
     * The hashcode. It is cached to avoid running the same operation again and 
     * again. The hashcode is identical to a hashcode of a String with the same 
     * content.
     */
    private int hashCode;

    /**
     * New buffer from a raw char array
     * 
     * @param src a char array
     */
    public XltCharBuffer(final char[] src)
    {
        this.src = src == null ? new char[0] : src;
        this.from = 0;
        this.length = this.src.length;
    }

    /**
     * A new buffer from an open string builder, so we can directly 
     * use its buffer and don't have to copy. This is highly unsafe, so 
     * make sure you know what you are doing!
     * 
     * @param src an open string builder
     */
    public XltCharBuffer(final OpenStringBuilder src)
    {
        this(src.getCharArray(), 0, src.length());
    }

    /**
     * A new buffer from a char array including a view port.
     * 
     * @param src the char array, if is is null, we fix that silently
     * @param from from where to deliver the buffer
     * @param length how long should the buffer be
     */
    public XltCharBuffer(final char[] src, final int from, final int length)
    {
        this.src = src == null ? new char[0] : src;
        this.from = from;
        this.length = length;
    }

    public char charAt(final int pos)
    {
        return src[from + pos];
    }

    public XltCharBuffer put(final int pos, final char c)
    {
        src[from + pos] = c;

        return this;
    }

    public List<XltCharBuffer> split(final char splitChar)
    {
        final List<XltCharBuffer> result = new SimpleArrayList<>(10);

        int last = -1;
        for (int i = 0; i < this.length; i++)
        {
            char c = this.charAt(i);
            if (c == splitChar)
            {
                result.add(this.substring(last + 1, i));
                last = i;
            }
        }

        if (last == -1 || last + 1 < this.length)
        {
            result.add(this.substring(last + 1, this.length));
        }

        return result;
    }

    public XltCharBuffer replace(char c, String s)
    {
        final OpenStringBuilder result = new OpenStringBuilder(s.length() > 1 ? s.length() + 10 : s.length());
        final char[] sChars = s.toCharArray();

        for (int i = 0; i < this.length; i++)
        {
            final char cc = this.charAt(i);
            if (cc == c)
            {
                result.append(sChars);
            }
            else
            {
                result.append(cc);
            }
        }

        return XltCharBuffer.valueOf(result);
    }

    /**
     * Looks ahead, otherwise returns 0. Only safety bound against ahead misses, not 
     * any behind misses
     * 
     * @param pos the position to look at
     * @return the content of the peaked pos or 0 if this position does not exist
     */
    public char peakAhead(final int pos)
    {
        return from + pos < length ? charAt(pos) : 0;
    }

    public XltCharBuffer viewByLength(final int from, final int length)
    {
        return new XltCharBuffer(this.src, from, length);
    }

    public XltCharBuffer viewFromTo(final int from, final int to)
    {
        return new XltCharBuffer(this.src, this.from + from, to - from);
    }

    public XltCharBuffer substring(final int from, final int to)
    {
        return viewFromTo(from, to);
    }

    public XltCharBuffer substring(final int from)
    {
        return viewFromTo(from, length());
    }

    public static XltCharBuffer empty()
    {
        return EMPTY;
    }

    public static XltCharBuffer valueOf(final String s)
    {
        return new XltCharBuffer(s.toCharArray());
    }

    public static XltCharBuffer valueOf(final OpenStringBuilder s)
    {
        return new XltCharBuffer(s.getCharArray(), 0, s.length());
    }

    /**
     * Append a string to a stringbuilder without an array copy operation
     * 
     * @param target the target
     * @param src the source
     * @return the passed target for fluid syntax
     */
    private static OpenStringBuilder append(final OpenStringBuilder target, final String src)
    {
        final int length = src.length();
        for (int i = 0; i < length; i++)
        {
            // because of JDK 11 compact strings, that is not perfect but we want to 
            // avoid memory waste here and not cpu cycles... always a trade-off
            target.append(src.charAt(i));
        }

        return target;
    }

    /**
     * Append a string to a stringbuilder without an array copy operation
     * 
     * @param target the target
     * @param src the source
     * @return the passed target for fluid syntax
     */
    private static OpenStringBuilder append(final OpenStringBuilder target, final XltCharBuffer src)
    {
        final int length = src.length();
        for (int i = 0; i < length; i++)
        {
            // because of JDK 11 compact strings, that is not perfect but we want to 
            // avoid memory waste here and not cpu cycles... always a trade-off
            target.append(src.charAt(i));
        }

        return target;
    }

    /**
     * Creates a new char buffer by merging strings
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static XltCharBuffer valueOf(final String s1, final String s2)
    {
        // our problem is that a String.toCharArray already creates a copy and we
        // than copy the copy into a new array, hence wasting one full array of 
        // s1 and s2

        // let's instead see if we can run with openstringbuilder nicely
        // more cpu in favour of less memory
        final OpenStringBuilder sb = new OpenStringBuilder(s1.length() + s2.length());
        append(sb, s1);
        append(sb, s2);

        return new XltCharBuffer(sb.getCharArray(), 0, sb.length());
    }

    /**
     * Creates a new char buffer by merging XltCharBuffers
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static XltCharBuffer valueOf(final XltCharBuffer s1, final XltCharBuffer s2)
    {
        // our problem is that a String.toCharArray already creates a copy and we
        // than copy the copy into a new array, hence wasting one full array of 
        // s1 and s2

        // let's instead see if we can run with openstringbuilder nicely
        // more cpu in favour of less memory
        final OpenStringBuilder sb = new OpenStringBuilder(s1.length() + s2.length());
        append(sb, s1);
        append(sb, s2);

        return new XltCharBuffer(sb.getCharArray(), 0, sb.length());
    }

    /**
     * Creates a new char buffer by adding a single char
     * 
     * @param s1
     * @param c
     * @return
     */
    public static XltCharBuffer valueOf(final XltCharBuffer s1, final char c)
    {
        // our problem is that a String.toCharArray already creates a copy and we
        // than copy the copy into a new array, hence wasting one full array of 
        // s1 and s2

        // let's instead see if we can run with openstringbuilder nicely
        // more cpu in favour of less memory
        final OpenStringBuilder sb = new OpenStringBuilder(s1.length() + 1);
        append(sb, s1);
        sb.append(c);

        return new XltCharBuffer(sb.getCharArray(), 0, sb.length());
    }

    /**
     * Creates a new char buffer by merging strings
     * 
     * @param s
     * @return
     */
    public static XltCharBuffer valueOf(final String s1, final String s2, final String s3)
    {
        // our problem is that a String.toCharArray already creates a copy and we
        // than copy the copy into a new array, hence wasting one full array of 
        // s1 and s2

        // let's instead see if we can run with openstringbuilder nicely
        // more cpu in favour of less memory
        final OpenStringBuilder sb = new OpenStringBuilder(s1.length() + s2.length() + s3.length());
        append(sb, s1);
        append(sb, s2);
        append(sb, s3);

        return new XltCharBuffer(sb.getCharArray(), 0, sb.length());
    }

    /**
     * Creates a new char buffer by merging strings
     * 
     * @param s
     * @return
     */
    public static XltCharBuffer valueOf(final XltCharBuffer s1, final XltCharBuffer s2, final XltCharBuffer s3)
    {
        // our problem is that a String.toCharArray already creates a copy and we
        // than copy the copy into a new array, hence wasting one full array of 
        // s1 and s2

        // let's instead see if we can run with openstringbuilder nicely
        // more cpu in favour of less memory
        final OpenStringBuilder sb = new OpenStringBuilder(s1.length() + s2.length() + s3.length());
        append(sb, s1);
        append(sb, s2);
        append(sb, s3);

        // getCharArray does not create a copy, hence OpenStringBuilder from now on should not be used anymore, because it would modify
        // the XltCharBuffer as well. Speed over luxury.
        return new XltCharBuffer(sb.getCharArray(), 0, sb.length());
    }

    /**
     * Creates a new char buffer by merging strings
     * 
     * @param s
     * @return
     */
    public static XltCharBuffer valueOf(final String s1, final String s2, final String s3, final String... more)
    {
        // shortcut 
        if (more == null || more.length == 0)
        {
            return valueOf(s1, s2, s3);
        }

        // new total size
        int newSize = s1.length() + s2.length() + s3.length();
        for (int i = 0; i < more.length; i++)
        {
            newSize += more[i].length();
        }

        final OpenStringBuilder sb = new OpenStringBuilder(newSize);
        append(sb, s1);
        append(sb, s2);
        append(sb, s3);

        for (int i = 0; i < more.length; i++)
        {
            append(sb, more[i]);
        }

        return new XltCharBuffer(sb.getCharArray(), 0, sb.length());
    }

    public static XltCharBuffer valueOf(final char[] s)
    {
        return new XltCharBuffer(s);
    }

    @Override
    public String toString()
    {
        return String.valueOf(src, from, length);
    }

    public char[] toCharArray()
    {
        final char[] target = new char[length];

        System.arraycopy(src, from, target, 0, length);

        return target;
    }

    /**
     * Code shared by String and StringBuffer to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   targetOffset offset of the target string.
     * @param   targetCount  count of the target string.
     * @param   fromIndex    the index to begin searching from.
     */
    private static int indexOf(char[] source, int sourceOffset, int sourceCount,
                               char[] target, int targetOffset, int targetCount,
                               int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first);
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                    == target[k]; j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    public int indexOf(final char c) 
    {
        final int end = length + this.from;
        for (int i = this.from; i < end; i++)
        {
            if (this.src[i] == c)
            {
                return i - this.from;
            }
        }

        return -1;
    }

    public boolean endsWith(final XltCharBuffer s) 
    {
        if (s.length > this.length)
        {
            return false;
        }

        return indexOf(s, this.length - s.length) > -1;
    }

    public boolean startsWith(final XltCharBuffer s) 
    {
        return indexOf(s, 0) == 0;
    }

    public int lastIndexOf(final XltCharBuffer s) 
    {
        return lastIndexOf(s, this.length);
    }

    public int lastIndexOf(final XltCharBuffer s, int from) 
    {
        for (int i = from; i >= 0; i--)
        {
            int last = indexOf(s, i);
            if (last > -1)
            {
                return last <= from ? last : -1;
            }
        }

        return -1;
    }

    public int indexOf(final XltCharBuffer s) 
    {
        return indexOf(this.src, from, length, s.src, s.from, s.length, 0);
    }

    public int indexOf(final XltCharBuffer s, final int fromIndex) 
    {
        return indexOf(this.src, from, length, s.src, s.from, s.length, fromIndex);
    }

    public int length()
    {
        return length;
    }

    /**
     * Optimized hashcode calculation for large strings using all execution units of the CPU.
     * You are not supposed to call this directly, it is rather public for testing. This is a trade
     * off between cpu and branches. 
     * 
     * @return the hash code
     */
    public int hashCodeVectored()
    {
        final int last = length + from;

        int h = 0;
        int i0 = from;
        int i1 = from + 1;
        int i2 = from + 2;
        int i3 = from + 3;
        while (i3 < last) {
            h = h * (31 * 31 * 31 * 31) + src[i0] * (31 * 31 * 31) + src[i1] * (31 * 31) + src[i2] * 31 + src[i3];
            
            i0 = i3 + 1;
            i1 = i3 + 2;
            i2 = i3 + 3;
            i3 = i3 + 4;
        }
        if (i2 < last) {
            h = h * (31 * 31 * 31) + src[i0] * (31 * 31) + src[i1] * (31) + src[i2];
        }
        else if (i1 < last) {
            h = h * (31 * 31) + src[i0] * (31) + src[i1];
        }
        else if (i0 < last) {
            h = h * 31 + src[i0];
        }
        
        return h; 
    }
    
    /**
     * Assume we are not mutating... if we mutate, we would have to reset the hashCode
     * 
     * @return the hashcode, similar to what a normal string would deliver
     */
    @Override
    public int hashCode()
    {
        // it was cached before
        if (hashCode != 0)
        {
            return hashCode;
        }
        
        // use the vectored approach for longer strings
        // disabled for now, does not seem to fly well when the entire program runs, while as JMH it is faster
//        if (length > 50)
//        {
//            // cache and return
//            return hashCode = hashCodeVectored();
//        }
        
        final int last = length + from;

        int h = 0;
        for (int i = from; i < last; i++) {
            h = ((h << 5) - h) + src[i];
        }
        
        return hashCode = h; 
    }

    /**
     * Returns the empty string if the provided buffer is null the buffer otherwise
     */
    public static XltCharBuffer emptyWhenNull(final XltCharBuffer s)
    {
        return s == null ? XltCharBuffer.empty() : s;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final XltCharBuffer other = (XltCharBuffer) obj;
        if (this.length == other.length)
        {
            return Arrays.equals(this.src, from, from + length, other.src, other.from, other.from + length);
        }

        return false;
    }

    @Override
    public CharSequence subSequence(int start, int end)
    {
        return substring(start, end);
    }

    @Override
    public int compareTo(XltCharBuffer other)
    {
        return Arrays.compare(this.src, from, from + length, 
                              other.src, other.from, other.from + other.length);
    }

    public String toDebugString()
    {
        return String.format("base=%s\ncurrent=%s\nfrom=%d, length=%d", new String(src), this, from, length);
    }
}
