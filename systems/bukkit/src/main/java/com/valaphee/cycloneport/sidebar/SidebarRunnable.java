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

package com.valaphee.cycloneport.sidebar;

import com.valaphee.cyclone.Cyclone;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Default
 *
 * @author valaphee
 */
public final class SidebarRunnable
		extends BukkitRunnable
{
	private final SidebarModule module;
	private final Player player;
	private final Scoreboard sidebar;
	private final String title;
	private StringBuilder animatedTitle = new StringBuilder();
	private Objective sidebarObjective;
	private final List<String> sidebarFields = new ArrayList<>();
	private int frameSection;
	private int frame;

	public SidebarRunnable(final SidebarModule module, final Player player)
	{
		this.module = module;
		this.player = player;

		sidebar = module.getServer().getScoreboardManager().getNewScoreboard();
		title = "DeinServer";
		frame();

		sidebarObjective = sidebar.getObjective("sidebar");
		if (sidebarObjective == null)
		{
			sidebarObjective = sidebar.registerNewObjective("sidebar", "dummy");
		}
		sidebarObjective.setDisplayName(animatedTitle.toString());
		sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

		int i = 0;
		final List<String> fields = new ArrayList<>();
		for (final Map.Entry<String, SidebarObserver> observer : this.module.getObservers().entrySet())
		{
			final String value = observer.getValue().value(this.player);
			if (value != null)
			{
				fields.add("\u00A7" + i++);
				fields.add("\u00A77\u25A5 \u00A73\u00A7l" + observer.getKey());
				fields.add("      " + observer.getValue().value(this.player));
			}
		}

		int j;
		for (i = fields.size() - 1, j = 0; i >= 0; --i, ++j)
		{
			final String field = fields.get(i);
			sidebarFields.add(j, field);
			sidebarObjective.getScore(field).setScore(j);
		}

		player.setScoreboard(sidebar);
	}

	private boolean frame()
	{
		if ((frameSection == 0) && (frame >= 10))
		{
			frameSection = 1;
			frame = 0;
		}
		else if ((frameSection == 1) && (frame >= title.length()))
		{
			frameSection = 2;
			frame = 0;
		}
		else if ((frameSection == 2) && (frame >= 5))
		{
			frameSection = 0;
			frame = 0;
		}
		frame++;

		if ((frameSection == 0) && (frame == 1))
		{
			animatedTitle = new StringBuilder();
			animatedTitle.append("\u00A77\u00BB ");
			animatedTitle.append(ChatColor.AQUA).append(title);
			animatedTitle.append("\u00A77 \u00AB");
		}
		else if (frameSection == 1)
		{
			int position = frame - 1;
			if (position == 0)
			{
				position = 1;
			}

			animatedTitle = new StringBuilder();
			animatedTitle.append("\u00A77\u00BB ");
			animatedTitle.append(ChatColor.GREEN).append(title.substring(0, position - 1));
			animatedTitle.append(ChatColor.DARK_GREEN).append(title.substring(position - 1, position));
			animatedTitle.append(ChatColor.AQUA).append(title.substring(position, title.length()));
			animatedTitle.append("\u00A77 \u00AB");
		}
		else if (frameSection == 2)
		{
			animatedTitle = new StringBuilder();
			animatedTitle.append("\u00A77\u00BB ");
			if ((frame % 2) == 0)
			{
				animatedTitle.append(ChatColor.AQUA).append(title);
			}
			else
			{
				animatedTitle.append(ChatColor.GREEN).append(title);
			}
			animatedTitle.append("\u00A77 \u00AB");
		}

		return (frame == 1) && (frameSection == 0);
	}

	@Override
	public void run()
	{
		if (!player.isOnline())
		{
			sidebarObjective.unregister();
			cancel();

			return;
		}

		if (Cyclone.getInstance().getStatusManager().isMaintenance())
		{
			sidebarObjective.setDisplayName(((frame % 2) == 0) ? "\u00A7cWARTUNGEN" : "\u00A76WARTUNGEN");
		}
		else
		{
			sidebarObjective.setDisplayName(animatedTitle.toString());
		}
		if (frame())
		{
			int i = 0;
			final List<String> fields = new ArrayList<>();
			for (final Map.Entry<String, SidebarObserver> observer : module.getObservers().entrySet())
			{
				final String value = observer.getValue().value(this.player);
				if (value != null)
				{
					fields.add("\u00A7" + i++);
					fields.add("\u00A77\u25A5 \u00A73\u00A7l" + observer.getKey());
					fields.add("      " + observer.getValue().value(this.player));
				}
			}

			int j;
			for (i = fields.size() - 1, j = 0; i >= 0; i--, j++)
			{
				final String newField = fields.get(i);
				if (j < sidebarFields.size())
				{
					final String field = sidebarFields.get(j);
					if (!field.equals(newField))
					{
						sidebarFields.set(j, newField);
						sidebar.resetScores(field);
						sidebarObjective.getScore(newField).setScore(j);
					}
				}
				else
				{
					sidebarFields.add(newField);
					sidebarObjective.getScore(newField).setScore(j);
				}
			}
			for (; j < sidebarFields.size(); j++)
			{
				sidebar.resetScores(sidebarFields.remove(j));
			}
		}
	}
}
