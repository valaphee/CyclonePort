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
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Default
 *
 * @author valaphee
 */
public final class ShortTag
		extends AbstractNumberTag
{
	private short value;

	protected ShortTag()
	{}

	protected ShortTag(final short value)
	{
		this.value = value;
	}

	@Override
	public boolean isInteger()
	{
		return true;
	}

	@Override
	public AbstractNumberTag asIntegerTag()
	{
		return this;
	}

	@Override
	public byte byteValue()
	{
		return (byte) value;
	}

	@Override
	public short shortValue()
	{
		return value;
	}

	@Override
	public int intValue()
	{
		return value;
	}

	@Override
	public long longValue()
	{
		return value;
	}

	@Override
	public BigInteger bigIntegerValue()
	{
		return BigInteger.valueOf(longValue());
	}

	@Override
	public float floatValue()
	{
		return value;
	}

	@Override
	public double doubleValue()
	{
		return value;
	}

	@Override
	public BigDecimal bigDecimalValue()
	{
		return BigDecimal.valueOf(doubleValue());
	}

	@Override
	public void read(final DataInputStream stream)
			throws IOException
	{
		value = stream.readShort();
	}

	@Override
	public void write(final DataOutputStream stream)
			throws IOException
	{
		stream.writeShort(value);
	}
}
