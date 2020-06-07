package spinnery.client.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import spinnery.Spinnery;

public class SpinneryModMenuIntegration implements ModMenuApi {
	@Override
	public String getModId() {
		return Spinnery.MOD_ID;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return new ConfigScreenFactory<Screen>() {
			@Override
			public Screen create(Screen parent) {

			}
		}
	}
}
