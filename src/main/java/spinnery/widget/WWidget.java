package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import spinnery.registry.ThemeRegistry;
import spinnery.registry.WidgetRegistry;
import spinnery.widget.api.*;
import spinnery.widget.api.listener.*;

import static spinnery.registry.ThemeRegistry.DEFAULT_THEME;

@SuppressWarnings({"unchecked", "rawtypes"})
public class WWidget implements Tickable, Comparable<WWidget>, WLayoutElement, WThemable, WStyleProvider {
	protected WInterface linkedInterface;
	protected WLayoutElement parent;

	protected WPosition position = WPosition.ORIGIN;
	protected WSize size = WSize.of(0, 0);

	protected Text label = new LiteralText("");
	protected boolean isHidden = false;
	protected boolean hasFocus = false;

	protected WCharTypeListener runnableOnCharTyped;
	protected WMouseClickListener runnableOnMouseClicked;
	protected WKeyPressListener runnableOnKeyPressed;
	protected WKeyReleaseListener runnableOnKeyReleased;
	protected WFocusGainListener runnableOnFocusGained;
	protected WFocusLossListener runnableOnFocusReleased;
	protected WTooltipDrawListener runnableOnDrawTooltip;
	protected WMouseReleaseListener runnableOnMouseReleased;
	protected WMouseMoveListener runnableOnMouseMoved;
	protected WMouseDragListener runnableOnMouseDragged;
	protected WMouseScrollListener runnableOnMouseScrolled;
	protected WAlignListener runnableOnAlign;

	protected Identifier theme;
	protected WStyle styleOverrides;

	public WWidget() {
	}

	////// SHARED //////

