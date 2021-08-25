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
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.status.StatusManager;
import com.valaphee.cycloneport.CyclonePortCPIP;
import com.valaphee.cycloneport.CyclonePortGP;
import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.economy.EconomyModule;
import com.valaphee.cycloneport.hub.command.GameModeCommand;
import com.valaphee.cycloneport.sidebar.SidebarModule;
import com.valaphee.cycloneport.teleport.TeleportModule;
import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class HubModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bHub \u00A78\u00A7l\u25CF ";
	private static final int SLOT = 5;
	private static final ItemStack ITEM = ItemStackBuilderFactory.builder().setMaterial(Material.CLOCK).setName("\u00A7cSwitcher").build();
	private final List<String> splashes = new ArrayList<>();
	private SwitcherInventory switcherInventory;

	public HubModule(final JavaPlugin plugin)
	{
		super("Hub", plugin);
	}

	public List<String> getSplashes()
	{
		return Collections.unmodifiableList(splashes);
	}

	@Override
	public void startup()
	{
		final SidebarModule sidebarModule = (SidebarModule) CyclonePortCPIP.getInstance().getModule("Sidebar");
		sidebarModule.addObserver("\u00A7aLobby", (player) ->
		{
			return "Keine Angabe";
		});
		final AccountManager accountManager = Cyclone.getInstance().getAccountManager();
		final PermissionHandler permissionHandler = Cyclone.getInstance().getPermissionHandler();
		sidebarModule.addObserver("\u00A7bRang", (player) ->
		{
			final User user = accountManager.getUser(accountManager.findUserId(player.getUniqueId()));
			final String color = "\u00A7" + permissionHandler.getColor(user).getCharacter();
			final String prefix = permissionHandler.getPrefix(user);

			if (prefix != null)
			{
				return color + prefix;
			}

			return null;
		});
		final EconomyModule economyModule = (EconomyModule) CyclonePortCPIP.getInstance().getModule("Economy");
		sidebarModule.addObserver("\u00A7cGeld", (player) ->
		{
			return EconomyModule.displayCurrency(economyModule.getCurrency(player.getUniqueId()));
		});
		final StatusManager statusManager = Cyclone.getInstance().getStatusManager();
		sidebarModule.addObserver("\u00A7eSpieler", (player) ->
		{
			return statusManager.getPlayerCount() + "/" + statusManager.getPlayerLimit();
		});

		splashes.add("Dein Spielenetzwerk");
		splashes.add("Es wurden eine menge Bugfixes gemacht");
		splashes.add("Hier könnte ihre Werbung stehen");

		switcherInventory = new SwitcherInventory(this);

		runTaskSynchronously(() ->
		{
			switcherInventory.update();
		}, 0L, 60L);
		runTaskSynchronously(new Clock(this), 0L, 20L);
	}

	@Override
	public void registerListeners()
	{
		super.registerListeners();

		registerListener(switcherInventory);
	}

	@Override
	public void registerCommands()
	{
		final TeleportModule teleportModule = (TeleportModule) CyclonePortCPIP.getInstance().getModule("Teleport");
		teleportModule.registerTeleportCommands();
		teleportModule.registerSpawnCommand();
		registerCommand(new GameModeCommand(this));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		player.setHealthScale(6.0f);
		player.setExp(0.0f);
		player.setTotalExperience(0);
		player.getInventory().clear();
		player.getInventory().setHeldItemSlot(4);
		player.teleport(new Location(player.getWorld(), 0, 100, 0, 0, 0));

		new Splash(this, player).runTaskTimer(CyclonePortGP.getInstance(), 80L, 40L);

		player.getInventory().setItem(SLOT, ITEM);

		if (player.hasPermission("cycloneport.premium"))
		{
			event.setJoinMessage(player.getDisplayName() + "§6§l hat den Server betreten");
		}
		else
		{
			event.setJoinMessage(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		if (player.hasPermission("cycloneport.premium"))
		{
			event.setQuitMessage(player.getDisplayName() + "§6§l hat den Server verlassen");
		}
		else
		{
			event.setQuitMessage(null);
		}
	}

	@EventHandler
	public void on(final BlockBurnEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockDamageEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockDispenseEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockExplodeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockFadeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockFormEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockFromToEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockGrowEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockIgniteEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockPhysicsEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockPistonExtendEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockPistonRetractEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockSpreadEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final LeavesDecayEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final ThunderChangeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final WeatherChangeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	@SuppressWarnings("null")
	public void on(final PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();

		if (Action.RIGHT_CLICK_BLOCK.equals(event.getAction()))
		{
			switch (event.getClickedBlock().getType())
			{
			case NOTE_BLOCK:
			case LEGACY_BED:
			case LEGACY_WORKBENCH:
			case FURNACE:
			case LEGACY_BURNING_FURNACE:
			case LEGACY_WOODEN_DOOR:
			case LEVER:
			case STONE_BUTTON:
			case JUKEBOX:
			case LEGACY_DIODE_BLOCK_OFF:
			case LEGACY_DIODE_BLOCK_ON:
			case LEGACY_ENCHANTMENT_TABLE:
			case BREWING_STAND:
			case CAULDRON:
			case DRAGON_EGG:
			case ENDER_CHEST:
			case LEGACY_COMMAND:
			case LEGACY_WOOD_BUTTON:
			case ANVIL:
			case TRAPPED_CHEST:
			case LEGACY_REDSTONE_COMPARATOR_OFF:
			case LEGACY_REDSTONE_COMPARATOR_ON:
			case HOPPER:
			case DROPPER:
			case SPRUCE_DOOR:
			case BIRCH_DOOR:
			case JUNGLE_DOOR:
			case ACACIA_DOOR:
			case DARK_OAK_DOOR:
				event.setCancelled(true);
			}
		}
		else if (Action.LEFT_CLICK_BLOCK.equals(event.getAction()))
		{
			switch (event.getClickedBlock().getType())
			{
			case ITEM_FRAME:
				event.setCancelled(true);
			}
		}

		if (player.getInventory().getHeldItemSlot() == SLOT)
		{
			if (Action.RIGHT_CLICK_BLOCK.equals(event.getAction()) || Action.RIGHT_CLICK_AIR.equals(event.getAction()))
			{
				switcherInventory.show(player);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(final EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			if (DamageCause.VOID.equals(event.getCause()))
			{
				event.getEntity().teleport(new Location(event.getEntity().getWorld(), 0, 0, 0, 0, 0));
			}
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final EntityDamageByEntityEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final EntityExplodeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final EntityChangeBlockEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final FoodLevelChangeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final BlockPlaceEvent event)
	{
		if (!event.getPlayer().hasPermission("cycloneport.hub.build"))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void on(final BlockMultiPlaceEvent event)
	{
		if (!event.getPlayer().hasPermission("cycloneport.hub.build"))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void on(final BlockBreakEvent event)
	{
		if (!event.getPlayer().hasPermission("cycloneport.hub.build"))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void on(final InventoryClickEvent event)
	{
		event.setResult(Event.Result.DENY);
	}

	@EventHandler
	public void on(final InventoryDragEvent event)
	{
		event.setResult(Event.Result.DENY);
	}

	@EventHandler
	public void on(final InventoryMoveItemEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final PlayerDropItemEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final InventoryPickupItemEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final PlayerFishEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void on(final PlayerShearEntityEvent event)
	{
		event.setCancelled(true);
	}
}
