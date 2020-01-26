package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
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
	protected Runnable runnableOnCharTyped;
	protected Runnable runnableOnMouseClicked;
	protected Runnable runnableOnKeyPressed;
	protected Runnable runnableOnKeyReleased;
	protected Runnable runnableOnFocusGained;
	protected Runnable runnableOnFocusReleased;
	protected Runnable runnableOnDrawTooltip;
	protected Runnable runnableOnMouseReleased;
	protected Runnable runnableOnMouseMoved;
	protected Runnable runnableOnMouseDragged;
	protected Runnable runnableOnMouseScrolled;
	protected Runnable runnableOnAlign;
	protected String theme = "light";
	protected Text label = new LiteralText("");

	public WWidget() {
	}

	public static Theme of(Map<String, String> rawTheme) {
		return null;
	}

	public void onCharTyped(char character) {
		if (runnableOnCharTyped != null) {
			runnableOnCharTyped.run();
		}
	}

	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (runnableOnKeyPressed != null) {
			runnableOnKeyPressed.run();
		}
	}

	public Runnable getOnKeyPressed() {
		return runnableOnKeyPressed;
	}

	public void setOnKeyPressed(Runnable linkedRunnableOnKeyPressed) {
		this.runnableOnKeyPressed = linkedRunnableOnKeyPressed;
	}

	public void onKeyReleased(int keyReleased) {
		if (runnableOnKeyReleased != null) {
			runnableOnKeyReleased.run();
		}
	}

	public Runnable getOnKeyReleased() {
		return runnableOnKeyReleased;
	}

	public void setOnKeyReleased(Runnable linkedRunnableOnKeyReleased) {
		this.runnableOnKeyReleased = linkedRunnableOnKeyReleased;
	}

	public void onFocusGained() {
		if (runnableOnFocusGained != null) {
			runnableOnFocusGained.run();
		}
	}

	public Runnable getOnFocusGained() {
		return runnableOnFocusGained;
	}

	public void setOnFocusGained(Runnable linkedRunnableOnFocusGained) {
		this.runnableOnFocusGained = linkedRunnableOnFocusGained;
	}

	public void onFocusReleased() {
		if (runnableOnFocusReleased != null) {
			runnableOnFocusReleased.run();
		}
	}

	public Runnable getOnFocusReleased() {
		return runnableOnFocusReleased;
	}

	public void setOnFocusReleased(Runnable linkedRunnableOnFocusReleased) {
		this.runnableOnFocusReleased = linkedRunnableOnFocusReleased;
	}

	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (runnableOnMouseReleased != null) {
			runnableOnMouseReleased.run();
		}
	}

	public Runnable getOnMouseReleased() {
		return runnableOnMouseReleased;
	}

	public void setOnMouseReleased(Runnable linkedRunnable) {
		this.runnableOnMouseReleased = linkedRunnable;
	}

	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (runnableOnMouseClicked != null) {
			runnableOnMouseClicked.run();
		}
	}

	public Runnable getOnMouseClicked() {
		return runnableOnMouseClicked;
	}

	public void setOnMouseClicked(Runnable linkedRunnable) {
		this.runnableOnMouseClicked = linkedRunnable;
	}

	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (runnableOnMouseDragged != null) {
			runnableOnMouseDragged.run();
		}
	}

	public Runnable getOnMouseDragged() {
		return runnableOnMouseDragged;
	}

	public void setOnMouseDragged(Runnable linkedRunnable) {
		this.runnableOnMouseDragged = linkedRunnable;
	}

	public void onMouseMoved(double mouseX, double mouseY) {
		if (runnableOnMouseMoved != null) {
			runnableOnMouseMoved.run();
		}
	}

	public Runnable getOnMouseMoved() {
		return runnableOnMouseMoved;
	}

	public void setOnMouseMoved(Runnable linkedRunnable) {
		this.runnableOnMouseMoved = linkedRunnable;
	}

	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (runnableOnMouseScrolled != null) {
			runnableOnMouseScrolled.run();
		}
	}

	public Runnable getOnMouseScrolled() {
		return runnableOnMouseScrolled;
	}

	public void setOnMouseScrolled(Runnable linkedRunnable) {
		this.runnableOnMouseScrolled = linkedRunnable;
	}

	public void onDrawTooltip() {
		if (runnableOnDrawTooltip != null) {
			runnableOnDrawTooltip.run();
		}
	}

	public Runnable getOnDrawTooltip() {
		return runnableOnDrawTooltip;
	}

	public void setOnDrawTooltip(Runnable linkedRunnableOnDrawTooltip) {
		this.runnableOnDrawTooltip = linkedRunnableOnDrawTooltip;
	}

	public void onAlign() {
		if (runnableOnAlign != null) {
			runnableOnAlign.run();
		}
	}

	public Runnable getOnAlign() {
		return runnableOnAlign;
	}

	public void setOnAlign(Runnable linkedRunnableOnAlign) {
		this.runnableOnAlign = linkedRunnableOnAlign;
	}

	public boolean hasLabel() {
		return !label.asFormattedString().isEmpty();
	}

	public Text getLabel() {
		return label;
	}

	public void setLabel(Text label) {
		this.label = label;
	}

	public boolean isLabelShadowed() {
		return isLabelShadowed;
	}

	public void setLabelShadow(boolean isLabelShadowed) {
		this.isLabelShadowed = isLabelShadowed;
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

	public WSize getSize() {
		return size;
	}

	public void setSize(WSize size) {
		this.size = size;
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

	public WPosition getPosition() {
		return position;
	}

	public void setPosition(WPosition position) {
		this.position = position;
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

	public void setFocus(boolean hasFocus) {
		if (!getFocus() && hasFocus) {
			onFocusGained();
		}
		if (getFocus() && !hasFocus) {
			onFocusReleased();
		}
		this.hasFocus = hasFocus;
	}

	public boolean scanFocus(int mouseX, int mouseY) {
		if (isHidden()) {
			return false;
		}

		Optional<? extends WWidget> optional = linkedInterface.getWidgets().stream().filter((widget) ->
				widget.getZ() > getZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		setFocus(!optional.isPresent() && isWithinBounds(mouseX, mouseY));
		return getFocus();
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

	public void center() {
		if (this instanceof WInterface && ((WInterface) this).isClient() || getInterface() != null && getInterface().isClient()) {
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
