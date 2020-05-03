package com.github.vini2003.spinnery.widget.api;

import net.minecraft.util.ResourceLocation;

/**
 * Generic interface representing an object that may have a theme.
 */
public interface WThemable {
	/**
	 * Retrieves the Theme ResourceLocation of this object.
	 *
	 * @return The Theme ResourceLocation of this object.
	 */
	ResourceLocation getTheme();
}
