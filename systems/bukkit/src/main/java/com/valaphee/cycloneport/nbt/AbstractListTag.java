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

import java.util.List;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class AbstractListTag
		implements Tag
{
	@Override
	public boolean isList()
	{
		return true;
	}

	@Override
	public AbstractListTag asListTag()
	{
		return this;
	}

	public abstract Tag[] arrayValue();

	public abstract List<Tag> listValue();

	public abstract int size();

	public abstract boolean contains(int index);

	public abstract Tag get(int index);

	public abstract void put(Tag tag);

	public abstract byte getByte(int index);

	public abstract void putByte(byte value);

	public abstract short getShort(int index);

	public abstract void putShort(short value);

	public abstract int getInt(int index);

	public abstract void putInt(int value);

	public abstract long getLong(int index);

	public abstract void putLong(long value);

	public abstract float getFloat(int index);

	public abstract void putFloat(float value);

	public abstract double getDouble(int index);

	public abstract void putDouble(double value);

	public abstract byte[] getBytes(int index);

	public abstract void putBytes(byte[] value);

	public abstract String getString(int index);

	public abstract void putString(String value);

	public abstract int[] getInts(int index);

	public abstract void putInts(int[] value);
}
