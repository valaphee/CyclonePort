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

import com.valaphee.cycloneport.CyclonePortGP;
import com.valaphee.cycloneport.util.InventoryUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Default
 *
 * @author valaphee
 */
public class GadgetTypeInventory
		implements Listener, InventoryHolder
{
	private final GadgetModule module;
	private final Inventory inventory;
	private final String title;
	private final Gadget.Type type;

	@SuppressWarnings("LeakingThisInConstructor")
	public GadgetTypeInventory(final GadgetModule module, final Gadget.Type type)
	{
		this.module = module;
		inventory = module.getServer().createInventory(this, 27, title = "\u00A77\u00BB \u00A73Gadgets \u00A77\u00BB \u00A73" + type.getName());
		this.type = type;
	}

	@Override
	public Inventory getInventory()
	{
		return inventory;
	}

	public Gadget.Type getType()
	{
		return type;
	}

	public void show(final Player player)
	{
		final Inventory presentationInventory = module.getServer().createInventory(null, inventory.getSize(), title);
		InventoryUtil.transition(CyclonePortGP.getInstance(), presentationInventory, false, inventory.getContents(), module.getInventory().getInventory().getContents(), inventory);
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
			{}
			else if (ClickType.RIGHT.equals(event.getClick()))
			{
				module.getInventory().show(player);
			}
			else
			{
				player.closeInventory();
			}
			event.setResult(Event.Result.DENY);
		}
	}
}
