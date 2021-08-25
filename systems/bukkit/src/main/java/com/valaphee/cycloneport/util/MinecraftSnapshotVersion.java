/*
 * Copyright (c) by Valaphee 2019.
 *
 * Licensed under the 4-clause BSD license (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://deploy.valaphee.com/license/BSD-4-Clause.txt
 *
 * THIS SOFTWARE IS PROVIDED BY VALAPHEE "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.
 */

package com.valaphee.cycloneport.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class MinecraftSnapshotVersion
		implements Comparable<MinecraftSnapshotVersion>
{
	private static final Pattern SNAPSHOT_VERSION_PATTERN = Pattern.compile("(\\d{2}w\\d{2})([a-z])");
	private static final DateFormat SNAPSHOT_DATE_FORMAT = new SimpleDateFormat("yy'w'ww", Locale.US);
	final Date date;
	final int revision;
	private String snapshotVersion;

	public MinecraftSnapshotVersion(final String snapshotVersion)
	{
		final Matcher matcher = SNAPSHOT_VERSION_PATTERN.matcher(snapshotVersion.trim());
		if (matcher.matches())
		{
			try
			{
				date = SNAPSHOT_DATE_FORMAT.parse(matcher.group(1));
				revision = matcher.group(2).charAt(0) - 'a';
			}
			catch (final ParseException ex)
			{
				throw new IllegalArgumentException("Date implied by snapshot version is invalid.", ex);
			}
		}
		else
		{
			throw new IllegalArgumentException("Cannot parse " + snapshotVersion + " as a snapshot version.");
		}

		this.snapshotVersion = snapshotVersion;
	}

	public MinecraftSnapshotVersion(final Date date, final int revision)
	{
		this.date = date;
		this.revision = revision;
	}

	public Date getDate()
	{
		return date;
	}

	public int getRevision()
	{
		return revision;
	}

	public String getSnapshotVersion()
	{
		if (snapshotVersion == null)
		{
			final Calendar calendar = Calendar.getInstance(Locale.US);
			calendar.setTime(date);
			snapshotVersion = String.format("%02dw%02d%s", calendar.get(Calendar.YEAR) % 100, calendar.get(Calendar.WEEK_OF_YEAR), (char) ('a' + revision));
		}

		return snapshotVersion;
	}

	@Override
	public int compareTo(final MinecraftSnapshotVersion other)
	{
		return 0;
	}

	@Override
	public boolean equals(final Object object)
	{
		if (object == this)
		{
			return true;
		}
		if (object instanceof MinecraftSnapshotVersion)
		{
			final MinecraftSnapshotVersion other = (MinecraftSnapshotVersion) object;

			return date.equals(other.date) && (revision == other.revision);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 67 * hash + Objects.hashCode(date);
		hash = 67 * hash + revision;

		return hash;
	}

	@Override
	public String toString()
	{
		return getSnapshotVersion();
	}

	static
	{
		SNAPSHOT_DATE_FORMAT.setLenient(true);
	}
}
