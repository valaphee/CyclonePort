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

package com.valaphee.cycloneport.playerlist;

import com.valaphee.cycloneport.Module;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Default
 *
 * @author valaphee
 */
public final class PlayerListModule
		extends Module
{
	private static final BaseComponent[] HEADER = TextComponent.fromLegacyText("\u00A77\u00A7l\u00A7k::::::::::\u00A7r \u00A7a\u00A7lDein\u00A7b\u00A7lNetzwerk \u00A77\u00A7l\u00A7k::::::::::\u00A7r\n");
	private static final BaseComponent[] FOOTER = TextComponent.fromLegacyText("\n\u00A74Admin \u00A79Dev \u00A76Builder \u00A7cSrMod \u00A75Mod \u00A72Sup\n\u00A7eVIP \u00A7dMystical \u00A73Legendary \u00A7aEpic");

	public PlayerListModule(final Plugin plugin)
	{
		super("PlayerList", plugin);
	}

	@EventHandler
	public void on(final PostLoginEvent event)
	{
		event.getPlayer().setTabHeader(HEADER, FOOTER);
	}
}
