package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import spinnery.registry.ThemeRegistry;
import spinnery.registry.WidgetRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spinnery.registry.ThemeRegistry.DEFAULT_THEME;

public class WWidget implements Tickable, Comparable<WWidget> {
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

	protected Identifier theme;
	protected WStyle styleOverrides;

	protected Text label = new LiteralText("");

	public WWidget() {
	}

	public WStyle getStyle() {
		if (styleOverrides == null) {
			styleOverrides = ThemeRegistry.getStyle(getTheme(), WidgetRegistry.getId(getClass()));
		}
		return styleOverrides;
	}

	public <T> void overrideStyle(String property, T value) {
		getStyle().override(property, value);
	}

	@Environment(EnvType.CLIENT)
	public void onCharTyped(char character) {
		if (runnableOnCharTyped != null) {
			runnableOnCharTyped.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (runnableOnKeyPressed != null) {
			runnableOnKeyPressed.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnKeyPressed() {
		return runnableOnKeyPressed;
	}

	@Environment(EnvType.CLIENT)
	public void setOnKeyPressed(Runnable linkedRunnableOnKeyPressed) {
		this.runnableOnKeyPressed = linkedRunnableOnKeyPressed;
	}

	@Environment(EnvType.CLIENT)
	public void onKeyReleased(int keyReleased) {
		if (runnableOnKeyReleased != null) {
			runnableOnKeyReleased.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public void onFocusGained() {
		if (runnableOnFocusGained != null) {
			runnableOnFocusGained.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public void onFocusReleased() {
		if (runnableOnFocusReleased != null) {
			runnableOnFocusReleased.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnKeyReleased() {
		return runnableOnKeyReleased;
	}

	@Environment(EnvType.CLIENT)
	public void setOnKeyReleased(Runnable linkedRunnableOnKeyReleased) {
		this.runnableOnKeyReleased = linkedRunnableOnKeyReleased;
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnFocusGained() {
		return runnableOnFocusGained;
	}

	@Environment(EnvType.CLIENT)
	public void setOnFocusGained(Runnable linkedRunnableOnFocusGained) {
		this.runnableOnFocusGained = linkedRunnableOnFocusGained;
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnFocusReleased() {
		return runnableOnFocusReleased;
	}

	@Environment(EnvType.CLIENT)
	public void setOnFocusReleased(Runnable linkedRunnableOnFocusReleased) {
		this.runnableOnFocusReleased = linkedRunnableOnFocusReleased;
	}

	@Environment(EnvType.CLIENT)
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (runnableOnMouseReleased != null) {
			runnableOnMouseReleased.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnMouseReleased() {
		return runnableOnMouseReleased;
	}

	@Environment(EnvType.CLIENT)
	public void setOnMouseReleased(Runnable linkedRunnable) {
		this.runnableOnMouseReleased = linkedRunnable;
	}

	@Environment(EnvType.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (runnableOnMouseClicked != null) {
			runnableOnMouseClicked.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnMouseClicked() {
		return runnableOnMouseClicked;
	}

	@Environment(EnvType.CLIENT)
	public void setOnMouseClicked(Runnable linkedRunnable) {
		this.runnableOnMouseClicked = linkedRunnable;
	}

	@Environment(EnvType.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (runnableOnMouseDragged != null) {
			runnableOnMouseDragged.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnMouseDragged() {
		return runnableOnMouseDragged;
	}

	@Environment(EnvType.CLIENT)
	public void setOnMouseDragged(Runnable linkedRunnable) {
		this.runnableOnMouseDragged = linkedRunnable;
	}

	@Environment(EnvType.CLIENT)
	public void onMouseMoved(double mouseX, double mouseY) {
		if (runnableOnMouseMoved != null) {
			runnableOnMouseMoved.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnMouseMoved() {
		return runnableOnMouseMoved;
	}

	@Environment(EnvType.CLIENT)
	public void setOnMouseMoved(Runnable linkedRunnable) {
		this.runnableOnMouseMoved = linkedRunnable;
	}

	@Environment(EnvType.CLIENT)
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (runnableOnMouseScrolled != null) {
			runnableOnMouseScrolled.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnMouseScrolled() {
		return runnableOnMouseScrolled;
	}

	@Environment(EnvType.CLIENT)
	public void setOnMouseScrolled(Runnable linkedRunnable) {
		this.runnableOnMouseScrolled = linkedRunnable;
	}

	@Environment(EnvType.CLIENT)
	public void onDrawTooltip() {
		if (runnableOnDrawTooltip != null) {
			runnableOnDrawTooltip.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnDrawTooltip() {
		return runnableOnDrawTooltip;
	}

	@Environment(EnvType.CLIENT)
	public void setOnDrawTooltip(Runnable linkedRunnableOnDrawTooltip) {
		this.runnableOnDrawTooltip = linkedRunnableOnDrawTooltip;
	}

	@Environment(EnvType.CLIENT)
	public void onAlign() {
		if (runnableOnAlign != null) {
			runnableOnAlign.run();
		}
	}

	@Environment(EnvType.CLIENT)
	public Runnable getOnAlign() {
		return runnableOnAlign;
	}

	@Environment(EnvType.CLIENT)
	public void setOnAlign(Runnable linkedRunnableOnAlign) {
		this.runnableOnAlign = linkedRunnableOnAlign;
	}

	@Environment(EnvType.CLIENT)
	public Text getLabel() {
		return label;
	}

	@Environment(EnvType.CLIENT)
	public void setLabel(Text label) {
		this.label = label;
	}

	@Environment(EnvType.CLIENT)
	public boolean hasLabel() {
		return !label.asFormattedString().isEmpty();
	}

	@Environment(EnvType.CLIENT)
	public boolean isLabelShadowed() {
		return isLabelShadowed;
	}

	@Environment(EnvType.CLIENT)
	public void setLabelShadow(boolean isLabelShadowed) {
		this.isLabelShadowed = isLabelShadowed;
	}

	@Environment(EnvType.CLIENT)
	public WSize getSize() {
		return size;
	}

	@Environment(EnvType.CLIENT)
	public void setSize(WSize size) {
		this.size = size;
	}

	@Environment(EnvType.CLIENT)
	public int getX() {
		return position.getX();
	}

	@Environment(EnvType.CLIENT)
	public void setX(int x) {
		position.setX(x);
	}

	@Environment(EnvType.CLIENT)
	public int getY() {
		return position.getY();
	}

	@Environment(EnvType.CLIENT)
	public void setY(int y) {
		position.setY(y);
	}

	@Environment(EnvType.CLIENT)
	public int getZ() {
		return position.getZ();
	}

	@Environment(EnvType.CLIENT)
	public void setZ(int z) {
		position.setZ(z);
	}

	@Environment(EnvType.CLIENT)
	public int getWidth() {
		return size.getX();
	}

	@Environment(EnvType.CLIENT)
	public void setWidth(int width) {
		size.setX(width);
	}

	@Environment(EnvType.CLIENT)
	public int getHeight() {
		return size.getY();
	}

	@Environment(EnvType.CLIENT)
	public void setHeight(int height) {
		size.setY(height);
	}

	@Environment(EnvType.CLIENT)
	public WPosition getPosition() {
		return position;
	}

	@Environment(EnvType.CLIENT)
	public void setPosition(WPosition position) {
		this.position = position;
	}

	@Environment(EnvType.CLIENT)
	public int getWidth(int number) {
		return size.getX(number);
	}

	@Environment(EnvType.CLIENT)
	public void setWidth(int number, int width) {
		size.setX(number, width);
	}

	@Environment(EnvType.CLIENT)
	public int getHeight(int number) {
		return size.getY(number);
	}

	@Environment(EnvType.CLIENT)
	public void setHeight(int number, int height) {
		size.setY(number, height);
	}

	@Environment(EnvType.CLIENT)
	public boolean isWithinBounds(int positionX, int positionY) {
		return isWithinBounds(positionX, positionY, 0);
	}

	@Environment(EnvType.CLIENT)
	public boolean isWithinBounds(int positionX, int positionY, int tolerance) {
		return positionX + tolerance > getX()
				&& positionX - tolerance < getX() + getWidth()
				&& positionY + tolerance > getY()
				&& positionY - tolerance < getY() + getHeight();
	}

	@Environment(EnvType.CLIENT)
	public boolean updateFocus(int mouseX, int mouseY) {
		if (isHidden()) {
			return false;
		}

		Optional<? extends WWidget> optional = linkedInterface.getWidgets().stream().filter((widget) ->
				widget.getZ() > getZ() && widget.isWithinBounds(mouseX, mouseY)
		).findAny();
		setFocus(!optional.isPresent() && isWithinBounds(mouseX, mouseY));
		return getFocus();
	}

	@Environment(EnvType.CLIENT)
	public boolean getFocus() {
		return hasFocus;
	}

	@Environment(EnvType.CLIENT)
	public void setFocus(boolean hasFocus) {
		if (!getFocus() && hasFocus) {
			onFocusGained();
		}
		if (getFocus() && !hasFocus) {
			onFocusReleased();
		}
		this.hasFocus = hasFocus;
	}

	@Environment(EnvType.CLIENT)
	public boolean isHidden() {
		return isHidden;
	}

	@Environment(EnvType.CLIENT)
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@Environment(EnvType.CLIENT)
	public void align() {
		position.align();
	}

	@Environment(EnvType.CLIENT)
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

	public WInterface getInterface() {
		return linkedInterface;
	}

	public void setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
	}

	@Override
	public void tick() {
	}

	@Environment(EnvType.CLIENT)
	public void draw() {
	}

	@Environment(EnvType.CLIENT)
	public Identifier getTheme() {
		if (theme != null) return theme;
		if (linkedInterface != null && linkedInterface.theme != null) return linkedInterface.theme;
		return DEFAULT_THEME;
	}

	@Environment(EnvType.CLIENT)
	public void setTheme(Identifier theme) {
		this.theme = theme;
	}

	@Override
	public int compareTo(WWidget o) {
		return Integer.compare(o.getZ(), getZ());
	}

	@Environment(EnvType.CLIENT)
	public static class Theme {
		private Map<Object, Object> resources = new HashMap<>();

		public void put(Object objectA, Object objectB) {
			resources.put(objectA, objectB);
		}

		public Object get(Object objectA) {
			return resources.get(objectA);
		}
	}
}
