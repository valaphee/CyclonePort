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
import com.valaphee.cyclone.util.TimestampUtil;
import com.valaphee.cycloneport.chat.ChatModule;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import java.sql.Timestamp;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Default
 *
 * @author valaphee
 */
public final class SlowmodeCommand
		extends Command
{
	private final ChatModule module;

	public SlowmodeCommand(final ChatModule module)
	{
		this.module = module;

		setName("Slowmode");
		setDescription("Aktivert oder deaktiviert den Slowmode.");
		setUsage("slowmode <Delay>");
		setArgumentsRange(1, 1);
		addKey("slowmode");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.chat.slowmode"))
		{
			if (arguments.get(0).equals("0"))
			{
				module.setSlowmode(0, true);
			}
			else
			{
				module.setSlowmode(TimestampUtil.getTimestampForDiff(new Timestamp(0), arguments.get(0), true).getTime(), true);
			}
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}
}
