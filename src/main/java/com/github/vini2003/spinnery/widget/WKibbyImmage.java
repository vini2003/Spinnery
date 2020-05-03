package com.github.vini2003.spinnery.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.Spinnery;

@OnlyIn(Dist.CLIENT)
public final class WKibbyImmage extends WStaticImage {
	public WKibbyImmage() {
		setTexture(new ResourceLocation(Spinnery.MOD_ID, "textures/kirby.png"));
	}
}
