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
public final class SpeedCommand
		extends Command
{
	private final FreeBuildModule module;

	public SpeedCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Geschwindigkeit");
		setDescription("Verändert die Geschwindigkeit des Spielers.");
		setUsage("speed <Geschwindigkeit> [Spieler]");
		setArgumentsRange(1, 2);
		addKey("speed");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.speed"))
		{
			if (arguments.size() > 1)
			{
				if (sender.hasPermission("cycloneport.freebuild.speed.other"))
				{
					final Player forPlayer = module.getServer().getPlayer(arguments.get(0));
					if (forPlayer != null)
					{
						speed(forPlayer, Float.parseFloat(arguments.get(0)));
						if (!sender.equals(forPlayer))
						{
							sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Die Geschwindigkeit von \u00A73" + forPlayer.getName() + "\u00A77 wurde verändert.");
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
				speed((Player) sender, Float.parseFloat(arguments.get(0)));
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

	private void speed(final Player player, final float speed)
	{
		if (player.isFlying())
		{
			player.setFlySpeed(speed);
		}
		else
		{
			player.setWalkSpeed(speed);
		}

		player.sendMessage(FreeBuildModule.PREFIX + "\u00A7bDein\u00A77 Geschwindigkeit wurde zu \u00A73" + speed + " gesetzt\u00A77.");
	}
}
