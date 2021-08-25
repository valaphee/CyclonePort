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

package com.valaphee.cycloneport.chat.command;

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cycloneport.chat.ChatModule;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Default
 *
 * @author valaphee
 */
public final class BroadcastCommand
		extends Command
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bBroadcast \u00A78\u00A7l\u25CF ";
	private final ChatModule module;

	public BroadcastCommand(final ChatModule module)
	{
		this.module = module;

		setName("Broadcast");
		setDescription("Sendet eine Nachricht an alle Spieler.");
		setUsage("broadcast <Nachricht ...>");
		setArgumentsRange(1, -1);
		addKey("broadcast");
		addKey("bc");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.chat.broadcast"))
		{
			final StringBuilder message = new StringBuilder();
			for (int i = 0; i < arguments.size(); ++i)
			{
				message.append(arguments.get(i)).append(' ');
			}
			message.setLength(message.length() - 1);

			module.broadcast(PREFIX + "\u00A73\u00A7l" + ChatModule.format(sender, message.toString()));
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}
}
