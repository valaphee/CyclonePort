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

package com.valaphee.cycloneport.teleport;

import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.teleport.command.*;
import com.valaphee.cycloneport.util.CircularQueue;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class TeleportModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bChat \u00A78\u00A7l\u25CF ";
	private static final int TELEPORT_HISTORY_LIMIT = 16;
	private final Deque<Teleporter> teleports = new ArrayDeque<>();
	private final Map<UUID, CircularQueue<Location>> teleportHistories = new HashMap<>();

	public TeleportModule(JavaPlugin plugin)
	{
		super("Teleport", plugin);
	}

	@Override
	public void startup()
	{
		runTaskSynchronously(() ->
		{
			if (!teleports.isEmpty())
			{
				teleports.removeFirst().teleport();
			}
		}, 0L, 10L);
	}

	public void registerTeleportCommands()
	{
		registerCommand(new TeleportCommand(this));
		registerCommand(new TeleportAllCommand(this));
	}

	public void registerSpawnCommand()
	{
		registerCommand(new SpawnCommand(this));
	}

	public void registerTeleportRequestCommands()
	{
		registerCommand(new TeleportAcceptCommand(this));
		registerCommand(new TeleportDenyCommand(this));
		registerCommand(new TeleportRequestCommand(this));
		registerCommand(new TeleportHereRequestCommand(this));
	}

	public void teleportPlayerToAnotherPlayer(final CommandSender caller, final Player player, final Player anotherPlayer)
	{
		final List<Player> players = new ArrayList<>();
		if (player == null)
		{
			getServer().getOnlinePlayers().forEach((_player) ->
			{
				players.add(_player);
			});
		}
		else
		{
			players.add(player);
		}

		if (players.size() == 1)
		{
			final Player _player = players.get(0);

			String message;
			String callerMessage = null;
			if (_player.equals(caller))
			{
				message = PREFIX + "\u00A77Du wurdest zu \u00A73" + anotherPlayer.getDisplayName() + "\u00A77 teleportiert.";
			}
			else if (anotherPlayer.equals(caller))
			{
				message = PREFIX + "\u00A7b" + caller.getName() + "\u00A77 wurde zu \u00A73dir\u00A77 teleportiert.";
				callerMessage = PREFIX + "\u00A77Du hast \u00A73" + _player.getDisplayName() + "\u00A77 zu \u00A73dir\u00A77 teleportiert.";
			}
			else
			{
				message = PREFIX + "\u00A7b" + _player.getDisplayName() + "\u00A77 wurde zu \u00A73" + anotherPlayer.getDisplayName() + "\u00A77 teleportiert.";
				callerMessage = PREFIX + "\u00A77Du hast \u00A73" + _player.getDisplayName() + "\u00A77 zu \u00A73" + anotherPlayer.getDisplayName() + "\u00A77 teleportiert.";
			}

			teleports.addLast(new Teleporter(this, _player, anotherPlayer.getLocation(), message, caller, callerMessage));
		}
		else
		{
			players.forEach((_player) ->
			{
				String message;
				String callerMessage = null;
				if (_player.equals(caller))
				{
					message = PREFIX + "\u00A77Du wurdest zu \u00A73" + anotherPlayer.getDisplayName() + "\u00A77 teleportiert.";
				}
				else if (anotherPlayer.equals(caller))
				{
					message = PREFIX + "\u00A7b" + caller.getName() + "\u00A77 wurde zu \u00A73dir\u00A77 teleportiert.";
					callerMessage = PREFIX + "\u00A77Du hast \u00A73" + players.size() + " Spieler\u00A77 zu \u00A73dir\u00A77 teleportiert.";
				}
				else
				{
					message = PREFIX + "\u00A7b" + _player.getDisplayName() + "\u00A77 wurde zu \u00A73" + anotherPlayer.getDisplayName() + "\u00A77 teleportiert.";
					callerMessage = PREFIX + "\u00A77Du hast \u00A73" + players.size() + " Spieler\u00A77 zu \u00A73" + anotherPlayer.getDisplayName() + "\u00A77 teleportiert.";
				}

				teleports.addLast(new Teleporter(this, _player, anotherPlayer.getLocation(), message, caller, callerMessage));
			});
		}
	}

	public void teleportPlayerToLocation(final CommandSender caller, final Player player, final int x, final int y, final int z)
	{
		teleportPlayerToLocation(caller, player, player.getWorld(), x, y, z, 0, 0);
	}

	public void teleportPlayerToLocation(final CommandSender caller, final Player player, final World world, final int x, final int y, final int z)
	{
		teleportPlayerToLocation(caller, player, world, x, y, z, 0, 0);
	}

	public void teleportPlayerToLocation(final CommandSender caller, final Player player, final int x, final int y, final int z, final int yaw, final int pitch)
	{
		teleportPlayerToLocation(caller, player, player.getWorld(), x, y, z, yaw, pitch);
	}

	public void teleportPlayerToLocation(final CommandSender caller, final Player player, final World world, final int x, final int y, final int z, final int yaw, final int pitch)
	{
		final List<Player> players = new ArrayList<>();
		if (player == null)
		{
			getServer().getOnlinePlayers().forEach((_player) ->
			{
				players.add(_player);
			});
		}
		else
		{
			players.add(player);
		}

		final Location location = new Location(world, x, y, z, yaw, pitch);
		if (players.size() == 1)
		{
			final Player _player = players.get(0);

			String message;
			String callerMessage = null;
			if (_player.equals(caller))
			{
				message = PREFIX + "\u00A77Du wurdest zu \u00A73" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\u00A77 teleportiert.";
			}
			else
			{
				message = PREFIX + "\u00A7b" + _player.getDisplayName() + "\u00A77 wurde zu \u00A73" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\u00A77 teleportiert.";
				callerMessage = PREFIX + "\u00A77Du hast \u00A73" + _player.getDisplayName() + "\u00A77 zu \u00A73" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\u00A77 teleportiert.";
			}

			teleports.addLast(new Teleporter(this, _player, location, message, caller, callerMessage));
		}
		else
		{
			players.forEach((_player) ->
			{
				String message;
				String callerMessage = null;
				if (_player.equals(caller))
				{
					message = PREFIX + "\u00A77Du wurdest zu \u00A73" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\u00A77 teleportiert.";
				}
				else
				{
					message = PREFIX + "\u00A73" + _player.getDisplayName() + "\u00A77 wurde zu \u00A73" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\u00A77 teleportiert.";
					callerMessage = PREFIX + "\u00A77Du hast \u00A73" + players.size() + " Spieler\u00A77 zu \u00A73" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "\u00A77 teleportiert.";
				}

				teleports.addLast(new Teleporter(this, _player, location, message, caller, callerMessage));
			});
		}
	}

	void teleport(final Player player, final Location location)
	{
		teleport(player, location, true);
	}

	void teleport(final Player player, final Location location, final boolean detach)
	{
		if (detach)
		{
			player.eject();
			player.leaveVehicle();
		}
		player.setFallDistance(0);
		player.setVelocity(new Vector(0, 0, 0));
		player.teleport(location);

		teleportHistories.get(player.getUniqueId()).add(location);
	}

	public void back(final Player player)
	{
		final CircularQueue<Location> teleportHistory = teleportHistories.get(player.getUniqueId());
		if (!teleportHistory.isEmpty())
		{
			player.eject();
			player.leaveVehicle();
			player.setFallDistance(0);
			player.setVelocity(new Vector(0, 0, 0));
			player.teleport(teleportHistory.pop());
		}
	}

	@EventHandler
	public void on(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		if (!teleportHistories.containsKey(player.getUniqueId()))
		{
			teleportHistories.put(player.getUniqueId(), new CircularQueue<>(TELEPORT_HISTORY_LIMIT));
		}
	}

	@EventHandler
	public void on(final PlayerQuitEvent event)
	{
		teleportHistories.remove(event.getPlayer().getUniqueId());
	}
}
