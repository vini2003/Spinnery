package com.github.vini2003.spinnery.debug;

import com.github.vini2003.spinnery.common.BaseInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TestBlock extends Block {
	public final BaseInventory testInventory = new BaseInventory(36);

	public TestBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockHitResult) {
		if (!world.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new INamedContainerProvider() {
				@Override
				public ITextComponent getDisplayName() {
					return new StringTextComponent("Debug");
				}

				@Nullable
				@Override
				public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
					return new TestContainer(windowId, playerInventory);
				}
			});
		}

		return ActionResultType.SUCCESS;
	}

	public IInventory getInventory() {
		return testInventory;
	}
}