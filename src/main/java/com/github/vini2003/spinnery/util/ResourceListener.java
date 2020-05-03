package com.github.vini2003.spinnery.util;

import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.registry.ResourceRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A basic implementation of
 * a resource listener for
 * Spinnery's resources/themes.
 */
@OnlyIn(Dist.CLIENT)
public class ResourceListener implements IResourceManagerReloadListener {
	private static final ResourceLocation ID = new ResourceLocation("spinnery", "reload_listener");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		ResourceRegistry.clear();
		ResourceRegistry.load((SimpleReloadableResourceManager) resourceManager);
	}
}
