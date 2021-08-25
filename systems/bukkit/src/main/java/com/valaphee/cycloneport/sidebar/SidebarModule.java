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

package com.valaphee.cycloneport.sidebar;

import com.valaphee.cycloneport.CyclonePortCPIP;
import com.valaphee.cycloneport.Module;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public class SidebarModule
		extends Module
{
	private final Map<String, SidebarObserver> observers = new LinkedHashMap<>();

	public SidebarModule(final JavaPlugin plugin)
	{
		super("Sidebar", plugin);
	}

	public Map<String, SidebarObserver> getObservers()
	{
		return observers;
	}

	public void addObserver(final String name, final SidebarObserver observer)
	{
		observers.put(name, observer);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(final PlayerJoinEvent event)
	{
		new SidebarRunnable(this, event.getPlayer()).runTaskTimer(CyclonePortCPIP.getInstance(), 0L, 5L);
	}
}
