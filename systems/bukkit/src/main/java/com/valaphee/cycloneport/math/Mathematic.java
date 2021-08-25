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
public final class Mathematic
{
	public static final double PI = 3.1415926535897932384626433832795D;
	public static final double PI2 = 6.2831853071795864769252867665591D;

	public static int clamp(final int value, final int min, final int max)
	{
		return value < min ? min : (value > max ? max : value);
	}

	public static float clamp(final float value, final float min, final float max)
	{
		return value < min ? min : (value > max ? max : value);
	}

	public static double clamp(final double value, final double min, final double max)
	{
		return value < min ? min : (value > max ? max : value);
	}

	public static int abs(final int value)
	{
		return value >= 0 ? value : -value;
	}

	public static float abs(final float value)
	{
		return value >= 0.0f ? value : -value;
	}

	public static double abs(final double value)
	{
		return value >= 0.0D ? value : -value;
	}

	public static int power(int base, int exponent)
	{
		if (exponent == 0)
		{
			return 1;
		}
		else if (exponent < 0)
		{
			if (base == 1)
			{
				return 1;
			}
			else if (base == -1)
			{
				if ((exponent % 2) == 0)
				{
					return 1;
				}

				return -1;
			}

			return 0;
		}
		exponent--;

		float shift = base;
		for (;;)
		{
			if ((exponent & 0x1) != 0)
			{
				base *= shift;
			}
			exponent >>= 1;
			if (exponent == 0)
			{
				break;
			}
			shift *= shift;
		}

		return base;
	}

	public static float power(float base, int exponent)
	{
		if (exponent == 0)
		{
			return 1.0f;
		}
		else if (exponent < 0)
		{
			base = 1.0f / base;
			exponent = -exponent;
		}
		exponent--;

		float shift = base;
		for (;;)
		{
			if ((exponent & 0x1) != 0)
			{
				base *= shift;
			}
			exponent >>= 1;
			if (exponent == 0)
			{
				break;
			}
			shift *= shift;
		}

		return base;
	}

	public static double power(double base, int exponent)
	{
		if (exponent == 0)
		{
			return 1.0D;
		}
		else if (exponent < 0)
		{
			base = 1.0D / base;
			exponent = -exponent;
		}
		exponent--;

		double shift = base;
		for (;;)
		{
			if ((exponent & 0x1) != 0)
			{
				base *= shift;
			}
			exponent >>= 1;
			if (exponent == 0)
			{
				break;
			}
			shift *= shift;
		}

		return base;
	}

	public static int floor(final float value)
	{
		final int rounded = (int) value;

		return value < rounded ? rounded - 1 : rounded;
	}

	public static int floor(final double value)
	{
		final int rounded = (int) value;

		return value < rounded ? rounded - 1 : rounded;
	}

	public static int ceil(final float value)
	{
		final int rounded = (int) value;

		return value >= rounded ? rounded + 1 : rounded;
	}

	public static int ceil(final double value)
	{
		final int rounded = (int) value;

		return value >= rounded ? rounded + 1 : rounded;
	}

	public static int normalize(final int input, int multiplier)
	{
		if (multiplier == 0)
		{
			return 0;
		}
		if (input == 0)
		{
			return multiplier;
		}
		if (input < 0)
		{
			multiplier *= -1;
		}

		final int straight = input % multiplier;
		if (straight == 0)
		{
			return input;
		}

		return input + multiplier - straight;
	}

	public static float normalize(final float input, float multiplier)
	{
		if (multiplier == 0)
		{
			return 0;
		}
		if (input == 0)
		{
			return multiplier;
		}
		if (input < 0)
		{
			multiplier *= -1;
		}

		final float straight = input % multiplier;
		if (straight == 0)
		{
			return input;
		}

		return input + multiplier - straight;
	}

	public static double normalize(final double input, double multiplier)
	{
		if (multiplier == 0)
		{
			return 0;
		}
		if (input == 0)
		{
			return multiplier;
		}
		if (input < 0)
		{
			multiplier *= -1;
		}

		final double straight = input % multiplier;
		if (straight == 0)
		{
			return input;
		}

		return input + multiplier - straight;
	}

