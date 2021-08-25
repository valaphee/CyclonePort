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

package com.valaphee.cycloneport.hub;

import com.valaphee.cyclone.text.MinecraftJSONTextComponent;
import com.valaphee.cycloneport.packet.ChatPacket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Default
 *
 * @author valaphee
 */
public class Clock
		extends BukkitRunnable
{
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final HubModule module;

	public Clock(final HubModule module)
	{
		this.module = module;
	}

	@Override
	public void run()
	{
		final ChatPacket chatPacket = new ChatPacket(true, new MinecraftJSONTextComponent(LocalDateTime.now().format(FORMATTER)));
		module.getServer().getOnlinePlayers().forEach((player) ->
		{
			chatPacket.send(player);
		});
	}
}
