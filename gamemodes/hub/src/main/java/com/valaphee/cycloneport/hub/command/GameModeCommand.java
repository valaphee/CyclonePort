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

package com.valaphee.cycloneport.hub.command;

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import com.valaphee.cycloneport.hub.HubModule;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class GameModeCommand
		extends Command
{
	private final HubModule module;

	public GameModeCommand(final HubModule module)
	{
		this.module = module;

		setName("Spielmodus");
		setDescription("Setzt den Spielmodus für entweder den aktuellen oder den angegebenen Benutzer.");
		setUsage("gamemode <Spielmodus> [Benutzer]");
		setArgumentsRange(1, 2);
		addKey("gamemode");
		addKey("gm");
		addKey("gms", 0, 1);
		addKey("gmc", 0, 1);
		addKey("gma", 0, 1);
		addKey("gmsp", 0, 1);
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.gamemode"))
		{
			if (((arguments.size() > 1) && (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm"))) || ((arguments.size() > 0) && (!(label.equalsIgnoreCase("gamemode") && label.equalsIgnoreCase("gm")))))
			{
				if (sender.hasPermission("cycloneport.freebuild.gamemode.other"))
				{
					final Player forPlayer = module.getServer().getPlayer(arguments.size() > 1 ? arguments.get(1) : label);
					if (forPlayer != null)
					{
						final GameMode gameMode = parse(arguments.get(0));
						gamemode(forPlayer, gameMode);
						sender.sendMessage(HubModule.PREFIX + "\u00A77Der Spielmodus von \u00A73" + forPlayer.getName() + "\u00A77 wurde zu \u00A73" + gameMode.name() + "\u00A77 geändert.");
					}
					else
					{
						sender.sendMessage(HubModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(0) + "\u00A77 wurde nicht gefunden.");
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
				final GameMode gamemode = parse(arguments.size() > 0 ? arguments.get(0) : label);
				gamemode(byPlayer, gamemode);
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

	private GameMode parse(final String gamemode)
	{
		if (gamemode.equalsIgnoreCase("gms") || gamemode.equalsIgnoreCase("0") || gamemode.equalsIgnoreCase("s"))
		{
			return GameMode.SURVIVAL;
		}
		else if (gamemode.equalsIgnoreCase("gmc") || gamemode.equalsIgnoreCase("1") || gamemode.equalsIgnoreCase("c"))
		{
			return GameMode.CREATIVE;
		}
		else if (gamemode.equalsIgnoreCase("gma") || gamemode.equalsIgnoreCase("2") || gamemode.equalsIgnoreCase("a"))
		{
			return GameMode.ADVENTURE;
		}
		else if (gamemode.equalsIgnoreCase("gmsp") || gamemode.equalsIgnoreCase("3") || gamemode.equalsIgnoreCase("sp"))
		{
			return GameMode.SPECTATOR;
		}

		return null;
	}

	private void gamemode(final Player player, final GameMode gameMode)
	{
		player.setGameMode(gameMode);
		player.sendMessage(HubModule.PREFIX + "\u00A7bDein\u00A77 Spielmodus wurde zu \u00A73" + gameMode.name() + "\u00A77 geändert.");
	}
}
