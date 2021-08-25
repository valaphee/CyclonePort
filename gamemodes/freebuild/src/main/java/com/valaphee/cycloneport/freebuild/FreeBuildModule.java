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

package com.valaphee.cycloneport.freebuild;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.User;
import com.valaphee.cycloneport.CyclonePortCPIP;
import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.economy.EconomyModule;
import com.valaphee.cycloneport.freebuild.command.*;
import com.valaphee.cycloneport.sidebar.SidebarModule;
import com.valaphee.cycloneport.teleport.TeleportModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class FreeBuildModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bFreeBuild \u00A78\u00A7l\u25CF ";

	public FreeBuildModule(final JavaPlugin plugin)
	{
		super("FreeBuild", plugin);
	}

	@Override
	public void startup()
	{
		final SidebarModule sidebarModule = (SidebarModule) CyclonePortCPIP.getInstance().getModule("Sidebar");
		sidebarModule.addObserver("\u00A7aSpielmodus", (player) ->
		{
			return "FreeBuild";
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
	}

	@Override
	public void registerCommands()
	{
		final TeleportModule teleportModule = (TeleportModule) CyclonePortCPIP.getInstance().getModule("Teleport");
		teleportModule.registerTeleportCommands();
		teleportModule.registerSpawnCommand();
		teleportModule.registerTeleportRequestCommands();
		registerCommand(new FeedCommand(this));
		registerCommand(new HealCommand(this));
		registerCommand(new FlyModeCommand(this));
		registerCommand(new GodModeCommand(this));
		registerCommand(new GameModeCommand(this));
		registerCommand(new GiveCommand(this));
		registerCommand(new GiveAllCommand(this));
		registerCommand(new TimeCommand(this));
		registerCommand(new WeatherCommand(this));
		registerCommand(new EnderChestCommand(this));
		registerCommand(new WorkbenchCommand(this));
		registerCommand(new KillCommand(this));
		registerCommand(new KillAllCommand(this));
		registerCommand(new SpeedCommand(this));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		if (player.hasPermission("cycloneport.premium"))
		{
			event.setJoinMessage(player.getDisplayName() + "\u00A76\u00A7l hat den Server betreten");
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
			event.setQuitMessage(player.getDisplayName() + "\u00A76\u00A7l hat den Server verlassen");
		}
		else
		{
			event.setQuitMessage(null);
		}
	}
}
