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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Default
 *
 * @author valaphee
 */
public class NBTOutputStream
		extends DataOutputStream
{
	public NBTOutputStream(final OutputStream stream, final boolean compressed)
			throws IOException
	{
		super(compressed ? new GZIPOutputStream(stream) : stream);
	}

	public void writeTag(final Tag tag)
			throws IOException
	{
		writeByte(NBTConstants.getTagId(tag.getClass()));
		writeShort(0);
		tag.write(this);
	}
}
