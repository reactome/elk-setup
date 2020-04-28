package org.reactome.logstash.plugins.usagetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;
import org.logstash.plugins.ContextImpl;
import org.reactome.logstash.plugins.usagetype.util.IPRangeTree;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.FilterMatchListener;

public class TestUsageTypePlugin
{

	private Configuration config;
	private Context context;
	private UsageType usageTypefilter;

	class TestMatchListener implements FilterMatchListener
	{
		private AtomicInteger matchCount = new AtomicInteger(0);

		@Override
		public void filterMatched(Event event)
		{
			matchCount.incrementAndGet();
		}

		public int getMatchCount()
		{
			return matchCount.get();
		}
	}

	@Before
	public void set()
	{
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(UsageType.IP_USAGE_TYPE_FILE_FIELD_NAME, "sample_usage_type_file.csv");
		configMap.put(UsageType.START_INDEX, 0l);
		configMap.put(UsageType.END_INDEX, new Long(1));
		configMap.put(UsageType.USAGE_TYPE_INDEX, 9l);
		configMap.put(UsageType.IP_FIELD_NAME, "ip");
		this.config = new ConfigurationImpl(configMap);

		this.context = new ContextImpl(null, null);

		this.usageTypefilter = new UsageType("test_id", config, context);
	}

	@Test
	public void testPlugin()
	{
		Event e = new org.logstash.Event();
		TestMatchListener matchListener = new TestMatchListener();

		// Of course, you should verify that this IP *is* in the file before running the test.
		// In my sample file, this IP's usage type is "ISP".
		e.setField(UsageType.IP_FIELD_NAME, "104.145.10.202");
		Collection<Event> results = usageTypefilter.filter(Collections.singletonList(e), matchListener);

		assertEquals(1, results.size());
		assertEquals("ISP", e.getField(UsageType.USAGE_TYPE_FIELD_NAME));
		assertEquals(1, matchListener.getMatchCount());
	}

	@Test
	public void testPluginInvalidIP()
	{
		Event e = new org.logstash.Event();
		TestMatchListener matchListener = new TestMatchListener();

		// Of course, you should verify that this IP is NOT in the file before running the test.
		e.setField(UsageType.IP_FIELD_NAME, "177.243.23.105");
		Collection<Event> results = usageTypefilter.filter(Collections.singletonList(e), matchListener);

		assertEquals(1, results.size());
		assertEquals(IPRangeTree.UNKNOWN_USAGE_TYPE, e.getField(UsageType.USAGE_TYPE_FIELD_NAME));
		assertEquals(1, matchListener.getMatchCount());
	}

	@Test
	public void testPluginNullIP()
	{
		Event e = new org.logstash.Event();
		TestMatchListener matchListener = new TestMatchListener();

		// a NULL value will results in NULL usage_type
		e.setField(UsageType.IP_FIELD_NAME, null);
		Collection<Event> results = usageTypefilter.filter(Collections.singletonList(e), matchListener);

		assertEquals(1, results.size());
		assertNull(e.getField(UsageType.USAGE_TYPE_FIELD_NAME));
		assertEquals(0, matchListener.getMatchCount());
	}

}
