package com.github.vini2003.spinnery.debug;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
	public ItemRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static <I extends Item> I register(ResourceLocation ID, I item) {
		return Registry.register(Registry.ITEM, ID, item);
	}
}