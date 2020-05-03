package com.github.vini2003.spinnery.debug;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ContainerRegistry {
	public ContainerRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static void register(RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(TestContainer.TYPE);
	}
}