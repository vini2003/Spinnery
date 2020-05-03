package com.github.vini2003.spinnery;

import com.github.vini2003.spinnery.registry.ResourceRegistry;
import com.github.vini2003.spinnery.registry.WidgetRegistry;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.vini2003.spinnery.registry.NetworkRegistry;

@Mod("spinnery")
public class Spinnery {
	public static final String LOG_ID = "Spinnery";
	public static final String MOD_ID = LOG_ID.toLowerCase();
	public static Logger LOGGER = LogManager.getLogger("Spinnery");

	public Spinnery() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::registerResources);
		eventBus.addListener(this::initializeCommon);
		eventBus.addListener(this::registerWidgets);
	}

	public void initializeCommon(FMLCommonSetupEvent event) {
		NetworkRegistry.initialize();
	}

	public void registerResources(FMLClientSetupEvent resourceRegister) {
		ResourceRegistry.register(resourceRegister);
		ResourceRegistry.load((SimpleReloadableResourceManager) resourceRegister.getMinecraftSupplier().get().getResourceManager());
	}

	public void registerWidgets(FMLClientSetupEvent widgetRegister) {
		WidgetRegistry.initialize();
	}
}
