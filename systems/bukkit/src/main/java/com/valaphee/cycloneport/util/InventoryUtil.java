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

package com.valaphee.cycloneport.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Default
 *
 * @author valaphee
 */
public class InventoryUtil
{
	private static final int COLUMNS_IN_ROW = 9;
	private final static ItemStack AIR = ItemStackBuilderFactory.builder().setMaterial(Material.AIR).build();

	public static void transition(final Plugin plugin, final Inventory inventory, final boolean leftDirection, final ItemStack[] newItems, final ItemStack[] oldItems, final Inventory finalInventory)
	{
		new BukkitRunnable()
		{
			private int step = leftDirection ? COLUMNS_IN_ROW : -COLUMNS_IN_ROW;

			@Override
			@SuppressWarnings("deprecation")
			public void run()
			{
				if (inventory.getViewers().isEmpty())
				{
					cancel();
				}

				for (int row = (inventory.getSize() / COLUMNS_IN_ROW) - 1; row >= 0; --row)
				{
					if (leftDirection)
					{
						for (int column = 0; column < 9; ++column)
						{
							final int index = ((row * 9) + column);
							if ((oldItems != null) && (oldItems[index] != null))
							{
								if ((column - (9 - step)) >= 0)
								{
									inventory.setItem(index - (9 - step), oldItems[index]);
								}
								if ((((column - (9 - step)) + 1) >= 0) && (((column - (9 - step)) + 1) < 9))
								{
									inventory.setItem(index - (9 - step) + 1, AIR);
								}
							}
							if ((newItems != null) && (newItems[index] != null) && (column + step < 9))
							{
								inventory.setItem(index + step, newItems[index]);
								if (((column + step + 1) >= 0) && ((column + step + 1) < 9))
								{
									inventory.setItem(index + step + 1, AIR);
								}
							}
						}
					}
					else
					{
						for (int column = 8; column >= 0; --column)
						{
							final int index = ((row * 9) + column);
							if ((oldItems != null) && (oldItems[index] != null))
							{
								if ((column + (9 + step)) < 9)
								{
									inventory.setItem(index + (9 + step), oldItems[index]);
								}
								if ((((column + (9 + step)) - 1) >= 0) && (((column + (9 + step)) - 1) < 9))
								{
									inventory.setItem(index + (9 + step) - 1, AIR);
								}
							}
							if ((newItems != null) && (newItems[index] != null) && (column + step >= 0))
							{
								inventory.setItem(index + step, newItems[index]);
								if (((column + step - 1) >= 0) && ((column + step - 1) < 9))
								{
									inventory.setItem(index + step - 1, AIR);
								}
							}
						}
					}
				}

				if (step == 0)
				{
					if (finalInventory != null)
					{
						final List<Player> viewersCopy = new ArrayList(inventory.getViewers());
						viewersCopy.stream().map((viewer) -> (Player) viewer).forEach((player) ->
						{
							player.openInventory(finalInventory);
						});
					}

					cancel();
				}
				else
				{
					inventory.getViewers().stream().map((viewer) -> (Player) viewer).forEach((player) ->
					{
						player.updateInventory();
					});
				}
				step += leftDirection ? -1 : +1;
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}

	private InventoryUtil()
	{}
}
