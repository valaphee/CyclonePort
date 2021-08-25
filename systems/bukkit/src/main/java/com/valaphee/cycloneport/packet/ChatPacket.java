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

package com.valaphee.cycloneport.packet;

import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.ConstructorAccessor;
import com.valaphee.cyclone.reflect.FieldAccessor;
import com.valaphee.cyclone.reflect.MethodAccessor;
import com.valaphee.cyclone.text.MinecraftJSONComponent;
import com.valaphee.cycloneport.reflect.PackageType;
import org.bukkit.entity.Player;

/**
 * Default
 *
 * @author valaphee
 */
public final class ChatPacket
{
	private static MethodAccessor CHAT_SERIALIZER_A;
	private static MethodAccessor CHAT_MESSAGE_TYPE_A;
	private static ConstructorAccessor PACKET_PLAY_OUT_CHAT;
	private static MethodAccessor CRAFT_PLAYER_GET_HANDLE;
	private static FieldAccessor ENTITY_PLAYER_PLAYER_CONNECTION;
	private static MethodAccessor PLAYER_CONNECTION_SEND_PACKET;
	private final Object message;
	private final Object messageType;

	public ChatPacket(final boolean notice, final MinecraftJSONComponent message)
	{
		this.message = CHAT_SERIALIZER_A.invoke(null, MinecraftJSONComponent.Serializer.encode(message));
		this.messageType = CHAT_MESSAGE_TYPE_A.invoke(null, (byte) (notice ? 2 : 1));
	}

	public void send(final Player player)
	{
		if (player.isOnline())
		{
			final Object playerConnection = ENTITY_PLAYER_PLAYER_CONNECTION.get(CRAFT_PLAYER_GET_HANDLE.invoke(player));
			final Object packet = PACKET_PLAY_OUT_CHAT.invoke(message, messageType);

			PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, packet);
		}
	}

	static
	{
		try
		{
			final Class<?> chatSerializer = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer");
			final Class<?> chatBaseComponent = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent");
			final Class<?> chatMessageType = PackageType.MINECRAFT_SERVER.getClass("ChatMessageType");
			final Class<?> packet = PackageType.MINECRAFT_SERVER.getClass("Packet");
			final Class<?> packetPlayOutChat = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutChat");
			final Class<?> craftPlayer = PackageType.CRAFTBUKKIT_ENTITY.getClass("CraftPlayer");
			final Class<?> entityPlayer = PackageType.MINECRAFT_SERVER.getClass("EntityPlayer");
			final Class<?> playerConnection = PackageType.MINECRAFT_SERVER.getClass("PlayerConnection");

			CHAT_SERIALIZER_A = Accessors.getMethodAccessorOrNull(chatSerializer, "a", String.class);
			CHAT_MESSAGE_TYPE_A = Accessors.getMethodAccessorOrNull(chatMessageType, "a", byte.class);
			PACKET_PLAY_OUT_CHAT = Accessors.getConstructorAccessorOrNull(packetPlayOutChat, chatBaseComponent, chatMessageType);
			CRAFT_PLAYER_GET_HANDLE = Accessors.getMethodAccessorOrNull(craftPlayer, "getHandle");
			ENTITY_PLAYER_PLAYER_CONNECTION = Accessors.getFieldAccessorOrNull(entityPlayer, "playerConnection");
			PLAYER_CONNECTION_SEND_PACKET = Accessors.getMethodAccessorOrNull(playerConnection, "sendPacket", packet);
		}
		catch (final ClassNotFoundException ignore)
		{
		}
	}
}
