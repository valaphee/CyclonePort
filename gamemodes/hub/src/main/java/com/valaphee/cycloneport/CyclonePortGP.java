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

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.server.data.ServerData;
import com.valaphee.cycloneport.gadget.GadgetModule;
import com.valaphee.cycloneport.hub.HubModule;
import com.valaphee.cycloneport.navigator.NavigatorModule;
import com.valaphee.cycloneport.settings.SettingsModule;
import com.valaphee.cycloneport.social.SocialModule;
import com.valaphee.cycloneport.visibility.VisibilityModule;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class CyclonePortGP
		extends JavaPlugin
{
	public static final Logger LOGGER = Logger.getLogger(CyclonePortGP.class.getName());
	private static CyclonePortGP instance;
	private final Deque<Module> modules = new ArrayDeque<>();

	public static CyclonePortGP getInstance()
	{
		return instance;
	}

	public CyclonePortGP()
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
		modules.add(new HubModule(this));
		modules.add(new VisibilityModule(this));
		modules.add(new SettingsModule(this));
		modules.add(new NavigatorModule(this));
		modules.add(new GadgetModule(this));
		modules.add(new SocialModule(this));

		try
		{
			modules.forEach((module) ->
			{
				module.startup();
			});

			modules.forEach((module) ->
			{
				module.registerListeners();
				module.registerCommands();
			});
		}
		catch (final Throwable thrown)
		{
			LOGGER.log(Level.SEVERE, "An exception occured while starting modules", thrown);
		}

		Cyclone.getInstance().setState(ServerData.State.LOBBY);
	}

	@Override
	public void onDisable()
	{
		modules.descendingIterator().forEachRemaining((module) ->
		{
			try
			{
				module.shutdown();
			}
			catch (final Throwable thrown)
			{
				LOGGER.log(Level.WARNING, "One module cannot be shutted down.", thrown);
			}
		});
	}
}
