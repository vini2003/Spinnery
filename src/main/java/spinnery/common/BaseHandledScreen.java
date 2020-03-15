package spinnery.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;
import spinnery.widget.api.WCollection;
import spinnery.widget.api.WContextLock;
import spinnery.widget.api.WInterfaceProvider;

public class BaseHandledScreen<T extends BaseScreenHandler> extends AbstractInventoryScreen<T> implements WInterfaceProvider {
	protected final WInterface clientInterface;
	protected int tooltipX = 0;
	protected int tooltipY = 0;
	protected WSlot drawSlot;

	@Environment(EnvType.CLIENT)
	public BaseHandledScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		clientInterface = new WInterface(linkedContainer);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void render(int mouseX, int mouseY, float tick) {
		clientInterface.draw();

		if (getDrawSlot() != null && getHandler().getPlayerInventory().getCursorStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), getTooltipX(), getTooltipY());
		}

		ItemStack stackA;

		if (getHandler().getPreviewCursorStack().isEmpty()
				&& getHandler().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).isEmpty()
				&& getHandler().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).isEmpty()) {
			stackA = getHandler().getPlayerInventory().getCursorStack();
		} else {
			stackA = getHandler().getPreviewCursorStack();
		}

		RenderSystem.pushMatrix();
		RenderSystem.translatef(0, 0, 200);
		BaseRenderer.getItemRenderer().renderGuiItem(stackA, mouseX - 8, mouseY - 8);
		BaseRenderer.getItemRenderer().renderGuiItemOverlay(BaseRenderer.getTextRenderer(), stackA, mouseX - 8, mouseY - 8);
		RenderSystem.popMatrix();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		clientInterface.onDrawMouseoverTooltip(mouseX, mouseY);

		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseClicked((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		getInterface().onMouseDragged((int) mouseX, (int) mouseY, mouseButton, (int) deltaX, (int) deltaY);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseReleased((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyPressed(int keyCode, int character, int keyModifier) {
		clientInterface.onKeyPressed(keyCode, character, keyModifier);

		if (keyCode == GLFW.GLFW_KEY_ESCAPE || MinecraftClient.getInstance().options.keyInventory.matchesKey(keyCode, character)) {
			if (clientInterface.getAllWidgets().stream().noneMatch(widget -> widget instanceof WContextLock && ((WContextLock) widget).isActive())) {
				MinecraftClient.getInstance().player.closeHandledScreen();
				return true;
			}
		}

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		getInterface().tick();
		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public WSlot getDrawSlot() {
		return drawSlot;
	}

	@Environment(EnvType.CLIENT)
	public T getHandler() {
		return super.handler;
	}

	@Environment(EnvType.CLIENT)
	public int getTooltipX() {
		return tooltipX;
	}

	@Environment(EnvType.CLIENT)
	public <S extends BaseHandledScreen> S setTooltipX(int tooltipX) {
		this.tooltipX = tooltipX;
		return (S) this;
	}

	@Environment(EnvType.CLIENT)
	public int getTooltipY() {
		return tooltipY;
	}

	@Environment(EnvType.CLIENT)
	public <S extends BaseHandledScreen> S setTooltipY(int tooltipY) {
		this.tooltipY = tooltipY;
		return (S) this;
	}

	@Environment(EnvType.CLIENT)
	public <S extends BaseHandledScreen> S setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
		return (S) this;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		getInterface().onAlign();
		super.resize(client, width, height);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public WInterface getInterface() {
		return clientInterface;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		getInterface().onMouseScrolled((int) mouseX, (int) mouseY, deltaY);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getInterface().onKeyReleased(character, keyCode, keyModifier);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean charTyped(char character, int keyCode) {
		getInterface().onCharTyped(character, keyCode);

		return super.charTyped(character, keyCode);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void mouseMoved(double mouseX, double mouseY) {
		clientInterface.onMouseMoved((int) mouseX, (int) mouseY);

		updateTooltip((int) mouseX, (int) mouseY);
	}

	@Environment(EnvType.CLIENT)
	public void updateTooltip(int mouseX, int mouseY) {
		setDrawSlot(null);
		for (WAbstractWidget widgetA : getInterface().getWidgets()) {
			if (widgetA.isFocused() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);
				setTooltipX(mouseX);
				setTooltipY(mouseY);
			} else if (widgetA instanceof WCollection) {
				for (WAbstractWidget widgetB : ((WCollection) widgetA).getWidgets()) {
					if (widgetB.updateFocus(mouseX, mouseY) && widgetB instanceof WSlot) {
						setDrawSlot((WSlot) widgetB);
						setTooltipX(mouseX);
						setTooltipY(mouseY);
					}
				}
			}
		}
	}
}
