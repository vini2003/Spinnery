package spinnery.entity;

import net.minecraft.block.entity.BlockEntity;
import spinnery.registry.EntityRegistry;

public class TestBlockEntity extends BlockEntity {
	public TestBlockEntity() {
		super(EntityRegistry.TEST_BLOCK_ENTITY);
	}
}
