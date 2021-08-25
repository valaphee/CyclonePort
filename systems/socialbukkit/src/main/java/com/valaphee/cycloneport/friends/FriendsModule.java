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

package com.valaphee.cycloneport.friends;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.friends.command.FriendsCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class FriendsModule
		extends Module
{
	private FriendsInventory inventory;

	public FriendsModule(final JavaPlugin plugin)
	{
		super("Friends", plugin);
	}

	@Override
	public void registerListeners()
	{
		super.registerListeners();

		registerListener(inventory);
	}

	@Override
	public void registerCommands()
	{
		registerCommand(new FriendsCommand(this));
	}

	@Override
	public void startup()
	{
		inventory = new FriendsInventory(this);
	}

	public FriendsInventory getInventory()
	{
		return inventory;
	}
}
