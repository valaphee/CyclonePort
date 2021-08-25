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

import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
@Deprecated
public class CensorshipChatFilter
		implements ChatFilter
{
	private final List<String> censored;

	public CensorshipChatFilter(final List<String> censored)
	{
		this.censored = censored;
	}

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte achte auf deine \u00A73scheiß\u00A77 Wortwahl.";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		return censored.stream().anyMatch(message.getContent()::contains);
	}
}
