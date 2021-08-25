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

package com.valaphee.cycloneport.teleport.command;

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import com.valaphee.cycloneport.teleport.TeleportModule;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Default
 *
 * @author valaphee
 */
public final class TeleportAllCommand
		extends Command
{
	private final TeleportModule module;

	public TeleportAllCommand(final TeleportModule module)
	{
		this.module = module;

		setName("Alle teleportieren");
		setDescription("Teleportiert alle Spieler entweder zu einen Spieler oder einer Koordinate.");
		setUsage("tpall {<Spieler> | <X> <Y> <Z>}");
		setArgumentsRange(1, 6);
		addKey("teleport all");
		addKey("tp all");
		addKey("tpall");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.teleport.all"))
		{
			if (arguments.size() == 1)
			{
				module.teleportPlayerToAnotherPlayer(sender, null, module.getServer().getPlayer(arguments.get(0)));
			}
			else if (arguments.size() == 3)
			{
				try
				{
					module.teleportPlayerToLocation(sender, null, Integer.parseInt(arguments.get(0)), Integer.parseInt(arguments.get(1)), Integer.parseInt(arguments.get(2)));
				}
				catch (final NumberFormatException ex)
				{
					sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Wert \u00A73" + arguments.get(0) + "\u00A77 konnte nicht erkannt werden.");
				}
			}
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}
}
