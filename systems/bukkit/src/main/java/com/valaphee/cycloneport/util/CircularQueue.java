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

package com.valaphee.cycloneport.util;

import java.util.LinkedList;

/**
 * Default
 *
 * @author valaphee
 * @param <E>
 */
@SuppressWarnings("serial")
public class CircularQueue<E>
		extends LinkedList<E>
{
	private final int limit;

	public CircularQueue(final int limit)
	{
		this.limit = limit;
	}

	@Override
	public boolean add(E element)
	{
		final boolean added = super.add(element);
		while (added && (size() > limit))
		{
			remove();
		}

		return added;
	}
}
