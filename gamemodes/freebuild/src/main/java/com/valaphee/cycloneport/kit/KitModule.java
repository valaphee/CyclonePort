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

package com.valaphee.cycloneport.kit;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.kit.command.KitsCommand;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class KitModule
		extends Module
{
	private final Map<String, KitTypeInventory> typeInventories = new HashMap<>();
	private KitInventory inventory;

	public KitModule(final JavaPlugin plugin)
	{
		super("Kit", plugin);
	}

	@Override
	public void registerListeners()
	{
		super.registerListeners();

		registerListener(inventory);
		typeInventories.values().forEach(this::registerListener);
	}

	@Override
	public void registerCommands()
	{
		registerCommand(new KitsCommand(this));
	}

	@Override
	public void startup()
	{
		inventory = new KitInventory(this);
	}

	public Collection<KitTypeInventory> getTypeInventories()
	{
		return typeInventories.values();
	}

	public KitTypeInventory getTypeInventory(final String name)
	{
		return typeInventories.get(name);
	}

	public KitInventory getInventory()
	{
		return inventory;
	}
}
