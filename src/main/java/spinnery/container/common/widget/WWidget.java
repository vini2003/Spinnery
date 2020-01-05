package spinnery.container.common.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.util.Tickable;

import java.util.Optional;

public class WWidget implements Tickable {
	protected WPanel linkedWPanel;

	protected WAnchor anchor;

	protected double positionX = 0;
	protected double positionY = 0;
	protected double positionZ = 0;

	protected double sizeX = 0;
	protected double sizeY = 0;

	protected boolean isHidden = false;

	protected boolean hasFocus = false;

	protected boolean canMove = false;

	protected Runnable linkedRunnableOnMouseClicekd;
	protected Runnable linkedRunnableOnKeyPressed;
	protected Runnable linkedRunnableOnKeyReleased;
	protected Runnable linkedRunnableOnFocusGained;
	protected Runnable linkedRunnableOnFocusReleased;
	protected Runnable linkedRunnableOnDrawTooltip;
	protected Runnable linkedRunnableOnMouseReleased;
	protected Runnable linkedRunnableOnMouseMoved;
	protected Runnable linkedRunnableOnMouseDragged;
	protected Runnable linkedRunnableOnMouseScrolled;
	protected Runnable linkedRunnableOnSlotClicked;

	public WWidget() {
	}

	public void onKeyPressed(int keyPressed) {
		if (linkedRunnableOnKeyPressed != null) {
			linkedRunnableOnKeyPressed.run();
		}
	}

	public Runnable getOnKeyPressed() {
		return linkedRunnableOnKeyPressed;
	}

	public void setOnKeyPressed(Runnable linkedRunnableOnKeyPressed) {
		this.linkedRunnableOnKeyPressed = linkedRunnableOnKeyPressed;
	}

	public void onKeyReleased(int keyReleased) {
		if (linkedRunnableOnKeyReleased != null) {
			linkedRunnableOnKeyReleased.run();
		}
	}

	public Runnable getOnKeyReleased() {
		return linkedRunnableOnKeyReleased;
	}

	public void setOnKeyReleased(Runnable linkedRunnableOnKeyReleased) {
		this.linkedRunnableOnKeyReleased = linkedRunnableOnKeyReleased;
	}

	public void onFocusGained() {
		if (linkedRunnableOnFocusGained != null) {
			linkedRunnableOnFocusGained.run();
		}
	}

	public Runnable getOnFocusGained() {
		return linkedRunnableOnFocusGained;
	}

	public void setOnFocusGained(Runnable linkedRunnableOnFocusGained) {
		this.linkedRunnableOnFocusGained = linkedRunnableOnFocusGained;
	}

	public void onFocusReleased() {
		if (linkedRunnableOnFocusReleased != null) {
			linkedRunnableOnFocusReleased.run();
		}
	}

	public Runnable getOnFocusReleased() {
		return linkedRunnableOnFocusReleased;
	}

