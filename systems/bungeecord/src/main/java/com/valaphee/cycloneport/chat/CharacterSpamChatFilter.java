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
public final class CharacterSpamChatFilter
		implements ChatFilter
{
	private final int charactersLimit;

	public CharacterSpamChatFilter(final int charactersLimit)
	{
		this.charactersLimit = charactersLimit;
	}

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte \u00A73wiiiiiiiderhole\u00A77 keine Zeichen.";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		char lastCharacter = 0;
		int equal = 0;
		for (int i = message.getContent().length() - 1; i > 0; --i)
		{
			final char character = message.getContent().charAt(i);
			if (lastCharacter == 0)
			{
				lastCharacter = character;
			}
			else if (lastCharacter == character)
			{
				equal++;
			}
			else
			{
				lastCharacter = character;
			}

			if (equal == charactersLimit)
			{
				return true;
			}
		}

		return false;
	}
}
