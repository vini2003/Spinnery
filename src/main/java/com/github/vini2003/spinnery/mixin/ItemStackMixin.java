package com.github.vini2003.spinnery.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract void setCount(int count);

	@Shadow
	private int count;

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/nbt/CompoundNBT;)V")
	void onDeserialization(CompoundNBT tag, CallbackInfo callbackInformation) {
		if (tag.contains("countInteger")) {
			setCount(tag.getInt("countInteger"));
		}
	}

	@Inject(at = @At("HEAD"), method = "write(Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/nbt/CompoundNBT;")
	void onSerialization(CompoundNBT tag, CallbackInfoReturnable<CompoundNBT> callbackInformationReturnable) {
		if (this.count > Byte.MAX_VALUE) {
			tag.putInt("countInteger", this.count);
		}
	}
}
