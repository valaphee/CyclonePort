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

/**
 * Default
 *
 * @author valaphee
 */
public final class SpamChatFilter
		implements ChatFilter
{
	private final long sentLimit;

	public SpamChatFilter(final long sentLimit)
	{
		this.sentLimit = sentLimit;
	}

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte schreibe nicht so \u00A73schnell\u00A77.";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		if (lastMessage != null)
		{
			if ((message.getSent() - lastMessage.getSent()) < sentLimit)
			{
				return true;
			}
		}

		return false;
	}
}
