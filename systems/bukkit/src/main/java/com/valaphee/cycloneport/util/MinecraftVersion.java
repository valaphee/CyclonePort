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

import com.google.common.base.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

/**
 * Default
 *
 * @author valaphee
 */
public final class MinecraftVersion
		implements Comparable<MinecraftVersion>
{
	private static final Pattern VERSION_PATTERN = Pattern.compile(".*\\(.*MC.\\s*([a-zA-z0-9\\-\\.]+)\\s*\\)");
	public static final MinecraftVersion BOUNTIFUL_UPDATE = new MinecraftVersion("1.8");
	public static final MinecraftVersion SKIN_UPDATE = new MinecraftVersion("1.7.8");
	public static final MinecraftVersion WORLD_UPDATE = new MinecraftVersion("1.7.2");
	public static final MinecraftVersion HORSE_UPDATE = new MinecraftVersion("1.6.1");
	public static final MinecraftVersion REDSTONE_UPDATE = new MinecraftVersion("1.5.0");
	public static final MinecraftVersion SCARY_UPDATE = new MinecraftVersion("1.4.2");
	private static MinecraftVersion currentVersion;
	final int major;
	final int minor;
	final int build;
	final String developmentStage;
	final MinecraftSnapshotVersion snapshot;
	private String version;
	private String packageVersion;

	public static MinecraftVersion getCurrentVersion()
	{
		if (currentVersion == null)
		{
			final Matcher version = VERSION_PATTERN.matcher(Bukkit.getVersion());
			if (version.matches() && (version.group(1) != null))
			{
				currentVersion = new MinecraftVersion(version.group(1));
			}
			else
			{
				throw new IllegalStateException("Cannot parse version " + Bukkit.getBukkitVersion() + ".");
			}
		}

		return currentVersion;
	}

	public MinecraftVersion(final String version)
	{
		final String[] section = version.split("-");
		final int[] numbers = new int[3];
		MinecraftSnapshotVersion snapshot = null;
		try
		{
			final String[] elements = section[0].split("\\.");
			for (int i = 0; i < Math.min(numbers.length, elements.length); ++i)
			{
				numbers[i] = Integer.parseInt(elements[i].trim());
			}
		}
		catch (final NumberFormatException ex)
		{
			snapshot = new MinecraftSnapshotVersion(section[0]);
		}

		this.major = numbers[0];
		this.minor = numbers[1];
		this.build = numbers[2];
		developmentStage = section.length > 1 ? section[1] : (snapshot != null ? "snapshot" : null);
		this.snapshot = snapshot;
		this.version = version;
	}

	public MinecraftVersion(final int major, final int minor, final int build)
	{
		this(major, minor, build, null);
	}

	public MinecraftVersion(final int major, final int minor, final int build, final String developmentStage)
	{
		this.major = major;
		this.minor = minor;
		this.build = build;
		this.developmentStage = developmentStage;
		snapshot = null;
	}

	public int getMajor()
	{
		return major;
	}

	public int getMinor()
	{
		return minor;
	}

	public int getBuild()
	{
		return build;
	}

	public String getDevelopmentStage()
	{
		return developmentStage;
	}

	public boolean isSnapshot()
	{
		return snapshot != null;
	}

	public MinecraftSnapshotVersion getSnapshot()
	{
		return snapshot;
	}

	public String getVersion()
	{
		if (version == null)
		{
			if (developmentStage == null)
			{
				version = String.format("%s.%s.%s", major, minor, build);
			}
			else
			{
				version = String.format("%s.%s.%s-%s%s", major, minor, build, developmentStage, isSnapshot() ? snapshot : "");
			}
		}

		return version;
	}

	public String getPackageVersion()
	{
		if (packageVersion == null)
		{
			packageVersion = String.format("v%s_%s_R1", major, minor);
		}

		return packageVersion;
	}

	@Override
	public int compareTo(final MinecraftVersion other)
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
		if (object instanceof MinecraftVersion)
		{
			final MinecraftVersion other = (MinecraftVersion) object;

			return (major == other.major) && (minor == other.minor) && (build == other.build) && Objects.equal(developmentStage, other.developmentStage) && Objects.equal(snapshot, other.snapshot);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 73 * hash + major;
		hash = 73 * hash + minor;
		hash = 73 * hash + build;
		hash = 73 * hash + java.util.Objects.hashCode(developmentStage);
		hash = 73 * hash + java.util.Objects.hashCode(snapshot);

		return hash;
	}

	@Override
	public String toString()
	{
		return String.format("(MC: %s)", getVersion());
	}
}
