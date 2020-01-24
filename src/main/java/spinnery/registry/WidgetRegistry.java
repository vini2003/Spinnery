package spinnery.registry;

import spinnery.widget.WButton;
import spinnery.widget.WDropdown;
import spinnery.widget.WDynamicImage;
import spinnery.widget.WDynamicText;
import spinnery.widget.WHorizontalSlider;
import spinnery.widget.WTabHolder;
import spinnery.widget.WInterface;
import spinnery.widget.WList;
import spinnery.widget.WSlot;
import spinnery.widget.WStaticImage;
import spinnery.widget.WStaticText;
import spinnery.widget.WTabToggle;
import spinnery.widget.WToggle;
import spinnery.widget.WVerticalSlider;
import spinnery.widget.WWidget;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WidgetRegistry {
	private static List<Class<? extends WWidget>> widgetClasses = new LinkedList<>();

	public static void register(Class<? extends WWidget>... classes) {
		widgetClasses.addAll(Arrays.asList(classes));
	}

	public static Class<? extends WWidget> get(String className) {
		for (Class<? extends WWidget> widgetClass : widgetClasses) {
			if (widgetClass.getName().equals(className)) {
				return widgetClass;
			}
		}
		return null;
	}

	public static void initialize() {
		register(WWidget.class);
		register(WButton.class);
		register(WDropdown.class);
		register(WDynamicImage.class);
		register(WDynamicText.class);
		register(WHorizontalSlider.class);
		register(WInterface.class);
		register(WList.class);
		register(WSlot.class);
		register(WStaticImage.class);
		register(WStaticText.class);
		register(WTabHolder.class);
		register(WTabToggle.class);
		register(WToggle.class);
		register(WVerticalSlider.class);
	}
}
