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
public final class StringTag
		extends AbstractArrayTag
{
	private String value;

	protected StringTag()
	{}

	protected StringTag(final String value)
	{
		this.value = value;
	}

	@Override
	public byte[] bytesValue()
	{
		return value.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public int[] intsValue()
	{
		return ByteBuffer.wrap(bytesValue()).asIntBuffer().array();
	}

	@Override
	public String stringValue()
	{
		return value;
	}

	@Override
	public void read(final DataInputStream stream)
			throws IOException
	{
		final byte[] bytes = new byte[stream.readUnsignedShort()];
		stream.read(bytes);

		value = new String(bytes, CharsetUtil.UTF_8);
	}

	@Override
	public void write(final DataOutputStream stream)
			throws IOException
	{
		final byte[] bytes = value.getBytes(CharsetUtil.UTF_8);
		stream.writeShort(bytes.length);
		stream.write(bytes);
	}
}
