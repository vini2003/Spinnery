package spinnery.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spinnery.common.BaseContainer;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	@Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"))
	public void updateSpinneryContainer(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		PlayerInventory inventory = ((PlayerInventory) (Object) this);
		PlayerEntity player = inventory.player;

		if (player.container instanceof BaseContainer) {
			BaseContainer container = ((BaseContainer) player.container);
			container.onContentChanged(null);
		}
	}

}
