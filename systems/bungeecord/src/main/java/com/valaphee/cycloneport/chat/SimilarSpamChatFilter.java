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
public class SimilarSpamChatFilter
		implements ChatFilter
{
	private final float similarityPercentageLimit;

	public SimilarSpamChatFilter(final float similarityPercentageLimit)
	{
		this.similarityPercentageLimit = similarityPercentageLimit;
	}

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte \u00A73Spamme Spamme Spamme...,\u00A77 nicht das gleiche Wort.";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		if (lastMessage != null)
		{
			String longer = message.getContent(), shorter = lastMessage.getContent();
			if (longer.length() < shorter.length())
			{
				longer = lastMessage.getContent();
				shorter = message.getContent();
			}

			final int[] costs = new int[(shorter.length() + 1)];
			for (int i = 0; i <= longer.length(); ++i)
			{
				int lastValue = i;
				for (int j = 0; j <= shorter.length(); ++j)
				{
					if (i == 0)
					{
						costs[j] = j;
					}
					else if (j > 0)
					{
						int newValue = costs[(j - 1)];
						if (longer.toLowerCase().charAt(i - 1) != shorter.toLowerCase().charAt(j - 1))
						{
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						}
						costs[(j - 1)] = lastValue;
						lastValue = newValue;
					}
				}
				if (i > 0)
				{
					costs[shorter.length()] = lastValue;
				}
			}

			final int longerLength = longer.length();
			final double similarity = (longerLength - costs[shorter.length()]) / (double) longerLength;
			if (similarity > similarityPercentageLimit)
			{
				return true;
			}
		}

		return false;
	}
}