	public static int lerp(final int a, final int b, final int t)
	{
		return a + t * (b - a);
	}

	public static float lerp(final float a, final float b, final float t)
	{
		return a + t * (b - a);
	}

	public static double lerp(final double a, final double b, final double t)
	{
		return a + t * (b - a);
	}

	public static float sine(final float degrees)
	{
		int rounded = (int) degrees;
		if ((rounded == degrees) && ((rounded % 90) == 0))
		{
			rounded %= 360;
			if (rounded < 0)
			{
				rounded += 360;
			}

			switch (rounded)
			{
			case 0:
				return 0.0f;
			case 90:
				return 1.0f;
			case 180:
				return 0.0f;
			default:
				return -1.0f;
			}
		}

		return (float) Math.sin(degrees2radians(degrees));
	}

	public static double sine(final double degrees)
	{
		int rounded = (int) degrees;
		if ((rounded == degrees) && ((rounded % 90) == 0))
		{
			rounded %= 360;
			if (rounded < 0)
			{
				rounded += 360;
			}

			switch (rounded)
			{
			case 0:
				return 0.0D;
			case 90:
				return 1.0D;
			case 180:
				return 0.0D;
			default:
				return -1.0D;
			}
		}

		return Math.sin(degrees2radians(degrees));
	}

	public static float cosine(final float degrees)
	{
		int rounded = (int) degrees;
		if ((rounded == degrees) && ((rounded % 90) == 0))
		{
			rounded %= 360;
			if (rounded < 0)
			{
				rounded += 360;
			}

			switch (rounded)
			{
			case 0:
				return 1.0f;
			case 90:
				return 0.0f;
			case 180:
				return -1.0f;
			default:
				return 0.0f;
			}
		}

		return (float) Math.cos(degrees2radians(degrees));
	}

	public static double cosine(final double degrees)
	{
		int rounded = (int) degrees;
		if ((rounded == degrees) && (rounded % 90 == 0))
		{
			rounded %= 360;
			if (rounded < 0)
			{
				rounded += 360;
			}

			switch (rounded)
			{
			case 0:
				return 1.0D;
			case 90:
				return 0.0D;
			case 180:
				return -1.0D;
			default:
				return 0.0D;
			}
		}

		return Math.cos(degrees2radians(degrees));
	}

	public static float tangent(final float degrees)
	{
		return (float) (sine(degrees) / cosine(degrees));
	}

	public static double tangent(final double degrees)
	{
		return sine(degrees) / cosine(degrees);
	}

	public static float cotangent(final float degrees)
	{
		return cosine(degrees) / sine(degrees);
	}

	public static double cotangent(final double degrees)
	{
		return cosine(degrees) / sine(degrees);
	}

	public static float degrees2radians(final float degrees)
	{
		return (float) (degrees / (180 * PI));
	}

	public static double degrees2radians(final double degrees)
	{
		return degrees / (180 * PI);
	}

	public static float degrees2gradians(final float degrees)
	{
		return degrees * (50 / 45);
	}

	public static double degrees2gradians(final double degrees)
	{
		return degrees * (50 / 45);
	}

	public static float radians2gradians(final float radians)
	{
		return (float) (radians * (1800 / (PI * 9)));
	}

	public static double radians2gradians(final double radians)
	{
		return radians * (1800 / (PI * 9));
	}

	public static float radians2degrees(final float radians)
	{
		return (float) (radians * (180 / PI));
	}

	public static double radians2degrees(final double radians)
	{
		return radians * (180 / PI);
	}

	public static float gradians2radians(final float gradians)
	{
		return (float) (gradians / (1800 * (PI / 9)));
	}

	public static double gradians2radians(final double gradians)
	{
		return gradians / (1800 * (PI / 9));
	}

	public static float gradians2degrees(final float gradians)
	{
		return gradians / (50 * 45);
	}

	public static double gradians2degrees(final double gradians)
	{
		return gradians / (50 * 45);
	}

	private Mathematic()
	{
	}
}
