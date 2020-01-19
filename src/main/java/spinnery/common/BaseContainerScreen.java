package spinnery.common;

import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.common.BaseContainer;
import spinnery.widget.WCollection;
import spinnery.widget.WList;
import spinnery.widget.WSlot;
import spinnery.widget.WWidget;

import java.util.List;

public class BaseContainerScreen<T extends BaseContainer> extends AbstractContainerScreen<T> {
	double tooltipX = 0;
	double tooltipY = 0;
	WSlot drawSlot;

	public BaseContainerScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		resizeAll();
		linkedContainer.tick();
	}

	public T getLinkedContainer() {
		return super.container;
	}

	public WSlot getDrawSlot() {
		return drawSlot;
	}

	public void setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
	}

	public double getTooltipX() {
		return tooltipX;
	}

	public void setTooltipX(double tooltipX) {
		this.tooltipX = tooltipX;
	}

	public double getTooltipY() {
		return tooltipY;
	}

	public void setTooltipY(double tooltipY) {
		this.tooltipY = tooltipY;
	}

	public void resizeAll() {
		super.containerWidth = (int) getLinkedContainer().getInterface().getSizeX();
		super.containerHeight = (int) getLinkedContainer().getInterface().getSizeY();
		super.width = containerWidth;
		super.height = containerHeight;
		super.x = (int) ((getLinkedContainer().getInterface().getPositionX()));
		super.y = (int) ((getLinkedContainer().getInterface().getPositionY()));
		getLinkedContainer().setPositionX(super.x);
		getLinkedContainer().setPositionY(super.y);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.scanFocus(mouseX, mouseY);
			widget.onMouseMoved(mouseX, mouseY);
		}

		updateTooltip(mouseX, mouseY);

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onMouseScrolled(mouseX, mouseY, mouseZ);
		}

		return super.mouseScrolled(mouseX, mouseY, mouseZ);
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onKeyReleased(keyCode);
		}
		return super.keyReleased(character, keyCode, keyModifier);
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onCharTyped(character);
		}

		return super.charTyped(character, keyCode);
	}

	public void renderTooltip() {
		if (getDrawSlot() != null && getLinkedContainer().getLinkedPlayerInventory().getCursorStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), (int) getTooltipX(), (int) getTooltipY());
		}
	}

	public void updateTooltip(double mouseX, double mouseY) {
		setDrawSlot(null);
		for (WWidget widgetA : getLinkedContainer().getInterface().getWidgets()) {
			if (widgetA.getFocus() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);
				setTooltipX(mouseX);
				setTooltipY(mouseY);
			} else if (widgetA instanceof WCollection) {
				for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
					if (widgetB.scanFocus(mouseX, mouseY) && widgetB instanceof WSlot) {
						setDrawSlot((WSlot) widgetB);
						setTooltipX(mouseX);
						setTooltipY(mouseY);
					}
				}
			}
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		getLinkedContainer().getInterface().draw();

		super.render(mouseX, mouseY, tick);

		renderTooltip();
	}

	@Override
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onDrawTooltip();
		}
		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Override
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
	}

	@Override
	public boolean mouseDragged(double slotX, double slotY, int mouseButton, double mouseX, double mouseY) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onMouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
		}

		return super.mouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onMouseReleased(mouseX, mouseY, mouseButton);
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void onMouseClick(Slot slot, int slotX, int slotY, SlotActionType slotActionType) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onSlotClicked(slot, slotX, slotY, slotActionType);
		}

		super.onMouseClick(slot, slotX, slotY, slotActionType);
	}

	@Override
	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getLinkedContainer().getInterface().getWidgets()) {
			widget.onKeyPressed(keyCode, character, keyModifier);
		}
		if (character == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.player.closeContainer();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		getLinkedContainer().tick();
		super.tick();
	}
}
