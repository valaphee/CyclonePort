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

import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class IPv4AddressChatFilter
		implements ChatFilter
{
	public static final Pattern IPV4_PATTERN = Pattern.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte schreibe keine \u00A73127.0.0.1\u00A77 Adressen!";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		return IPV4_PATTERN.matcher(message.getContent()).find();
	}
}
