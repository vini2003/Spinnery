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
public abstract class WAbstractWidget implements Tickable, Comparable<WAbstractWidget>, WLayoutElement, WThemable, WStyleProvider {
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
	protected WStyle styleOverrides = new WStyle();

	public WAbstractWidget() {
	}

	////// SHARED //////

	public <W extends WAbstractWidget> W setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
		return (W) this;
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
	public <W extends WAbstractWidget> W setLabel(Text label) {
		this.label = label;
		onLayoutChange();
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setLabel(String label) {
		this.label = new LiteralText(label);
		onLayoutChange();
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setHidden(boolean isHidden) {
		this.isHidden = isHidden;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setParent(WLayoutElement parent) {
		this.parent = parent;
		return (W) this;
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
		Identifier widgetId = WidgetRegistry.getId(getClass());
		if (widgetId == null) {
			Class superClass = getClass().getSuperclass();
			while (superClass != Object.class) {
				widgetId = WidgetRegistry.getId(superClass);
				if (widgetId != null) break;
				superClass = superClass.getSuperclass();
			}
		}
		return WStyle.of(ThemeRegistry.getStyle(getTheme(), widgetId)).mergeFrom(styleOverrides);
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W overrideStyle(String property, Object value) {
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
	public <W extends WAbstractWidget> W setTheme(Identifier theme) {
		this.theme = theme;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setTheme(String theme) {
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
	public <W extends WAbstractWidget> W setPosition(WPosition position) {
		this.position = position;
		onLayoutChange();
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setX(int x) {
		return setPosition(WPosition.of(position).setX(x));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setY(int y) {
		return setPosition(WPosition.of(position).setY(y));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setZ(int z) {
		setPosition(WPosition.of(position).setZ(z));
		return (W) this;
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
	public <W extends WAbstractWidget> W setSize(WSize size) {
		this.size = size;
		onLayoutChange();
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setWidth(int width) {
		return setSize(WSize.of(size).setWidth(width));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setHeight(int height) {
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
	public <W extends WAbstractWidget> W setOnFocusGained(WFocusGainListener<W> linkedRunnable) {
		this.runnableOnFocusGained = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnFocusReleased(WFocusLossListener<W> linkedRunnable) {
		this.runnableOnFocusReleased = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnKeyPressed(WKeyPressListener<W> linkedRunnable) {
		this.runnableOnKeyPressed = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnCharTyped(WCharTypeListener<W> linkedRunnable) {
		this.runnableOnCharTyped = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnKeyReleased(WKeyReleaseListener<W> linkedRunnable) {
		this.runnableOnKeyReleased = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseClicked(WMouseClickListener<W>linkedRunnable) {
		this.runnableOnMouseClicked = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseDragged(WMouseDragListener<W> linkedRunnable) {
		this.runnableOnMouseDragged = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseMoved(WMouseMoveListener<W> linkedRunnable) {
		this.runnableOnMouseMoved = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseScrolled(WMouseScrollListener<W> linkedRunnable) {
		this.runnableOnMouseScrolled = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseReleased(WMouseReleaseListener<W> linkedRunnable) {
		this.runnableOnMouseReleased = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnDrawTooltip(WTooltipDrawListener<W> linkedRunnable) {
		this.runnableOnDrawTooltip = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnAlign(WAlignListener<W> linkedRunnable) {
		this.runnableOnAlign = linkedRunnable;
		return (W) this;
	}

	// Event runner getters

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WFocusGainListener<W> getOnFocusGained() {
		return runnableOnFocusGained;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WFocusLossListener<W> getOnFocusReleased() {
		return runnableOnFocusReleased;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WKeyPressListener<W> getOnKeyPressed() {
		return runnableOnKeyPressed;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WCharTypeListener<W> getOnCharTyped() {
		return runnableOnCharTyped;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WKeyReleaseListener<W> getOnKeyReleased() {
		return runnableOnKeyReleased;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseClickListener<W>getOnMouseClicked() {
		return runnableOnMouseClicked;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseDragListener<W> getOnMouseDragged() {
		return runnableOnMouseDragged;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseMoveListener<W> getOnMouseMoved() {
		return runnableOnMouseMoved;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseScrollListener<W> getOnMouseScrolled() {
		return runnableOnMouseScrolled;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseReleaseListener<W> getOnMouseReleased() {
		return runnableOnMouseReleased;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WTooltipDrawListener<W> getOnDrawTooltip() {
		return runnableOnDrawTooltip;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WAlignListener<W> getOnAlign() {
		return runnableOnAlign;
	}

	// Comparable

	@Override
	public int compareTo(WAbstractWidget o) {
		return Integer.compare(o.getZ(), getZ());
	}
}
