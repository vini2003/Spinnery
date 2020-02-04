package spinnery.registry;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import io.github.cottonmc.jankson.JanksonFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.util.ResourceListener;
import spinnery.widget.api.WTheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class ResourceRegistry {
	public static final ResourceListener RESOURCE_LISTENER = new ResourceListener();

	public static void initialize() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RESOURCE_LISTENER);
	}

	public static void clear() {
		ThemeRegistry.clear();
	}

	public static void register(InputStream inputStream) {
		try {
			JsonObject themeDef = JanksonFactory.createJankson().load(inputStream);
			WTheme theme = WTheme.of(themeDef);
			ThemeRegistry.register(theme);
		} catch (IOException e) {
			Spinnery.LOGGER.log(Level.ERROR, "Could not read theme file", e);
		} catch (SyntaxError syntaxError) {
			Spinnery.LOGGER.log(Level.ERROR, "Syntax error in theme file", syntaxError);
		}
	}

	public static void load(ResourceManager resourceManager) {
		Collection<Identifier> themeFiles = resourceManager.findResources("spinnery",
				(string) -> string.endsWith(".theme.json5"));

		for (Identifier id : themeFiles) {
			try {
				register(resourceManager.getResource(id).getInputStream());
			} catch (IOException e) {
				Spinnery.LOGGER.warn("[Spinnery] Failed to load theme {}.", id);
			}
		}
	}
}
