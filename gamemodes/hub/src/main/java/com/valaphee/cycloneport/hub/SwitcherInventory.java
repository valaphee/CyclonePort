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

package com.valaphee.cycloneport.hub;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.communication.packet.PlayerMessagePacket;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cyclone.server.data.ServerRepository;
import com.valaphee.cycloneport.CyclonePortGP;
import com.valaphee.cycloneport.util.InventoryUtil;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.*;
import org.bukkit.Material;
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
public class SwitcherInventory
		implements Listener, InventoryHolder
{
	private final HubModule module;
	private final Inventory inventory;
	private final String title = "\u00A77\u00BB \u00A74Switcher";
	private final UUID self = Cyclone.getInstance().getConnectionManager().getId();
	private final ServerRepository repository = new ServerRepository(Cyclone.getInstance().getRedisPool(), Cyclone.getInstance().getSerializer(), "spigot:hub");
	private final Map<String, UUID> servers = new LinkedHashMap<>();

	@SuppressWarnings("LeakingThisInConstructor")
	public SwitcherInventory(final HubModule module)
	{
		this.module = module;
		inventory = module.getServer().createInventory(this, 27, title);
	}

	@Override
	public Inventory getInventory()
	{
		return inventory;
	}

	public void show(final Player player)
	{
		final Inventory presentationInventory = module.getServer().createInventory(null, inventory.getSize(), title);
		InventoryUtil.transition(CyclonePortGP.getInstance(), presentationInventory, true, inventory.getContents(), null, inventory);
		player.openInventory(presentationInventory);
		player.playSound(player.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.0f);
	}

	public void update()
	{
		inventory.clear();
		servers.clear();

		int i = 0;
		for (final ServerData server : repository.getCachedServers().values())
		{
			final String name = "Hub #" + (i + 1);
			servers.put(name, server.id);

			Material material = Material.GUNPOWDER;
			final List<String> itemLore = new ArrayList<>();
			if (ServerData.State.LOBBY.equals(server.state))
			{
				if (self.equals(server.id))
				{
					material = Material.SUGAR;
				}
				itemLore.add("\u00A77Spieler\u00A78» \u00A7a" + server.playerCount);
				itemLore.add("");
				itemLore.add("\u00A7e» \u00A7lKLICKE ZUM BETRETEN \u00A7e«");
			}
			else
			{
				material = Material.REDSTONE;
				itemLore.add("\u00A77Vorgang\u00A78\u00BB \u00A7a" + (server.state == null ? "NOT_SPECIFIED" : server.state));
				itemLore.add("");
				itemLore.add("\u00A7e\u00BB \u00A7lNICHT VERFÜGBAR \u00A7e\u00AB");
			}

			inventory.setItem(i, ItemStackBuilderFactory.builder().setMaterial(material).setName(name).setLore(itemLore).build());

			i++;
		}
	}

	@EventHandler
	@SuppressWarnings("null")
	public void on(final InventoryClickEvent event)
	{
		if (title.equals(event.getView().getTitle()))
		{
			final Player player = (Player) event.getWhoClicked();
			if (ClickType.LEFT.equals(event.getClick()) && InventoryType.SlotType.CONTAINER.equals(event.getSlotType()))
			{
				if (Material.GUNPOWDER.equals(event.getCurrentItem().getType()))
				{
					Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(0, "switch " + servers.get(event.getCurrentItem().getItemMeta().getDisplayName()) + " " + player.getName()));
					player.closeInventory();
				}
			}
			else
			{
				player.closeInventory();
			}
			event.setResult(Event.Result.DENY);
		}
	}
}
