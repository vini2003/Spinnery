package com.github.vini2003.spinnery;

import com.github.vini2003.spinnery.debug.*;
import com.github.vini2003.spinnery.registry.ResourceRegistry;
import com.github.vini2003.spinnery.registry.ThemeRegistry;
import com.github.vini2003.spinnery.registry.WidgetRegistry;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
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

		eventBus.addGenericListener(Block.class, this::registerBlocks);
		eventBus.addGenericListener(ContainerType.class, this::registerContainers);
		eventBus.addListener(this::registerResources);
		eventBus.addListener(this::initializeCommon);
		eventBus.addListener(this::registerScreens);
		eventBus.addListener(this::registerWidgets);
	}

	public void initializeCommon(FMLCommonSetupEvent event) {
		NetworkRegistry.initialize();
		BlockRegistry.initialize();
		ContainerRegistry.initialize();
	}

	public void registerBlocks(RegistryEvent.Register<Block> blockRegister) {
		BlockRegistry.register(blockRegister);
	}

	public void registerContainers(RegistryEvent.Register<ContainerType<?>> containerRegister) {
		ContainerRegistry.register(containerRegister);
	}

	public void registerResources(FMLClientSetupEvent resourceRegister) {
		ResourceRegistry.register(resourceRegister);
		ResourceRegistry.register((FMLClientSetupEvent) resourceRegister.getMinecraftSupplier().get().getResourceManager());
	}

	public void registerScreens(FMLClientSetupEvent screenRegister) {
		ScreenRegistry.register(screenRegister);
	}

	public void registerWidgets(FMLClientSetupEvent widgetRegister) {
		WidgetRegistry.initialize();
	}
}
