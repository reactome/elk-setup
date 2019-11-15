package org.reactome.logstash.plugins.usagetype.util;

/**
 * Represents a range of comparable numerics. This is intended for NON-overlapping ranges ONLY.
 * @author sshorser
 *
 * @param <T>
 */
public class Range<T extends Number & Comparable<T>> implements Comparable<Range<T>>
{
	private T start;
	private T end;

	public Range(T start, T end)
	{
		if (start.compareTo(end) > 0)
		{
			throw new IllegalArgumentException("Start must be less than end. Start: " + start.toString() + "; End: " + end.toString());
		}
		this.start = start;
		this.end = end;
	}

	/**
	 * Checks a value and indicates if it is in this range, or outside this range.
	 * @param value - the value to check.
	 * @return 0 if value is within the bounds of this range. -1 if value is less than this range's start. +1 if value is greater than this range's end.
	 */
	public int isInRange(T value)
	{
		if (value.compareTo(start) >= 0 && value.compareTo(end) <= 0)
		{
			return 0;
		}
		else if (value.compareTo(start) < 0)
		{
			return -1;
		}
		else //if (value.compareTo(end) > 0)
		{
			return 1;
		}
	}

	public T getStart()
	{
		return this.start;
	}

	public T getEnd()
	{
		return this.end;
	}

	@Override
	public String toString()
	{
		return start.toString() + ".." + end.toString();
	}

	@Override
	public int compareTo(Range<T> o)
	{
		// A range is considered < another range if its start is < the other's start. This assumes that you won't be working with overlapping/containing ranges; it *might* work
		// in those cases, I just haven't tested it, since the IP ranges I'm using are known to be non-overlapping.
		return this.start.compareTo(o.start);
	}
}