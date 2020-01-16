package spinnery.container.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.container.common.BaseContainer;
import spinnery.container.common.widget.WList;
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WWidget;

import java.util.List;

public class BaseScreen<T extends BaseContainer> extends AbstractContainerScreen<T> {
	double tooltipX = 0;
	double tooltipY = 0;
	WSlot drawSlot;

	public BaseScreen(Text name, T linkedContainer, PlayerEntity player) {
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
		super.containerWidth = (int) getLinkedContainer().getLinkedPanel().getSizeX();
		super.containerHeight = (int) getLinkedContainer().getLinkedPanel().getSizeY();
		super.width = containerWidth;
		super.height = containerHeight;
		super.left = (int) ((getLinkedContainer().getLinkedPanel().getPositionX()));
		super.top = (int) ((getLinkedContainer().getLinkedPanel().getPositionY()));
		getLinkedContainer().setPositionX(super.left);
		getLinkedContainer().setPositionY(super.top);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.scanFocus(mouseX, mouseY);
			widget.onMouseMoved(mouseX, mouseY);
		}

		updateTooltip(mouseX, mouseY);

		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.onMouseScrolled(mouseX, mouseY, mouseZ);
		}

		return super.mouseScrolled(mouseX, mouseY, mouseZ);
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.onKeyReleased(keyCode);
		}
		return super.keyReleased(character, keyCode, keyModifier);
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.onCharTyped(character);
		}

		return super.charTyped(character, keyCode);
	}

	public void renderTooltip() {
		if (getDrawSlot() != null && getLinkedContainer().getLinkedPlayerInventory().getCursorStack().isEmpty() && ! getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), (int) getTooltipX(), (int) getTooltipY());
		}
	}

	public void updateTooltip(double mouseX, double mouseY) {
		setDrawSlot(null);
		for (WWidget widgetA : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			if (widgetA.getFocus() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);
				setTooltipX(mouseX);
				setTooltipY(mouseY);
			} else if (widgetA instanceof WList) {
				for (List<WWidget> widgetB : ((WList) widgetA).getListWidgets()) {
					for (WWidget widgetC : widgetB) {
						if (widgetC.scanFocus(mouseX, mouseY)) {
							setDrawSlot((WSlot) widgetC);
							setTooltipX(mouseX);
							setTooltipY(mouseY);
						}
					}
				}
			}
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float tick) {
		getLinkedContainer().getLinkedPanel().draw();

		super.render(mouseX, mouseY, tick);

		renderTooltip();
	}

	@Override
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.onDrawTooltip();
		}
		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Override
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (! InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
				widget.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
	}

	@Override
	public boolean mouseDragged(double slotX, double slotY, int mouseButton, double mouseX, double mouseY) {
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			ItemStack stackA = MinecraftClient.getInstance().player.inventory.getCursorStack();
			int quantityA = mouseButton == 0 ? (int) Math.floor((float) stackA.getCount() / getLinkedContainer().getDragSlots().size()) : mouseButton == 1 ? 1 : 0;
			for (WSlot widgetA : getLinkedContainer().getDragSlots()) {
				if ((widgetA.getStack().getCount() != widgetA.getStack().getMaxCount())) {
					if (widgetA.getStack().isEmpty()) {
						widgetA.setPreviewStack(new ItemStack(stackA.getItem(), quantityA));
					} else if (widgetA.getStack().isItemEqualIgnoreDamage(stackA)) {
						int quantityB = Math.min(quantityA, widgetA.getStack().getMaxCount() - widgetA.getStack().getCount());
						widgetA.setPreviewStack(widgetA.getStack().copy());
						widgetA.getPreviewStack().increment(quantityB);
					}
				}
			}
		}

		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.onMouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
		}

		return super.mouseDragged(slotX, slotY, mouseButton, mouseX, mouseY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			ItemStack[] stackA = {MinecraftClient.getInstance().player.inventory.getCursorStack()};
			int quantityA = mouseButton == 0 ? (int) Math.floor((float) stackA[0].getCount() / getLinkedContainer().getDragSlots().size()) : mouseButton == 1 ? 1 : 0;
			for (WSlot widget : getLinkedContainer().getDragSlots()) {
				if (widget.getStack().getCount() != widget.getStack().getMaxCount()) {
					if (widget.getStack().isEmpty()) {
						widget.setStack(new ItemStack(stackA[0].getItem(), quantityA));
						stackA[0].decrement(quantityA);
					} else if (widget.getStack().isItemEqualIgnoreDamage(stackA[0])) {
						int quantityB = Math.min(quantityA, widget.getStack().getMaxCount() - widget.getStack().getCount());
						widget.getStack().increment(quantityB);
						stackA[0].decrement(quantityB);
					}
					widget.setPreviewStack(ItemStack.EMPTY);
				}
			}

			getLinkedContainer().getDragSlots().clear();
		} else {
			for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			}
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void onMouseClick(Slot slot, int slotX, int slotY, SlotActionType slotActionType) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
			widget.onSlotClicked(slot, slotX, slotY, slotActionType);
		}

		super.onMouseClick(slot, slotX, slotY, slotActionType);
	}

	@Override
	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getLinkedContainer().getLinkedPanel().getLinkedWidgets()) {
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
