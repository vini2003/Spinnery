package spinnery.client.compatibility;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.DisplayHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.common.screenhandler.BaseScreenHandler;

public class SpinneryRoughlyEnoughItemsPlugin implements REIPluginV0 {
	private static final Identifier IDENTIFIER = new Identifier(Spinnery.MOD_ID, "rei_plugin");

	@Override
	public Identifier getPluginIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void registerBounds(DisplayHelper displayHelper) {
		displayHelper.registerHandler(new DisplayHelper.DisplayBoundsProvider<BaseHandledScreen<BaseScreenHandler>>() {
			@Override
			public Class<?> getBaseSupportedClass() {
				return BaseHandledScreen.class;
			}

			@Override
			public Rectangle getScreenBounds(BaseHandledScreen<BaseScreenHandler> screen) {
				return screen.getHandler().getRectangle();
			}

			@Override
			public float getPriority() {
				return 16;
			}
		});
	}
}
