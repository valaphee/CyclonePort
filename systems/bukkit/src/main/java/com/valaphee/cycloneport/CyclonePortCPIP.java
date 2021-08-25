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
import com.valaphee.cycloneport.chat.ChatModule;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import com.valaphee.cycloneport.economy.EconomyModule;
import com.valaphee.cycloneport.playerlist.PlayerListModule;
import com.valaphee.cycloneport.reflect.PackageType;
import com.valaphee.cycloneport.sidebar.SidebarModule;
import com.valaphee.cycloneport.teleport.TeleportModule;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.*;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class CyclonePortCPIP
		extends JavaPlugin
{
	public static final Logger LOGGER = Logger.getLogger(CyclonePortCPIP.class.getName());
	private static CyclonePortCPIP instance;
	private final Deque<Module> modules = new ArrayDeque<>();
	private CommandHandler commandHandler;

	public static CyclonePortCPIP getInstance()
	{
		return instance;
	}

	public CyclonePortCPIP()
	{
		instance = this;
	}

	@Override
	public void onLoad()
	{
		LOGGER.setParent(getLogger());

		commandHandler = new CommandHandler();
		setCommandMap(CommandHandlerViaCommandMap.class);
	}

	@Override
	public void onEnable()
	{
		modules.add(new ChatModule(this));
		modules.add(new EconomyModule(this));
		modules.add(new PlayerListModule(this));
		modules.add(new SidebarModule(this));
		modules.add(new TeleportModule(this));

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

	private void setCommandMap(final Class<? extends SimpleCommandMap> commandMapClass)
	{
		try
		{
			final ClassPool classPool = new ClassPool();
			classPool.insertClassPath(new ClassClassPath(getClass()));
			ClassLoader classLoader = classPool.getClassLoader();
			if (classLoader != null)
			{
				classPool.appendClassPath(new LoaderClassPath(classLoader));
			}
			else
			{
				classPool.appendSystemPath();
			}
			classLoader = getClass().getClassLoader();
			final CtClass commandMapRefurbishedClass = classPool.get(commandMapClass.getName());
			commandMapRefurbishedClass.setSuperclass(classPool.get(PackageType.CRAFTBUKKIT_COMMAND.getClass("CraftCommandMap").getName()));
			commandMapRefurbishedClass.replaceClassName(commandMapClass.getName(), commandMapClass.getName() + "Refurbished");
			final Object craftCommandMap = commandMapRefurbishedClass.toClass(classLoader, getClass().getProtectionDomain()).getConstructor(new Class[]
			{
				Server.class, CommandHandler.class
			}).newInstance(new Object[]
			{
				getServer(), commandHandler
			});
			final FieldAccessor fieldModifiers = Accessors.getFieldAccessorOrNull(Field.class, "modifiers");
			final FieldAccessor craftServerKnownCommands = Accessors.getFieldAccessorOrNull(getServer().getClass(), "commandMap");
			fieldModifiers.set(craftServerKnownCommands.getField(), craftServerKnownCommands.getField().getModifiers() & ~Modifier.FINAL);
			craftServerKnownCommands.set(getServer(), craftCommandMap);
			fieldModifiers.set(craftServerKnownCommands.getField(), craftServerKnownCommands.getField().getModifiers() | Modifier.FINAL);
			final FieldAccessor craftServerPluginManager = Accessors.getFieldAccessorOrNull(getServer().getClass(), "pluginManager");
			final FieldAccessor simplePluginManagerCommandMap = Accessors.getFieldAccessorOrNull(SimplePluginManager.class, "commandMap");
			fieldModifiers.set(simplePluginManagerCommandMap.getField(), simplePluginManagerCommandMap.getField().getModifiers() & ~Modifier.FINAL);
			simplePluginManagerCommandMap.set(craftServerPluginManager.get(getServer()), craftCommandMap);
			fieldModifiers.set(simplePluginManagerCommandMap.getField(), simplePluginManagerCommandMap.getField().getModifiers() | Modifier.FINAL);

			LOGGER.log(Level.INFO, "Command Map changed to {0}.", craftCommandMap.getClass().getName());
		}
		catch (final CannotCompileException | ClassNotFoundException | NotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ignore)
		{
		}
	}
}
