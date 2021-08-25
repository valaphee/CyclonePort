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

package com.valaphee.cycloneport.playerlist;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.account.event.UserLoadEvent;
import com.valaphee.cyclone.account.event.UserUnloadEvent;
import com.valaphee.cyclone.account.event.UserUpdateEvent;
import com.valaphee.cyclone.text.TextAttribute;
import com.valaphee.cycloneport.Module;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Default
 *
 * @author valaphee
 */
public final class PlayerListModule
		extends Module
{
	private AccountManager accountManager;
	private PermissionHandler permissionHandler;
	private final Map<String, PlayerListTeam> teams = new HashMap<>();

	public PlayerListModule(final JavaPlugin plugin)
	{
		super("PlayerList", plugin);
	}

	@Override
	public void startup()
	{
		accountManager = Cyclone.getInstance().getAccountManager();
		permissionHandler = Cyclone.getInstance().getPermissionHandler();

		accountManager.getGroupRepository().getCachedGroups().forEach((groupData) ->
		{
			TextAttribute color = TextAttribute.WHITE;
			try
			{
				color = TextAttribute.valueOf(groupData.variables.get("color"));
			}
			catch (final Throwable ignore)
			{}

			teams.put(groupData.name, new PlayerListTeam(StringUtils.left(groupData.name, 16), color));
		});
	}

	@EventHandler
	public void on(final UserLoadEvent event)
	{
		final User user = event.getUser();
		final Player player = getServer().getPlayer(accountManager.findAssociation(user.getId()));
		final Scoreboard scoreboard = player.getScoreboard();
		teams.values().forEach((team) ->
		{
			final Team scoreboardTeam = scoreboard.registerNewTeam(team.getName());
			scoreboardTeam.setPrefix("\u00A7" + team.getColor().getCharacter());
			team.getPlayers().forEach((teamPlayer) ->
			{
				scoreboardTeam.addEntry(teamPlayer);
			});
		});
		teams.get(user.getGroup().getName()).addPlayer(player.getName());
	}

	@EventHandler
	public void on(final UserUpdateEvent event)
	{
		final User user = event.getUser();
		final Player player = getServer().getPlayer(accountManager.findAssociation(user.getId()));
		if (player != null)
		{
			final String color = "\u00A7" + permissionHandler.getColor(user).getCharacter();
			player.setDisplayName(color + user.getName());
			player.setPlayerListName(color + user.getName());
			teams.get(user.getGroup().getName()).removePlayer(player.getName());
			teams.get(user.getGroup().getName()).addPlayer(player.getName());
		}
	}

	@EventHandler
	public void on(final UserUnloadEvent event)
	{
		final User user = event.getUser();
		final Player player = getServer().getPlayer(accountManager.findAssociation(user.getId()));
		teams.get(user.getGroup().getName()).removePlayer(player.getName());
	}
}
