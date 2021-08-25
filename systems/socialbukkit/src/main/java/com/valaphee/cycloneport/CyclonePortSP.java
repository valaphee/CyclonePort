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

import com.valaphee.cyclone.command.CommandHandler;
import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.FieldAccessor;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import com.valaphee.cycloneport.friends.FriendsModule;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public class CyclonePortSP
		extends JavaPlugin
{
	public static final Logger LOGGER = Logger.getLogger(CyclonePortSP.class.getName());
	private static CyclonePortSP instance;
	private final Deque<Module> modules = new ArrayDeque<>();
	private CommandHandler commandHandler;

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

		setCommandMap(new CommandHandlerViaCommandMap(getServer(), commandHandler = new CommandHandler()));
	}

	@Override
	public void onEnable()
	{
		modules.add(new FriendsModule(this));

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

	public CommandHandler getCommandHandler()
	{
		return commandHandler;
	}

	public Module getModule(final String name)
	{
		return modules.stream().filter((module) -> name.equalsIgnoreCase(module.getName())).findFirst().orElse(null);
	}

	private void setCommandMap(final SimpleCommandMap commandMap)
	{
		final FieldAccessor fieldModifiers = Accessors.getFieldAccessorOrNull(Field.class, "modifiers");
		final FieldAccessor craftServerCommandMap = Accessors.getFieldAccessorOrNull(getServer().getClass(), "commandMap");
		fieldModifiers.set(craftServerCommandMap.getField(), craftServerCommandMap.getField().getModifiers() & ~Modifier.FINAL);
		craftServerCommandMap.set(getServer(), commandMap);
		fieldModifiers.set(craftServerCommandMap.getField(), craftServerCommandMap.getField().getModifiers() | Modifier.FINAL);
		final FieldAccessor craftServerPluginManager = Accessors.getFieldAccessorOrNull(getServer().getClass(), "pluginManager");
		final FieldAccessor simplePluginManagerCommandMap = Accessors.getFieldAccessorOrNull(SimplePluginManager.class, "commandMap");
		fieldModifiers.set(simplePluginManagerCommandMap.getField(), simplePluginManagerCommandMap.getField().getModifiers() & ~Modifier.FINAL);
		simplePluginManagerCommandMap.set(craftServerPluginManager.get(getServer()), commandMap);
		fieldModifiers.set(simplePluginManagerCommandMap.getField(), simplePluginManagerCommandMap.getField().getModifiers() | Modifier.FINAL);

		LOGGER.log(Level.INFO, "Command Map changed to {0}.", commandMap.getClass().getName());
	}
}
