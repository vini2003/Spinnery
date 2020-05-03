package com.github.vini2003.spinnery.debug;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ScreenRegistry {
	public ScreenRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static void register(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(TestContainer.TYPE, TestContainerScreen::new);
	}
}