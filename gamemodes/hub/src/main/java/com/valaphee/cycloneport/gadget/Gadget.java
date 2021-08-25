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

import com.valaphee.cycloneport.util.ItemStackBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class Gadget
{
	private final String name;
	private final ItemStack item;
	private final Type type;
	private final Rarity rarity;
	private final float price;

	public Gadget(final String name, final ItemStack item, final Type type, final Rarity rarity, final float price)
	{
		this.name = name;
		this.item = item;
		this.type = type;
		this.rarity = rarity;
		this.price = price;
	}

	public String getRawName()
	{
		return name;
	}

	public String getName()
	{
		switch (getRarity())
		{
		case COMMON:
			return "\u00A77\u00A7o" + name;
		case UNCOMMON:
			return "\u00A72\u00A7o" + name;
		case RARE:
			return "\u00A71\u00A7o" + name;
		case EPIC:
			return "\u00A75\u00A7o" + name;
		case LEGENDARY:
			return "\u00A76\u00A7o" + name;
		}

		return name;
	}

	public ItemStack getRawItem()
	{
		return item;
	}

	public ItemStack getItem()
	{
		final List<String> itemLore = new ArrayList<>();
		switch (getRarity())
		{
		case COMMON:
			itemLore.add("\u00A77\u00A7oGewöhnlich");
			break;
		case UNCOMMON:
			itemLore.add("\u00A72\u00A7oUngewöhnlich");
			break;
		case RARE:
			itemLore.add("\u00A71\u00A7oSelten");
			break;
		case EPIC:
			itemLore.add("\u00A75\u00A7oEpisch");
			break;
		case LEGENDARY:
			itemLore.add("\u00A76\u00A7oLegendär");
		}
		itemLore.add("\u00A77\u00A7oPreis: " + getPrice());

		return ItemStackBuilderFactory.editor(item).setName(getName()).setLore(itemLore).build();
	}

	public Type getType()
	{
		return type;
	}

	public Rarity getRarity()
	{
		return rarity;
	}

	public float getPrice()
	{
		return price;
	}

	public abstract void apply(Player player);

	public static enum Type
	{
		SKULL("Köpfe"),
		BOOTS("Stiefel"),
		WINGS("Flügel"),
		TOOL("Werkzeuge");
		private final String name;

		private Type(final String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}

	public static enum Rarity
	{
		COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
	}
}