	public void setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
	}

	public WInterface getInterface() {
		return linkedInterface;
	}

	@Override
	public void tick() {
	}

	////// CLIENTSIDE //////

	// Common functionality

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setLabel(Text label) {
		this.label = label;
		onLayoutChange();
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setLabel(String label) {
		this.label = new LiteralText(label);
		onLayoutChange();
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setHidden(boolean isHidden) {
		this.isHidden = isHidden;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setParent(WLayoutElement parent) {
		this.parent = parent;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public boolean hasLabel() {
		return !label.asFormattedString().isEmpty();
	}

	@Environment(EnvType.CLIENT)
	public Text getLabel() {
		return label;
	}

	@Environment(EnvType.CLIENT)
	public boolean isLabelShadowed() {
		return getStyle().asBoolean("label.shadow");
	}

	@Environment(EnvType.CLIENT)
	public boolean isHidden() {
		return isHidden;
	}

	@Environment(EnvType.CLIENT)
	public WLayoutElement getParent() {
		return parent;
	}

	// Alignment helpers

	@Environment(EnvType.CLIENT)
	public void align() {
		position.align();
	}

	@Environment(EnvType.CLIENT)
	public void center() {
		setPosition(WPosition.of(getParent(),
				getParent().getWidth() / 2 - getWidth() / 2,
				getParent().getHeight() / 2 - getHeight() / 2,
				getPosition().getOffsetZ()));
	}

	@Environment(EnvType.CLIENT)
	public void centerX() {
		setPosition(WPosition.of(getParent(),
				getParent().getWidth() / 2 - getWidth() / 2,
				getPosition().getOffsetY(),
				getPosition().getOffsetZ()));
	}

	@Environment(EnvType.CLIENT)
	public void centerY() {
		setPosition(WPosition.of(getParent(),
				getPosition().getOffsetX(),
				getParent().getHeight() / 2 - getHeight() / 2,
				getPosition().getOffsetZ()));
	}

	// Focus helpers

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

		setFocus(isWithinBounds(mouseX, mouseY));
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

	// WStyleProvider

	@Override
	@Environment(EnvType.CLIENT)
	public WStyle getStyle() {
		if (styleOverrides == null) {
			styleOverrides = ThemeRegistry.getStyle(getTheme(), WidgetRegistry.getId(getClass()));
		}
		return styleOverrides;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WWidget> W overrideStyle(String property, Object value) {
		getStyle().override(property, value);
		return (W) this;
	}

	// WThemable

	@Override
	@Environment(EnvType.CLIENT)
	public Identifier getTheme() {
		if (theme != null) return theme;
		if (parent != null && parent instanceof WThemable) return ((WThemable) parent).getTheme();
		if (linkedInterface != null && linkedInterface.getTheme() != null) return linkedInterface.getTheme();
		return DEFAULT_THEME;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setTheme(Identifier theme) {
		this.theme = theme;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setTheme(String theme) {
		return setTheme(new Identifier(theme));
	}

	// WLayoutElement

	@Override
	@Environment(EnvType.CLIENT)
	public void draw() {
	}

	// WPositioned

	@Environment(EnvType.CLIENT)
	public WPosition getPosition() {
		return position;
	}

	@Environment(EnvType.CLIENT)
	public int getX() {
		return position.getX();
	}

	@Environment(EnvType.CLIENT)
	public int getY() {
		return position.getY();
	}

	@Environment(EnvType.CLIENT)
	public int getZ() {
		return position.getZ();
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setPosition(WPosition position) {
		this.position = position;
		onLayoutChange();
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setX(int x) {
		return setPosition(WPosition.of(position).setX(x));
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setY(int y) {
		return setPosition(WPosition.of(position).setY(y));
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setZ(int z) {
		setPosition(WPosition.of(position).setZ(z));
		return (T) this;
	}

	// WSized

	@Environment(EnvType.CLIENT)
	public WSize getSize() {
		return size;
	}

	@Environment(EnvType.CLIENT)
	public int getHeight() {
		return size.getHeight();
	}

	@Environment(EnvType.CLIENT)
	public int getWidth() {
		return size.getWidth();
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setSize(WSize size) {
		this.size = size;
		onLayoutChange();
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setWidth(int width) {
		return setSize(WSize.of(size).setWidth(width));
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setHeight(int height) {
		return setSize(WSize.of(size).setHeight(height));
	}

	// Event runners

	@Environment(EnvType.CLIENT)
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (runnableOnKeyPressed != null) {
			runnableOnKeyPressed.event(this, keyPressed, character, keyModifier);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onKeyReleased(int keyReleased, int character, int keyModifier) {
		if (runnableOnKeyReleased != null) {
			runnableOnKeyReleased.event(this, keyReleased, character, keyModifier);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onCharTyped(char character, int keyCode) {
		if (runnableOnCharTyped != null) {
			runnableOnCharTyped.event(this, character, keyCode);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onFocusGained() {
		if (runnableOnFocusGained != null) {
			runnableOnFocusGained.event(this);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onFocusReleased() {
		if (runnableOnFocusReleased != null) {
			runnableOnFocusReleased.event(this);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (runnableOnMouseReleased != null) {
			runnableOnMouseReleased.event(this, mouseX, mouseY, mouseButton);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (runnableOnMouseClicked != null) {
			runnableOnMouseClicked.event(this, mouseX, mouseY, mouseButton);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (runnableOnMouseDragged != null) {
			runnableOnMouseDragged.event(this, mouseX, mouseY, mouseButton, deltaX, deltaY);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onMouseMoved(int mouseX, int mouseY) {
		if (runnableOnMouseMoved != null) {
			runnableOnMouseMoved.event(this, mouseX, mouseY);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (runnableOnMouseScrolled != null) {
			runnableOnMouseScrolled.event(this, mouseX, mouseY, deltaY);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onDrawTooltip(int mouseX, int mouseY) {
		if (runnableOnDrawTooltip != null) {
			runnableOnDrawTooltip.event(this, mouseX, mouseY);
		}
	}

	@Environment(EnvType.CLIENT)
	public void onAlign() {
		if (runnableOnAlign != null) {
			runnableOnAlign.event(this);
		}
		onLayoutChange();
	}

	// Event runner setters

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnFocusGained(WFocusGainListener<T> linkedRunnable) {
		this.runnableOnFocusGained = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnFocusReleased(WFocusLossListener<T> linkedRunnable) {
		this.runnableOnFocusReleased = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnKeyPressed(WKeyPressListener<T> linkedRunnable) {
		this.runnableOnKeyPressed = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnCharTyped(WCharTypeListener<T> linkedRunnable) {
		this.runnableOnCharTyped = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnKeyReleased(WKeyReleaseListener<T> linkedRunnable) {
		this.runnableOnKeyReleased = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnMouseClicked(WMouseClickListener<T> linkedRunnable) {
		this.runnableOnMouseClicked = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnMouseDragged(WMouseDragListener<T> linkedRunnable) {
		this.runnableOnMouseDragged = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnMouseMoved(WMouseMoveListener<T> linkedRunnable) {
		this.runnableOnMouseMoved = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnMouseScrolled(WMouseScrollListener<T> linkedRunnable) {
		this.runnableOnMouseScrolled = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnMouseReleased(WMouseReleaseListener<T> linkedRunnable) {
		this.runnableOnMouseReleased = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnDrawTooltip(WTooltipDrawListener<T> linkedRunnable) {
		this.runnableOnDrawTooltip = linkedRunnable;
		return (T) this;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> T setOnAlign(WAlignListener<T> linkedRunnable) {
		this.runnableOnAlign = linkedRunnable;
		return (T) this;
	}

	// Event runner getters

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WFocusGainListener<T> getOnFocusGained() {
		return runnableOnFocusGained;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WFocusLossListener<T> getOnFocusReleased() {
		return runnableOnFocusReleased;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WKeyPressListener<T> getOnKeyPressed() {
		return runnableOnKeyPressed;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WCharTypeListener<T> getOnCharTyped() {
		return runnableOnCharTyped;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WKeyReleaseListener<T> getOnKeyReleased() {
		return runnableOnKeyReleased;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WMouseClickListener<T> getOnMouseClicked() {
		return runnableOnMouseClicked;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WMouseDragListener<T> getOnMouseDragged() {
		return runnableOnMouseDragged;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WMouseMoveListener<T> getOnMouseMoved() {
		return runnableOnMouseMoved;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WMouseScrollListener<T> getOnMouseScrolled() {
		return runnableOnMouseScrolled;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WMouseReleaseListener<T> getOnMouseReleased() {
		return runnableOnMouseReleased;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WTooltipDrawListener<T> getOnDrawTooltip() {
		return runnableOnDrawTooltip;
	}

	@Environment(EnvType.CLIENT)
	public <T extends WWidget> WAlignListener<T> getOnAlign() {
		return runnableOnAlign;
	}

	// Comparable

	@Override
	public int compareTo(WWidget o) {
		return Integer.compare(o.getZ(), getZ());
	}
}
