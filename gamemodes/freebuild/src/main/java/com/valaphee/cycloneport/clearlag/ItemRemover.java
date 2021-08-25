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

package com.valaphee.cycloneport.clearlag;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

/**
 * Default
 *
 * @author valaphee
 */
public final class ItemRemover
		implements Runnable
{
	private final ClearLagModule module;
	private int secondsBeforeRemoving = 1000;

	public ItemRemover(final ClearLagModule module)
	{
		this.module = module;
	}

	@Override
	public void run()
	{
		if (secondsBeforeRemoving == 300)
		{
			module.getServer().broadcastMessage(ClearLagModule.PREFIX + "Die Items werden in 5 Minuten vom Boden entfernt.");
		}
		else if ((secondsBeforeRemoving == 20) || (secondsBeforeRemoving == 10) || ((secondsBeforeRemoving < 6) && (secondsBeforeRemoving > 0)))
		{
			module.getServer().broadcastMessage(ClearLagModule.PREFIX + "Die Items werden in " + secondsBeforeRemoving + " Sekunden vom Boden entfernt entfernt.");
		}
		else if (secondsBeforeRemoving == 0)
		{
			module.getServer().getWorlds().forEach((world) ->
			{
				world.getEntities().stream().filter((entity) -> (entity instanceof Item)).forEach(Entity::remove);
			});

			secondsBeforeRemoving = 1000;
		}

		secondsBeforeRemoving--;
	}
}
