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

import com.valaphee.cycloneport.chat.ChatMessage;
import com.valaphee.cycloneport.chat.ChatModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Default
 *
 * @author valaphee
 */
public final class PrivateMessageCommand
		extends Command
{
	private final ChatModule module;

	public PrivateMessageCommand(final ChatModule module)
	{
		super("privatemessage", "bungeecord.nopermissions", "pm", "msg", "tell");

		this.module = module;
	}

	@Override
	public void execute(final CommandSender sender, final String[] args)
	{
		final ProxiedPlayer player = (ProxiedPlayer) sender;
		final ChatMessage message = new ChatMessage(player, System.currentTimeMillis(), "@" + String.join(" ", args));

		module.sendPrivateMessage(player, message);
	}
}
