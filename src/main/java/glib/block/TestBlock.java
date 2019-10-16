package glib.block;

import glib.registry.ContainerRegistry;
import glib.registry.ScreenRegistry;
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

public class TestBlock extends Block implements InventoryProvider {
	public TestBlock(Block.Settings settings) {
		super(settings);
	}

	public BasicInventory testInventory = new BasicInventory(1);

	@Override
	public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
		if (!world_1.isClient) {
			ContainerProviderRegistry.INSTANCE.openContainer(ContainerRegistry.TEST_CONTAINER, playerEntity_1, (buffer)-> buffer.writeBlockPos(blockPos_1));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
		return (SidedInventory) testInventory;
	}
}
