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

import com.valaphee.cyclone.util.CharsetUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public final class CompoundTag
		extends AbstractCompoundTag
{
	private final Map<String, Tag> value;

	protected CompoundTag()
	{
		value = new HashMap<>();
	}

	protected CompoundTag(final Map<String, Tag> value)
	{
		this.value = value;
	}

	@Override
	public Map<String, Tag> mapValue()
	{
		return value;
	}

	@Override
	public boolean contains(final String name)
	{
		return value.containsKey(name);
	}

	@Override
	public Tag get(final String name)
	{
		return value.get(name);
	}

	@Override
	public void set(final String name, final Tag tag)
	{
		value.put(name, tag);
	}

	@Override
	public byte getByte(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isInteger())
		{
			return tag.asIntegerTag().byteValue();
		}

		return 0;
	}

	@Override
	public void setByte(final String name, final byte value)
	{
		this.value.put(name, new ByteTag(value));
	}

	@Override
	public short getShort(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isInteger())
		{
			return tag.asIntegerTag().shortValue();
		}

		return 0;
	}

	@Override
	public void setShort(final String name, final short value)
	{
		this.value.put(name, new ShortTag(value));
	}

	@Override
	public int getInt(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isInteger())
		{
			return tag.asIntegerTag().intValue();
		}

		return 0;
	}

	@Override
	public void setInt(final String name, final int value)
	{
		this.value.put(name, new IntegerTag(value));
	}

	@Override
	public long getLong(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isInteger())
		{
			return tag.asIntegerTag().longValue();
		}

		return 0L;
	}

	@Override
	public void setLong(final String name, final long value)
	{
		this.value.put(name, new LongTag(value));
	}

	@Override
	public float getFloat(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isDecimal())
		{
			return tag.asDecimalTag().floatValue();
		}

		return Float.NaN;
	}

	@Override
	public void setFloat(final String name, final float value)
	{
		this.value.put(name, new FloatTag(value));
	}

	@Override
	public double getDouble(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isDecimal())
		{
			return tag.asDecimalTag().doubleValue();
		}

		return Double.NaN;
	}

	@Override
	public void setDouble(final String name, final double value)
	{
		this.value.put(name, new DoubleTag(value));
	}

	@Override
	public byte[] getBytes(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isArray())
		{
			return tag.asArrayTag().bytesValue();
		}

		return new byte[0];
	}

	@Override
	public void setBytes(final String name, final byte[] value)
	{
		this.value.put(name, new ByteArrayTag(value));
	}

	@Override
	public String getString(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isArray())
		{
			return tag.asArrayTag().stringValue();
		}

		return new String();
	}

	@Override
	public void setString(final String name, final String value)
	{
		this.value.put(name, new StringTag(value));
	}

	@Override
	public int[] getInts(final String name)
	{
		final Tag tag = value.get(name);
		if ((tag != null) && tag.isArray())
		{
			return tag.asArrayTag().intsValue();
		}

		return new int[0];
	}

	@Override
	public void setInts(final String name, final int[] value)
	{
		this.value.put(name, new IntegerArrayTag(value));
	}

	@Override
	public void read(final DataInputStream stream)
			throws IOException
	{
		value.clear();

		for (byte type = stream.readByte(); type != 0; type = stream.readByte())
		{
			final byte[] bytes = new byte[stream.readUnsignedShort()];
			stream.read(bytes);

			final String name = new String(bytes, CharsetUtil.UTF_8);
			final Tag tag;
			try
			{
				tag = NBTConstants.getTagClass(type).newInstance();
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
			value.put(name, tag);
		}
	}

	@Override
	public void write(final DataOutputStream stream)
			throws IOException
	{
		for (final Map.Entry<String, Tag> entry : value.entrySet())
		{
			stream.writeByte(NBTConstants.getTagId(entry.getValue().getClass()));
			final byte[] bytes = entry.getKey().getBytes(CharsetUtil.UTF_8);
			stream.writeShort(bytes.length);
			stream.write(bytes);
			entry.getValue().write(stream);
		}
		stream.writeByte(0);
	}
}
