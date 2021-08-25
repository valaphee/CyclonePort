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
public final class CapsChatFilter
		implements ChatFilter
{
	private final float capsLimit;

	public CapsChatFilter(final float capsLimit)
	{
		this.capsLimit = capsLimit;
	}

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte schreibe nicht in \u00A73CAPS\u00A77.";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		int caps = 0;
		for (int i = message.getContent().length() - 1; i > 0; --i)
		{
			final char character = message.getContent().charAt(i);
			if ((character >= 'A') && (character <= 'Z'))
			{
				caps++;
			}
		}

		return (caps / ((float) message.getContent().length())) > capsLimit;
	}
}
