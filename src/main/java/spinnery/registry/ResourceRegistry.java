package spinnery.registry;

import com.google.gson.Gson;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.util.ResourceListener;
import spinnery.widget.WWidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ResourceRegistry {
	public static final ResourceListener RESOURCE_LISTENER = new ResourceListener();
	private static Map<String, ThemeRegistry> themes = new HashMap<>();

	ResourceRegistry() {
		// NO-OP
	}

	public static void initialize() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RESOURCE_LISTENER);
	}

	public static ThemeRegistry get(String themeID) {
		return themes.get(themeID);
	}

	// Close stream after use!
	public static void register(InputStream inputStream) {
		InputStreamReader reader = new InputStreamReader(inputStream);
		Map<String, Map<String, String>> data = new Gson().fromJson(reader, Map.class);
		String name = "";
		for (String key : data.keySet()) {
			if (key.equals("Identifier")) {
				name = data.get(key).get("name");
			} else {
				try {
					Class<? extends WWidget> widgetClass = WidgetRegistry.get(key);
					if (!Objects.isNull(widgetClass)) {
						try {
							WWidget.Theme theme = (WWidget.Theme) widgetClass.getMethod("of", Map.class).invoke(widgetClass, data.get(key));
							ThemeRegistry.register(name, widgetClass, theme);
						} catch (InvocationTargetException | IllegalAccessException exception) {
							Spinnery.logger.log(Level.ERROR, "[Spinnery] Could not invoke WWidget::of!");
						}
					}
				} catch (NoSuchMethodException exception) {
					Spinnery.logger.log(Level.ERROR, "[Spinnery] Could not find WWidget::of!");
				}
			}
		}
	}

	public static void reload(InputStream... inputStream) {
		themes.clear();
		for (InputStream stream : inputStream) {
			register(stream);
		}
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
				Spinnery.logger.log(Level.INFO, "[Spinnery] No custom themes found.");
			}
		} catch (IOException exception) {
			Spinnery.logger.log(Level.INFO, "[Spinnery] No custom themes found.");
		}
	}
}
