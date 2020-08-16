package spinnery.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import spinnery.common.utilities.Positions;
import spinnery.widget.implementation.WAbstractWidget;
import spinnery.widget.implementation.WInterface;

public abstract class BaseScreen extends Screen {
	protected final WInterface screenInterface = new WInterface();

	public BaseScreen(Text title) {
		super(title);
	}

	public void init() {
		super.init();

		getInterface().getWidgets().clear();
		initialize(width, height);
		getInterface().getAllWidgets().forEach(WAbstractWidget::onLayoutChange);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		super.renderBackground(matrices);

		VertexConsumerProvider.Immediate provider = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();

		for (WAbstractWidget widget : getInterface().getWidgets()) {
			widget.draw(matrices, provider);
		}

		for (WAbstractWidget widget : getInterface().getAllWidgets()) {
			if (!widget.isHidden() && widget.isFocused()) {
				renderTooltip(matrices, widget.getTooltip(), mouseX, mouseY);
			}
		}

		provider.draw();

		super.render(matrices, mouseX, mouseY, tickDelta);
	}

	public WInterface getInterface() {
		return screenInterface;
	}

	@Override
	public void tick() {
		getInterface().tick();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseClicked((float) mouseX, (float) mouseY, mouseButton);

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseReleased((float) mouseX, (float) mouseY, mouseButton);

		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void mouseMoved(double mouseX, double mouseY) {
		getInterface().onMouseMoved((float) mouseX, (float) mouseY);

		Positions.setMouseX((float) mouseX);
		Positions.setMouseY((float) mouseY);

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		getInterface().onMouseDragged((float) mouseX, (float) mouseY, mouseButton, deltaX, deltaY);

		return super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		getInterface().onMouseScrolled((float) mouseX, (float) mouseY, deltaY);

		return super.mouseScrolled(mouseX, mouseY, deltaY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyPressed(int keyCode, int character, int keyModifier) {
		getInterface().onKeyPressed(keyCode, character, keyModifier);

		return super.keyPressed(keyCode, character, keyModifier);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getInterface().onKeyReleased(character, keyCode, keyModifier);

		return super.keyReleased(character, keyCode, keyModifier);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean charTyped(char character, int keyCode) {
		getInterface().onCharTyped(character, keyCode);

		return super.charTyped(character, keyCode);
	}

	public abstract void initialize(int width, int height);
}
