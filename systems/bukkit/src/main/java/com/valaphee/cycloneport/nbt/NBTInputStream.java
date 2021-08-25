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
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Default
 *
 * @author valaphee
 */
public class NBTInputStream
		extends DataInputStream
{
	public NBTInputStream(final InputStream stream)
			throws IOException
	{
		this(stream, true);
	}

	public NBTInputStream(final InputStream stream, boolean compressed)
			throws IOException
	{
		super(compressed ? new GZIPInputStream(stream) : stream);
	}

	public Tag readTag()
			throws IOException
	{
		final Tag tag;
		try
		{
			final Class<? extends Tag> clazz = NBTConstants.getTagClass(readByte());
			tag = clazz.newInstance();
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

		read(new byte[readUnsignedShort()]);
		tag.read(this);

		return tag;
	}
}
