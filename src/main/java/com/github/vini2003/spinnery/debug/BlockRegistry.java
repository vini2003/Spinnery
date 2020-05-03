package com.github.vini2003.spinnery.debug;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockRegistry {
	//public static final Block TEST_BLOCK = register(new ResourceLocation("test_block"), new TestBlock(Block.Properties.create(Blocks.COBBLESTONE_WALL.getDefaultState().getMaterial())), new Item.Properties().group(ItemGroup.MISC));

	public BlockRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static void register(RegistryEvent.Register<Block> blockRegister) {
		//blockRegister.getRegistry().register(TEST_BLOCK);
	}

	public static <B extends Block> B register(ResourceLocation ID, B block, Item.Properties properties) {
		Registry.register(Registry.BLOCK, ID, block);
		ItemRegistry.register(ID, new BlockItem(block, properties));

		return block;
	}
}