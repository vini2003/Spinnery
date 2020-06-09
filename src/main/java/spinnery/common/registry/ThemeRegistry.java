package spinnery.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.client.integration.SpinneryConfigurationScreen;
import spinnery.widget.api.Style;
import spinnery.widget.api.Theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Registers all the theme-related
 * assortments Spinnery makes use of,
 * implementing our parser and
 * Style manipulation.
 */
@Environment(EnvType.CLIENT)
public class ThemeRegistry {
	public static final Identifier DEFAULT_THEME = new Identifier("spinnery", "default");
	private static final BiMap<Identifier, Theme> themes = HashBiMap.create();
	private static Theme defaultTheme;

	public static void clear() {
		themes.clear();
	}

	public static void register(Theme theme) {
		if (theme == null) return;
		if (theme.getId().equals(DEFAULT_THEME)) {
			defaultTheme = theme;
		} else {
			themes.put(theme.getId(), theme);
		}
	}

	public static boolean contains(Identifier theme) {
		return themes.containsKey(theme);
	}

	public static Style getStyle(Identifier themeId, Identifier widgetId) {
		Identifier preferredTheme = new Identifier(SpinneryConfigurationScreen.preferredTheme.getValue());
		Theme theme = themes.get(themeId);
		if (theme == null) theme = (contains(preferredTheme) ? themes.get(preferredTheme) : defaultTheme);
		return theme.getStyle(widgetId);
	}
}
