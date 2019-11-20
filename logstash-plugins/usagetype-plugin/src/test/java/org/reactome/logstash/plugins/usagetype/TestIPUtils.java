package org.reactome.logstash.plugins.usagetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.net.UnknownHostException;

import org.junit.Test;
import org.reactome.logstash.plugins.usagetype.util.IPUtils;

@SuppressWarnings("static-method")
public class TestIPUtils
{
	@Test
	public void testConvertIPToNumber() throws UnknownHostException
	{
		String ipString = "192.168.10.1";
		BigInteger ipNum = IPUtils.convertIPToNumber(ipString);
		assertEquals(3232238081l, ipNum.longValue());

		ipString = "0.0.0.0";
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertEquals(0l, ipNum.longValue());

		ipString = "255.255.255.255";
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertEquals(4294967295l, ipNum.longValue());

		ipString = "172.217.165.14"; // This one is Google!
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertEquals(2899944718l, ipNum.longValue());

		ipString = "127.0.0.1";
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertEquals(2130706433l, ipNum.longValue());

		ipString = "206.108.127.16";
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertEquals(3463216912l, ipNum.longValue());

		// Test some IPv6 values.
		ipString = "2001:4860:4860::8888";
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertTrue("42541956123769884636017138956568135816".equals(ipNum.toString()));

		ipString = "::1";
		ipNum = IPUtils.convertIPToNumber(ipString);
		assertTrue("1".equals(ipNum.toString()));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConvertIPToNumberNegative() throws UnknownHostException
	{
		String ipString = "123.456.789.-123";

		@SuppressWarnings("unused")
		BigInteger num = IPUtils.convertIPToNumber(ipString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertIPToNumberInvalidParameter() throws UnknownHostException
	{
		String ipString = "123.456.789";

		IPUtils.convertIPToNumber(ipString);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConvertIPToNumberNonNumericInput() throws UnknownHostException
	{
		String ipString = "123.456.789.abc";

		IPUtils.convertIPToNumber(ipString);
	}

	@Test(expected = UnknownHostException.class)
	public void testConvertIPToNumberInvalidIPv6() throws UnknownHostException
	{
		String ipString = ":1:2::4:5::6::7";

		IPUtils.convertIPToNumber(ipString);
	}

	@Test(expected = UnknownHostException.class)
	public void testConvertIPToNumberInvalidIPv6_2() throws UnknownHostException
	{
		String ipString = "1200:0000:AB00:1234:O00Z:2552:7777:1313";

		IPUtils.convertIPToNumber(ipString);
	}
}
