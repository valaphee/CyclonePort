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

package com.valaphee.cycloneport.settings;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class SettingsModule
		extends Module
{
	private static final int SLOT = 1;
	private static final ItemStack ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.REPEATER).setName("\u00A72Einstellungen").build();
	private SettingsInventory inventory;

	public SettingsModule(final JavaPlugin plugin)
	{
		super("Settings", plugin);

		inventory = new SettingsInventory(this);
	}

	@Override
	public void startup()
	{
		inventory = new SettingsInventory(this);
	}

	@Override
	public void registerListeners()
	{
		super.registerListeners();

		registerListener(inventory);
	}

	public SettingsInventory getInventory()
	{
		return inventory;
	}

	@EventHandler
	public void on(final PlayerJoinEvent event)
	{
		event.getPlayer().getInventory().setItem(SLOT, ITEM);
	}

	@EventHandler
	public void on(final PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		if (player.getInventory().getHeldItemSlot() == SLOT)
		{
			if (Action.RIGHT_CLICK_BLOCK.equals(event.getAction()) || Action.RIGHT_CLICK_AIR.equals(event.getAction()))
			{
				inventory.show(player);
				event.setCancelled(true);
			}
		}
	}
}
