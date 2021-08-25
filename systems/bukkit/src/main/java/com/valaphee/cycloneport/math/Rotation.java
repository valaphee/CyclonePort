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
public enum Rotation
{
	NONE(0),
	CLOCKWISE_90(0.5D * Mathematic.PI),
	CLOCKWISE_180(Mathematic.PI),
	CLOCKWISE_270(-0.5D * Mathematic.PI);
	private final double radians;

	private Rotation(final double radians)
	{
		this.radians = radians;
	}

	public double getDegrees()
	{
		return Mathematic.radians2degrees(radians);
	}

	public double getRadians()
	{
		return radians;
	}

	public double getGradians()
	{
		return Mathematic.radians2gradians(radians);
	}
}
