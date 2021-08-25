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
import com.valaphee.cycloneport.packet.TitlePacket;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Default
 *
 * @author valaphee
 */
public class Splash
		extends BukkitRunnable
{
	private final HubModule module;
	private final Player player;
	private int frame = -1;

	public Splash(final HubModule module, final Player player)
	{
		this.module = module;
		this.player = player;
	}

	@Override
	public void run()
	{
		if (!player.isOnline())
		{
			cancel();
		}

		if (frame == -1)
		{
			new TitlePacket(40, 60, 60, new MinecraftJSONTextComponent("\u00A7a\u00A7lDein\u00A7b\u00A7lName"), null).send(player);
		}
		else if (frame < module.getSplashes().size())
		{
			new TitlePacket(null, new MinecraftJSONTextComponent(module.getSplashes().get(frame))).send(player);
		}
		else
		{
			new TitlePacket(null, new MinecraftJSONTextComponent("")).send(player);
			cancel();
		}

		frame++;
	}
}
