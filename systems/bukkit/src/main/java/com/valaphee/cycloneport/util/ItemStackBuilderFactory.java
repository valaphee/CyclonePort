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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.valaphee.cyclone.reflect.Accessors;
import com.valaphee.cyclone.reflect.FieldAccessor;
import com.valaphee.cycloneport.reflect.PackageType;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

/**
 * Default
 *
 * @author valaphee
 */
@SuppressWarnings("null")
public final class ItemStackBuilderFactory
{
	public static ItemStackBuilder builder()
	{
		return new ItemStackBuilder();
	}

	public static ItemStackBuilder editor(final ItemStack itemStack)
	{
		return new ItemStackBuilder(itemStack);
	}

	public static class ItemStackBuilder
	{
		protected final ItemStack itemStack;

		private ItemStackBuilder()
		{
			this(new ItemStack(Material.STONE));
		}

		private ItemStackBuilder(final ItemStack itemStack)
		{
			this.itemStack = itemStack;
		}

		public ItemStackBuilder setItemMeta(final ItemMeta itemMeta)
		{
			itemStack.setItemMeta(itemMeta);

			return this;
		}

		public ItemStackBuilder setMaterial(final Material material)
		{
			itemStack.setType(material);

			return this;
		}

		public ItemStackBuilder setAmount(final int amount)
		{
			itemStack.setAmount(amount);

			return this;
		}

		public ItemStackBuilder setData(final short data)
		{
			itemStack.setDurability(data);

			return this;
		}

		public ItemStackBuilder setData(final MaterialData data)
		{
			itemStack.setData(data);

			return this;
		}

		public ItemStackBuilder setEnchantments(final Map<Enchantment, Integer> enchantments)
		{
			itemStack.getEnchantments().keySet().forEach((enchantment) ->
			{
				itemStack.removeEnchantment(enchantment);
			});
			itemStack.addUnsafeEnchantments(enchantments);

			return this;
		}

		public ItemStackBuilder addEnchantment(final Enchantment enchantment, final int level)
		{
			itemStack.addUnsafeEnchantment(enchantment, level);

			return this;
		}

		public ItemStackBuilder setName(final String name)
		{
			final ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName(name);
			itemStack.setItemMeta(itemMeta);

			return this;
		}

		public ItemStackBuilder addBlankLore()
		{
			addLore(" ");

			return this;
		}

		public ItemStackBuilder addLore(final String... lore)
		{
			final ItemMeta itemMeta = itemStack.getItemMeta();
			List<String> original = itemMeta.getLore();
			if (original == null)
			{
				original = new ArrayList<>();
			}
			original.addAll(Arrays.asList(lore));
			itemMeta.setLore(original);
			itemStack.setItemMeta(itemMeta);

			return this;
		}

		public ItemStackBuilder addLore(final List<String> lore)
		{
			final ItemMeta itemMeta = itemStack.getItemMeta();
			List<String> original = itemMeta.getLore();
			if (original == null)
			{
				original = new ArrayList<>();
			}
			original.addAll(lore);
			itemMeta.setLore(original);
			itemStack.setItemMeta(itemMeta);

			return this;
		}

		public ItemStackBuilder setLore(final String... lore)
		{
			final ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setLore(Arrays.asList(lore));
			itemStack.setItemMeta(itemMeta);

			return this;
		}

		public ItemStackBuilder setLore(final List<String> lore)
		{
			final ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setLore(lore);
			itemStack.setItemMeta(itemMeta);

			return this;
		}

		public ItemStack build()
		{
			return itemStack.clone();
		}
	}

	public static BookBuilder bookBuilder()
	{
		return new BookBuilder();
	}

	public static final class BookBuilder
			extends ItemStackBuilder
	{
		private BookBuilder()
		{
			super(new ItemStack(Material.WRITTEN_BOOK));

			setTitle("\u00A7lWritten Book");
			setAuthor("Valaphee");
		}

		public BookBuilder setTitle(final String title)
		{
			final BookMeta bookMeta = ((BookMeta) itemStack.getItemMeta());
			bookMeta.setTitle(title);
			itemStack.setItemMeta(bookMeta);

			return this;
		}

		public BookBuilder setAuthor(final String author)
		{
			final BookMeta bookMeta = ((BookMeta) itemStack.getItemMeta());
			bookMeta.setAuthor(author);
			itemStack.setItemMeta(bookMeta);

			return this;
		}

		public BookBuilder addPage(final String... lines)
		{
			final BookMeta bookMeta = ((BookMeta) itemStack.getItemMeta());
			final StringBuilder page = new StringBuilder();
			for (final String line : lines)
			{
				page.append(line).append('\n');
			}
			bookMeta.addPage(page.toString());
			itemStack.setItemMeta(bookMeta);

			return this;
		}

		@Override
		public ItemStack build()
		{
			return super.build();
		}
	}

	public static SkullBuilder skullBuilder()
	{
		return new SkullBuilder();
	}

	public static final class SkullBuilder
			extends ItemStackBuilder
	{
		private static FieldAccessor CRAFT_SKULL_META_PROFILE;

		private SkullBuilder()
		{
			super(new ItemStack(Material.SKELETON_SKULL, 1, (short) 3));

			setOwner("MHF_Exclamation");
		}

		public SkullBuilder setOwner(final String name)
		{
			final SkullMeta skullMeta = ((SkullMeta) itemStack.getItemMeta());
			skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
			itemStack.setItemMeta(skullMeta);

			return this;
		}

		public SkullBuilder setOwner(final GameProfile profile)
		{
			final SkullMeta skullMeta = ((SkullMeta) itemStack.getItemMeta());
			CRAFT_SKULL_META_PROFILE.set(skullMeta, profile);
			itemStack.setItemMeta(skullMeta);

			return this;
		}

		public SkullBuilder setTexture(final String url)
		{
			final SkullMeta skullMeta = ((SkullMeta) itemStack.getItemMeta());
			final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
			final PropertyMap properties = profile.getProperties();
			properties.put("textures", new Property("textures", new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[]
			{
				url
			}).getBytes()))));
			setOwner(profile);
			itemStack.setItemMeta(skullMeta);

			return this;
		}

		@Override
		public ItemStack build()
		{
			return super.build();
		}

		static
		{
			try
			{
				final Class<?> craftMetaSkull = PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaSkull");

				CRAFT_SKULL_META_PROFILE = Accessors.getFieldAccessorOrNull(craftMetaSkull, "profile");
			}
			catch (ClassNotFoundException ignore)
			{}
		}
	}

	private ItemStackBuilderFactory()
	{}
}
