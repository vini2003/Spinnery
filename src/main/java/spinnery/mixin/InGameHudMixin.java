package spinnery.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spinnery.client.screen.InGameHudScreen;
import spinnery.widget.WInterface;

/**
 * Injections into InGameHudScreen to
 * allow for addition of Spinner widgets.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin implements InGameHudScreen.Accessor {
	WInterface hudInterface = new WInterface();

	@Inject(method = "<init>", at = @At("RETURN"))
	public void onInitialize(MinecraftClient client, CallbackInfo ci) {
		InGameHudScreen.onInitialize(getInGameHud());
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void renderInterfaces(float tickDelta, CallbackInfo ci) {
		hudInterface.draw();
	}

	@Override
	public WInterface getInterface() {
		return hudInterface;
	}

	@Override
	public InGameHud getInGameHud() {
		return (InGameHud) (Object) this;
	}
}
