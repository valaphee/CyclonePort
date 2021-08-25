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

/**
 * Default
 *
 * @author valaphee
 */
public interface Tag
{
	default boolean isInteger()
	{
		return false;
	}

	default AbstractNumberTag asIntegerTag()
	{
		return null;
	}

	default boolean isDecimal()
	{
		return false;
	}

	default AbstractNumberTag asDecimalTag()
	{
		return null;
	}

	default boolean isList()
	{
		return false;
	}

	default AbstractListTag asListTag()
	{
		return null;
	}

	default boolean isCompound()
	{
		return false;
	}

	default AbstractCompoundTag asCompoundTag()
	{
		return null;
	}

	default boolean isArray()
	{
		return false;
	}

	default AbstractArrayTag asArrayTag()
	{
		return null;
	}

	void read(DataInputStream stream)
			throws IOException;

	void write(DataOutputStream stream)
			throws IOException;
}
