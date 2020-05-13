package org.reactome.logstash.plugins.usagetype.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * A Tree that contains ranges. The nodes in this tree are keyed by ranges, and store Strings as values.
 * @author sshorser
 *
 * @param <N> N is a numeric that extends Number and Comparable.
 */
public class RangeTree<N extends Number & Comparable<N>> extends TreeMap<Range<N>, String>
{
	private static final long serialVersionUID = 720362968233917584L;

	public RangeTree()
	{
		super(new Comparator<Range<N>>()
		{
			@Override
			public int compare(Range<N> o1, Range<N> o2)
			{
				return o1.compareTo(o2);
			}
		});
	}

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

	/**
	 * Gets the value that a numeric value refers to. Creates a range where start and end are scalarKey,
	 * and then looks that range up as a key. If it falls within a range already described in the tree, the value
	 * indexed by that range will be returned. If it is not found, NULL is returned.
	 * @param scalarKey
	 * @return
	 */
	public String getForValue(N scalarKey)
	{
		// Create a new range with start/end the same value so that it is easier to find.
		Range<N> tmpRange = new Range<>(scalarKey, scalarKey);
		String value = this.get(tmpRange);
		// value _could_ be null at this point, which would happen if there are no ranges containing scalerKey
		return value;
	}

}