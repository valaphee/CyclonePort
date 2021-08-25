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

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
public final class Teleporter
{
	private final TeleportModule module;
	private final Player player;
	private final Location location;
	private final String message;
	private final CommandSender caller;
	private final String callerMessage;

	public Teleporter(final TeleportModule module, final Player player, final Location location, final String message, final CommandSender caller, final String callerMessage)
	{
		this.module = module;
		this.player = player;
		this.location = location;
		this.message = message;
		this.caller = caller;
		this.callerMessage = callerMessage;
	}

	public Player getPlayer()
	{
		return player;
	}

	public Location getLocation()
	{
		return location;
	}

	public String getMessage()
	{
		return message;
	}

	public CommandSender getCaller()
	{
		return caller;
	}

	public String getCallerMessage()
	{
		return callerMessage;
	}

	public void teleport()
	{
		if (location != null)
		{
			if (player != null)
			{
				module.teleport(player, location);
				if (message != null)
				{
					player.sendMessage(message);
				}
			}

			if ((caller != null) && (callerMessage != null))
			{
				caller.sendMessage(callerMessage);
			}
		}
	}
}
