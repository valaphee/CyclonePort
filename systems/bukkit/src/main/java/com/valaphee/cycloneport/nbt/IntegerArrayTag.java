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
import java.nio.ByteBuffer;

/**
 * Default
 *
 * @author valaphee
 */
public final class IntegerArrayTag
		extends AbstractArrayTag
{
	private int[] value;

	protected IntegerArrayTag()
	{}

	protected IntegerArrayTag(final int[] value)
	{
		this.value = value;
	}

	@Override
	public byte[] bytesValue()
	{
		final ByteBuffer buffer = ByteBuffer.allocate(value.length * Integer.BYTES);
		buffer.asIntBuffer().put(value);
		return buffer.array();
	}

	@Override
	public int[] intsValue()
	{
		return value;
	}

	@Override
	public String stringValue()
	{
		return new String(bytesValue(), CharsetUtil.UTF_8);
	}

	@Override
	public void read(final DataInputStream stream)
			throws IOException
	{
		value = new int[stream.readInt()];
	}

	@Override
	public void write(final DataOutputStream stream)
			throws IOException
	{
		stream.writeInt(value.length);
	}
}
