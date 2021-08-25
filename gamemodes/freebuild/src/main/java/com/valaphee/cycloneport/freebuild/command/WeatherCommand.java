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
public final class WeatherCommand
		extends Command
{
	private final FreeBuildModule module;

	public WeatherCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Wetter");
		setDescription("Zeigt das Wetter an oder ändert dieses für die aktuelle Welt.");
		setUsage("weather [Wetter]");
		setArgumentsRange(0, 1);
		addKey("weather");
		addKey("toggledownfall", 0, 0);
		addKey("sun", 0, 0);
		addKey("storm", 0, 0);
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.weather"))
		{
			if (sender instanceof Player)
			{
				final Player byPlayer = (Player) sender;
				if (label.equalsIgnoreCase("toggledownfall") || label.equalsIgnoreCase("sun") || label.equalsIgnoreCase("storm") || (arguments.size() > 0))
				{
					boolean storm;
					if (label.equalsIgnoreCase("toggledownfall"))
					{
						storm = byPlayer.getWorld().isThundering();
					}
					else if (label.equalsIgnoreCase("sun"))
					{
						storm = false;
					}
					else if (label.equalsIgnoreCase("storm"))
					{
						storm = true;
					}
					else
					{
						storm = arguments.get(0).equalsIgnoreCase("storm");
					}

					byPlayer.getWorld().setStorm(storm);
					byPlayer.sendMessage(FreeBuildModule.PREFIX + "\u00A77Das Wetter wurde zu \u00A73" + (arguments.get(0).equals("storm") ? "Storm" : "Sonne") + "\u00A77 gesetzt.");
				}
				else
				{
					sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Aktuell ist \u00A73" + (byPlayer.getWorld().isThundering() ? "Storm" : "Sonne") + "\u00A77.");
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
