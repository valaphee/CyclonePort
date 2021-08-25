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
public final class IPv6AddressChatFilter
		implements ChatFilter
{
	public static final Pattern IPV6_PATTERN = Pattern.compile("\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z");
	public static final Pattern IPV6_COMPRESSED_PATTERN = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)\\z");

	@Override
	public String getMessage()
	{
		return "\u00A77Bitte schreibe keine IPv6-Adressen, wieso auch immer...";
	}

	@Override
	public boolean filter(final ChatMessage message, final ChatMessage lastMessage)
	{
		return IPV6_PATTERN.matcher(message.getContent()).find() || IPV6_COMPRESSED_PATTERN.matcher(message.getContent()).find();
	}
}
