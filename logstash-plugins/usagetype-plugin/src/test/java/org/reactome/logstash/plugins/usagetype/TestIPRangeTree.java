package org.reactome.logstash.plugins.usagetype;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reactome.logstash.plugins.usagetype.util.IPRangeTree;

@SuppressWarnings("static-method")
public class TestIPRangeTree
{
	private static IPRangeTree tree;

	@BeforeClass
	public static void startup()
	{
		tree = new IPRangeTree("sample_usage_type_file.csv");
		System.out.println(tree.size());
	}

	@Test
	public void testIPRangeTree() throws IllegalArgumentException, UnknownHostException
	{

		Instant start = Instant.now();
		String usageType = tree.getUsageTypeForIP("120.5.163.33");
		assertEquals("ISP/MOB", usageType);
		Instant end = Instant.now();
		System.out.println(Duration.between(start, end).toMillis());

		start = Instant.now();
		usageType = tree.getUsageTypeForIP("200.33.158.14");
		assertEquals("ISP", usageType);
		end = Instant.now();
		System.out.println(Duration.between(start, end).toMillis());

		start = Instant.now();
		usageType = tree.getUsageTypeForIP("192.70.253.27");
		assertEquals("EDU", usageType);
		end = Instant.now();
		System.out.println(Duration.between(start, end).toMillis());
	}

	@Test
	public void testInvalidIPAddress() throws IllegalArgumentException, UnknownHostException
	{
		Instant start = Instant.now();
		String usageType = tree.getUsageTypeForIP("999.999.999.999");
		assertEquals("unknown", usageType);
		Instant end = Instant.now();
		System.out.println(Duration.between(start, end).toMillis());
	}

}
