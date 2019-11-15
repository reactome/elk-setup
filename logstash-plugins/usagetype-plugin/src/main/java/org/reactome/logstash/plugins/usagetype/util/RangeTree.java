package org.reactome.logstash.plugins.usagetype.util;

import java.util.Iterator;
import java.util.TreeMap;

public class RangeTree<N extends Number & Comparable<N>> extends TreeMap<Range<N>, String>
{
	private static final long serialVersionUID = 720362968233917584L;
	/**
	 * Searches for a node whose key is a range which contains value. If there is more than one range that could contain value,
	 * the first range encountered will be returned, it should be the range with the lowest starting value.
	 * @param value The value to search for.
	 * @return A Range object, or NULL if no Range could contain value.
	 */
	public Range<N> findRangeForValue(N value)
	{
		boolean done = false;
		Range<N> foundRange = null;
		Iterator<Range<N>> iterator = this.keySet().iterator();
		// Go through elements until we find one the range containing the value.
		while (iterator.hasNext() && !done)
		{
			Range<N> r = iterator.next();
			if (r.isInRange(value) == 0)
			{
				foundRange = r;
				done = true;
			}
		}
		return foundRange;
	}
}