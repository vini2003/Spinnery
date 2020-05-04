package com.github.vini2003.spinnery.mixin;

import com.github.vini2003.spinnery.client.InGameHudScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.github.vini2003.spinnery.widget.WInterface;

/**
 * Injections into IngameGuiScreen to
 * allow for addition of Spinner widgets.
 */
@Mixin(IngameGui.class)
public class InGameHudMixin implements InGameHudScreen.Accessor {
	WInterface hudInterface = new WInterface();

	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	public void onInitialize(Minecraft client, CallbackInfo ci) {
		InGameHudScreen.onInitialize(getIngameGui());
	}

	@Inject(method = "renderGameOverlay", at = @At("RETURN"), remap = false)
	public void renderInterfaces(float tickDelta, CallbackInfo ci) {
		hudInterface.draw();
	}

	@Override
	public WInterface getInterface() {
		return hudInterface;
	}

	@Override
	public IngameGui getIngameGui() {
		return (IngameGui) (Object) this;
	}
}
