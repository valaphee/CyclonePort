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

package com.valaphee.cycloneport.nbt;

/**
 * Default
 *
 * @author valaphee
 */
public final class NBTConstants
{
	public static final int END = 0x00,
			BYTE = 0x01,
			SHORT = 0x02,
			INTEGER = 0x03,
			LONG = 0x04,
			FLOAT = 0x05,
			DOUBLE = 0x06,
			BYTE_ARRAY = 0x07,
			STRING = 0x08,
			LIST = 0x09,
			COMPOUND = 0x0a,
			INTEGER_ARRAY = 0x0b;

	public static int getTagId(final Class<? extends Tag> clazz)
	{
		if (clazz.equals(ByteTag.class))
		{
			return BYTE;
		}
		else if (clazz.equals(ShortTag.class))
		{
			return SHORT;
		}
		else if (clazz.equals(IntegerTag.class))
		{
			return INTEGER;
		}
		else if (clazz.equals(LongTag.class))
		{
			return LONG;
		}
		else if (clazz.equals(FloatTag.class))
		{
			return FLOAT;
		}
		else if (clazz.equals(DoubleTag.class))
		{
			return DOUBLE;
		}
		else if (clazz.equals(ByteArrayTag.class))
		{
			return BYTE_ARRAY;
		}
		else if (clazz.equals(StringTag.class))
		{
			return STRING;
		}
		else if (clazz.equals(ListTag.class))
		{
			return LIST;
		}
		else if (clazz.equals(CompoundTag.class))
		{
			return COMPOUND;
		}
		else if (clazz.equals(IntegerArrayTag.class))
		{
			return INTEGER_ARRAY;
		}

		throw new IllegalArgumentException("Invalid tag " + clazz.getName() + ".");
	}

	public static Class<? extends Tag> getTagClass(final int id)
	{
		switch (id)
		{
		case BYTE:
			return ByteTag.class;
		case SHORT:
			return ShortTag.class;
		case INTEGER:
			return IntegerTag.class;
		case LONG:
			return LongTag.class;
		case FLOAT:
			return FloatTag.class;
		case DOUBLE:
			return DoubleTag.class;
		case BYTE_ARRAY:
			return ByteArrayTag.class;
		case STRING:
			return StringTag.class;
		case LIST:
			return ListTag.class;
		case COMPOUND:
			return CompoundTag.class;
		case INTEGER_ARRAY:
			return IntegerArrayTag.class;
		default:
			throw new IllegalArgumentException("Invalid tag " + id + ".");
		}
	}

	private NBTConstants()
	{}
}
