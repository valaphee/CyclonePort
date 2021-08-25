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

package com.valaphee.cycloneport.reflect;

import com.valaphee.cycloneport.util.MinecraftVersion;

/**
 * Default
 *
 * @author valaphee
 */
public enum PackageType
{
	MINECRAFT_SERVER("net.minecraft.server." + MinecraftVersion.getCurrentVersion().getPackageVersion()),
	CRAFTBUKKIT("org.bukkit.craftbukkit." + MinecraftVersion.getCurrentVersion().getPackageVersion()),
	CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
	CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
	CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
	CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
	CRAFTBUKKIT_ENCHANTMENTS(CRAFTBUKKIT, "enchantments"),
	CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
	CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
	CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
	CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
	CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
	CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
	CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
	CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
	CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
	CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
	CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");
	private final String path;

	private PackageType(final String path)
	{
		this.path = path;
	}

	private PackageType(final PackageType parent, final String path)
	{
		this(parent.getPath() + "." + path);
	}

	public String getPath()
	{
		return path;
	}

	public Class<?> getClass(final String name)
			throws ClassNotFoundException
	{
		return Class.forName(path + "." + name);
	}
}
