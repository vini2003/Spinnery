package spinnery.registry;

import com.google.gson.Gson;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.SpinneryMod;
import spinnery.theme.Theme;
import spinnery.util.ResourceListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ResourceRegistry {
	private static Map<String, Theme> themes = new HashMap<>();
	public static final ResourceListener RESOURCE_LISTENER = new ResourceListener();

	ResourceRegistry() {
		// NO-OP
	}

	public static void initialize() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RESOURCE_LISTENER);
	}

	public static Theme get(String themeID) {
		return themes.get(themeID);
	}

	public static void register(InputStream inputStream) {
		Theme theme = new Gson().fromJson(new InputStreamReader(inputStream), Theme.class);
		themes.put(theme.getID(), theme);
	}

	public static void reload(InputStream inputStream) {
		themes.clear();
		register(inputStream);
		load();
	}

	public static void load() {
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
						register(new FileInputStream(themeFile));
					} catch (FileNotFoundException impossibleException) {
						impossibleException.printStackTrace();
					}

				});
			} catch (NullPointerException exception) {
				SpinneryMod.logger.log(Level.INFO, "[Spinnery] No custom themes found.");
			}
		} catch (IOException exception) {
			SpinneryMod.logger.log(Level.INFO, "[Spinnery] No custom themes found.");
		}
	}
}
