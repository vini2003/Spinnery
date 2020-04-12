package spinnery.widget.api;

import net.minecraft.util.Identifier;

/**
 * Generic interface representing an object that may have a theme.
 */
public interface WThemable {
	/**
	 * Retrieves the Theme Identifier of this object.
	 *
	 * @return The Theme Identifier of this object.
	 */
	Identifier getTheme();
}
