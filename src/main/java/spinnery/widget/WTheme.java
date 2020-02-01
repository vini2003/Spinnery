package spinnery.widget;

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
	protected final String name;
	protected final ImmutableMap<Identifier, WStyle> styles;

	protected WTheme(Identifier id, String name, Map<Identifier, WStyle> styles) {
		this.id = id;
		this.name = name;
		this.styles = ImmutableMap.copyOf(styles);
	}

	private static JsonObject createObject(Map<String, JsonElement> map) {
		JsonObject obj = new JsonObject();
		obj.putAll(map);
		return obj;
	}

	// Recursively populate objects with $ references
	private static Map<String, JsonElement> lookupRefs(JsonObject object, Map<String, JsonElement> refs) {
		Map<String, JsonElement> outObj = new HashMap<>();
		for (String property : object.keySet()) {
			JsonElement el = object.get(property);
			if (el instanceof JsonObject) {
				outObj.put(property, createObject(lookupRefs((JsonObject) el, refs)));
				continue;
			}
			String strValue = JanksonOps.INSTANCE.getStringValue(el).orElse("");
			// Look up variables
			if (strValue.startsWith("$") && !strValue.startsWith("$$")) {
				outObj.put(property, refs.get(strValue));
				continue;
			}

			if (strValue.startsWith("$$")) {
				outObj.put(property, JanksonOps.INSTANCE.createString(strValue.substring(1)));
			} else {
				outObj.put(property, el);
			}
		}
		return outObj;
	}

	public static WTheme of(JsonObject themeDef) throws SyntaxError {
		// Get theme metadata and validate
		String themeId = JanksonOps.INSTANCE.getStringValue(themeDef.get("id")).orElseThrow(() -> new SyntaxError("Missing `id` in theme definition"));
		String themeName = JanksonOps.INSTANCE.getStringValue(themeDef.get("name")).orElse(themeId);
		JsonObject themeProps = themeDef.getObject("theme");
		if (themeProps == null) {
			Spinnery.LOGGER.warn("Invalid theme definition for theme {}", themeId);
			return null;
		}
		Spinnery.LOGGER.log(Level.INFO, "Parsing theme {}", themeName);

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
				JsonObject protoObj = themePrototypes.getObject(varKey);
				if (protoObj == null) continue;
				prototypes.put("$" + varKey, createObject(lookupRefs(protoObj, vars)));
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
			if (widgetProps.get("$extend") != null) {
				JsonElement protoArray = widgetProps.get("$extend");
				if (protoArray instanceof JsonArray) {
					((JsonArray) protoArray).forEach(el -> {
						String protoName = JanksonOps.INSTANCE.getStringValue(el).orElse("");
						JsonObject protoObj = prototypes.get(protoName);
						if (!protoName.isEmpty() && protoObj != null) {
							properties.putAll(protoObj);
						}
					});
				}
			}

			// Apply actual values
			properties.putAll(lookupRefs(widgetProps, vars));
			styles.put(new Identifier(widgetId), new WStyle(properties));
		}
		return new WTheme(new Identifier(themeId), themeName, styles);
	}

	public Identifier getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public WStyle getStyle(Identifier widgetId) {
		WStyle style = styles.get(widgetId);
		return style == null ? new WStyle() : style;
	}
}
