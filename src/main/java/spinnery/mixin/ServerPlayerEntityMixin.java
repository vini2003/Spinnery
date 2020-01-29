package spinnery.mixin;

import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.client.network.packet.ContainerSlotUpdateS2CPacket;
import net.minecraft.container.Container;
import net.minecraft.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spinnery.common.BaseContainer;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

	@Inject(method = "Lnet/minecraft/server/network/ServerPlayerEntity;onContainerSlotUpdate(Lnet/minecraft/container/Container;ILnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	public void onContainerSlotUpdate(Container container, int slotId, ItemStack itemStack, CallbackInfo callback) {
		if (container instanceof BaseContainer) {
			((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(new ContainerSlotUpdateS2CPacket(container.syncId, slotId, itemStack));
			callback.cancel();
		}
	}
}
