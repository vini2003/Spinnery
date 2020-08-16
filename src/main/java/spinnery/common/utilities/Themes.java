package spinnery.common.utilities;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.widget.api.Style;
import spinnery.widget.api.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Environment(EnvType.CLIENT)
public class Themes {
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
		Theme theme = themes.get(themeId);
		if (theme == null) theme = defaultTheme;
		return theme.getStyle(widgetId);
	}

	public static class Resources {
		public static final spinnery.common.utilities.Resources RESOURCE_LISTENER = new spinnery.common.utilities.Resources();

		public static void initialize() {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RESOURCE_LISTENER);
		}

		public static void clear() {
			Themes.clear();
		}

		public static void load(ResourceManager resourceManager) {
			Collection<Identifier> themeFiles = resourceManager.findResources("theme", (string) -> string.endsWith(".theme.json5"));

			for (Identifier id : themeFiles) {
				try {
					Identifier themeId = new Identifier(id.getNamespace(),
							id.getPath().replaceFirst("theme/", "").replaceFirst("\\.theme\\.json5", ""));
					register(themeId, resourceManager.getResource(id).getInputStream());
				} catch (IOException e) {
					Spinnery.LOGGER.warn("[Spinnery] Failed to load theme {}.", id);
				}
			}
		}

		public static void register(Identifier id, InputStream inputStream) {
			try {
				JsonObject themeDef = Jankson.builder().build().load(inputStream);
				Theme theme = Theme.of(id, themeDef);
				Themes.register(theme);
			} catch (IOException e) {
				Spinnery.LOGGER.log(Level.ERROR, "Could not read theme file", e);
			} catch (SyntaxError syntaxError) {
				Spinnery.LOGGER.log(Level.ERROR, "Syntax error in theme file", syntaxError);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					Spinnery.LOGGER.log(Level.ERROR, "Could not close input stream", e);
				}
			}
		}
	}
}
