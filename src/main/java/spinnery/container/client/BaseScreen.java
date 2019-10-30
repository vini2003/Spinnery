package spinnery.container.client;

import spinnery.container.common.BaseContainer;
import spinnery.container.common.widget.WDropdown;
import spinnery.container.common.widget.WList;
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WWidget;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BaseScreen<T extends BaseContainer> extends AbstractContainerScreen<T> {
	List<Slot> dragSlots = new ArrayList<>();

	public BaseScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		linkedContainer.tick();
	}

	public T getLinkedContainer() {
		return getContainer();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected boolean isPointWithinBounds(int slotX, int slotY, int defaultSizeX, int defaultSizeY, double mouseX, double mouseY) {
		Optional<? extends WWidget> focusedWidget = getLinkedContainer().getLinkedPanel().getLinkedWidgets().stream().filter((widget) -> widget.isFocused(mouseX, mouseY)).findAny();
		if (focusedWidget.isPresent()) {
			if (focusedWidget.get() instanceof WSlot) {
					WSlot wSlot = (WSlot) focusedWidget.get();
				return (slotX > wSlot.internalSlot.xPosition - 8
						&&  slotX < wSlot.internalSlot.xPosition + 8
						&&  slotY > wSlot.internalSlot.yPosition - 8
						&&  slotY < wSlot.internalSlot.yPosition + 8);
			} else {
				return (mouseX > slotX - 8
						&&  mouseX < slotX + 8
						&&  mouseY > slotY - 8
						&&  mouseY < slotY + 8);
			}
		} else {
			return false;
		}
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		boolean[] isOutsideBounds = { true };
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> {
			if (widget.isWithinBounds(mouseX, mouseY)) {
				isOutsideBounds[0] = false;
			}
		});
		return isOutsideBounds[0];
	}

	@Override
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onDrawTooltip());
		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Override
	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onKeyPressed(keyCode));
		return super.keyPressed(character, keyCode, keyModifier);
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onKeyReleased(keyCode));
		return super.keyPressed(character, keyCode, keyModifier);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseClicked(mouseX, mouseY, mouseButton));
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseReleased(mouseX, mouseY, mouseButton));
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseMoved(mouseX, mouseY));
		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseScrolled(mouseX, mouseY, mouseZ));
		return super.mouseScrolled(mouseX, mouseY, mouseZ);
	}

	@Override
	protected void onMouseClick(Slot slot, int slotX, int slotY, SlotActionType slotActionType) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onSlotClicked(slot, slotX, slotY, slotActionType));
		super.onMouseClick(slot, slotX, slotY, slotActionType);
	}

	@Override
	public boolean mouseDragged(double slotX, double slotY, int mouseButton, double mouseX, double mouseY) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseDragged(slotX, slotY, mouseButton, mouseX, mouseY));
		return super.mouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	public void tick() {
		super.containerWidth = (int) getLinkedContainer().getLinkedPanel().getSizeX();
		super.containerHeight = (int) getLinkedContainer().getLinkedPanel().getSizeY();
		super.width = containerWidth;
		super.height = containerHeight;
		super.left = (int) ((getLinkedContainer().getLinkedPanel().getPositionX()));
		super.top = (int) ((getLinkedContainer().getLinkedPanel().getPositionY()));
		getLinkedContainer().left = super.left;
		getLinkedContainer().top = super.top;
		getLinkedContainer().tick();
		super.tick();
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		getLinkedContainer().getLinkedPanel().drawPanel();
		getLinkedContainer().getLinkedPanel().drawWidget();
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> {
			widget.isFocused(mouseX, mouseY);
		});

		super.render(mouseX, mouseY, tick);

		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> {
			if (widget.getFocus() && widget instanceof WSlot && playerInventory.getCursorStack().isEmpty() && !((WSlot) widget).getSlot().getStack().isEmpty()) {
				this.renderTooltip(((WSlot) widget).getSlot().getStack(), mouseX, mouseY);
			}
		});
	}
}
