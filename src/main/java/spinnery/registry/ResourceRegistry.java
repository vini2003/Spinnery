package spinnery.registry;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import io.github.cottonmc.jankson.JanksonFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
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
import java.util.Objects;

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

	public static void loadCustom() {
		File file = new File("./resources/spinnery/themes");

		try {
			if (!file.exists()) {
				if (!file.mkdirs() || !file.createNewFile()) {
					throw new IOException("Could not create file(s): ./resources/spinnery/themes");
				}
			}

			try {
				Arrays.asList(Objects.requireNonNull(new File("./resources/spinnery/themes").listFiles())).forEach((themeFile) -> {
					try {
						if (themeFile.getName().endsWith(".theme.json5")) register(new FileInputStream(themeFile));
					} catch (FileNotFoundException impossibleException) {
						impossibleException.printStackTrace();
					}

				});
			} catch (NullPointerException exception) {
				Spinnery.LOGGER.log(Level.INFO, "[Spinnery] No custom themes found.");
			}
		} catch (IOException exception) {
			Spinnery.LOGGER.log(Level.INFO, "[Spinnery] No custom themes found.");
		}
	}
}
