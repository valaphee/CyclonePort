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
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.PermissionHandler;
import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.account.data.UserPunishmentData;
import com.valaphee.cyclone.util.TimestampUtil;
import com.valaphee.cycloneport.Module;
import com.valaphee.cycloneport.chat.command.BroadcastCommand;
import com.valaphee.cycloneport.chat.command.FlushCommand;
import com.valaphee.cycloneport.chat.command.SilenceCommand;
import com.valaphee.cycloneport.chat.command.SlowmodeCommand;
import com.valaphee.cycloneport.util.FormatUtil;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class ChatModule
		extends Module
{
	public static final String PREFIX = "\u00A78\u00A7l\u258E \u00A7aDein\u00A7bChat \u00A78\u00A7l\u25CF ";
	private AccountManager accountManager;
	private PermissionHandler permissionHandler;
	private long slowmode = 0;
	private boolean silence = false;
	private final Map<UUID, ChatMessage> lastMessages = new ConcurrentHashMap<>();

	public ChatModule(final JavaPlugin plugin)
	{
		super("Chat", plugin);
	}

	@Override
	public void startup()
	{
		accountManager = Cyclone.getInstance().getAccountManager();
		permissionHandler = Cyclone.getInstance().getPermissionHandler();
	}

	@Override
	public void registerCommands()
	{
		registerCommand(new SilenceCommand(this));
		registerCommand(new SlowmodeCommand(this));
		registerCommand(new BroadcastCommand(this));
		registerCommand(new FlushCommand(this));
	}

	public long getSlowmode()
	{
		return slowmode;
	}

	public void setSlowmode(final long slowmode, final boolean inform)
	{
		this.slowmode = slowmode;

		if (inform)
		{
			if (slowmode != 0)
			{
				getServer().broadcastMessage(PREFIX + "\u00A77Slowmode wurde für alle \u00A73aktiviert\u00A77. Bedeutet du kannst nur alle \u00A73" + TimestampUtil.formatTimestamp(new Timestamp(slowmode)) + "\u00A77 schreiben.");
			}
			else
			{
				getServer().broadcastMessage(PREFIX + "\u00A77Slowmode wurde für alle wieder \u00A73deaktiviert\u00A77.");
			}
		}
	}

	public boolean isSilence()
	{
		return silence;
	}

	public void setSilence(final boolean silence, final boolean inform)
	{
		this.silence = silence;

		if (inform)
		{
			if (silence)
			{
				getServer().broadcastMessage(PREFIX + "\u00A77Stille wurde für alle \u00A73aktiviert\u00A77.");
			}
			else
			{
				getServer().broadcastMessage(PREFIX + "\u00A77Stille wurde für alle wieder \u00A73deaktiviert\u00A77.");
			}
		}
	}

	public void broadcast(final String message)
	{
		getServer().getOnlinePlayers().forEach((player) ->
		{
			player.sendMessage(message);
		});
	}

	@EventHandler
	public void on(final AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		final User user = accountManager.getUser(accountManager.findUserId(player.getUniqueId()));

		final UserPunishmentData punishment = user.getActivePunishment(UserPunishmentData.Type.MUTE);
		if (punishment != null && (!player.hasPermission("cycloneport.chat.moderator")))
		{
			if (TimestampUtil.getCurrentTimestamp().after(punishment.expire))
			{
				punishment.active = false;
			}
			else
			{
				player.sendMessage(PREFIX + "\u00A77Du kannst nicht schreiben da du für \u00A73" + TimestampUtil.formatTimestamp(punishment.createdAt, punishment.expire) + "\u00A77 wegen \u00A73" + punishment.reason + "\u00A77 stummgeschaltet wurdest.");
				event.setCancelled(true);
			}
		}

		final ChatMessage message = new ChatMessage(player, System.currentTimeMillis(), event.getMessage());
		final ChatMessage lastMessage = lastMessages.get(player.getUniqueId());
		if (isSilence() && (!player.hasPermission("cycloneport.chat.moderator")))
		{
			player.sendMessage(PREFIX + "\u00A77Du kannst nicht schreiben da der Chat \u00A73stummgeschaltet\u00A77 ist.");
			event.setCancelled(true);
		}
		else if (lastMessage != null)
		{
			final long difference = message.getSent() - lastMessage.getSent();
			if ((difference < getSlowmode()) && (!permissionHandler.has(user, "cycloneport.chat.moderator")))
			{
				player.sendMessage(PREFIX + "\u00A77Bitte warte \u00A73" + TimestampUtil.formatTimestamp(new Timestamp(difference)) + "\u00A77 bevor du wieder schreiben kannst.");
				event.setCancelled(true);
			}
		}

		if (!event.isCancelled())
		{
			final StringBuilder format = new StringBuilder();
			final String color = "\u00A7" + permissionHandler.getColor(user).getCharacter();
			final String prefix = permissionHandler.getPrefix(user);
			if (prefix != null)
			{
				format.append(color).append(prefix).append("\u00A78 \u299A ");
			}
			format.append("%s");
			final String suffix = permissionHandler.getSuffix(user);
			if (suffix != null)
			{
				format.append("\u00A78 \u299A ").append(suffix);
			}
			format.append("\u00A78 \u00BB \u00A7f%s");
			event.setFormat(format.toString());
			event.setMessage(format(player, message.getContent()));

			lastMessages.put(player.getUniqueId(), message);
		}
	}

	@EventHandler
	public void on(final PlayerQuitEvent event)
	{
		lastMessages.remove(event.getPlayer().getUniqueId());
	}

	@SuppressWarnings("AssignmentToMethodParameter")
	public static String format(final CommandSender sender, String message)
	{
		if (sender.hasPermission("cycloneport.chat.color"))
		{
			message = FormatUtil.replace(message, FormatUtil.REPLACE_COLOR_PATTERN);
		}
		else
		{
			message = FormatUtil.strip(message, FormatUtil.VANILLA_COLOR_PATTERN);
		}
		if (sender.hasPermission("cycloneport.chat.format"))
		{
			message = FormatUtil.replace(message, FormatUtil.REPLACE_FORMAT_PATTERN);
		}
		else
		{
			message = FormatUtil.strip(message, FormatUtil.VANILLA_FORMAT_PATTERN);
		}
		if (sender.hasPermission("cycloneport.chat.magic"))
		{
			message = FormatUtil.replace(message, FormatUtil.REPLACE_MAGIC_PATTERN);
		}
		else
		{
			message = FormatUtil.strip(message, FormatUtil.VANILLA_MAGIC_PATTERN);
		}

		return message;
	}
}
