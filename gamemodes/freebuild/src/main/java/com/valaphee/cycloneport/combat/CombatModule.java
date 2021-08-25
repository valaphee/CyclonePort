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

package com.valaphee.cycloneport.combat;

import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.MethodAccessor;
import com.valaphee.cycloneport.Module;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class CombatModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aMein\u00A7bKampf \u00A78\u00A7l\u25CF ";
	private static MethodAccessor PLAYER_SPIGOT;
	private static MethodAccessor PLAYER_SPIGOT_RESPAWN;

	public CombatModule(final JavaPlugin plugin)
	{
		super("Combat", plugin);
	}

	@EventHandler
	public void on(final PlayerDeathEvent event)
	{
		final Player player = event.getEntity();
		final Player killer = event.getEntity().getKiller();
		if ((killer == null) || killer.equals(player))
		{
			player.sendMessage(PREFIX + "\u00A77Du bist \u00A7agestorben\u00A77.");
		}
		else
		{
			player.sendMessage(PREFIX + "\u00A77Du wurdest von \u00A7a" + killer.getName() + "\u00A77(" + Math.round(killer.getHealth() / 2.0D) + "\u2764\u00A77)" + " getötet.");
			killer.sendMessage(PREFIX + "\u00A77Du hast \u00A7a" + player.getName() + " \u00A77getötet.");
			killer.playSound(killer.getLocation(), Sound.ENTITY_CAT_HURT, 1F, 1F);
		}

		event.setDeathMessage(null);

		runTaskSynchronously(() ->
		{
			PLAYER_SPIGOT_RESPAWN.invoke(PLAYER_SPIGOT.invoke(player));
		}, 20L);
	}

	static
	{
		try
		{
			PLAYER_SPIGOT = Accessors.getMethodAccessor(Player.class, "spigot");
			PLAYER_SPIGOT_RESPAWN = Accessors.getMethodAccessor(Class.forName("org.bukkit.entity.Player$Spigot"), "respawn");
		}
		catch (ClassNotFoundException ignore)
		{
		}
	}
}
