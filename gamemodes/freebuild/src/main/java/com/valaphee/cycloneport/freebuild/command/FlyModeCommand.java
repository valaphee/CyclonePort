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
public final class FlyModeCommand
		extends Command
{
	private final FreeBuildModule module;

	public FlyModeCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Flugmodus");
		setDescription("Aktivert oder deaktivert den Flugmodus für entweder den aktuellen oder den angegebenen Spieler.");
		setUsage("flymode [Spieler]");
		setArgumentsRange(0, 1);
		addKey("flymode");
		addKey("fly");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.flymode"))
		{
			if (arguments.size() > 0)
			{
				if (sender.hasPermission("cycloneport.freebuild.flymode.other"))
				{
					final Player forPlayer = module.getServer().getPlayer(arguments.get(0));
					if (forPlayer != null)
					{
						final boolean enabled = !forPlayer.getAllowFlight();
						flymode(forPlayer, enabled);
						if (!sender.equals(forPlayer))
						{
							sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Flugmodus von \u00A73" + forPlayer.getName() + "\u00A77 wurde \u00A73" + (enabled ? "aktiviert" : "deaktiviert") + "\u00A77.");
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
				final Player byPlayer = (Player) sender;
				flymode(byPlayer, !byPlayer.getAllowFlight());
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

	private void flymode(final Player player, final boolean enabled)
	{
		player.setFallDistance(0.0f);
		player.setAllowFlight(enabled);
		if (!player.getAllowFlight())
		{
			player.setFlying(false);
		}

		player.sendMessage(FreeBuildModule.PREFIX + "\u00A7bDein\u00A77 Flugmodus wurde \u00A73" + (enabled ? "aktiviert" : "deaktiviert") + "\u00A77.");
	}
}
