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

package com.valaphee.cycloneport.gadget;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Default
 *
 * @author valaphee
 */
public class BootsGadget
		extends Gadget
{
	public BootsGadget(final String name, final ItemStack item, final Rarity rarity, final float price)
	{
		super(name, item, Type.BOOTS, rarity, price);
	}

	@Override
	public void apply(final Player player)
	{
		player.getInventory().setBoots(getRawItem());
	}
}
