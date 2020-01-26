package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;

@Environment(EnvType.CLIENT)
public final class WKibbySprite extends WStaticImage {

	public WKibbySprite(WPosition position, WInterface linkedInterface) {
		super(position, WSize.of(32, 32), linkedInterface, new Identifier(Spinnery.MOD_ID, "textures/kirby.png"));
	}
}
