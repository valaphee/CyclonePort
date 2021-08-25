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

import java.util.*;

/**
 * Default
 *
 * @author valaphee
 */
public final class TagFactory
{
	public static AbstractNumberTag createNumberTag(final byte value)
	{
		return new ByteTag(value);
	}

	public static AbstractNumberTag createNumberTag(final short value)
	{
		return new ShortTag(value);
	}

	public static AbstractNumberTag createNumberTag(final int value)
	{
		return new IntegerTag(value);
	}

	public static AbstractNumberTag createNumberTag(final long value)
	{
		return new LongTag(value);
	}

	public static AbstractNumberTag createNumberTag(final float value)
	{
		return new FloatTag(value);
	}

	public static AbstractNumberTag createNumberTag(final double value)
	{
		return new DoubleTag(value);
	}

	public static AbstractListTag createListTag()
	{
		return new ListTag();
	}

	public static AbstractListTag createListTag(final Tag[] value)
	{
		return new ListTag(value[0].getClass(), new ArrayList<>(Arrays.asList(value)));
	}

	public static AbstractListTag createListTag(final List<Tag> value)
	{
		return new ListTag(value.get(0).getClass(), value);
	}

	public static AbstractCompoundTag createCompoundTag()
	{
		return new CompoundTag();
	}

	public static AbstractCompoundTag createMapTag(final String[] key, final Tag[] value)
	{
		if (key.length != value.length)
		{
			throw new IllegalArgumentException("Length of keys and values have not same length.");
		}

		final Map<String, Tag> map = new HashMap<>();
		for (int i = 0; i < key.length; i++)
		{
			map.put(key[i], value[i]);
		}

		return new CompoundTag(map);
	}

	public static AbstractCompoundTag createMapTag(final Map<String, Tag> value)
	{
		return new CompoundTag(value);
	}

	public static AbstractArrayTag createArrayTag(final byte[] value)
	{
		return new ByteArrayTag(value);
	}

	public static AbstractArrayTag createArrayTag(final int[] value)
	{
		return new IntegerArrayTag(value);
	}

	public static AbstractArrayTag createArrayTag(final String value)
	{
		return new StringTag(value);
	}

	private TagFactory()
	{}
}
