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

package com.valaphee.cycloneport.chat;

import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.communication.packet.PlayerMessagePacket;
import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.chat.command.ChannelMessageCommand;
import com.valaphee.cycloneport.chat.command.PrivateMessageCommand;
import java.util.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Default
 *
 * @author valaphee
 */
public final class ChatModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bChat \u00A78\u00A7l\u25CF ";
	private final List<ChatFilter> filters = new ArrayList<>();
	private final Map<UUID, ChatMessage> lastMessages = new HashMap<>();

	public ChatModule(final Plugin plugin)
	{
		super("Chat", plugin);
	}

	@Override
	public void startup()
	{
		addFilter(new CapsChatFilter(0.75f));
		addFilter(new SpamChatFilter(2500L));
		addFilter(new SimilarSpamChatFilter(0.925f));
		addFilter(new CharacterSpamChatFilter(6));
		addFilter(new DomainNameChatFilter());
		addFilter(new IPv4AddressChatFilter());
		addFilter(new IPv6AddressChatFilter());
		addFilter(new CensorshipChatFilter(Arrays.asList("arschloch", "hurensohn", "scheiß", "anal", "fuck", "dildo", "vibrator", "kack", "bastard", "schwanzlutcher", "peitsche")));
	}

	@Override
	public void registerCommands()
	{
		registerCommand(new PrivateMessageCommand(this));
		registerCommand(new ChannelMessageCommand(this));
	}

	@Override
	public void shutdown()
	{
		filters.clear();
	}

	public void addFilter(final ChatFilter filter)
	{
		filters.add(filter);
	}

	public void sendPrivateMessage(final ProxiedPlayer sender, final ChatMessage message)
	{
		final ProxiedPlayer receiver = getProxy().getPlayer(message.getReceiver());
		if (receiver == null)
		{
			sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "\u00A77Der Spieler \u00A73" + message.getReceiver() + "\u00A77 ist nicht online."));
		}
		else if (message.getSender().getUniqueId().equals(receiver.getUniqueId()))
		{
			sender.sendMessage(TextComponent.fromLegacyText(PREFIX + "\u00A77Du kannst \u00A73dir\u00A77 keine Nachricht senden."));
		}
		else
		{
			final BaseComponent[] formattedMessage = TextComponent.fromLegacyText("\u00A77" + sender.getDisplayName() + " \u00A78\u00A7l\u27EB \u00A7c@\u00A77" + receiver.getDisplayName() + "» \u00A77" + message.getContent());

			Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(PlayerMessagePacket.Type.PRIVATE_MESSAGE, Cyclone.getInstance().getAccountManager().findUserId(message.getSender().getUniqueId()), message.getReceiver(), message.getContent()));

			message.getSender().sendMessage(formattedMessage);
		}
	}

	public void sendChannelMessage(final ProxiedPlayer sender, final ChatMessage message)
	{
		final String channel = message.getChannel();
		final BaseComponent[] formattedMessage = TextComponent.fromLegacyText("\u00A77" + sender.getDisplayName() + " \u00A78\u00A7l\u27EB \u00A7c@\u00A77" + channel + "\u00BB \u00A77" + message.getContent());

		Cyclone.getInstance().getConnectionManager().getOutgoingConnection().sendPacket(new PlayerMessagePacket(PlayerMessagePacket.Type.CHANNEL_MESSAGE, Cyclone.getInstance().getAccountManager().findUserId(message.getSender().getUniqueId()), message.getChannel(), message.getContent()));

		message.getSender().sendMessage(formattedMessage);
	}

	@EventHandler
	public void on(final ChatEvent event)
	{
		if (event.isCancelled() || event.isCommand())
		{
			return;
		}

		final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		final ChatMessage message = new ChatMessage(player, System.currentTimeMillis(), event.getMessage());
		final ChatMessage lastMessage = lastMessages.get(player.getUniqueId());
		for (final ChatFilter filter : filters)
		{
			if (filter.filter(message, lastMessage))
			{
				player.sendMessage(TextComponent.fromLegacyText(PREFIX + filter.getMessage()));
				event.setCancelled(true);

				break;
			}
		}

		if (!event.isCancelled())
		{
			if (message.getReceiver() != null)
			{
				sendPrivateMessage(player, message);

				event.setCancelled(true);
			}
			else if (message.getChannel() != null)
			{
				sendChannelMessage(player, message);
			}

			lastMessages.put(player.getUniqueId(), message);
		}
	}

	@EventHandler
	public void on(final PlayerDisconnectEvent event)
	{
		lastMessages.remove(event.getPlayer().getUniqueId());
	}
}
