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
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class TeleportCommand
		extends Command
{
	private final TeleportModule module;

	public TeleportCommand(final TeleportModule module)
	{
		this.module = module;

		setName("Teleportieren");
		setDescription("Teleportiert entweder den eigenen oder den angegebenen Spieler entweder zu einen Spieler oder einer Koordinate.");
		setUsage("teleport {<Spieler> [Ziel] | <X> <Y> <Z> <Spieler>}");
		setArgumentsRange(1, 4);
		addKey("teleport");
		addKey("tp");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.teleport"))
		{
			if ((arguments.size() == 2) || (arguments.size() == 4))
			{
				if (sender.hasPermission("cycloneport.teleport.other"))
				{
					final Player forPlayer = module.getServer().getPlayer(arguments.get(0));
					if (arguments.size() == 2)
					{
						final Player anotherPlayer = module.getServer().getPlayer(arguments.get(1));
						if (forPlayer != null)
						{
							if (anotherPlayer != null)
							{
								module.teleportPlayerToAnotherPlayer(sender, forPlayer, anotherPlayer);
							}
							else
							{
								sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(1) + "\u00A77 wurde nicht gefunden.");
							}
						}
						else
						{
							sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(0) + "\u00A77 wurde nicht gefunden.");
						}
					}
					else if (arguments.size() == 4)
					{
						if (forPlayer != null)
						{
							try
							{
								module.teleportPlayerToLocation(sender, forPlayer, Integer.parseInt(arguments.get(1)), Integer.parseInt(arguments.get(2)), Integer.parseInt(arguments.get(3)));
							}
							catch (final NumberFormatException ex)
							{
								sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Wert \u00A73" + arguments.get(0) + "\u00A77 konnte nicht erkannt werden.");
							}
						}
						else
						{
							sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(0) + "\u00A77 wurde nicht gefunden.");
						}
					}
				}
				else
				{
					sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
				}
			}
			else if (sender instanceof Player)
			{
				final Player byPlayer = (Player) sender;
				if (arguments.size() == 1)
				{
					final Player anotherPlayer = module.getServer().getPlayer(arguments.get(0));
					if (anotherPlayer != null)
					{
						module.teleportPlayerToAnotherPlayer(byPlayer, byPlayer, anotherPlayer);
					}
					else
					{
						sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(0) + "\u00A77 wurde nicht gefunden.");
					}
				}
				else if (arguments.size() == 3)
				{
					try
					{
						module.teleportPlayerToLocation(byPlayer, byPlayer, Integer.parseInt(arguments.get(0)), Integer.parseInt(arguments.get(1)), Integer.parseInt(arguments.get(2)));
					}
					catch (final NumberFormatException ex)
					{
						sender.sendMessage(TeleportModule.PREFIX + "\u00A77Der Wert \u00A73" + arguments.get(0) + "\u00A77 konnte nicht erkannt werden.");
					}
				}
			}
			else
			{
				sender.sendMessage(CommandHandlerViaCommandMap.NO_CONSOLE);
			}
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}
}
