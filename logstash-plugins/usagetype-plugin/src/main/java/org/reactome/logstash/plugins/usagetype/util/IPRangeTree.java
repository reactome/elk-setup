package org.reactome.logstash.plugins.usagetype.util;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class IPRangeTree extends RangeTree<BigInteger>
{
	private static final long serialVersionUID = -8621007191107570320L;
	public static final String UNKNOWN_USAGE_TYPE = "unknown";

	private int startIndex = 0;
	private int endIndex = 1;
	private int usageTypeIndex = 9;

	public IPRangeTree()
	{
	}

	public IPRangeTree(String filename)
	{
		this.populateFromFile(filename);
	}

	public void populateFromFile(String filename)
	{
		try(CSVParser parser = new CSVParser(new FileReader(filename), CSVFormat.DEFAULT))
		{
			for (CSVRecord rec : parser.getRecords())
			{
				Range<BigInteger> range = new Range<>(new BigInteger(rec.get(this.startIndex)), new BigInteger(rec.get(this.endIndex)));
				this.put(range, rec.get(this.usageTypeIndex));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getUsageTypeForIP(String ip) throws IllegalArgumentException, UnknownHostException
	{
		return Optional.ofNullable(this.getForValue(IPUtils.convertIPToNumber(ip))).orElse(UNKNOWN_USAGE_TYPE);
	}

	public int getStartIndex()
	{
		return this.startIndex;
	}

	public void setStartIndex(int startIndex)
	{
		this.startIndex = startIndex;
	}

	public int getEndIndex()
	{
		return this.endIndex;
	}

	public void setEndIndex(int endIndex)
	{
		this.endIndex = endIndex;
	}

	public int getUsageTypeIndex()
	{
		return this.usageTypeIndex;
	}

	public void setUsageTypeIndex(int usageTypeIndex)
	{
		this.usageTypeIndex = usageTypeIndex;
	}
}
