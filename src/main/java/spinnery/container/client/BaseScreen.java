package spinnery.container.client;

import net.minecraft.block.BarrelBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import spinnery.container.common.BaseContainer;
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WSlotList;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BaseScreen<T extends BaseContainer> extends AbstractContainerScreen<T> {
	double tooltipX = 0;
	double tooltipY = 0;
	boolean isMouseHovering = false;
	WSlot drawSlot;

	public BaseScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		resizeAll();
		linkedContainer.tick();
	}

	public T getLinkedContainer() {
		return getContainer();
	}

	public void resizeAll() {
		super.containerWidth = (int) getLinkedContainer().getLinkedPanel().getSizeX();
		super.containerHeight = (int) getLinkedContainer().getLinkedPanel().getSizeY();
		super.width = containerWidth;
		super.height = containerHeight;
		super.left = (int) ((getLinkedContainer().getLinkedPanel().getPositionX()));
		super.top = (int) ((getLinkedContainer().getLinkedPanel().getPositionY()));
		getLinkedContainer().left = super.left;
		getLinkedContainer().top = super.top;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

//	@Override
//	protected boolean isPointWithinBounds(int slotX, int slotY, int defaultSizeX, int defaultSizeY, double mouseX, double mouseY) {
//		return drawSlot != null
//		    && drawSlot.isWithinBounds(mouseX, mouseY)
//		    && slotX > drawSlot.internalSlot.xPosition - 8
//		    && slotX < drawSlot.internalSlot.xPosition + 8
//		    && slotY > drawSlot.internalSlot.yPosition - 8
//		    && slotY < drawSlot.internalSlot.yPosition + 8;
//	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
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
		if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseClicked(mouseX, mouseY, mouseButton));
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			ItemStack[] stackA = { MinecraftClient.getInstance().player.inventory.getCursorStack() };
			int quantityA = mouseButton == 0 ? (int) Math.floor((float) stackA[0].getCount() / getLinkedContainer().dragSlots.size()) : mouseButton == 1 ? 1 : 0;
			getLinkedContainer().dragSlots.forEach(widget -> {
				if (((WSlot) widget).getStack().getCount() != ((WSlot) widget).getStack().getMaxCount()) {
					if (((WSlot) widget).getStack().isEmpty()) {
						((WSlot) widget).setStack(new ItemStack(stackA[0].getItem(), quantityA));
						stackA[0].decrement(quantityA);
					} else if (((WSlot) widget).getStack().isItemEqualIgnoreDamage(stackA[0])) {
						int quantityB = Math.min(quantityA, ((WSlot) widget).getStack().getMaxCount() - ((WSlot) widget).getStack().getCount());
						((WSlot) widget).getStack().increment(quantityB);
						stackA[0].decrement(quantityB);
					}
					((WSlot) widget).previewStack = ItemStack.EMPTY;
				}
			});
			isMouseHovering = false;
			getLinkedContainer().dragSlots.clear();
		} else {
			getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseReleased(mouseX, mouseY, mouseButton));
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> {
			widget.scanFocus(mouseX, mouseY);
			widget.onMouseMoved(mouseX, mouseY);
		});

		updateTooltip(mouseX, mouseY);

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
		isMouseHovering = true;
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			ItemStack stackA = MinecraftClient.getInstance().player.inventory.getCursorStack();
			int quantityA = mouseButton == 0 ? (int) Math.floor((float) stackA.getCount() / getLinkedContainer().dragSlots.size()) : mouseButton == 1 ? 1 : 0;
			getLinkedContainer().dragSlots.forEach(widgetA -> {
				if ((widgetA.getStack().getCount() !=  widgetA.getStack().getMaxCount())) {
					if (widgetA.getStack().isEmpty()) {
						widgetA.previewStack = new ItemStack(stackA.getItem(), quantityA);
					} else if (widgetA.getStack().isItemEqualIgnoreDamage(stackA)) {
						int quantityB = Math.min(quantityA, widgetA.getStack().getMaxCount() - widgetA.getStack().getCount());
						widgetA.previewStack = widgetA.getStack().copy();
						widgetA.previewStack.increment(quantityB);
					}
				}
			});
		}
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onMouseDragged(slotX, slotY, mouseButton, mouseX, mouseY));
		return super.mouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	public void tick() {
		getLinkedContainer().tick();
		super.tick();
	}

	public void renderTooltip() {
		if (drawSlot != null && playerInventory.getCursorStack().isEmpty() && !drawSlot.getStack().isEmpty()) {
			this.renderTooltip(drawSlot.getStack(), (int) tooltipX, (int) tooltipY);
		}
	}

	public void updateTooltip(double mouseX, double mouseY) {
		drawSlot = null;

		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach(widget -> {
			if (widget.getFocus() && widget instanceof WSlot) {
				drawSlot = ((WSlot) widget);
				tooltipX = mouseX;
				tooltipY = mouseY;
			} else if (widget.getFocus() && widget instanceof WSlotList) {
				((WSlotList) widget).listWidgets.forEach(slots -> slots.forEach(slot -> {
					if (slot.isWithinBounds(mouseX, mouseY)) {
						drawSlot = ((WSlot) slot);
						tooltipX = mouseX;
						tooltipY = mouseY;
					}
				}));
			}
		});
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		getLinkedContainer().getLinkedPanel().drawPanel();
		getLinkedContainer().getLinkedPanel().drawWidget();

		super.render(mouseX, mouseY, tick);

		renderTooltip();
	}
}
