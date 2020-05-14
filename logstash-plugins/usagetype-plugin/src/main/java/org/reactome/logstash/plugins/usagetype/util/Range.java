package org.reactome.logstash.plugins.usagetype.util;

/**
 * Represents a range of comparable numerics. This is intended for NON-overlapping ranges ONLY.
 * @author sshorser
 *
 * @param <T> T is any numeric type that extends Number and Comparable.
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
		return this.start.toString() + ".." + this.end.toString();
	}

	@Override
	public int compareTo(Range<T> o)
	{
		// A range is considered < another range if its start is < the other's start.
		// Ranges are == if one's start/end are within another's start/end.
		// A range is > if it's end is > than the other's.
		// This assumes that you won't be working with overlapping/containing ranges,
		// and I'm not sure this would work with overlapping ranges - I think a different
		// comparison would be necessary in that case.

		// Throw an exception for overlapping ranges.
		// Overlap at upper, for example, this: [10..20] and other: [15..26]
		boolean overlapAtUpperBound = this.getStart().compareTo(o.getStart()) < 0 // This start-point is smaller than other's start-point.
										&& this.getEnd().compareTo(o.getStart()) > 0 // This end-point is bigger than other's start-point.
										&& this.getEnd().compareTo(o.getEnd()) < 0; // this end-point is smaller than other's end-point

		// Overlap at lower, for example this: [1..5] and other: [0..3]
		boolean overlapAtLowerBound = this.getStart().compareTo(o.getStart()) > 0
										&& this.getStart().compareTo(o.getEnd()) < 0
										&& this.getEnd().compareTo(o.getEnd()) > 0;


		if (overlapAtUpperBound || overlapAtLowerBound)
		{
			throw new IllegalArgumentException("These ranges cannot overlap. Cannot compare " + this.toString() + " to " + o.toString());
		}

		if (this.getStart().compareTo(o.getStart()) >= 0 && this.getEnd().compareTo(o.getEnd()) <= 0)
		{
			return 0;
		}
		else if (this.getStart().compareTo(o.getStart()) < 0)
		{
			return -1;
		}
		else //if (this.getEnd().compareTo(o.getEnd()) > 0)
		{
			return 1;
		}
	}
}