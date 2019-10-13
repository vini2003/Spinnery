package electron.container.common.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

public class Widget implements Tickable {
	protected Panel linkedPanel;

	protected int offsetX;
	protected int offsetY;

	protected double positionX = 0;
	protected double positionY = 0;

	protected double sizeX = 0;
	protected double sizeY = 0;

	protected boolean hasFocus = false;

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

	protected Identifier linkedImage;

	public Widget() {
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

	public void onMouseDragged(double slotX, double slotY, int mouseButton, double mouseX, double mouseY) {
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


	/**
	 * Get 'linkedImage' of Widget.
	 * @return 'linkedImage' of Widget.
	 */
	public Identifier getLinkedImage() {
		return linkedImage;
	}

	/**
	 * Set 'linkedImage' of Widget.
	 */
	public void setLinkedImage(Identifier linkedImage) {
		this.linkedImage = linkedImage;
	}

	/**
	 * Get X (horizontal) size of Widget.
	 * @return Integer of 'sizeX'.
	 */
	public double getSizeX() {
		return sizeX;
	}

	/**
	 * Set X (horizontal) size of Widget.
	 * @param sizeX Size to be set.
	 */
	public void setSizeX(double sizeX) {
		this.sizeX = sizeX;
	}

	/**
	 * Get Y (vertical) size of Widget.
	 * @return Integer of 'sizeY'.
	 */
	public double getSizeY() {
		return sizeY;
	}

	/**
	 * Set Y (vertical) size of Widget.
	 * @param sizeY Size to be set.
	 */
	public void setSizeY(double sizeY) {
		this.sizeY = sizeY;
	}

	/**
	 * Get X (horizontal) position of Widget.
	 * @return Integer of 'positionX'.
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Set X (horizontal) position of Widget.
	 * @param positionX Position to be set.
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	/**
	 * Get Y (vertical) position of Widget.
	 * @return Integer of 'positionY'.
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Set Y (vertical) position of Widget.
	 * @param positionY Position to be set.
	 */
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	/**
	 * Called when rendering a Widget's tooltip.
	 */
	public void onDrawTooltip() {
		if (linkedRunnableOnDrawTooltip != null) {
			linkedRunnableOnDrawTooltip.run();
		}
	}

	/**
	 * Get 'linkedRunnableOnDrawTooltip' of 'onDrawTooltip'.
	 * @return Runnable of 'onFocusReleased'.
	 */
	public Runnable getOnDrawTooltip() {
		return linkedRunnableOnDrawTooltip;
	}

	/**
	 * Set 'linkedRunnableOnDrawTooltip' of 'onDrawTooltip'.
	 */
	public void setOnDrawTooltip(Runnable linkedRunnableOnDrawTooltip) {
		this.linkedRunnableOnDrawTooltip = linkedRunnableOnDrawTooltip;
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

	public boolean getFocus() {
		return hasFocus;
	}

	public boolean update(double mouseX, double mouseY) {
		setFocus(mouseX > getPositionX() - 1
				&&  mouseX < getPositionX() + getSizeX() - 2
				&&  mouseY > getPositionY() - 1
				&&  mouseY < getPositionY() + getSizeY() - 2);
		return getFocus();
	}

	public void alignWithContainerEdge() {
		this.setPositionX(getPositionX() + MinecraftClient.getInstance().window.getScaledWidth() / 2 - linkedPanel.getSizeX() / 2);
		this.setPositionY(getPositionY() + MinecraftClient.getInstance().window.getScaledHeight() / 2 - linkedPanel.getSizeY() / 2);
	}

	public void alignWithContainerCenter() {
		this.setPositionX(getPositionX() + MinecraftClient.getInstance().window.getScaledWidth() / 2 - (this.getSizeX() / 2));
	}

	public void draw() {
	}

	@Override
	public void tick() {
	}
}
