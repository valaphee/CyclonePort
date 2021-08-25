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

package com.valaphee.cycloneport.freebuild.command;

import com.valaphee.cyclone.command.Command;
import com.valaphee.cyclone.command.CommandException;
import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.command.CommandHandlerViaCommandMap;
import com.valaphee.cycloneport.freebuild.FreeBuildModule;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("deprecation")
public final class GiveCommand
		extends Command
{
	private final FreeBuildModule module;

	public GiveCommand(final FreeBuildModule module)
	{
		this.module = module;

		setName("Geben");
		setDescription("Gibt entweder den aktuellen oder den angegebenen Spieler ein Item");
		setUsage("give <Item> [Menge] [Spieler] [Schaden] [Daten ...]");
		setArgumentsRange(1, -1);
		addKey("give");
	}

	@Override
	public void execute(final CommandSender sender, final String label, final List<String> arguments)
			throws CommandException
	{
		if (sender.hasPermission("cycloneport.freebuild.give"))
		{
			final Material material = parseMaterial(module, arguments.get(0));
			int amount = 1;
			short damage = 0;
			if (arguments.size() > 1)
			{
				try
				{
					amount = Math.min(Math.max(Integer.parseInt(arguments.get(1)), 1), 64);
				}
				catch (final NumberFormatException ex)
				{}

				if (arguments.size() > 3)
				{
					try
					{
						damage = Short.parseShort(arguments.get(3));
					}
					catch (final NumberFormatException ex)
					{}
				}
			}

			if (arguments.size() > 2)
			{
				final Player forPlayer = module.getServer().getPlayer(arguments.get(2));
				if (forPlayer != null)
				{
					if (material != null)
					{
						ItemStack item = new ItemStack(material, amount, damage);
						if (arguments.size() > 4)
						{
							final StringBuilder data = new StringBuilder();
							for (int i = 4; i < arguments.size(); ++i)
							{
								data.append(arguments.get(i)).append(' ');
							}
							data.setLength(data.length() - 1);
							item = module.getServer().getUnsafe().modifyItemStack(item, data.toString());
						}
						give(forPlayer, item);
						if (!sender.equals(forPlayer))
						{
							sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Dem Spieler \u00A73" + forPlayer.getName() + "\u00A77 wurde \u00A73" + item.getType().name() + "\u00A77x\u00A7b" + amount + "\u00A77 gegeben.");
						}
					}
					else
					{
						sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Wert \u00A73" + arguments.get(0) + "\u00A77 konnte nicht erkannt werden.");
					}
				}
				else
				{
					sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Spieler \u00A73" + arguments.get(2) + "\u00A77 wurde nicht gefunden.");
				}
			}
			else if (sender instanceof Player)
			{
				final Player byPlayer = (Player) sender;
				if (material != null)
				{
					ItemStack item = new ItemStack(material);
					give(byPlayer, item);
				}
				else
				{
					sender.sendMessage(FreeBuildModule.PREFIX + "\u00A77Der Wert \u00A73" + arguments.get(0) + "\u00A77 konnte nicht erkannt werden.");
				}
			}
			else
			{
				sender.sendMessage(CommandHandlerViaCommandMap.NO_CONSOLE);
			}
		}
		else
		{
			sender.sendMessage(CommandHandlerViaCommandMap.COMMAND_NOT_FOUND);
		}
	}

	static Material parseMaterial(final Module module, final String materialString)
	{
		Material material = Material.matchMaterial(materialString);
		if (material == null)
		{
			material = module.getServer().getUnsafe().getMaterial(materialString, 1);
		}

		return material;
	}

	static void give(final Player player, final ItemStack item)
	{
		player.getInventory().addItem(item);
		player.sendMessage(FreeBuildModule.PREFIX + "\u00A7bDir\u00A77 wurde \u00A73" + item.getType().name() + "\u00A77x\u00A7b" + item.getAmount() + "\u00A77 gegeben.");
	}
}
