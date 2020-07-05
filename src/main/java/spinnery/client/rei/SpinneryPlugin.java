package spinnery.client.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.DisplayHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import spinnery.Spinnery;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;

public class SpinneryPlugin implements REIPluginV0 {
    private static final Identifier IDENTIFIER = new Identifier(Spinnery.MOD_ID, "rei_plugin");

    @Override
    public Identifier getPluginIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void registerBounds(DisplayHelper displayHelper) {
        displayHelper.registerHandler(new DisplayHelper.DisplayBoundsHandler<BaseContainerScreen<BaseContainer>>() {
            @Override
            public Class<?> getBaseSupportedClass() {
                return BaseContainerScreen.class;
            }

            @Override
            public boolean isHandingScreen(Class<?> screen) {
                return BaseContainerScreen.class.isAssignableFrom(screen);
            }

            @Override
            public Rectangle getLeftBounds(BaseContainerScreen<BaseContainer> screen) {
                return new Rectangle(2, 0, screen.getX() - 2, screen.getMaxY() - screen.getMinY());
            }

            @Override
            public Rectangle getRightBounds(BaseContainerScreen<BaseContainer> screen) {
                return new Rectangle(screen.getMaxX() + 2, 0, MinecraftClient.getInstance().getWindow().getScaledWidth() - screen.getMaxX(), MinecraftClient.getInstance().getWindow().getScaledHeight());
            }

            @Override
            public boolean shouldRecalculateArea(boolean isOnRightSide, Rectangle rectangle) {
                if (BaseContainerScreen.shouldUpdate) {
                    BaseContainerScreen.shouldUpdate = false;
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
