package com.xceptance.common.lang;

import com.xceptance.common.util.XltCharBuffer;

public class ParseBoolean 
{
	/**
	 * Parses chars and evaluates if this is a boolean. Anything that is not true or TRUE
	 * or similar to True will evaluate to false. This is optimized for speed.
	 * 
	 * @param b buffer with characters, null is not permitted
	 * @return true when chars match case-insensitive, false in any other case
	 */
	public static boolean parse(final XltCharBuffer b)
	{
		// length is incorrect, it must be false
		if (b.length() != 4)
		{
			return false;
		}
		
		// it is length 4, safe here
		final char t = b.get(0);
		final char r = b.get(1);
		final char u = b.get(2);
		final char e = b.get(3);
		
		// fastpath and slowpath
		final boolean b1 = (t == 't' & r == 'r' & u == 'u' & e == 'e');
		
		// slowpath will only be taken when needed, expected most true/false char buffers to be lowercase only
		return b1 ? true : ((t == 't' || t == 'T') && (r == 'r' || r == 'R') && (u == 'u' || u == 'U') && (e == 'e' || e == 'E'));
	}
}
