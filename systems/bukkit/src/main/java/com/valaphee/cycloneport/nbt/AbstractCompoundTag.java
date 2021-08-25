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

import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class AbstractCompoundTag
		implements Tag
{
	@Override
	public boolean isCompound()
	{
		return true;
	}

	@Override
	public AbstractCompoundTag asCompoundTag()
	{
		return this;
	}

	public abstract Map<String, Tag> mapValue();

	public abstract boolean contains(String name);

	public abstract Tag get(String name);

	public abstract void set(String name, Tag tag);

	public abstract byte getByte(String name);

	public abstract void setByte(String name, byte value);

	public abstract short getShort(String name);

	public abstract void setShort(String name, short value);

	public abstract int getInt(String name);

	public abstract void setInt(String name, int value);

	public abstract long getLong(String name);

	public abstract void setLong(String name, long value);

	public abstract float getFloat(String name);

	public abstract void setFloat(String name, float value);

	public abstract double getDouble(String name);

	public abstract void setDouble(String name, double value);

	public abstract byte[] getBytes(String name);

	public abstract void setBytes(String name, byte[] value);

	public abstract String getString(String name);

	public abstract void setString(String name, String value);

	public abstract int[] getInts(String name);

	public abstract void setInts(String name, int[] value);
}
