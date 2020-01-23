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
	protected WInterface linkedInterface;
	protected WPosition position = WPosition.of(WType.FREE, 0, 0, 0);
	protected WSize size = WSize.of(0, 0);
	protected boolean isHidden = false;
	protected boolean hasFocus = false;
	protected boolean isLabelShadowed = false;
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
	protected Runnable linkedRunnableOnAlign;
	private String theme = "light";
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

	public void onAlign() {
		if (linkedRunnableOnAlign != null) {
			linkedRunnableOnAlign.run();
		}
	}

	public Runnable getOnAlign() {
		return linkedRunnableOnAlign;
	}

	public void setOnAlign(Runnable linkedRunnableOnAlign) {
		this.linkedRunnableOnAlign = linkedRunnableOnAlign;
	}

	public Text getLabel() {
		return label;
	}

	public void setLabel(Text label) {
		this.label = label;
	}

	public void setLabelShadow(boolean isLabelShadowed) {
		this.isLabelShadowed = isLabelShadowed;
	}

	public boolean isLabelShadowed() {
		return isLabelShadowed;
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

	public WInterface getInterface() {
		return linkedInterface;
	}

	public void setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
	}

	public void setSize(WSize size) {
		this.size = size;
	}

	public WSize getSize() {
		return size;
	}

	public int getWidth() {
		return size.getX();
	}

	public void setWidth(int width) {
		size.setX(width);
	}

	public int getHeight() {
		return size.getY();
	}

	public void setHeight(int height) {
		size.setY(height);
	}

	public int getWidth(int number) {
		return size.getX(number);
	}

	public void setWidth(int number, int width) {
		size.setX(number, width);
	}

	public int getHeight(int number) {
		return size.getY(number);
	}

	public void setHeight(int number, int height) {
		size.setY(number, height);
	}

	public void setPosition(WPosition position) {
		this.position = position;
	}

	public WPosition get() {
		return position;
	}

	public int getX() {
		return position.getX();
	}

	public void setX(int x) {
		position.setX(x);
	}

	public int getY() {
		return position.getY();
	}

	public void setY(int y) {
		position.setY(y);
	}

	public int getZ() {
		return position.getZ();
	}

	public void setZ(int z) {
		position.setZ(z);
	}

	public void align() {
		position.align();
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
		return positionX > getX()
				&& positionX < getX() + getWidth()
				&& positionY > getY()
				&& positionY < getY() + getHeight();
	}

	public boolean scanFocus(int mouseX, int mouseY) {
		if (isHidden) {
			return false;
		}
		Optional<? extends WWidget> optional = linkedInterface.getWidgets().stream().filter((widget) ->
				widget.getZ() > getZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		setFocus(!optional.isPresent() && isWithinBounds(mouseX, mouseY));
		return getFocus();
	}

	public void center() {
		int x, y;
		if (position.type == WType.FREE) {
			x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - getWidth() / 2;
			y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - getHeight() / 2;
		} else {
			x = position.anchor.getWidth() / 2 - getWidth() / 2;
			y = position.anchor.getHeight() / 2 - getHeight() / 2;
		}
		this.position.set(x, y, getZ());
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
