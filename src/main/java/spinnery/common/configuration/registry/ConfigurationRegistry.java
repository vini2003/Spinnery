package spinnery.common.configuration.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resource.ResourceManager;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.common.configuration.data.ConfigurationHolder;
import spinnery.common.configuration.data.ConfigurationOption;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationRegistry {
	private static final Map<String, BiMap<String, ConfigurationHolder<?>>> ENTRIES = new HashMap<>();

	private static final ArrayList<Class<?>> CLASSES = new ArrayList<>();

	public static void initialize() {
		// NO-OP
	}

	public static void register(Class<?> clazz) {
		try {
			CLASSES.add(clazz);

			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(ConfigurationOption.class) && field.getType() == ConfigurationHolder.class) {
					ConfigurationOption configuration = field.getAnnotation(ConfigurationOption.class);

					registerOption(configuration.name(), field.getName(), (ConfigurationHolder<?>) field.get(null));
				}
			}
		} catch (Exception exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Failed to parse class fields!");
			exception.printStackTrace();

			CLASSES.remove(clazz);
		}
	}

	private static void registerOption(String namespace, String path, ConfigurationHolder<?> holder) {
		ENTRIES.computeIfAbsent(namespace, (key) -> HashBiMap.create());

		ENTRIES.get(namespace).put(path, holder);

		holder.setNamespace(namespace);
		holder.setPath(path);
	}

	public static BiMap<String, ConfigurationHolder<?>> getOptions(String namespace) {
		return ENTRIES.get(namespace);
	}

	public static void load(ResourceManager resourceManager) {
		try {
			for (Class<?> clazz : CLASSES) {
				clazz.getMethod("load").invoke(null);
			}
		} catch (Exception exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Failed to load configuration data!");
			exception.printStackTrace();
		}

	}
}
