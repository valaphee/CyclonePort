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

package com.valaphee.cyclone.command;

import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class QueuedCommand
		extends Command
{
	private long expiration;

	public QueuedCommand()
	{
		setExpiration(15_000L);
	}

	public long getExpiration()
	{
		return expiration;
	}

	protected final void setExpiration(final long expiration)
	{
		this.expiration = expiration;
	}

	public Queuenation getQueuenation(final CommandSender sender, final List<String> arguments)
	{
		final Queuenation queuenation = new Queuenation(arguments);

		succeed(sender, queuenation);

		return queuenation;
	}

	public void succeed(final CommandSender sender, final Queuenation queuenation)
	{}

	public void failed(final CommandSender sender, final Queuenation queuenation)
	{}

	protected final class Queuenation
	{
		final List<String> arguments;

		private Queuenation(final List<String> arguments)
		{
			this.arguments = arguments;
		}

		public List<String> getArguments()
		{
			return Collections.unmodifiableList(arguments);
		}
	}
}
