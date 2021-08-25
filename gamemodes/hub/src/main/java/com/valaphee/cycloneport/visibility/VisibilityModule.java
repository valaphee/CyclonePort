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

package com.valaphee.cycloneport.visibility;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class VisibilityModule
		extends Module
{
	private static final int SLOT = 0;
	@SuppressWarnings("deprecation")
	private static final ItemStack ALL_VISIBLE_ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.INK_SAC).setData(DyeColor.LIME.getDyeData()).setName("\u00A7aSichtbarkeit \u00A77\u00A7o<Jeder>").build();
	@SuppressWarnings("deprecation")
	private static final ItemStack VIP_VISIBLE_ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.INK_SAC).setData(DyeColor.PURPLE.getDyeData()).setName("\u00A7aSichtbarkeit \u00A77\u00A7o<VIPs>").build();
	@SuppressWarnings("deprecation")
	private static final ItemStack NON_VISIBLE_ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.INK_SAC).setData(DyeColor.GRAY.getDyeData()).setName("\u00A7aSichtbarkeit \u00A77\u00A7o<Niemand>").build();
	private final Map<UUID, Visibility> visibilities = new HashMap<>();
	private final Plugin plugin;

	public VisibilityModule(final JavaPlugin plugin)
	{
		super("Visibility", plugin);

		this.plugin = plugin;
	}

	public Visibility getVisibility(final Player player)
	{
		if (visibilities.containsKey(player.getUniqueId()))
		{
			return visibilities.get(player.getUniqueId());
		}

		return Visibility.ALL;
	}

	public void setVisibility(final Player player, final Visibility visibility)
	{
		visibilities.put(player.getUniqueId(), visibility);
	}

	public void showAllPlayers(final Player player)
	{
		getServer().getOnlinePlayers().stream().filter((target) -> (!target.equals(player))).forEachOrdered((target) ->
		{
			player.showPlayer(plugin, target);
		});
	}

	public void showVIPPlayers(final Player player)
	{
		getServer().getOnlinePlayers().stream().filter((target) -> (!target.equals(player))).forEachOrdered((target) ->
		{
			if (target.hasPermission("cycloneport.hub.vip"))
			{
				player.showPlayer(plugin, target);
			}
			else
			{
				player.playEffect(target.getLocation(), Effect.ENDER_SIGNAL, null);
				player.hidePlayer(plugin, target);
			}
		});
	}

	public void showNonPlayer(final Player player)
	{
		getServer().getOnlinePlayers().stream().filter((target) -> (!target.equals(player))).forEachOrdered((target) ->
		{
			player.playEffect(target.getLocation(), Effect.ENDER_SIGNAL, null);
			player.hidePlayer(plugin, target);
		});
	}

	public void showForOthers(final Player target)
	{
		for (final Player player : getServer().getOnlinePlayers())
		{
			if (!player.equals(target))
			{
				switch (getVisibility(player))
				{
				case ALL:
					player.showPlayer(plugin, target);
					break;
				case VIP:
					if (target.hasPermission("cycloneport.hub.vip"))
					{
						player.showPlayer(plugin, target);
					}
					else
					{
						player.hidePlayer(plugin, target);
					}
					break;
				case NON:
					player.hidePlayer(plugin, target);
				}
			}
		}
	}

	@EventHandler
	public void on(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		switch (getVisibility(player))
		{
		case ALL:
			player.getInventory().setItem(SLOT, ALL_VISIBLE_ITEM);
			showAllPlayers(player);
			break;
		case VIP:
			player.getInventory().setItem(SLOT, VIP_VISIBLE_ITEM);
			showVIPPlayers(player);
			break;
		case NON:
			player.getInventory().setItem(SLOT, NON_VISIBLE_ITEM);
			showNonPlayer(player);
			break;
		}

		showForOthers(player);
	}

	@EventHandler
	public void on(final PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();

		if (player.getInventory().getHeldItemSlot() == SLOT)
		{
			if (Action.RIGHT_CLICK_BLOCK.equals(event.getAction()) || Action.RIGHT_CLICK_AIR.equals(event.getAction()))
			{
				if (NON_VISIBLE_ITEM.equals(player.getInventory().getItemInMainHand()))
				{
					player.getInventory().setItemInMainHand(ALL_VISIBLE_ITEM);
					setVisibility(player, VisibilityModule.Visibility.ALL);
					showAllPlayers(player);
					player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5.0F, 5.0F);
					event.setCancelled(true);
				}
				else if (ALL_VISIBLE_ITEM.equals(player.getInventory().getItemInMainHand()))
				{
					player.getInventory().setItemInMainHand(VIP_VISIBLE_ITEM);
					setVisibility(player, VisibilityModule.Visibility.VIP);
					showVIPPlayers(player);
					player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5.0F, 5.0F);
					event.setCancelled(true);
				}
				else if (VIP_VISIBLE_ITEM.equals(player.getInventory().getItemInMainHand()))
				{
					player.getInventory().setItemInMainHand(NON_VISIBLE_ITEM);
					setVisibility(player, VisibilityModule.Visibility.NON);
					showNonPlayer(player);
					player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5.0F, 5.0F);
					event.setCancelled(true);
				}
			}
		}
	}

	public static enum Visibility
	{
		ALL, VIP, NON;
	}
}
