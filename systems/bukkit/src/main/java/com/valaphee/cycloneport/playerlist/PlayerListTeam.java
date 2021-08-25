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

import com.valaphee.cyclone.text.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

/**
 * Default
 *
 * @author valaphee
 */
public final class PlayerListTeam
{
	private final String name;
	private final TextAttribute color;
	private final List<String> players = new ArrayList<>();

	public PlayerListTeam(final String name, final TextAttribute color)
	{
		this.name = name;
		this.color = color;
	}

	public String getName()
	{
		return name;
	}

	public TextAttribute getColor()
	{
		return color;
	}

	public List<String> getPlayers()
	{
		return players;
	}

	public void addPlayer(final String player)
	{
		players.add(player);
		Bukkit.getOnlinePlayers().stream().map((onlinePlayer) -> onlinePlayer.getScoreboard().getTeam(name)).filter((team) -> team != null).forEach((team) ->
		{
			team.addEntry(player);
		});
	}

	public void removePlayer(final String player)
	{
		players.remove(player);
		Bukkit.getOnlinePlayers().stream().map((onlinePlayer) -> onlinePlayer.getScoreboard().getTeam(name)).filter((team) -> team != null).forEach((team) ->
		{
			team.removeEntry(player);
		});
	}
}
