package spinnery.debug;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestBlock extends Block {
	public final BasicInventory testInventory = new BasicInventory(36);

	public TestBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient) {
			ContainerProviderRegistry.INSTANCE.openContainer(ContainerRegistry.TEST_CONTAINER, playerEntity, (buffer) -> {
			});
		} else {

		}
		if (world.isClient) {
			//	MinecraftClient.getInstance().openScreen(new TestScreen());
		}

		return ActionResult.SUCCESS;
	}

	public Inventory getInventory() {
		return testInventory;
	}
}