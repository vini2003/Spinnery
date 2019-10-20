package spinnery;

import spinnery.registry.BlockRegistry;
import spinnery.registry.ContainerRegistry;
import spinnery.registry.ItemRegistry;
import spinnery.registry.ScreenRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpinneryMod implements ModInitializer {
	@Override
	public void onInitialize() {
		BlockRegistry.initialize();
		ItemRegistry.initialize();
		ScreenRegistry.initialize();
		ContainerRegistry.initialize();
	}
}
