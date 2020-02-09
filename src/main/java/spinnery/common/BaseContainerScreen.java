package spinnery.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.api.WCollection;

public class BaseContainerScreen<T extends BaseContainer> extends ContainerScreen<T> {
	protected final WInterface clientInterface;
	protected int tooltipX = 0;
	protected int tooltipY = 0;
	protected WSlot drawSlot;

	@Environment(EnvType.CLIENT)
	public BaseContainerScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		clientInterface = new WInterface(linkedContainer);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void render(int mouseX, int mouseY, float tick) {
		clientInterface.draw();

		if (getDrawSlot() != null && getLinkedContainer().getPlayerInventory().getCursorStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), getTooltipX(), getTooltipY());
		}

		ItemStack stackA;

		if (getContainer().getPreviewCursorStack().isEmpty()
		&&	getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).isEmpty()
		&&  getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).isEmpty()) {
			stackA = getContainer().getPlayerInventory().getCursorStack();
		} else {
			stackA = getContainer().getPreviewCursorStack();
		}

		RenderSystem.translatef(0, 0, 200);
		BaseRenderer.getItemRenderer().renderGuiItem(stackA, mouseX - 8, mouseY - 8);
		BaseRenderer.getItemRenderer().renderGuiItemOverlay(BaseRenderer.getTextRenderer(), stackA, mouseX - 8, mouseY - 8);
		RenderSystem.translatef(0, 0, 0);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		clientInterface.onDrawMouseoverTooltip(mouseX, mouseY);

		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Environment(EnvType.CLIENT)
	public WSlot getDrawSlot() {
		return drawSlot;
	}

	@Environment(EnvType.CLIENT)
	public T getLinkedContainer() {
		return super.container;
	}

	@Environment(EnvType.CLIENT)
	public int getTooltipX() {
		return tooltipX;
	}

	@Environment(EnvType.CLIENT)
	public <S extends BaseContainerScreen> S setTooltipX(int tooltipX) {
		this.tooltipX = tooltipX;
		return (S) this;
	}

	@Environment(EnvType.CLIENT)
	public int getTooltipY() {
		return tooltipY;
	}

	@Environment(EnvType.CLIENT)
	public <S extends BaseContainerScreen> S setTooltipY(int tooltipY) {
		this.tooltipY = tooltipY;
		return (S) this;
	}

	@Environment(EnvType.CLIENT)
	public <S extends BaseContainerScreen> S setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
		return (S) this;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseClicked((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Environment(EnvType.CLIENT)
	public WInterface getInterface() {
		return clientInterface;
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

		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.player.closeContainer();
			return true;
		} else {
			return false;
		}
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
