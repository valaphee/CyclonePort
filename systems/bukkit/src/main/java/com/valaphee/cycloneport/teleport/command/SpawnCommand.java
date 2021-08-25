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
public class SpawnCommand
		extends Command
{
	private final TeleportModule module;

	public SpawnCommand(final TeleportModule module)
	{
		this.module = module;

		setName("Spawn");
		setDescription("Teleport entweder den eigenen oder den angegebenen Spieler zum Spawn.");
		setUsage("spawn [Spieler]");
		setArgumentsRange(0, 1);
		addKey("spawn");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.spawn"))
		{
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}
}
