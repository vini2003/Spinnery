package spinnery.registry;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.entity.TestBlockEntity;

public class EntityRegistry {
	public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, new Identifier("test_block"), BlockEntityType.Builder.create(TestBlockEntity::new, BlockRegistry.TEST_BLOCK).build(null));
}
