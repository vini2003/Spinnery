package spinnery.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import spinnery.widget.api.WStyle;
import spinnery.widget.api.WTheme;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ThemeRegistry {
    public static final Identifier DEFAULT_THEME = new Identifier("spinnery", "default");

    private static WTheme defaultTheme;
    private static final Map<Identifier, WTheme> themes = new HashMap<>();

    public static void clear() {
        themes.clear();
    }

    public static void register(WTheme theme) {
        if (theme == null) return;
        if (theme.getId().equals(DEFAULT_THEME)) {
            defaultTheme = theme;
        } else {
            themes.put(theme.getId(), theme);
        }
    }

    public static WStyle getStyle(Identifier themeId, Identifier widgetId) {
        WTheme theme = themes.get(themeId);
        if (theme == null) theme = defaultTheme;
        return theme.getStyle(widgetId);
    }
}
