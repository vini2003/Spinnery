package glib;

import glib.registry.BlockRegistry;
import glib.registry.ContainerRegistry;
import glib.registry.ItemRegistry;
import glib.registry.ScreenRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GlibMod implements ModInitializer {
	@Override
	public void onInitialize() {
		BlockRegistry.initialize();
		ItemRegistry.initialize();
		ScreenRegistry.initialize();
		ContainerRegistry.initialize();
	}
}
