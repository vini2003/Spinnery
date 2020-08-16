package spinnery.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Layers;
import spinnery.common.screenhandler.BaseScreenHandler;
import spinnery.common.utilities.Networks;
import spinnery.common.utilities.Positions;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;
import spinnery.widget.api.WContextLock;
import spinnery.widget.api.WInterfaceProvider;

public class BaseHandledScreen<T extends BaseScreenHandler> extends HandledScreen<T> implements WInterfaceProvider {
	protected final WInterface clientInterface;
	protected float tooltipX = 0;
	protected float tooltipY = 0;
	protected WSlot drawSlot;

	protected int minX = Integer.MAX_VALUE;
	protected int minY = Integer.MAX_VALUE;
	protected int maxX = Integer.MIN_VALUE;
	protected int maxY = Integer.MIN_VALUE;


	@Environment(EnvType.CLIENT)
	public BaseHandledScreen(Text name, T handler, PlayerEntity player) {
		super(handler, player.inventory, name);
		clientInterface = new WInterface(handler);
	}

	@Environment(EnvType.CLIENT)
	public BaseHandledScreen(T handler, PlayerInventory playerInventory, Text name) {
		super(handler, playerInventory, name);
		clientInterface = new WInterface(handler);
	}

	public void init() {
		super.init();

		handler.getInterface().getWidgets().clear();
		handler.slots.clear();
		this.backgroundWidth = width;
		this.backgroundHeight = height;
		handler.initialize(width, height);
		handler.onLayoutChange();
		handler.getInterface().getAllWidgets().forEach(WAbstractWidget::onLayoutChange);
		Networks.toServer(Networks.INITIALIZE, Networks.ofInitialize(handler.syncId, width, height));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		super.renderBackground(matrices);

		VertexConsumerProvider.Immediate provider = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();

		for (WAbstractWidget widget : handler.getInterface().getWidgets()) {
			widget.draw(matrices, provider);
		}

		for (WAbstractWidget widget : handler.getInterface().getAllWidgets()) {
			if (!widget.isHidden() && widget.isFocused()) {
				renderTooltip(matrices, widget.getTooltip(), mouseX, mouseY);
			}
		}

		provider.draw();

		super.render(matrices, mouseX, mouseY, tickDelta);

		super.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}


	@Override
	@Environment(EnvType.CLIENT)
	protected void drawBackground(MatrixStack matrices, float tick, int mouseX, int mouseY) {
	}
	

	@Override
	@Environment(EnvType.CLIENT)
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		handler.getInterface().onMouseClicked((float) mouseX, (float) mouseY, mouseButton);

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		handler.getInterface().onMouseReleased((float) mouseX, (float) mouseY, mouseButton);

		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void mouseMoved(double mouseX, double mouseY) {
		handler.getInterface().onMouseMoved((float) mouseX, (float) mouseY);

		Positions.setMouseX((float) mouseX);
		Positions.setMouseY((float) mouseY);

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		handler.getInterface().onMouseDragged((float) mouseX, (float) mouseY, mouseButton, deltaX, deltaY);

		return super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		handler.getInterface().onMouseScrolled((float) mouseX, (float) mouseY, deltaY);

		return super.mouseScrolled(mouseX, mouseY, deltaY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyPressed(int keyCode, int character, int keyModifier) {
		handler.getInterface().onKeyPressed(keyCode, character, keyModifier);
		
		return super.keyPressed(keyCode, character, keyModifier);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		handler.getInterface().onKeyReleased(character, keyCode, keyModifier);

		return super.keyReleased(character, keyCode, keyModifier);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean charTyped(char character, int keyCode) {
		handler.getInterface().onCharTyped(character, keyCode);

		return super.charTyped(character, keyCode);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		handler.getInterface().tick();

		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public T getHandler() {
		return super.handler;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public WInterface getInterface() {
		return clientInterface;
	}
}
