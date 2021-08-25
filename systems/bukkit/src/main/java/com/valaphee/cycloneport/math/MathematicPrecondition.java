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

package com.valaphee.cycloneport.math;

/**
 * Default
 *
 * @author valaphee
 */
public final class MathematicPrecondition
{
	public static int assertPositive(final String message, final int value)
	{
		if (value <= 0)
		{
			throw new AssertionError(String.format("%s (%d) must be positive.", message, Integer.valueOf(value)));
		}

		return value;
	}

	public static int assertNegative(final String message, final int value)
	{
		if (value >= 0)
		{
			throw new AssertionError(String.format("%s (%d) must be negative.", message, Integer.valueOf(value)));
		}

		return value;
	}

	public static int assertNonPositive(final String message, final int value)
	{
		if (value > 0)
		{
			throw new AssertionError(String.format("%s (%d) must be negative or equal zero.", message, Integer.valueOf(value)));
		}

		return value;
	}

	public static int assertNonNegative(final String message, final int value)
	{
		if (value < 0)
		{
			throw new AssertionError(String.format("%s (%d) must be greater or equal 0", message, Integer.valueOf(value)));
		}

		return value;
	}

	public static int assertBetween(final String message, final int value, final int minimum, final int maximum)
	{
		if ((value > maximum) || (value < minimum))
		{
			throw new AssertionError(String.format("%s (%d) must be greater %d and lesser %d", message, Integer.valueOf(value), Integer.valueOf(minimum), Integer.valueOf(maximum)));
		}

		return value;
	}
}
