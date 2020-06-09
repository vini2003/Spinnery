package spinnery.client.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import spinnery.Spinnery;

public class SpinneryModMenu implements ModMenuApi {
	@Override
	public String getModId() {
		return Spinnery.MOD_ID;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (ConfigScreenFactory<Screen>) parent -> new SpinneryConfigurationScreen();
	}
}