	public void setOnFocusReleased(Runnable linkedRunnableOnFocusReleased) {
		this.linkedRunnableOnFocusReleased = linkedRunnableOnFocusReleased;
	}

	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (linkedRunnableOnMouseReleased != null) {
			linkedRunnableOnMouseReleased.run();
		}
	}

	public Runnable getOnMouseReleased() {
		return linkedRunnableOnMouseReleased;
	}

	public void setOnMouseReleased(Runnable linkedRunnable) {
		this.linkedRunnableOnMouseReleased = linkedRunnable;
	}

	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (linkedRunnableOnMouseClicekd != null) {
			linkedRunnableOnMouseClicekd.run();
		}
	}

	public Runnable getOnMouseClicked() {
		return linkedRunnableOnMouseClicekd;
	}

	public void setOnMouseClicked(Runnable linkedRunnable) {
		this.linkedRunnableOnMouseClicekd = linkedRunnable;
	}

	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (linkedRunnableOnMouseDragged != null) {
			linkedRunnableOnMouseDragged.run();
		}
	}

	public Runnable getOnMouseDragged() {
		return linkedRunnableOnMouseDragged;
	}

	public void setOnMouseDragged(Runnable linkedRunnable) {
		this.linkedRunnableOnMouseDragged = linkedRunnable;
	}

	public void onMouseMoved(double mouseX, double mouseY) {
		if (linkedRunnableOnMouseMoved != null) {
			linkedRunnableOnMouseMoved.run();
		}
	}

	public Runnable getOnMouseMoved() {
		return linkedRunnableOnMouseMoved;
	}

	public void setOnMouseMoved(Runnable linkedRunnable) {
		this.linkedRunnableOnMouseMoved = linkedRunnable;
	}

	public void onMouseScrolled(double mouseX, double mouseY, double mouseZ) {
		if (linkedRunnableOnMouseScrolled != null) {
			linkedRunnableOnMouseScrolled.run();
		}
	}

	public Runnable getOnMouseScrolled() {
		return linkedRunnableOnMouseScrolled;
	}

	public void setOnMouseScrolled(Runnable linkedRunnable) {
		this.linkedRunnableOnMouseScrolled = linkedRunnable;
	}

	public void onSlotClicked(Slot slot, int slotX, int slotY, SlotActionType slotActionType) {
		if (linkedRunnableOnSlotClicked != null) {
			linkedRunnableOnSlotClicked.run();
		}
	}

	public Runnable getOnSlotClicked() {
		return linkedRunnableOnSlotClicked;
	}

	public void setOnSlotClicked(Runnable linkedRunnable) {
		this.linkedRunnableOnSlotClicked = linkedRunnable;
	}

	public void onDrawTooltip() {
		if (linkedRunnableOnDrawTooltip != null) {
			linkedRunnableOnDrawTooltip.run();
		}
	}

	public Runnable getOnDrawTooltip() {
		return linkedRunnableOnDrawTooltip;
	}

	public void setOnDrawTooltip(Runnable linkedRunnableOnDrawTooltip) {
		this.linkedRunnableOnDrawTooltip = linkedRunnableOnDrawTooltip;
	}

	public boolean getCanMove() {
		return canMove;
	}

	public void setMovable(boolean canMove) {
		this.canMove = canMove;
	}

	public WPanel getLinkedPanel() {
		return linkedWPanel;
	}

	public void setLinkedPanel(WPanel linkedWPanel) {
		this.linkedWPanel = linkedWPanel;
	}

	public double getSizeX() {
		return sizeX;
	}

	public void setSizeX(double sizeX) {
		this.sizeX = sizeX;
	}

	public double getSizeY() {
		return sizeY;
	}

	public void setSizeY(double sizeY) {
		this.sizeY = sizeY;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	public double getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(double positionZ) {
		this.positionZ = positionZ;
	}

	public boolean getFocus() {
		return hasFocus;
	}

	public boolean setFocus(boolean hasFocus) {
		if (!getFocus() && hasFocus) {
			onFocusGained();
		}
		if (getFocus() && !hasFocus) {
			onFocusReleased();
		}
		this.hasFocus = hasFocus;
		return hasFocus;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isWithinBounds(double positionX, double positionY) {
		return positionX > getPositionX()
			&& positionX < getPositionX() + getSizeX()
			&& positionY > getPositionY()
			&& positionY < getPositionY() + getSizeY();
	}

	public boolean scanFocus(double mouseX, double mouseY) {
		if (isHidden) {
			return false;
		}
		Optional<? extends WWidget> optional = linkedWPanel.getLinkedWidgets().stream().filter((widget) ->
				widget.getPositionZ() > getPositionZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		setFocus(!optional.isPresent() && isWithinBounds(mouseX, mouseY));
		return getFocus();
	}

	public void drawWidget() {
		if (isHidden) {
			return;
		}
	}

	public void setAnchor(WAnchor anchor) {
		this.anchor = anchor;
	}

	public WAnchor getAnchor() {
		return anchor;
	}

	@Override
	public void tick() {
	}
}
