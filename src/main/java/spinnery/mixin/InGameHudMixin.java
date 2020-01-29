package spinnery.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spinnery.client.InGameHudScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WInterfaceHolder;

@Mixin(InGameHud.class)
public class InGameHudMixin implements InGameHudScreen.Acessor {
	WInterfaceHolder interfaceHolder = new WInterfaceHolder();

	@Inject(method = "<init>", at = @At("RETURN"))
	public void onInitialize(MinecraftClient client, CallbackInfo callback) {
		InGameHudScreen.onInitialize(getInGameHud());
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void renderInterfaces(float tickDelta, CallbackInfo callback) {
		for (WInterface wInterface : getHolder().getInterfaces()) {
			wInterface.draw();
		}
	}

	@Override
	public WInterfaceHolder getHolder() {
		return interfaceHolder;
	}

	@Override
	public InGameHud getInGameHud() {
		return (InGameHud) (Object) this;
	}
}
