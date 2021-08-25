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
public final class TimeCommand
		extends Command
{
	private final FreeBuildModule module;

	public TimeCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Zeit");
		setDescription("Zeigt die Zeit an oder ändert diese entweder für die aktuelle Welt.");
		setUsage("time [Zeit]");
		setArgumentsRange(0, 1);
		addKey("time");
		addKey("day", 0, 0);
		addKey("night", 0, 0);
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.time"))
		{
			if (sender instanceof Player)
			{
				final Player byPlayer = (Player) sender;
				if (label.equalsIgnoreCase("day") || label.equalsIgnoreCase("night") || (arguments.size() > 0))
				{
					Integer time = null;
					if (label.equalsIgnoreCase("day"))
					{
						time = 6000;
					}
					else if (label.equalsIgnoreCase("night"))
					{
						time = 18000;
					}
					else
					{
						try
						{
							time = Integer.parseInt(arguments.get(0));
						}
						catch (final NumberFormatException ignore)
						{
						}
					}

					if (time != null)
					{
						byPlayer.getWorld().setTime(time);
						byPlayer.sendMessage(FreeBuildModule.PREFIX + "\u00A77Die Zeit wurde zu \u00A73" + time + "\u00A77 gesetzt.");
					}
					else
					{
						sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Wert \u00A73" + arguments.get(0) + "\u00A77 konnte nicht erkannt werden.");
					}
				}
				else
				{
					sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Aktuell ist es \u00A73" + byPlayer.getWorld().getTime() + "\u00A77.");
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
