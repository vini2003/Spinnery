package spinnery.registry;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.block.TestBlock;

public class BlockRegistry {
	public static final Block TEST_BLOCK = register(new Identifier("test_block"), new TestBlock(FabricBlockSettings.copy(Blocks.COBBLESTONE_WALL).build()), new Item.Settings().group(ItemGroup.MISC));

	public BlockRegistry() {
		// NO-OP
	}

	public static void initialize() {
		// NO-OP
	}

	public static <B extends Block> B register(Identifier ID, B block, Item.Settings settings) {
		Registry.register(Registry.BLOCK, ID, block);
		ItemRegistry.register(ID, new BlockItem(block, settings));

		return block;
	}
}
