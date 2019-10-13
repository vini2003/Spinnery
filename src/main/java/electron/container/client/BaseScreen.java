package electron.container.client;

import electron.container.common.BaseContainer;
import electron.container.common.widget.ItemSlot;
import electron.container.common.widget.Sprite;
import electron.container.common.widget.Widget;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseScreen<T extends BaseContainer> extends AbstractContainerScreen<T> {
	T linkedContainer;
	List<Slot> dragSlots = new ArrayList<>();

	public BaseScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		this.linkedContainer = linkedContainer;
		super.containerWidth = (int) linkedContainer.getLinkedPanel().getSizeX();
		super.containerHeight = (int) linkedContainer.getLinkedPanel().getSizeY();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected boolean isPointWithinBounds(int slotX, int slotY, int defaultSizeX, int defaultSizeY, double mouseX, double mouseY) {
		Optional<? extends Widget> focusedWidget = linkedContainer.getLinkedPanel().getLinkedWidgets().stream().filter((widget) -> ((Widget) widget).update(mouseX, mouseY)).findAny();
		if (focusedWidget.isPresent()) {
			if (focusedWidget.get() instanceof ItemSlot) {
				ItemSlot itemSlot = (ItemSlot) focusedWidget.get();
				return (slotX > itemSlot.internalSlot.xPosition - 8
					&&  slotX < itemSlot.internalSlot.xPosition + 8
					&&  slotY > itemSlot.internalSlot.yPosition - 8
					&&  slotY < itemSlot.internalSlot.yPosition + 8);
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
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onDrawTooltip());
		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Override
	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onKeyPressed(keyCode));
		return super.keyPressed(character, keyCode, keyModifier);
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onKeyReleased(keyCode));
		return super.keyPressed(character, keyCode, keyModifier);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseClicked(mouseX, mouseY, mouseButton));
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseReleased(mouseX, mouseY, mouseButton));
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseMoved(mouseX, mouseY));
		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseScrolled(mouseX, mouseY, mouseZ));
		return super.mouseScrolled(mouseX, mouseY, mouseZ);
	}

	@Override
	protected void onMouseClick(Slot slot, int slotX, int slotY, SlotActionType slotActionType) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onSlotClicked(slot, slotX, slotY, slotActionType));
		super.onMouseClick(slot, slotX, slotY, slotActionType);
	}

	@Override
	public boolean mouseDragged(double slotX, double slotY, int mouseButton, double mouseX, double mouseY) {
		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseDragged(slotX, slotY, mouseButton, mouseX, mouseY));
		return super.mouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	public void tick() {
		linkedContainer.tick();
		super.tick();
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		double panelX = linkedContainer.getLinkedPanel().getPositionX();
		double panelY = linkedContainer.getLinkedPanel().getPositionY();

		double sizeX = linkedContainer.getLinkedPanel().getSizeX();
		double sizeY = linkedContainer.getLinkedPanel().getSizeY();

		BaseRenderer.drawPanel(panelX, panelY, sizeX, sizeY, 0xFF555555, 0xFFC6C6C6, 0xFFFFFFFF, 0xFF000000);

		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> {
			widget.update(mouseX, mouseY);
			widget.draw();
		});

		super.render(mouseX, mouseY, tick);

		linkedContainer.getLinkedPanel().getLinkedWidgets().forEach((widget) -> {
			if (widget.getFocus() && widget instanceof ItemSlot && playerInventory.getCursorStack().isEmpty() && !((ItemSlot) widget).internalSlot.getStack().isEmpty()) {
				this.renderTooltip(((ItemSlot) widget).internalSlot.getStack(), mouseX, mouseY);
			}
		});
	}
}
