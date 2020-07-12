package spinnery.debug;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class DebugHandledScreens {
	public static void initialize() {
	}

	static {
		ScreenRegistry.register(DebugScreenHandlers.DEBUG, DebugHandledScreen::new);
	}
}
