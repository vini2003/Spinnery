package spinnery.container.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import spinnery.container.common.BaseContainer;
import spinnery.container.common.widget.WList;
import spinnery.container.common.widget.WSlot;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

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
		return super.container;
	}

	public void setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
	}

	public WSlot getDrawSlot() {
		return drawSlot;
	}

	public void setTooltipX(double tooltipX) {
		this.tooltipX = tooltipX;
	}

	public double getTooltipX() {
		return tooltipX;
	}

	public void setTooltipY(double tooltipY) {
		this.tooltipY = tooltipY;
	}

	public double getTooltipY() {
		return tooltipY;
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
	public boolean isPauseScreen() {
		return false;
	}

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
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onKeyPressed(keyCode, character, keyModifier));
		if (character == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.player.closeContainer();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onKeyReleased(keyCode));
		return super.keyReleased(character, keyCode, keyModifier);
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
	public boolean charTyped(char character, int keyCode) {
		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach((widget) -> widget.onCharTyped(character));
		return super.charTyped(character, keyCode);
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
						widgetA.setPreviewStack(new ItemStack(stackA.getItem(), quantityA));
					} else if (widgetA.getStack().isItemEqualIgnoreDamage(stackA)) {
						int quantityB = Math.min(quantityA, widgetA.getStack().getMaxCount() - widgetA.getStack().getCount());
						widgetA.setPreviewStack(widgetA.getStack().copy());
						widgetA.getPreviewStack().increment(quantityB);
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
		if (getDrawSlot() != null && getLinkedContainer().getLinkedPlayerInventory().getCursorStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), (int) getTooltipX(), (int) getTooltipY());
		}
	}

	public void updateTooltip(double mouseX, double mouseY) {
		setDrawSlot(null);

		getLinkedContainer().getLinkedPanel().getLinkedWidgets().forEach(widgetA -> {
			if (widgetA.getFocus() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);
				setTooltipX(mouseX);
				setTooltipY(mouseY);
			} else if (widgetA.getFocus() && widgetA instanceof WList) {
				((WList) widgetA).listWidgets.forEach(widgets -> widgets.forEach(widgetB -> {
					if (widgetB.isWithinBounds(mouseX, mouseY)) {
						setDrawSlot((WSlot) widgetB);
						setTooltipX(mouseX);
						setTooltipY(mouseY);
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
