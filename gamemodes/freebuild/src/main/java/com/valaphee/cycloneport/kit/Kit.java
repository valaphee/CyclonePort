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

package com.valaphee.cycloneport.kit;

import org.bukkit.inventory.ItemStack;

/**
 * Default
 *
 * @author valaphee
 */
public final class Kit
{
	private final String name;
	private final ItemStack icon;
	private final String permission;
	private final ItemStack[] content;

	public Kit(final String name, final ItemStack icon, final ItemStack... content)
	{
		this(name, icon, null, content);
	}

	public Kit(final String name, final ItemStack icon, final String permission, final ItemStack... content)
	{
		this.name = name;
		this.icon = icon;
		this.permission = permission;
		this.content = content;
	}

	public String getName()
	{
		return name;
	}

	public ItemStack getIcon()
	{
		return icon;
	}

	public String getPermission()
	{
		return permission;
	}

	public ItemStack[] getContent()
	{
		return content;
	}
}
