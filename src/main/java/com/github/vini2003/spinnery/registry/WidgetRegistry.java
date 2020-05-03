package com.github.vini2003.spinnery.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;
import com.github.vini2003.spinnery.widget.*;

/**
 * Registers all of Spinnery's widgets with Spinnery.
 */
public class WidgetRegistry {
	private static BiMap<ResourceLocation, Class<? extends WAbstractWidget>> widgetMap = HashBiMap.create();

	public static Class<? extends WAbstractWidget> get(String className) {
		for (Class<? extends WAbstractWidget> widgetClass : widgetMap.values()) {
			if (widgetClass.getName().equals(className)) {
				return widgetClass;
			}
		}
		return null;
	}

	public static Class<? extends WAbstractWidget> get(ResourceLocation id) {
		return widgetMap.get(id);
	}

	public static ResourceLocation getId(Class<? extends WAbstractWidget> wClass) {
		return widgetMap.inverse().get(wClass);
	}

	public static void initialize() {
		register(new ResourceLocation("spinnery", "widget"), WAbstractWidget.class);
		register(new ResourceLocation("spinnery", "button"), WButton.class);
		register(new ResourceLocation("spinnery", "dropdown"), WDropdown.class);
		register(new ResourceLocation("spinnery", "dynamic_image"), WDynamicImage.class);
		register(new ResourceLocation("spinnery", "horizontal_slider"), WHorizontalSlider.class);
		register(new ResourceLocation("spinnery", "panel"), WPanel.class);
		register(new ResourceLocation("spinnery", "slot"), WSlot.class);
		register(new ResourceLocation("spinnery", "static_image"), WStaticImage.class);
		register(new ResourceLocation("spinnery", "static_text"), WStaticText.class);
		register(new ResourceLocation("spinnery", "tab_holder"), WTabHolder.class);
		register(new ResourceLocation("spinnery", "tab_toggle"), WTabToggle.class);
		register(new ResourceLocation("spinnery", "toggle"), WToggle.class);
		register(new ResourceLocation("spinnery", "tooltip"), WTooltip.class);
		register(new ResourceLocation("spinnery", "vertical_slider"), WVerticalSlider.class);
		register(new ResourceLocation("spinnery", "vertical_bar"), WVerticalBar.class);
		register(new ResourceLocation("spinnery", "horizontal_bar"), WHorizontalBar.class);
		register(new ResourceLocation("spinnery", "vertical_scrollbar"), WVerticalScrollbar.class);
		register(new ResourceLocation("spinnery", "horizontal_scrollbar"), WHorizontalScrollbar.class);
		register(new ResourceLocation("spinnery", "textured_button"), WTexturedButton.class);
		register(new ResourceLocation("spinnery", "text_area"), WTextArea.class);
		register(new ResourceLocation("spinnery", "text_field"), WTextField.class);
		register(new ResourceLocation("spinnery", "item"), WItem.class);
		register(new ResourceLocation("spinnery", "horizontal_box_container"), WHorizontalBoxContainer.class);
		register(new ResourceLocation("spinnery", "vertical_box_container"), WVerticalBoxContainer.class);
		register(new ResourceLocation("spinnery", "form_container"), WFormContainer.class);
	}

	public static void register(ResourceLocation id, Class<? extends WAbstractWidget> wClass) {
		widgetMap.put(id, wClass);
	}
}
