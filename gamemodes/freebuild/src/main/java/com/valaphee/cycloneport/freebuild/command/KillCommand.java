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

package com.valaphee.cycloneport.freebuild.command;

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import com.valaphee.cycloneport.freebuild.FreeBuildModule;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class KillCommand
		extends Command
{
	private final FreeBuildModule module;

	public KillCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Töten");
		setDescription("Tötet entweder den aktuellen oder den angegebenen Spieler.");
		setUsage("kill [Spieler]");
		setArgumentsRange(0, 1);
		addKey("kill");
		addKey("death");
		addKey("suicide");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.kill"))
		{
			if (arguments.size() > 0)
			{
				if (sender.hasPermission("cycloneport.freebuild.kill.other"))
				{
					final Player forPlayer = module.getServer().getPlayer(arguments.get(0));
					if (forPlayer != null)
					{
						kill(forPlayer);
						if (!sender.equals(forPlayer))
						{
							sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Spieler \u00A73" + forPlayer.getName() + "\u00A77 wurde getötet.");
						}
					}
					else
					{
						sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(0) + "\u00A77 wurde nicht gefunden.");
					}
				}
				else
				{
					sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
				}
			}
			else if (sender instanceof Player)
			{
				kill((Player) sender);
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

	static void kill(final Player player)
	{
		player.setHealth(0.0D);
		player.sendMessage(FreeBuildModule.PREFIX + "\u00A7bDu\u00A77 wurdest getötet.");
	}
}
