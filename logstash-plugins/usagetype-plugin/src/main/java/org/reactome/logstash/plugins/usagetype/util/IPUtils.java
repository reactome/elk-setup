package org.reactome.logstash.plugins.usagetype.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public final class IPUtils
{
	private static final long MULT_FOR_FIRST_OCTET = 256 << 16;

	private static final long MULT_FOR_SECOND_OCTET = 256 << 8;

	private static final Pattern ipv4Pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

	private IPUtils () {}

	/**
	 * Converts an IP address to a numeric value. BigInteger is used to return the number, as IPv6 addresses may be too long to fit in a Java primitive.
	 * @param ipString The IP address, as a String.
	 * @return A BigInteger which contains the numeric value of the IP address.
	 * @throws IllegalArgumentException If the IPv4 address is not correctly structured.
	 * @throws UnknownHostException When converting an IPv6 address, Java InetAddress could throw UnknownHostException
	 */
	public static BigInteger convertIPToNumber(String ipString) throws IllegalArgumentException, UnknownHostException
	{
		// Check if this is IPv4 or IPv6 - IPv4 won't contain any ":" characters.
		if (!ipString.contains(":"))
		{
			// Regex matching catches too many/few octet blocks, non-numeric values, incorrect symbols.
			// If you don't use regexes, you need to validate all of the possible situations independently.
			if (!ipv4Pattern.matcher(ipString).matches())
			{
				throw new IllegalArgumentException("Input string was not formatted correctly. Should be formatted as ###.###.###.### (regular expression: \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) ; Input given was: " + ipString);
			}
			String[] octets = ipString.split("\\.");
			long ipAsNumber = (Long.parseLong(octets[0]) * MULT_FOR_FIRST_OCTET) // 2^24 = 16777216
							+ (Long.parseLong(octets[1]) * MULT_FOR_SECOND_OCTET) // 2^16 = 65536
							+ (Long.parseLong(octets[2]) * (256)) // 2^8 = 256
							+ (Long.parseLong(octets[3]));
			return BigInteger.valueOf( ipAsNumber );
		}
		else
		{
			// Handle IPv6 addresses. They are a little more complicated and I don't have time to code it, let InetAddress handle it, for now.
			InetAddress address = InetAddress.getByName(ipString);
			return new BigInteger(address.getAddress());
		}
	}
}
