package spinnery.widget;

import net.minecraft.util.Identifier;
import spinnery.Spinnery;

public final class WKibbySprite extends WStaticImage {

	public WKibbySprite(WPosition position, WInterface linkedInterface) {
		super(position, WSize.of(32, 32), linkedInterface, new Identifier(Spinnery.MOD_ID, "textures/kirby.png"));
	}
}
