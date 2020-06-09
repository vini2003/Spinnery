package spinnery.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.Identifier;
import spinnery.client.configuration.widget.WOptionField;
import spinnery.widget.*;

/**
 * Registers all of Spinnery's widgets with Spinnery.
 */
public class WidgetRegistry {
	private static BiMap<Identifier, Class<? extends WAbstractWidget>> widgetMap = HashBiMap.create();

	public static Class<? extends WAbstractWidget> get(String className) {
		for (Class<? extends WAbstractWidget> widgetClass : widgetMap.values()) {
			if (widgetClass.getName().equals(className)) {
				return widgetClass;
			}
		}
		return null;
	}

	public static Class<? extends WAbstractWidget> get(Identifier id) {
		return widgetMap.get(id);
	}

	public static Identifier getId(Class<? extends WAbstractWidget> wClass) {
		return widgetMap.inverse().get(wClass);
	}

	public static void initialize() {
		register(new Identifier("spinnery", "widget"), WAbstractWidget.class);
		register(new Identifier("spinnery", "button"), WButton.class);
		register(new Identifier("spinnery", "dropdown"), WDropdown.class);
		register(new Identifier("spinnery", "dynamic_image"), WDynamicImage.class);
		register(new Identifier("spinnery", "horizontal_slider"), WHorizontalSlider.class);
		register(new Identifier("spinnery", "panel"), WPanel.class);
		register(new Identifier("spinnery", "slot"), WSlot.class);
		register(new Identifier("spinnery", "static_image"), WStaticImage.class);
		register(new Identifier("spinnery", "static_text"), WStaticText.class);
		register(new Identifier("spinnery", "tab_holder"), WTabHolder.class);
		register(new Identifier("spinnery", "tab_toggle"), WTabToggle.class);
		register(new Identifier("spinnery", "toggle"), WToggle.class);
		register(new Identifier("spinnery", "tooltip"), WTooltip.class);
		register(new Identifier("spinnery", "vertical_slider"), WVerticalSlider.class);
		register(new Identifier("spinnery", "vertical_bar"), WVerticalBar.class);
		register(new Identifier("spinnery", "horizontal_bar"), WHorizontalBar.class);
		register(new Identifier("spinnery", "vertical_scrollbar"), WVerticalScrollbar.class);
		register(new Identifier("spinnery", "horizontal_scrollbar"), WHorizontalScrollbar.class);
		register(new Identifier("spinnery", "vertical_scrollable_container"), WVerticalScrollableContainer.class);
		register(new Identifier("spinnery", "horizontal_scrollable_container"), WHorizontalScrollableContainer.class);
		register(new Identifier("spinnery", "textured_button"), WTexturedButton.class);
		register(new Identifier("spinnery", "text_area"), WTextArea.class);
		register(new Identifier("spinnery", "text_field"), WTextField.class);
		register(new Identifier("spinnery", "item"), WItem.class);
		register(new Identifier("spinnery", "horizontal_box_container"), WHorizontalBoxContainer.class);
		register(new Identifier("spinnery", "vertical_box_container"), WVerticalBoxContainer.class);
		register(new Identifier("spinnery", "form_container"), WFormContainer.class);
		register(new Identifier("spinnery", "vertical_arrow_up"), WVerticalArrowUp.class);
		register(new Identifier("spinnery", "vertical_arrow_down"), WVerticalArrowDown.class);
		register(new Identifier("spinnery", "option_field"), WOptionField.class);
	}

	public static void register(Identifier id, Class<? extends WAbstractWidget> wClass) {
		widgetMap.put(id, wClass);
	}
}
