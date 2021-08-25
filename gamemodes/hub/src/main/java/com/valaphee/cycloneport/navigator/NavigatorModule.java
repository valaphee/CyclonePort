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

package com.valaphee.cycloneport.navigator;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.HashMap;
import java.util.Map;
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
public final class NavigatorModule
		extends Module
{
	private static final int SLOT = 3;
	private static final ItemStack ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.COMPASS).setName("\u00A7cNavigator").build();
	private NavigatorInventory inventory;
	private final Map<String, NavigatorTypeInventory> typeInventories = new HashMap<>();

	public NavigatorModule(final JavaPlugin plugin)
	{
		super("Navigator", plugin);
	}

	@Override
	public void startup()
	{
		typeInventories.put("FreeBuild", new NavigatorTypeInventory(this, "FreeBuild", "spigot:freebuild", ItemStackBuilderFactory.builder().setMaterial(Material.GRASS).setName("FreeBuild").build()));
		typeInventories.put("CityBuild", new NavigatorTypeInventory(this, "CityBuild", "spigot:citybuild", ItemStackBuilderFactory.builder().setMaterial(Material.IRON_AXE).setName("CityBuild").build()));
		typeInventories.put("Creative", new NavigatorTypeInventory(this, "Creative", "spigot:creative", ItemStackBuilderFactory.builder().setMaterial(Material.COMMAND_BLOCK).setName("Creative").build()));
		inventory = new NavigatorInventory(this);

		runTaskSynchronously(() ->
		{
			typeInventories.values().forEach((typeInventory) -> typeInventory.update());
		}, 0L, 50L);
	}

	@Override
	public void registerListeners()
	{
		super.registerListeners();

		registerListener(inventory);
		typeInventories.values().forEach(this::registerListener);
	}

	public NavigatorInventory getInventory()
	{
		return inventory;
	}

	public NavigatorTypeInventory getTypeInventory(final String name)
	{
		return typeInventories.get(name);
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
