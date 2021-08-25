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

package com.valaphee.cycloneport.command;

import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cyclone.command.CommandHandler;
import com.valaphee.cyclone.command.ConfirmCommand;
import com.valaphee.cyclone.command.HelpCommand;
import com.valaphee.cycloneport.CyclonePortCPIP;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
public class CommandHandlerViaCommandMap
		extends SimpleCommandMap // We need to do crazy runtime stuff!
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bBefehl \u00A78\u00A7l\u25CF ";
	public static final String COMMAND_NOT_FOUND = PREFIX + "\u00A77Der eingegebene \u00A73Befehl\u00A77 wurde leider nicht gefunden.";
	public static final String NO_CONSOLE = PREFIX + "\u00A77Der Befehl kann nicht in der \u00A73Konsole\u00A77 ausgeführt werden.";
	private static final Pattern ARGUMENTS_PATTERN = Pattern.compile(" ", Pattern.LITERAL);
	private final CommandHandler commandHandler;

	public CommandHandlerViaCommandMap(final Server server, final CommandHandler commandHandler)
	{
		super(server);

		this.commandHandler = commandHandler;

		setFallbackCommands();
	}

	@Override
	public void setFallbackCommands()
	{
		commandHandler.register(new HelpCommand(commandHandler));
		commandHandler.register(new ConfirmCommand(commandHandler));
	}

	@Override
	public boolean register(final String label, final String fallbackPrefix, final org.bukkit.command.Command command)
	{
		if (fallbackPrefix.equals("minecraft") || fallbackPrefix.equals("bukkit") || fallbackPrefix.equals("spigot"))
		{
			return true;
		}

		return super.register(label, "unity", command);
	}

	@Override
	public synchronized void clearCommands()
	{
		knownCommands.entrySet().forEach((command) ->
		{
			command.getValue().unregister(this);
		});
		knownCommands.clear();
	}

	@Override
	public boolean dispatch(final CommandSender sender, final String cmdLine)
			throws org.bukkit.command.CommandException
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			CyclonePortCPIP.LOGGER.log(Level.INFO, "{0} issued command: /{1}", new Object[]
			{
				player.getName(), cmdLine
			});
		}
		else if (sender instanceof BlockCommandSender)
		{
			final Block block = ((BlockCommandSender) sender).getBlock();
			CyclonePortCPIP.LOGGER.log(Level.INFO, "Block at {0}, {1}, {2} issed command: /{3}", new Object[]
			{
				block.getX(), block.getY(), block.getZ(), cmdLine
			});
		}

		try
		{
			if (commandHandler.dispatch(sender, cmdLine))
			{
				return true;
			}
		}
		catch (final CommandException ex)
		{
			throw new org.bukkit.command.CommandException("", ex);
		}
		catch (final Throwable thrown)
		{
			throw new org.bukkit.command.CommandException("\u00A77Fehler während des ausführen des Befehles \u00A73" + cmdLine + "\u00A77.", thrown);
		}

		final String[] arguments = ARGUMENTS_PATTERN.split(cmdLine);
		if (arguments.length == 0)
		{
			return false;
		}

		final String label = arguments[0].toLowerCase();
		final org.bukkit.command.Command foundCommand = getCommand(label);
		if (foundCommand == null)
		{
			sender.sendMessage(COMMAND_NOT_FOUND);

			return true;
		}

		try
		{
			foundCommand.execute(sender, label, Arrays.copyOfRange(arguments, 1, arguments.length));
		}
		catch (final org.bukkit.command.CommandException ex)
		{
			throw ex;
		}
		catch (final Throwable thrown)
		{
			throw new org.bukkit.command.CommandException("\u00A77Fehler während des ausführen des Befehles \u00A73" + label + "\u00A77.", thrown);
		}

		return true;
	}

	@Override
	public List<String> tabComplete(final CommandSender sender, final String cmdLine)
			throws IllegalArgumentException
	{
		final int labelLength = cmdLine.indexOf(' ');
		if (labelLength == -1)
		{
			final List<String> candidates = new ArrayList<>();
			final SortedSet<String> commands = new TreeSet<>();
			commandHandler.getCommands().forEach((command) -> command.getKeys().stream().map((commandKey) -> commandKey.getKey()).forEach(commands::add));
			for (final String command : commands.tailSet(cmdLine))
			{
				if (!command.startsWith(cmdLine))
				{
					break;
				}
				candidates.add("/" + command);
			}

			Collections.sort(candidates, String.CASE_INSENSITIVE_ORDER);
			if (candidates.size() == 1)
			{
				candidates.set(0, candidates.get(0) + " ");
			}

			return candidates;
		}

		final String[] arguments = ARGUMENTS_PATTERN.split(cmdLine.substring(labelLength + 1, cmdLine.length()));
		final String lastArgument = arguments[(arguments.length - 1)];
		final List<String> candidates = new ArrayList<>();
		final SortedSet<String> players = new TreeSet<>();
		sender.getServer().getOnlinePlayers().stream().map((player) -> player.getName()).forEach(players::add);
		for (final String player : players.tailSet(lastArgument))
		{
			if (!player.startsWith(lastArgument))
			{
				break;
			}
			candidates.add(player);
		}
		Collections.sort(candidates, String.CASE_INSENSITIVE_ORDER);
		if (candidates.size() == 1)
		{
			candidates.set(0, candidates.get(0) + " ");
		}

		return candidates;
	}
}
