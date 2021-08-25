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
@SuppressWarnings("unchecked")
public final class TitlePacket
{
	private static MethodAccessor CHAT_SERIALIZER_A;
	private static ConstructorAccessor PACKET_PLAY_OUT_TITLE;
	private static ConstructorAccessor PACKET_PLAY_OUT_TITLE_TIMINGS;
	private static Class<Enum> PACKET_PLAY_OUT_TITLE_ENUM_TITLE_ACTION;
	private static MethodAccessor CRAFT_PLAYER_GET_HANDLE;
	private static FieldAccessor ENTITY_PLAYER_PLAYER_CONNECTION;
	private static MethodAccessor PLAYER_CONNECTION_SEND_PACKET;
	private boolean overrideTimings;
	private int fadeIn;
	private int stay;
	private int fadeOut;
	private Object title;
	private Object subtitle;

	public TitlePacket(final MinecraftJSONComponent title, final MinecraftJSONComponent subtitle)
	{
		if (title != null)
		{
			this.title = CHAT_SERIALIZER_A.invoke(null, MinecraftJSONComponent.Serializer.encode(title));
		}
		if (subtitle != null)
		{
			this.subtitle = CHAT_SERIALIZER_A.invoke(null, MinecraftJSONComponent.Serializer.encode(subtitle));
		}
	}

	public TitlePacket(final int fadeIn, final int stay, final int fadeOut, final MinecraftJSONComponent title, final MinecraftJSONComponent subtitle)
	{
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
		if (title != null)
		{
			this.title = CHAT_SERIALIZER_A.invoke(null, MinecraftJSONComponent.Serializer.encode(title));
		}
		if (subtitle != null)
		{
			this.subtitle = CHAT_SERIALIZER_A.invoke(null, MinecraftJSONComponent.Serializer.encode(subtitle));
		}
	}

	public void send(final Player player)
	{
		final Object playerConnection = ENTITY_PLAYER_PLAYER_CONNECTION.get(CRAFT_PLAYER_GET_HANDLE.invoke(player));
		if (overrideTimings)
		{
			final Object timingsPacket = PACKET_PLAY_OUT_TITLE_TIMINGS.invoke(Enum.valueOf(PACKET_PLAY_OUT_TITLE_ENUM_TITLE_ACTION, "TIMES"), null, fadeOut, stay, fadeIn);

			PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, timingsPacket);
		}
		if (this.subtitle != null)
		{
			final Object subtitlePacket = PACKET_PLAY_OUT_TITLE.invoke(Enum.valueOf(PACKET_PLAY_OUT_TITLE_ENUM_TITLE_ACTION, "SUBTITLE"), subtitle);

			PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, subtitlePacket);
		}
		if (this.title != null)
		{
			final Object titlePacket = PACKET_PLAY_OUT_TITLE.invoke(Enum.valueOf(PACKET_PLAY_OUT_TITLE_ENUM_TITLE_ACTION, "TITLE"), title);

			PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, titlePacket);
		}
	}

	static
	{
		try
		{
			final Class<?> chatSerializer = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer");
			final Class<?> chatBaseComponent = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent");
			final Class<?> packet = PackageType.MINECRAFT_SERVER.getClass("Packet");
			final Class<?> packetPlayOutTitle = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle");
			final Class<?> packetPlayOutTitleEnumTitleAction = PACKET_PLAY_OUT_TITLE_ENUM_TITLE_ACTION = (Class<Enum>) PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle$EnumTitleAction");
			final Class<?> craftPlayer = PackageType.CRAFTBUKKIT_ENTITY.getClass("CraftPlayer");
			final Class<?> entityPlayer = PackageType.MINECRAFT_SERVER.getClass("EntityPlayer");
			final Class<?> playerConnection = PackageType.MINECRAFT_SERVER.getClass("PlayerConnection");

			CHAT_SERIALIZER_A = Accessors.getMethodAccessorOrNull(chatSerializer, "a", String.class);
			PACKET_PLAY_OUT_TITLE = Accessors.getConstructorAccessorOrNull(packetPlayOutTitle, packetPlayOutTitleEnumTitleAction, chatBaseComponent);
			PACKET_PLAY_OUT_TITLE_TIMINGS = Accessors.getConstructorAccessorOrNull(packetPlayOutTitle, packetPlayOutTitleEnumTitleAction, chatBaseComponent, int.class, int.class, int.class);
			CRAFT_PLAYER_GET_HANDLE = Accessors.getMethodAccessorOrNull(craftPlayer, "getHandle");
			ENTITY_PLAYER_PLAYER_CONNECTION = Accessors.getFieldAccessorOrNull(entityPlayer, "playerConnection");
			PLAYER_CONNECTION_SEND_PACKET = Accessors.getMethodAccessorOrNull(playerConnection, "sendPacket", packet);
		}
		catch (final ClassNotFoundException ignore)
		{
		}
	}
}
