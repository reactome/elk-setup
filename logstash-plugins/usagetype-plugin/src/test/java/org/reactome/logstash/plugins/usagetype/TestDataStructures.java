package org.reactome.logstash.plugins.usagetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;
import org.reactome.logstash.plugins.usagetype.util.Range;
import org.reactome.logstash.plugins.usagetype.util.RangeTree;

@SuppressWarnings("static-method")
public class TestDataStructures
{
	@Test
	public void testRange()
	{
		Range<BigInteger> r = new Range<>(BigInteger.valueOf(123), BigInteger.valueOf(456));
		// in range
		assertEquals(0, r.isInRange(BigInteger.valueOf(222)));
		// "less than" the range's lower bound
		assertTrue(0 > r.isInRange(BigInteger.valueOf(1)));
		// greater than the range's upper bound
		assertTrue( 0 < r.isInRange(BigInteger.valueOf(10000)));
	}

	@Test
	public void testRangeTree()
	{
		RangeTree<BigInteger> rt = new RangeTree<>();

		Range<BigInteger> r1 = new Range<>(BigInteger.valueOf(1), BigInteger.valueOf(1000));
		Range<BigInteger> r2 = new Range<>(BigInteger.valueOf(2000), BigInteger.valueOf(3000));
		Range<BigInteger> r3 = new Range<>(BigInteger.valueOf(1500), BigInteger.valueOf(1600));
		Range<BigInteger> r4 = new Range<>(BigInteger.valueOf(10000), BigInteger.valueOf(20000));
		Range<BigInteger> r5 = new Range<>(BigInteger.valueOf(30000), BigInteger.valueOf(30001));
		Range<BigInteger> r6 = new Range<>(BigInteger.valueOf(40000), BigInteger.valueOf(40000));

		rt.put(r1, "A");
		rt.put(r2, "B");
		rt.put(r3, "C");
		rt.put(r5, "D");
		rt.put(r4, "E");
		rt.put(r6, "F");

		Range<BigInteger> tmp = rt.findRangeForValue(BigInteger.valueOf(100));
		String value = rt.getForValue(BigInteger.valueOf(100));
		assertNotNull(tmp);
		assertNotNull(value);
		assertEquals(rt.get(tmp),"A");
		assertEquals(value, "A");
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(100000));
		value = rt.getForValue(BigInteger.valueOf(100000));
		assertNull(value);
		assertNull(tmp);
		System.out.println();

		tmp = rt.findRangeForValue(BigInteger.valueOf(1001));
		value = rt.getForValue(BigInteger.valueOf(1001));
		assertNull(tmp);
		assertNull(value);
		System.out.println();

		tmp = rt.findRangeForValue(BigInteger.valueOf(-1001));
		value = rt.getForValue(BigInteger.valueOf(-1001));
		assertNull(tmp);
		assertNull(value);
		System.out.println();

		tmp = rt.findRangeForValue(BigInteger.valueOf(1000));
		value = rt.getForValue(BigInteger.valueOf(1000));
		assertNotNull(tmp);
		assertEquals("A", rt.get(tmp));
		assertNotNull(value);
		assertEquals("A", value);
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(15000));
		value = rt.getForValue(BigInteger.valueOf(15000));
		assertNotNull(tmp);
		assertEquals("E", rt.get(tmp));
		assertNotNull(value);
		assertEquals("E", value);
		System.out.println(tmp);
		
		tmp = rt.findRangeForValue(BigInteger.valueOf(1550));
		value = rt.getForValue(BigInteger.valueOf(1550));
		assertNotNull(tmp);
		assertEquals("C", rt.get(tmp));
		assertNotNull(value);
		assertEquals("C", value);
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(30000));
		value = rt.getForValue(BigInteger.valueOf(30000));
		assertNotNull(tmp);
		assertEquals("D", rt.get(tmp));
		assertNotNull(value);
		assertEquals("D", value);
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(30001));
		value = rt.getForValue(BigInteger.valueOf(30001));
		assertNotNull(tmp);
		assertEquals("D", rt.get(tmp));
		assertNotNull(value);
		assertEquals("D", value);
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(40000));
		value = rt.getForValue(BigInteger.valueOf(40000));
		assertNotNull(tmp);
		assertEquals("F", rt.get(tmp));
		assertNotNull(value);
		assertEquals("F", value);
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(2345));
		assertNotNull(tmp);
		assertEquals("B", rt.get(tmp));
		System.out.println(tmp);

		value = rt.getForValue(BigInteger.valueOf(2345));
		assertNotNull(value);
		assertEquals("B", value);
		System.out.println(value);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOverlapExceptions()
	{
		Range<BigInteger> r1 = new Range<>(BigInteger.valueOf(15), BigInteger.valueOf(26));
		Range<BigInteger> r2 = new Range<>(BigInteger.valueOf(10), BigInteger.valueOf(20));

		r1.compareTo(r2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOverlapExceptions2()
	{
		Range<BigInteger> r1 = new Range<>(BigInteger.valueOf(1), BigInteger.valueOf(5));
		Range<BigInteger> r2 = new Range<>(BigInteger.valueOf(0), BigInteger.valueOf(3));

		r1.compareTo(r2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidStartEnd()
	{
		@SuppressWarnings("unused")
		Range<BigInteger> r = new Range<>(BigInteger.valueOf(100), BigInteger.valueOf(26));

	}
}
