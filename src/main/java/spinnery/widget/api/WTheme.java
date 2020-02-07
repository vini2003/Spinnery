package spinnery.widget.api;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.google.common.collect.ImmutableMap;
import io.github.cottonmc.jankson.JanksonOps;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;

import java.util.HashMap;
import java.util.Map;

public class WTheme {
	protected final Identifier id;
	protected final ImmutableMap<Identifier, WStyle> styles;

	protected WTheme(Identifier id, Map<Identifier, WStyle> styles) {
		this.id = id;
		this.styles = ImmutableMap.copyOf(styles);
	}

	private static JsonObject createObject(Map<String, JsonElement> map) {
		JsonObject obj = new JsonObject();
		obj.putAll(map);
		return obj;
	}

	// Dereference $ vars
	private static void processRefs(Map<String, JsonElement> style, Map<String, JsonElement> refs) {
		for (String property : style.keySet()) {
			JsonElement el = style.get(property);
			String strValue = JanksonOps.INSTANCE.getStringValue(el).orElse("");
			// Look up variables
			if (strValue.startsWith("$") && !strValue.startsWith("$$")) {
				style.put(property, refs.get(strValue));
				continue;
			}

			if (strValue.startsWith("$$")) {
				style.put(property, JanksonOps.INSTANCE.createString(strValue.substring(1)));
			} else {
				style.put(property, el);
			}
		}
	}

	private static void flattenObject(Map<String, JsonElement> collector, String rootKey, JsonObject object) {
		String prefix = rootKey == null ? "" : rootKey + ".";
		for (String key : object.keySet()) {
			JsonElement element = object.get(key);
			if (element instanceof JsonObject) {
				flattenObject(collector, key, (JsonObject) element);
			} else {
				collector.put(prefix + key, element);
			}
		}
	}

	public static WTheme of(Identifier themeId, JsonObject themeDef) {
		// Generic validation
		JsonObject themeProps = themeDef.getObject("theme");
		if (themeProps == null) {
			Spinnery.LOGGER.warn("Invalid theme definition for theme {}", themeId);
			return null;
		}
		Spinnery.LOGGER.log(Level.INFO, "Parsing theme {}", themeId);

		// Get variables
		JsonObject themeVars = themeDef.getObject("vars");
		Map<String, JsonElement> vars = new HashMap<>();
		if (themeVars != null) {
			for (String varKey : themeVars.keySet()) {
				vars.put("$" + varKey, themeVars.get(varKey));
			}
		}

		// Get prototypes
		JsonObject themePrototypes = themeDef.getObject("prototypes");
		Map<String, JsonObject> prototypes = new HashMap<>();
		if (themePrototypes != null) {
			for (String varKey : themePrototypes.keySet()) {
				Map<String, JsonElement> flatProto = new HashMap<>();
				JsonObject protoObj = themePrototypes.getObject(varKey);
				if (protoObj == null) continue;
				flattenObject(flatProto, null, protoObj);
				prototypes.put("$" + varKey, createObject(flatProto));
			}
		}

		// Process widget styles
		Map<Identifier, WStyle> styles = new HashMap<>();
		for (String widgetId : themeProps.keySet()) {
			Map<String, JsonElement> properties = new HashMap<>();
			JsonObject widgetProps = themeProps.getObject(widgetId);
			if (widgetProps == null) {
				Spinnery.LOGGER.warn("Invalid properties definition for widget {}, skipping", widgetId);
				continue;
			}

			// Apply prototypes
			JsonElement protoArray = widgetProps.remove("$extend");
			if (protoArray instanceof JsonArray) {
				for (JsonElement el : (JsonArray) protoArray) {
					String protoName = JanksonOps.INSTANCE.getStringValue(el).orElse("");
					JsonObject protoObj = prototypes.get(protoName);
					if (!protoName.isEmpty() && protoObj != null) {
						properties.putAll(protoObj);
					}
				}
			}

			// Apply actual values
			flattenObject(properties, null, widgetProps);
			processRefs(properties, vars);
			styles.put(new Identifier(widgetId), new WStyle(properties));
		}
		return new WTheme(themeId, styles);
	}

	public Identifier getId() {
		return id;
	}

	public WStyle getStyle(Identifier widgetId) {
		WStyle style = styles.get(widgetId);
		return style == null ? new WStyle() : style;
	}
}
