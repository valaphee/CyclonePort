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

package com.valaphee.cycloneport.gadget;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.Collection;
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
public final class GadgetModule
		extends Module
{
	private static final int SLOT = 7;
	private static final ItemStack ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.CHEST).setName("\u00A73Gadgets").build();
	private final Map<String, GadgetTypeInventory> typeInventories = new HashMap<>();
	private GadgetInventory inventory;

	public GadgetModule(final JavaPlugin plugin)
	{
		super("Gadget", plugin);
	}

	@Override
	public void registerListeners()
	{
		super.registerListeners();

		registerListener(inventory);
		typeInventories.values().forEach(this::registerListener);
	}

	@Override
	public void startup()
	{
		for (final Gadget.Type type : Gadget.Type.values())
		{
			typeInventories.put(type.getName(), new GadgetTypeInventory(this, type));
		}
		inventory = new GadgetInventory(this);
	}

	public Collection<GadgetTypeInventory> getTypeInventories()
	{
		return typeInventories.values();
	}

	public GadgetTypeInventory getTypeInventory(final String name)
	{
		return typeInventories.get(name);
	}

	public GadgetInventory getInventory()
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
