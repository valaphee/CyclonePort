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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class EnderChestCommand
		extends Command
{
	private final FreeBuildModule module;

	public EnderChestCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Endertruhe");
		setDescription("Öffnet entweder die eigene Endertruhe oder die des angegebenen Spieler.");
		setUsage("enderchest [Spieler]");
		setArgumentsRange(0, 1);
		addKey("enderchest");
		addKey("echest");
		addKey("ec");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.enderchest"))
		{
			if (sender instanceof Player)
			{
				final Player byPlayer = (Player) sender;
				if (arguments.size() > 0)
				{
					if (sender.hasPermission("cycloneport.freebuild.enderchest.other"))
					{
						final Player forPlayer = Bukkit.getPlayer(arguments.get(0));
						if (forPlayer != null)
						{
							if (sender.hasPermission("cycloneport.freebuild.enderchest.other.modify"))
							{
								byPlayer.openInventory(forPlayer.getEnderChest());
							}
							else
							{
								module.getServer().createInventory(null, forPlayer.getEnderChest().getSize(), "\u00A77\u00BB \u00A73Endertruhe \u00A77\u00BB \u00A73" + forPlayer.getName()).setContents(forPlayer.getEnderChest().getContents());
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
				else
				{
					byPlayer.openInventory(byPlayer.getEnderChest());
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
