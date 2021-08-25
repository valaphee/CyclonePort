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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Default
 *
 * @author valaphee
 */
public final class ListTag
		extends AbstractListTag
{
	private Class<? extends Tag> type;
	private final List<Tag> value;

	protected ListTag()
	{
		this(null, new ArrayList<>());
	}

	protected ListTag(final Class<? extends Tag> type, final List<Tag> value)
	{
		this.type = type;
		this.value = value;
	}

	@Override
	public Tag[] arrayValue()
	{
		return value.toArray(new Tag[value.size()]);
	}

	@Override
	public List<Tag> listValue()
	{
		return value;
	}

	@Override
	public int size()
	{
		return value.size();
	}

	@Override
	public boolean contains(final int index)
	{
		return index < value.size();
	}

	@Override
	public Tag get(final int index)
	{
		return value.get(index);
	}

	@Override
	public void put(final Tag tag)
	{
		value.add(tag);
	}

	@Override
	public byte getByte(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isInteger())
			{
				return tag.asIntegerTag().byteValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return 0;
	}

	@Override
	public void putByte(final byte value)
	{
		this.value.add(new ByteTag(value));
	}

	@Override
	public short getShort(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isInteger())
			{
				return tag.asIntegerTag().shortValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return 0;
	}

	@Override
	public void putShort(final short value)
	{
		this.value.add(new ShortTag(value));
	}

	@Override
	public int getInt(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isInteger())
			{
				return tag.asIntegerTag().intValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return 0;
	}

	@Override
	public void putInt(final int value)
	{
		this.value.add(new IntegerTag(value));
	}

	@Override
	public long getLong(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isInteger())
			{
				return tag.asIntegerTag().longValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return 0;
	}

	@Override
	public void putLong(final long value)
	{
		this.value.add(new LongTag(value));
	}

	@Override
	public float getFloat(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isDecimal())
			{
				return tag.asDecimalTag().floatValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return Float.NaN;
	}

	@Override
	public void putFloat(final float value)
	{
		this.value.add(new FloatTag(value));
	}

	@Override
	public double getDouble(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isDecimal())
			{
				return tag.asDecimalTag().doubleValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return Double.NaN;
	}

	@Override
	public void putDouble(final double value)
	{
		this.value.add(new DoubleTag(value));
	}

	@Override
	public byte[] getBytes(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isDecimal())
			{
				return tag.asArrayTag().bytesValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return new byte[0];
	}

	@Override
	public void putBytes(final byte[] value)
	{
		this.value.add(new ByteArrayTag(value));
	}

	@Override
	public String getString(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isDecimal())
			{
				return tag.asArrayTag().stringValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return new String();
	}

	@Override
	public void putString(final String value)
	{
		this.value.add(new StringTag(value));
	}

	@Override
	public int[] getInts(final int index)
	{
		try
		{
			final Tag tag = value.get(index);
			if (tag.isDecimal())
			{
				return tag.asArrayTag().intsValue();
			}
		}
		catch (final NoSuchElementException ignore)
		{
		}

		return new int[0];
	}

	@Override
	public void putInts(final int[] value)
	{
		this.value.add(new IntegerArrayTag(value));
	}

	@Override
	public void read(final DataInputStream stream)
			throws IOException
	{
		value.clear();

		type = NBTConstants.getTagClass(stream.readByte());
		for (int i = 0, length = stream.readInt(); i != 0; --i)
		{
			Tag tag;
			try
			{
				tag = type.newInstance();
			}
			catch (final InstantiationException ex)
			{
				throw new IllegalStateException("Could instantiate the object.", ex);
			}
			catch (final IllegalAccessException ex)
			{
				throw new IllegalStateException("Could not access the constructor.", ex);
			}
			catch (final IllegalArgumentException ex)
			{
				throw ex;
			}
			tag.read(stream);
			value.add(tag);
		}
	}

	@Override
	public void write(final DataOutputStream stream)
			throws IOException
	{
		if ((type == null) || type.equals(Tag.class))
		{
			type = value.get(0).getClass();
		}

		stream.writeByte(NBTConstants.getTagId(type));
		stream.writeInt(value.size());
		for (final Tag tag : value)
		{
			tag.write(stream);
		}
	}
}
