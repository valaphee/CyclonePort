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
import com.valaphee.cyclone.util.StringUtil;
import com.valaphee.cycloneport.chat.ChatModule;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Default
 *
 * @author valaphee
 */
public final class FlushCommand
		extends Command
{
	private final ChatModule module;

	public FlushCommand(final ChatModule module)
	{
		this.module = module;

		setName("Leeren");
		setDescription("Leert den Chat.");
		setUsage("flush");
		setArgumentsRange(0, 0);
		addKey("flush");
		addKey("cc");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.chat.flush"))
		{
			module.broadcast(StringUtil.repeat(99, " \n"));
			module.broadcast(ChatModule.PREFIX + "\u00A77Der Chat wurde von \u00A73" + sender.getName() + "\u00A77 geleert.");
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}
}
