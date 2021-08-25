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

import com.valaphee.cycloneport.CyclonePortSP;
import com.valaphee.cycloneport.util.InventoryUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * Default
 *
 * @author valaphee
 */
public class FriendsInventory
		implements Listener
{
	private final FriendsModule module;
	private final Inventory inventory;
	private final String title = "\u00A77\u00BB \u00A73Friends";

	public FriendsInventory(final FriendsModule module)
	{
		this.module = module;
		inventory = module.getServer().createInventory(null, 27, "\u00A77\u00BB \u00A73Friends");
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public void show(final Player player)
	{
		final Inventory presentationInventory = module.getServer().createInventory(null, inventory.getSize(), title);
		InventoryUtil.transition(CyclonePortSP.getInstance(), presentationInventory, true, inventory.getContents(), null, inventory);
		player.openInventory(presentationInventory);
		player.playSound(player.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.0f);
	}

	@EventHandler
	public void on(final InventoryClickEvent event)
	{
		if (title.equals(event.getView().getTitle()))
		{
			final Player player = (Player) event.getWhoClicked();
			if (ClickType.LEFT.equals(event.getClick()) && InventoryType.SlotType.CONTAINER.equals(event.getSlotType()))
			{
			}
			else if (ClickType.RIGHT.equals(event.getClick()))
			{
			}
			else
			{
				player.closeInventory();
			}
			event.setResult(Event.Result.DENY);
		}
	}

	@EventHandler
	public void on(final InventoryDragEvent event)
	{
		if (title.equals(event.getView().getTitle()))
		{
			event.setCancelled(true);
		}
	}
}
