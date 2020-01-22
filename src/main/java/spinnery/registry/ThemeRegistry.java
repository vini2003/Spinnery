package spinnery.registry;

import spinnery.widget.WWidget;

import java.util.HashMap;
import java.util.Map;

public class ThemeRegistry {
	public static Map<String, Map<Class<? extends WWidget>, WWidget.Theme>> widgetThemes = new HashMap<>();

	public static <W extends WWidget> WWidget.Theme get(String name, Class<W> widgetClass) {
 		return widgetThemes.get(name).get(widgetClass);
	}

	public static <T extends WWidget.Theme> void register(String name, Class<? extends WWidget> widgetClass, T theme) {
		widgetThemes.putIfAbsent(name, new HashMap<>());
		widgetThemes.get(name).put(widgetClass, theme);
	}
}
