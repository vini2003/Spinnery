package spinnery.debug;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TestBlock extends Block {
	public final BasicInventory testInventory = new BasicInventory(36);

	public TestBlock(Block.Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
		if (! world_1.isClient) {
			ContainerProviderRegistry.INSTANCE.openContainer(ContainerRegistry.TEST_CONTAINER, playerEntity_1, (buffer) -> buffer.writeBlockPos(blockPos_1));
		}
		return ActionResult.SUCCESS;
	}

	public Inventory getInventory() {
		return testInventory;
	}
}
