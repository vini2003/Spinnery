package spinnery.client.screen;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WVerticalScrollableContainer;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	private static final Map<String, Object> OPTIONS = new HashMap<String, Object>() {{
			put("hasSmoothing", true);
			put("hasArrows", true);
			put("hasFading", true);
			put("kineticReductionCoefficient", )
	}};

	public static <T> T getValue(String key) {
		return (T) OPTIONS.get(key);
	}

	public static class Screen extends BaseScreen {
		public Screen() {
			WInterface mainInterface = getInterface();

			final int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
			final int width = MinecraftClient.getInstance().getWindow().getScaledWidth();

			WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(width, height));

			mainPanel.setLabel("Spinnery");

			WVerticalScrollableContainer mainList = mainPanel.createChild(WVerticalScrollableContainer::new, Position.of(mainPanel, 0, 16, 0), Size.of(width, height - 16));



		}

		@Override
		public void onClose() {


			super.onClose();
		}
	}

}
