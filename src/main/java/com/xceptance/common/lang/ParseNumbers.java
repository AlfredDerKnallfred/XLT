package com.xceptance.common.lang;

import com.xceptance.common.util.XltCharBuffer;

/**
 * This is a small helper class for parsing strings and converting them into int, long, and double. This implementation is optimized for
 * speed not functionality. It is only able to parse plain numbers with base 10, e.g. 100828171. In case of parsing problems we will fall
 * back to the JDK but will lose the speed advantage of course. 
 * 
 * @author René Schwietzke
 */
public final class ParseNumbers
{
    private static final int DIGITOFFSET = 48;

    private static final double[] multipliers = {
        1, 1, 0.1, 0.01, 0.001, 0.000_1, 0.000_01, 0.000_001, 0.000_000_1, 0.000_000_01, 
        0.000_000_001, 0.000_000_000_1, 0.000_000_000_01, 0.000_000_000_001, 0.000_000_000_000_1,
        0.000_000_000_000_01, 0.000_000_000_000_001, 0.000_000_000_000_000_1, 0.000_000_000_000_000_01};
    /**
     * Parses the string and returns the result as long. Raises a NumberFormatException in case of non-convertible
     * chars. If the input data is larger than a long, we will silently overflow.
     * 
     * @param s the char buffer to parse
     * @return the converted chars as long
     * @throws java.lang.NumberFormatException
     */
    public static long parseLong(final XltCharBuffer s)
    {
        // no string
        if (s == null)
        {
            throw new NumberFormatException("null");
        }

        // determine length
        final int length = s.length();

        if (length == 0)
        {
            throw new NumberFormatException("length = 0");
        }

        // that is safe, we already know that we are > 0
        final int digit = s.get(0);

        // turn the compare around to allow the compiler and cpu
        // to run the next code most of the time
        if (digit < '0' || digit > '9')
        {
            return Long.parseLong(s.toString());
        }

        long value = digit - DIGITOFFSET;

        for (int i = 1; i < length; i++)
        {
            final int d = s.get(i);
            if (d < '0' || d > '9')
            {
                return Long.parseLong(s.toString());
            }

            value = ((value << 3) + (value << 1));
            value += (d - DIGITOFFSET);
        }

        return value;
    }

    /**
     * Parses the chars and returns the result as int. Raises a NumberFormatException in case of an non-convertible
     * chars. If the input is larger than an int, we will silently overflow.
     * @param s
     *            the string to parse
     * @return the converted string as int
     * @throws java.lang.NumberFormatException
     */
    public static int parseInt(final XltCharBuffer s)
    {
        // no string
        if (s == null)
        {
            throw new NumberFormatException("null");
        }

        // determine length
        final int length = s.length();

        if (length == 0)
        {
            throw new NumberFormatException("length = 0");
        }

        // that is safe, we already know that we are > 0
        final int digit = s.get(0);

        // turn the compare around to allow the compiler and cpu
        // to run the next code most of the time
        if (digit < '0' || digit > '9')
        {
            return Integer.parseInt(s.toString());
        }

        int value = digit - DIGITOFFSET;

        for (int i = 1; i < length; i++)
        {
            final int d = s.get(i);
            if (d < '0' || d > '9')
            {
                return Integer.parseInt(s.toString());
            }

            value = ((value << 3) + (value << 1));
            value += (d - DIGITOFFSET);
        }

        return value;
    }

    /**
     * Parses the chars and returns the result as double. Raises a NumberFormatException in case of an non-convertible
     * char set. Due to conversion limitations, the result might be different from Double.parseDouble aka precision. 
     * We also drop negative numbers and fallback to Double.parseDouble. 
     * 
     * @param s
     *            the string to parse
     * @return the converted string as double
     * @throws java.lang.NumberFormatException
     */
    public static double parseDouble(final XltCharBuffer s)
    {
        // no string
        if (s == null)
        {
            throw new NumberFormatException("null");
        }

        // determine length
        final int length = s.length();

        if (length == 0)
        {
            throw new NumberFormatException("length = 0");
        }

        // that is safe, we already know that we are > 0
        final int digit = s.get(0);

        // turn the compare around to allow the compiler and cpu
        // to run the next code most of the time
        if (digit < '0' || digit > '9')
        {
            return Double.parseDouble(s.toString());
        }

        long value = digit - DIGITOFFSET;

        int decimalPos = 0;

        for (int i = 1; i < length; i++)
        {
            final int d = s.get(i);
            if (d == '.')
            {
                decimalPos = i;
                continue;
            }
            if (d < '0' || d > '9')
            {
                return Double.parseDouble(s.toString());
            }

            value = ((value << 3) + (value << 1));
            value += (d - DIGITOFFSET);
        }

        // adjust the decimal places
        return decimalPos > 0 ? value * multipliers[length - decimalPos] : value;
    }
}
