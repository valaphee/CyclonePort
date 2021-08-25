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

package com.valaphee.cycloneport.chat;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Default
 *
 * @author valaphee
 */
public final class ChatMessage
{
	private final ProxiedPlayer sender;
	private String receiver;
	private String channel;
	private final long sent;
	private final String content;

	public ChatMessage(final ProxiedPlayer sender, final long sent, String content)
	{
		this.sender = sender;
		if (content.startsWith("@"))
		{
			final String[] contents = content.substring(1).split(" ", 2);
			if (contents.length == 2)
			{
				receiver = contents[0];
				content = contents[1];
			}
		}
		else if (content.startsWith("#"))
		{
			final String[] contents = content.substring(1).split(" ", 2);
			if (contents.length == 2)
			{
				channel = contents[0];
				content = contents[1];
			}
		}
		this.sent = sent;
		this.content = content;
	}

	public ProxiedPlayer getSender()
	{
		return sender;
	}

	public String getReceiver()
	{
		return receiver;
	}

	public String getChannel()
	{
		return channel;
	}

	public long getSent()
	{
		return sent;
	}

	public String getContent()
	{
		return content;
	}
}
