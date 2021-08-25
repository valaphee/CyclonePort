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

import java.util.EnumMap;
import java.util.Map;

/**
 * Default
 *
 * @author valaphee
 */
public enum Direction
{
	BOTTOM(true, false, true),
	TOP(true, false, true),
	FRONT(true, true, false),
	BACK(true, true, false),
	LEFT(false, true, true),
	RIGHT(false, true, true);
	private static final Map<Direction, Direction> reverse = new EnumMap<>(Direction.class);
	private static final Map<Direction, Direction> clockwiseYaw = new EnumMap<>(Direction.class);
	private static final Map<Direction, Direction> counterclockwiseYaw = new EnumMap<>(Direction.class);
	private static final Map<Direction, Direction> clockwisePitch = new EnumMap<>(Direction.class);
	private static final Map<Direction, Direction> counterclockwisePitch = new EnumMap<>(Direction.class);
	private static final Map<Direction, Direction> clockwiseRoll = new EnumMap<>(Direction.class);
	private static final Map<Direction, Direction> counterclockwiseRoll = new EnumMap<>(Direction.class);
	private final boolean pitch, yaw, roll;

	private Direction(final boolean pitch, final boolean yaw, final boolean roll)
	{
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	public Direction reverse()
	{
		return reverse.get(this);
	}

	public Direction rotateYaw(int steps)
	{
		if (!yaw)
		{
			return this;
		}
		if (steps < 0)
		{
			steps = -steps + 2;
		}
		steps = steps % 4;

		switch (steps)
		{
		case 1:
			return clockwiseYaw.get(this);
		case 2:
			return reverse.get(this);
		case 3:
			return counterclockwiseYaw.get(this);
		default:
			return this;
		}
	}

	public Direction rotatePitch(int steps)
	{
		if (!pitch)
		{
			return this;
		}
		if (steps < 0)
		{
			steps = -steps + 2;
		}
		steps = steps % 4;

		switch (steps)
		{
		case 1:
			return clockwisePitch.get(this);
		case 2:
			return reverse.get(this);
		case 3:
			return counterclockwisePitch.get(this);
		default:
			return this;
		}
	}

	public Direction rotateRoll(int steps)
	{
		if (!roll)
		{
			return this;
		}
		if (steps < 0)
		{
			steps = -steps + 2;
		}
		steps = steps % 4;

		switch (steps)
		{
		case 1:
			return clockwiseRoll.get(this);
		case 2:
			return reverse.get(this);
		case 3:
			return counterclockwiseRoll.get(this);
		default:
			return this;
		}
	}

	static
	{
		reverse.put(RIGHT, LEFT);
		clockwiseYaw.put(RIGHT, FRONT);
		counterclockwiseYaw.put(RIGHT, BACK);
		clockwiseYaw.put(BACK, RIGHT);
		counterclockwiseYaw.put(BACK, LEFT);
		clockwiseYaw.put(LEFT, BACK);
		counterclockwiseYaw.put(LEFT, FRONT);
		clockwiseYaw.put(FRONT, LEFT);
		counterclockwiseYaw.put(FRONT, RIGHT);
		reverse.put(LEFT, RIGHT);

		reverse.put(FRONT, BACK);
		clockwisePitch.put(FRONT, TOP);
		counterclockwisePitch.put(FRONT, BOTTOM);
		clockwisePitch.put(BOTTOM, FRONT);
		counterclockwisePitch.put(BOTTOM, BACK);
		clockwisePitch.put(BACK, BOTTOM);
		counterclockwisePitch.put(BACK, TOP);
		clockwisePitch.put(TOP, BACK);
		counterclockwisePitch.put(TOP, FRONT);
		reverse.put(BACK, FRONT);

		reverse.put(TOP, BOTTOM);
		clockwiseRoll.put(TOP, LEFT);
		counterclockwiseRoll.put(TOP, RIGHT);
		clockwiseRoll.put(LEFT, BOTTOM);
		counterclockwiseRoll.put(LEFT, TOP);
		clockwiseRoll.put(BOTTOM, RIGHT);
		counterclockwiseRoll.put(BOTTOM, LEFT);
		clockwiseRoll.put(RIGHT, TOP);
		counterclockwiseRoll.put(RIGHT, BOTTOM);
		reverse.put(BOTTOM, TOP);
	}
}
