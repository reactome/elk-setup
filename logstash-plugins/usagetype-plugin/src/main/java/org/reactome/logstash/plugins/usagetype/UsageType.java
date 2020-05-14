
package org.reactome.logstash.plugins.usagetype;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import org.reactome.logstash.plugins.usagetype.util.IPRangeTree;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.Filter;
import co.elastic.logstash.api.FilterMatchListener;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;

@LogstashPlugin(name = "usage_type")
public class UsageType implements Filter
{
	private String id;

	private static IPRangeTree ipRanges = new IPRangeTree();

	public static final String IP_FIELD_NAME = "ip";
	public static final String IP_USAGE_TYPE_FILE_FIELD_NAME = "ip_ranges_file";
	public static final String START_INDEX = "start_index";
	public static final String END_INDEX = "end_index";
	public static final String USAGE_TYPE_INDEX = "usage_type_index";
	public static final String USAGE_TYPE_FIELD_NAME = "usage_type";

	private static final PluginConfigSpec<String> FILENAME_CONFIG = PluginConfigSpec.stringSetting(IP_USAGE_TYPE_FILE_FIELD_NAME, "ips_to_usage_types.csv");
	private static final PluginConfigSpec<Long> START_INDEX_CONFIG = PluginConfigSpec.numSetting(START_INDEX, 0);
	private static final PluginConfigSpec<Long> END_INDEX_CONFIG = PluginConfigSpec.numSetting(END_INDEX, 1);
	private static final PluginConfigSpec<Long> USAGE_TYPE_INDEX_CONFIG = PluginConfigSpec.numSetting(USAGE_TYPE_INDEX, 9);
	private static final PluginConfigSpec<String> IP_CONFIG = PluginConfigSpec.stringSetting(IP_FIELD_NAME, "");

	private String ipFieldToUse = "";

	public UsageType(final String id, final Configuration configuration, final Context context)
	{
		synchronized(ipRanges)
		{
			if (ipRanges == null || ipRanges.size() == 0)
			{
				ipRanges.setStartIndex(configuration.get(START_INDEX_CONFIG).intValue());
				ipRanges.setEndIndex(configuration.get(END_INDEX_CONFIG).intValue());
				ipRanges.setUsageTypeIndex(configuration.get(USAGE_TYPE_INDEX_CONFIG).intValue());
				ipRanges.populateFromFile(configuration.get(FILENAME_CONFIG));
			}
		}
		this.ipFieldToUse = configuration.get(IP_CONFIG);
		this.id = id;
	}

	@Override
	public Collection<Event> filter(Collection<Event> events, FilterMatchListener matchListener)
	{
		for (Event event : events)
		{
			Object fieldValue = event.getField(this.ipFieldToUse);
			if (fieldValue != null && fieldValue instanceof String)
			{
				String ipString = (String) fieldValue;
				try
				{
					String usageType = ipRanges.getUsageTypeForIP(ipString);
					event.setField(USAGE_TYPE_FIELD_NAME, usageType);
					matchListener.filterMatched(event);
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
			}
		}
		return events;
	}

	@Override
	public Collection<PluginConfigSpec<?>> configSchema()
	{
		// should return a list of all configuration options for this plugin
		return Arrays.asList(FILENAME_CONFIG, START_INDEX_CONFIG, END_INDEX_CONFIG, USAGE_TYPE_INDEX_CONFIG, IP_CONFIG);
	}

	@Override
	public String getId()
	{
		return this.id;
	}
}
