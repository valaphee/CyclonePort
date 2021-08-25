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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Default
 *
 * @author valaphee
 */
public abstract class AbstractNumberTag
		implements Tag
{
	@Override
	public AbstractNumberTag asIntegerTag()
	{
		return this;
	}

	@Override
	public AbstractNumberTag asDecimalTag()
	{
		return this;
	}

	public abstract byte byteValue();

	public abstract short shortValue();

	public abstract int intValue();

	public abstract long longValue();

	public abstract BigInteger bigIntegerValue();

	public abstract float floatValue();

	public abstract double doubleValue();

	public abstract BigDecimal bigDecimalValue();
}
