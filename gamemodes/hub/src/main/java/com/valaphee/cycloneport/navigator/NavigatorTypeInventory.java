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

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.communication.packet.PlayerMessagePacket;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cyclone.server.data.ServerRepository;
import com.valaphee.cycloneport.CyclonePortGP;
import com.valaphee.cycloneport.hub.HubModule;
import com.valaphee.cycloneport.util.InventoryUtil;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.*;
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
import org.bukkit.inventory.ItemStack;

/**
 * Default
 *
 * @author valaphee
 */
public class NavigatorTypeInventory
		implements Listener, InventoryHolder
{
	private final NavigatorModule module;
	private final Inventory inventory;
	private final String title;
	private final ServerRepository repository;
	private final String name;
	private final ItemStack item;
	private final Map<String, UUID> servers = new LinkedHashMap<>();
	private final Map<UUID, Queue<String>> queues = new HashMap<>();

	public NavigatorTypeInventory(final NavigatorModule module, final String name, final String urn, final ItemStack item)
	{
		this.module = module;
		inventory = module.getServer().createInventory(null, 27, title = "\u00A77\u00BB \u00A7cNavigator \u00A77\u00BB \u00A7c" + name);
		repository = new ServerRepository(Cyclone.getInstance().getRedisPool(), Cyclone.getInstance().getSerializer(), urn);
		this.name = name;
		this.item = item;
	}

	@Override
	public Inventory getInventory()
	{
		return inventory;
	}

	public String getName()
	{
		return name;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public void show(final Player player)
	{
		final Inventory presentationInventory = module.getServer().createInventory(null, inventory.getSize(), title);
		InventoryUtil.transition(CyclonePortGP.getInstance(), presentationInventory, false, inventory.getContents(), module.getInventory().getInventory().getContents(), inventory);
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
			Queue<String> queue = queues.get(server.id);
			if (queue == null)
			{
				queues.put(server.id, queue = new LinkedList<>());
			}

			final String name = this.name + " #" + (i + 1);
			servers.put(name, server.id);

			final List<String> itemLore = new ArrayList<>();
			if (ServerData.State.LOBBY.equals(server.state))
			{
				if (server.stateDescription != null)
				{
					itemLore.add("\u00A77Karte\u00A78\u00BB \u00A7a" + server.stateDescription);
				}
				itemLore.add("\u00A77Spieler\u00A78\u00BB \u00A7a" + server.playerCount);
				itemLore.add("\u00A77Wartende Spieler\u00A78\u00BB \u00A7a" + queues.get(server.id).size());
				itemLore.add("");
				itemLore.add("\u00A7e\u00BB \u00A7lKLICKE ZUM BETRETEN \u00A7e\u00AB");

				for (int j = server.playerLimit - server.playerCount; j >= 0; --j)
				{
					final String player = queue.poll();
					if (player != null)
					{
						Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(0, "switch " + server.id + " " + player));
					}
				}
			}
			else
			{
				itemLore.add("\u00A77Vorgang\u00A78\u00BB \u00A7a" + (server.state == null ? "NOT_SPECIFIED" : server.state));
				itemLore.add("");
				itemLore.add("\u00A7e\u00BB \u00A7lNICHT VERFÜGBAR \u00A7e\u00AB");
			}

			inventory.setItem(i, ItemStackBuilderFactory.editor(this.item).setName(name).setLore(itemLore).build());

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
				if (event.getCurrentItem().getItemMeta() != null)
				{
					final UUID id = servers.get(event.getCurrentItem().getItemMeta().getDisplayName());
					if (player.hasPermission("cycloneport.premium"))
					{
						Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(0, "switch " + id.toString() + " " + player.getName()));
					}
					else
					{
						final Queue<String> queue = queues.get(id);
						if (!queue.contains(player.getName()))
						{
							queue.add(player.getName());
						}
						player.sendMessage(HubModule.PREFIX + "\u00A77Kaufe dir \u00A79Premium\u00A77 um schneller auf \u00A79volle Server\u00A77 zu joinen.");
					}
					player.closeInventory();
				}
			}
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
