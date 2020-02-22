package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import spinnery.registry.ThemeRegistry;
import spinnery.registry.WidgetRegistry;
import spinnery.util.EventUtilities;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.Style;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WStyleProvider;
import spinnery.widget.api.WThemable;
import spinnery.widget.api.listener.WAlignListener;
import spinnery.widget.api.listener.WCharTypeListener;
import spinnery.widget.api.listener.WFocusGainListener;
import spinnery.widget.api.listener.WFocusLossListener;
import spinnery.widget.api.listener.WKeyPressListener;
import spinnery.widget.api.listener.WKeyReleaseListener;
import spinnery.widget.api.listener.WMouseClickListener;
import spinnery.widget.api.listener.WMouseDragListener;
import spinnery.widget.api.listener.WMouseMoveListener;
import spinnery.widget.api.listener.WMouseReleaseListener;
import spinnery.widget.api.listener.WMouseScrollListener;
import spinnery.widget.api.listener.WTooltipDrawListener;

import static spinnery.registry.ThemeRegistry.DEFAULT_THEME;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class WAbstractWidget implements Tickable,
		WLayoutElement, WThemable, WStyleProvider, WEventListener {
	protected WInterface linkedInterface;
	protected WLayoutElement parent;

	protected Position position = Position.origin();
	protected Size size = Size.of(0, 0);

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
	protected Style styleOverrides = new Style();

	public WAbstractWidget() {
	}

	////// SHARED //////

	public WInterface getInterface() {
		return linkedInterface;
	}

	public <W extends WAbstractWidget> W setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
		return (W) this;
	}

	@Override
	public void tick() {
	}

	////// CLIENTSIDE //////

	// Common functionality

	@Environment(EnvType.CLIENT)
	public boolean hasLabel() {
		return !label.asFormattedString().isEmpty();
	}

	@Environment(EnvType.CLIENT)
	public Text getLabel() {
		return label;
	}

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
	public boolean isLabelShadowed() {
		return getStyle().asBoolean("label.shadow");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Style getStyle() {
		Identifier widgetId = WidgetRegistry.getId(getClass());
		if (widgetId == null) {
			Class superClass = getClass().getSuperclass();
			while (superClass != Object.class) {
				widgetId = WidgetRegistry.getId(superClass);
				if (widgetId != null) break;
				superClass = superClass.getSuperclass();
			}
		}
		return Style.of(ThemeRegistry.getStyle(getTheme(), widgetId)).mergeFrom(styleOverrides);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Identifier getTheme() {
		if (theme != null) return theme;
		if (parent != null && parent instanceof WThemable) return ((WThemable) parent).getTheme();
		if (linkedInterface != null && linkedInterface.getTheme() != null)
			return linkedInterface.getTheme();
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

	// Alignment helpers

	@Environment(EnvType.CLIENT)
	public void align() {
	}

	@Environment(EnvType.CLIENT)
	public void center() {
		setPosition(Position.of(getPosition())
				.setX(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2)
				.setY(getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2));
	}

	@Environment(EnvType.CLIENT)
	public Position getPosition() {
		return position;
	}

	@Environment(EnvType.CLIENT)
	public WLayoutElement getParent() {
		return parent;
	}

	@Override
	public void onLayoutChange() {
		if (parent != null) parent.onLayoutChange();
	}

	// Focus helpers

	public <W extends WAbstractWidget> W setParent(WLayoutElement parent) {
		this.parent = parent;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public int getWidth() {
		return size.getWidth();
	}

	@Environment(EnvType.CLIENT)
	public int getHeight() {
		return size.getHeight();
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setHeight(int height) {
		return setSize(Size.of(size).setHeight(height));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setWidth(int width) {
		return setSize(Size.of(size).setWidth(width));
	}

	// WStyleProvider

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setPosition(Position position) {
		if (!this.position.equals(position)) {
			this.position = position;
			onLayoutChange();
		}
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public void centerX() {
		setPosition(Position.of(getPosition())
				.setX(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2));
	}

	// WThemable

	@Environment(EnvType.CLIENT)
	public void centerY() {
		setPosition(Position.of(getPosition())
				.setY(getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W overrideStyle(String property, Object value) {
		styleOverrides.override(property, value);
		return (W) this;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void draw() {
	}

	// WLayoutElement

	@Environment(EnvType.CLIENT)
	public Size getSize() {
		return size;
	}

	// WPositioned

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setSize(Size size) {
		if (!this.size.equals(size)) {
			this.size = size;
			onLayoutChange();
		}
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveKeyboard(widget))
					widget.onKeyPressed(keyPressed, character, keyModifier);
			}
		}
		if (runnableOnKeyPressed != null) {
			runnableOnKeyPressed.event(this, keyPressed, character, keyModifier);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onKeyReleased(int keyReleased, int character, int keyModifier) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveKeyboard(widget))
					widget.onKeyReleased(keyReleased, character, keyModifier);
			}
		}
		if (runnableOnKeyReleased != null) {
			runnableOnKeyReleased.event(this, keyReleased, character, keyModifier);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onCharTyped(char character, int keyCode) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveKeyboard(widget))
					widget.onCharTyped(character, keyCode);
			}
		}
		if (runnableOnCharTyped != null) {
			runnableOnCharTyped.event(this, character, keyCode);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onFocusGained() {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onFocusGained();
			}
		}
		if (runnableOnFocusGained != null && isFocused()) {
			runnableOnFocusGained.event(this);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onFocusReleased() {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onFocusReleased();
			}
		}
		if (runnableOnFocusReleased != null && !isFocused()) {
			runnableOnFocusReleased.event(this);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			}
		}
		if (runnableOnMouseReleased != null) {
			runnableOnMouseReleased.event(this, mouseX, mouseY, mouseButton);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget))
					widget.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		if (runnableOnMouseClicked != null) {
			runnableOnMouseClicked.event(this, mouseX, mouseY, mouseButton);
		}
	}

	// WSized

	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget))
					widget.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
			}
		}
		if (runnableOnMouseDragged != null) {
			runnableOnMouseDragged.event(this, mouseX, mouseY, mouseButton, deltaX, deltaY);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseMoved(int mouseX, int mouseY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (widget instanceof WAbstractWidget) {
					WAbstractWidget updateWidget = ((WAbstractWidget) widget);
					boolean then = updateWidget.hasFocus;
					updateWidget.updateFocus(mouseX, mouseY);
					boolean now = updateWidget.hasFocus;

					if (then && !now) {
						updateWidget.onFocusReleased();
					} else if (!then && now) {
						updateWidget.onFocusGained();
					}

				}
				if (EventUtilities.canReceiveMouse(widget)) widget.onMouseMoved(mouseX, mouseY);
			}
		}
		if (runnableOnMouseMoved != null) {
			runnableOnMouseMoved.event(this, mouseX, mouseY);
		}
	}

	@Environment(EnvType.CLIENT)
	public boolean updateFocus(int mouseX, int mouseY) {
		if (isHidden()) {
			return false;
		}

		setFocus(isWithinBounds(mouseX, mouseY));
		return isFocused();
	}

	@Environment(EnvType.CLIENT)
	public boolean isHidden() {
		return isHidden;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setHidden(boolean isHidden) {
		this.isHidden = isHidden;
		setFocus(false);
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public void setFocus(boolean hasFocus) {
		if (!isFocused() && hasFocus) {
			this.hasFocus = hasFocus;
		}
		if (isFocused() && !hasFocus) {
			this.hasFocus = hasFocus;
		}
	}

	// Event runners

	@Environment(EnvType.CLIENT)
	public boolean isFocused() {
		return hasFocus;
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
	public <W extends WAbstractWidget> W setZ(int z) {
		return setPosition(Position.of(position).setZ(z));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setY(int y) {
		return setPosition(Position.of(position).setY(y));
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setX(int x) {
		return setPosition(Position.of(position).setX(x));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget))
					widget.onMouseScrolled(mouseX, mouseY, deltaY);
			}
		}
		if (runnableOnMouseScrolled != null) {
			runnableOnMouseScrolled.event(this, mouseX, mouseY, deltaY);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onDrawTooltip(int mouseX, int mouseY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onDrawTooltip(mouseX, mouseY);
			}
		}
		if (runnableOnDrawTooltip != null) {
			runnableOnDrawTooltip.event(this, mouseX, mouseY);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onAlign() {
		if (runnableOnAlign != null) {
			runnableOnAlign.event(this);
		}
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onAlign();
			}
		}
		onLayoutChange();
	}

	// Event runner setters

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WFocusGainListener<W> getOnFocusGained() {
		return runnableOnFocusGained;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnFocusGained(WFocusGainListener<W> linkedRunnable) {
		this.runnableOnFocusGained = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WFocusLossListener<W> getOnFocusReleased() {
		return runnableOnFocusReleased;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnFocusReleased(WFocusLossListener<W> linkedRunnable) {
		this.runnableOnFocusReleased = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WKeyPressListener<W> getOnKeyPressed() {
		return runnableOnKeyPressed;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnKeyPressed(WKeyPressListener<W> linkedRunnable) {
		this.runnableOnKeyPressed = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WCharTypeListener<W> getOnCharTyped() {
		return runnableOnCharTyped;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnCharTyped(WCharTypeListener<W> linkedRunnable) {
		this.runnableOnCharTyped = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WKeyReleaseListener<W> getOnKeyReleased() {
		return runnableOnKeyReleased;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnKeyReleased(WKeyReleaseListener<W> linkedRunnable) {
		this.runnableOnKeyReleased = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseClickListener<W> getOnMouseClicked() {
		return runnableOnMouseClicked;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseClicked(WMouseClickListener<W> linkedRunnable) {
		this.runnableOnMouseClicked = linkedRunnable;
		return (W) this;
	}

	// Event runner getters

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseDragListener<W> getOnMouseDragged() {
		return runnableOnMouseDragged;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseDragged(WMouseDragListener<W> linkedRunnable) {
		this.runnableOnMouseDragged = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseMoveListener<W> getOnMouseMoved() {
		return runnableOnMouseMoved;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseMoved(WMouseMoveListener<W> linkedRunnable) {
		this.runnableOnMouseMoved = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseScrollListener<W> getOnMouseScrolled() {
		return runnableOnMouseScrolled;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseScrolled(WMouseScrollListener<W> linkedRunnable) {
		this.runnableOnMouseScrolled = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WMouseReleaseListener<W> getOnMouseReleased() {
		return runnableOnMouseReleased;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnMouseReleased(WMouseReleaseListener<W> linkedRunnable) {
		this.runnableOnMouseReleased = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WTooltipDrawListener<W> getOnDrawTooltip() {
		return runnableOnDrawTooltip;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnDrawTooltip(WTooltipDrawListener<W> linkedRunnable) {
		this.runnableOnDrawTooltip = linkedRunnable;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> WAlignListener<W> getOnAlign() {
		return runnableOnAlign;
	}

	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setOnAlign(WAlignListener<W> linkedRunnable) {
		this.runnableOnAlign = linkedRunnable;
		return (W) this;
	}
}
