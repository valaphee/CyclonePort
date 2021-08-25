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

import com.valaphee.cyclone.command.Command;
import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class Module
		implements Listener
{
	private final String name;
	private final JavaPlugin plugin;

	public Module(final String name, final JavaPlugin plugin)
	{
		this.name = name;
		this.plugin = plugin;
	}

	public String getName()
	{
		return name;
	}

	public Server getServer()
	{
		return plugin.getServer();
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
		CyclonePortCPIP.LOGGER.log(Level.INFO, message);
	}

	public void registerListener(final Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, plugin);
	}

	public void registerCommand(final Command command)
	{
		CyclonePortCPIP.getInstance().getCommandHandler().register(command);
	}

	public BukkitTask runTaskSynchronously(final Runnable runnable)
	{
		return getServer().getScheduler().runTask(plugin, runnable);
	}

	public BukkitTask runTaskAsynchronously(final Runnable runnable)
	{
		return getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
	}

	public BukkitTask runTaskSynchronously(final Runnable runnable, final long delay)
	{
		return getServer().getScheduler().runTaskLater(plugin, runnable, delay);
	}

	public BukkitTask runTaskAsynchronously(final Runnable runnable, final long delay)
	{
		return getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
	}

	public BukkitTask runTaskSynchronously(final Runnable runnable, final long delay, final long period)
	{
		return getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period);
	}

	public BukkitTask runTaskAsynchronously(final Runnable runnable, final long delay, final long period)
	{
		return getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
	}
}
