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
		assertEquals(0,r.isInRange(BigInteger.valueOf(222)));
		assertTrue(0 > r.isInRange(BigInteger.valueOf(1)));
		assertTrue(r.isInRange(BigInteger.valueOf(10000)) > 0);
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
		assertNotNull(tmp);
		assertEquals(rt.get(tmp),"A");
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(100000));
		assertNull(tmp);
		System.out.println();

		tmp = rt.findRangeForValue(BigInteger.valueOf(1001));
		assertNull(tmp);
		System.out.println();

		tmp = rt.findRangeForValue(BigInteger.valueOf(-1001));
		assertNull(tmp);
		System.out.println();

		tmp = rt.findRangeForValue(BigInteger.valueOf(1000));
		assertNotNull(tmp);
		assertEquals("A", rt.get(tmp));
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(1550));
		assertNotNull(tmp);
		assertEquals("C", rt.get(tmp));
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(30000));
		assertNotNull(tmp);
		assertEquals("D", rt.get(tmp));
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(30001));
		assertNotNull(tmp);
		assertEquals("D", rt.get(tmp));
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(40000));
		assertNotNull(tmp);
		assertEquals("F", rt.get(tmp));
		System.out.println(tmp);

		tmp = rt.findRangeForValue(BigInteger.valueOf(2345));
		assertNotNull(tmp);
		assertEquals("B", rt.get(tmp));
		System.out.println(tmp);
	}
}
