package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import spinnery.registry.ThemeRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WWidget implements Tickable {
	protected WInterface linkedPanel;
	protected WAnchor anchor;
	protected int positionX = 0;
	protected int positionY = 0;
	protected int positionZ = 0;
	protected int sizeX = 0;
	protected int sizeY = 0;
	protected boolean isHidden = false;
	protected boolean hasFocus = false;
	protected boolean canMove = false;
	protected Runnable linkedRunnableOnCharTyped;
	protected Runnable linkedRunnableOnMouseClicked;
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
	private String theme = "default";
	private Text label = new LiteralText("");

	public WWidget() {
	}

	public static Theme of(Map<String, String> rawTheme) {
		return null;
	}

	public void onCharTyped(char character) {
		if (linkedRunnableOnCharTyped != null) {
			linkedRunnableOnCharTyped.run();
		}
	}

	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
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

	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (linkedRunnableOnMouseClicked != null) {
			linkedRunnableOnMouseClicked.run();
		}
	}

	public Runnable getOnMouseClicked() {
		return linkedRunnableOnMouseClicked;
	}

	public void setOnMouseClicked(Runnable linkedRunnable) {
		this.linkedRunnableOnMouseClicked = linkedRunnable;
	}

	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
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

	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
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

	public Text getLabel() {
		return label;
	}

	public void setLabel(Text label) {
		this.label = label;
	}

	public boolean hasLabel() {
		return !label.asFormattedString().isEmpty();
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public boolean getCanMove() {
		return canMove;
	}

	public void setMovable(boolean canMove) {
		this.canMove = canMove;
	}

	public WInterface getInterface() {
		return linkedPanel;
	}

	public void setInterface(WInterface linkedPanel) {
		this.linkedPanel = linkedPanel;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(int positionZ) {
		this.positionZ = positionZ;
	}

	public void setAnchoredPositionX(int positionX) {
		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getInterface().getPositionX() : 0));
	}

	public void setAnchoredPositionY(int positionY) {
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getInterface().getPositionY() : 0));
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

	public boolean isWithinBounds(int positionX, int positionY) {
		return positionX > getPositionX()
				&& positionX < getPositionX() + getSizeX()
				&& positionY > getPositionY()
				&& positionY < getPositionY() + getSizeY();
	}

	public boolean scanFocus(int mouseX, int mouseY) {
		if (isHidden) {
			return false;
		}
		Optional<? extends WWidget> optional = linkedPanel.getWidgets().stream().filter((widget) ->
				widget.getPositionZ() > getPositionZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		setFocus(!optional.isPresent() && isWithinBounds(mouseX, mouseY));
		return getFocus();
	}

	public WAnchor getAnchor() {
		return anchor;
	}

	public void setAnchor(WAnchor anchor) {
		this.anchor = anchor;
	}

	public void center() {
		this.positionX = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - sizeX / 2;
		this.positionY = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - sizeY / 2;
	}

	public void draw() {
	}

	@Override
	public void tick() {
	}

	public WColor getColor(int number) {
		return ThemeRegistry.get(getTheme(), getClass()).get(number);
	}

	public static class Theme {
		private Map<Integer, WColor> colors = new HashMap<>();

		public void add(int number, WColor color) {
			colors.put(number, color);
		}

		public WColor get(int number) {
			return colors.get(number);
		}
	}
}
