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

package com.valaphee.cycloneport.economy;

import com.google.common.util.concurrent.AtomicDouble;
import com.valaphee.cyclone.Cyclone;
import com.valaphee.cyclone.account.AccountManager;
import com.valaphee.cyclone.account.User;
import com.valaphee.cyclone.account.event.UserLoadEvent;
import com.valaphee.cyclone.account.event.UserUnloadEvent;
import com.valaphee.cycloneport.Module;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Default
 *
 * @author valaphee
 */
public final class EconomyModule
		extends Module
{
	private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));
	private final Map<Long, AtomicDouble> currencies = new HashMap<>();
	private AccountManager accountManager;

	public EconomyModule(final JavaPlugin plugin)
	{
		super("Economy", plugin);
	}

	@Override
	public void startup()
	{
		accountManager = Cyclone.getInstance().getAccountManager();
	}

	public double getCurrency(final UUID player)
	{
		return currencies.get(accountManager.findUserId(player)).get();
	}

	public double addCurrency(final UUID player, final double amount)
	{
		return currencies.get(accountManager.findUserId(player)).addAndGet(amount);
	}

	public double subtractCurrency(final UUID player, final double amount)
	{
		final AtomicDouble currency = currencies.get(accountManager.findUserId(player));
		currency.set(currency.get() - amount);

		return currency.get();
	}

	@EventHandler
	public void on(final UserLoadEvent event)
	{
		final User user = event.getUser();
		final Double currency = user.getVariableAsDouble("currency");
		currencies.put(user.getId(), new AtomicDouble(currency != null ? currency : 0.0D));
	}

	@EventHandler
	public void on(final UserUnloadEvent event)
	{
		final User user = event.getUser();
		if (currencies.containsKey(user.getId()))
		{
			user.addVariable("currency", currencies.get(user.getId()).toString());
		}
	}

	public static String formatCurrency(final double value)
	{
		CURRENCY_FORMAT.setRoundingMode(RoundingMode.FLOOR);
		final String string = CURRENCY_FORMAT.format(value);
		if (string.endsWith(".00"))
		{
			return string.substring(0, string.length() - 3);
		}

		return string;
	}

	public static String displayCurrency(final double value)
	{
		return formatCurrency(value) + " Rubinen";
	}
}
