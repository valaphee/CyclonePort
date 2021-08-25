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

package com.valaphee.cycloneport;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class CyclonePortSP
		extends Plugin
{
	public static final Logger LOGGER = Logger.getLogger(CyclonePortSP.class.getName());
	private static CyclonePortSP instance;
	private final Deque<Module> modules = new ArrayDeque<>();

	public static CyclonePortSP getInstance()
	{
		return instance;
	}

	public CyclonePortSP()
	{
		instance = this;
	}

	@Override
	public void onLoad()
	{
		LOGGER.setParent(getLogger());
	}

	@Override
	public void onEnable()
	{
		try
		{
			modules.forEach(module -> module.startup());
			modules.forEach(module ->
			{
				module.registerListeners();
				module.registerCommands();
			});
		}
		catch (final Throwable thrown)
		{
			LOGGER.log(Level.SEVERE, "An exception occured while starting modules", thrown);
		}
	}

	@Override
	public void onDisable()
	{
		modules.descendingIterator().forEachRemaining(module ->
		{
			try
			{
				module.shutdown();
			}
			catch (final Throwable thrown)
			{
				LOGGER.log(Level.WARNING, "A module cannot be shutted down.", thrown);
			}
		});
	}
}
