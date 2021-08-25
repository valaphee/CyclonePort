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

import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
public final class ChatMessage
{
	private final Player sender;
	private final long sent;
	private final String content;

	public ChatMessage(final Player sender, final long sent, String content)
	{
		this.sender = sender;
		this.sent = sent;
		this.content = content;
	}

	public Player getSender()
	{
		return sender;
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
