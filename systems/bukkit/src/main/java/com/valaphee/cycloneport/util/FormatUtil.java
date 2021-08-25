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

import java.util.regex.Pattern;

/**
 * Default
 *
 * @author valaphee
 */
public final class FormatUtil
{
	public static final Pattern VANILLA_COLOR_PATTERN = Pattern.compile("\u00A7+[0-9A-Fa-f]");
	public static final Pattern VANILLA_FORMAT_PATTERN = Pattern.compile("\u00A7+[L-ORl-or]");
	public static final Pattern VANILLA_MAGIC_PATTERN = Pattern.compile("\u00A7+[Kk]");
	public static final Pattern REPLACE_COLOR_PATTERN = Pattern.compile("(?<!&)&([0-9a-fA-F])");
	public static final Pattern REPLACE_FORMAT_PATTERN = Pattern.compile("(?<!&)&([l-orL-OR])");
	public static final Pattern REPLACE_MAGIC_PATTERN = Pattern.compile("(?<!&)&([Kk])");
	public static final Pattern REPLACE_PATTERN = Pattern.compile("&&(?=[0-9a-fk-orA-FK-OR])");

	public static String strip(final String message, final Pattern pattern)
	{
		return pattern.matcher(message).replaceAll("");
	}

	public static String replace(final String message, final Pattern pattern)
	{
		return REPLACE_PATTERN.matcher(pattern.matcher(message).replaceAll("\u00A7$1")).replaceAll("&");
	}

	private FormatUtil()
	{}
}
