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

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class Module
		implements Listener
{
	private final String name;
	private final Plugin plugin;

	public Module(final String name, final Plugin plugin)
	{
		this.name = name;
		this.plugin = plugin;
	}

	public String getName()
	{
		return name;
	}

	public ProxyServer getProxy()
	{
		return plugin.getProxy();
	}

	public void startup()
	{}

	public void registerListeners()
	{
		registerListener(this);
	}

	public void registerCommands()
	{}

	public void shutdown()
	{}

	public void log(final String message)
	{
		CyclonePortSP.LOGGER.log(Level.INFO, message);
	}

	public void registerListener(final Listener listener)
	{
		getProxy().getPluginManager().registerListener(plugin, listener);
	}

	public void registerCommand(final Command command)
	{
		getProxy().getPluginManager().registerCommand(plugin, command);
	}

	public void runTaskAsynchronously(final Runnable runnable)
	{
		getProxy().getScheduler().runAsync(plugin, runnable);
	}

	public void runTaskAsynchronously(final Runnable runnable, final long delay)
	{
		getProxy().getScheduler().schedule(plugin, runnable, delay, TimeUnit.MILLISECONDS);
	}

	public void runTaskAsynchronously(final Runnable runnable, final long delay, final long period)
	{
		getProxy().getScheduler().schedule(plugin, runnable, delay, period, TimeUnit.MILLISECONDS);
	}
}
