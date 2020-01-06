package spinnery.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.BlockView;
import spinnery.entity.TestBlockEntity;
import spinnery.registry.ContainerRegistry;
import spinnery.registry.ScreenRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class TestBlock extends Block implements BlockEntityProvider {
	public TestBlock(Block.Settings settings) {
		super(settings);
	}

	public final BasicInventory testInventory = new BasicInventory(36);

	@Override
	public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
		if (!world_1.isClient) {
			ContainerProviderRegistry.INSTANCE.openContainer(ContainerRegistry.TEST_CONTAINER, playerEntity_1, (buffer) -> buffer.writeBlockPos(blockPos_1));
			return true;
		} else {
			return false;
		}
	}

	public Inventory getInventory() {
		System.out.println(testInventory == null);
		return testInventory;
	}

	@Override
	public TestBlockEntity createBlockEntity(BlockView var1) {
		return new TestBlockEntity();
	}
}
