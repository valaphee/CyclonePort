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

package com.valaphee.cycloneport.clearlag;

import com.valaphee.cycloneport.Module;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class ClearLagModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bClearLag \u00A78\u00A7l\u25CF ";

	public ClearLagModule(final JavaPlugin plugin)
	{
		super("Combat", plugin);
	}

	@Override
	public void startup()
	{
		runTaskSynchronously(new ItemRemover(this), 20L, 20L);
		runTaskSynchronously(new InventoryLeakFixer(this), 20L, 20L);
	}
}
