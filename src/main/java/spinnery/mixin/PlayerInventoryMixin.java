package spinnery.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spinnery.common.BaseContainer;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

	@Shadow @Final public PlayerEntity player;

	@Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"))
	public void updateSpinneryContainer(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		PlayerInventory inventory = ((PlayerInventory) (Object) this);
		PlayerEntity player = inventory.player;

		if (player.container instanceof BaseContainer) {
			BaseContainer container = ((BaseContainer) player.container);
			for (int i : container.linkedInventories.keySet()) {
				container.linkedInventories.get(i).markDirty();
			}
			System.out.println("nani");
		}
	}

}
