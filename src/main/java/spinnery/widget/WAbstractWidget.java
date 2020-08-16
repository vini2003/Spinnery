package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import spinnery.Spinnery;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;
import spinnery.common.utilities.Themes;
import spinnery.common.utilities.Widgets;
import spinnery.common.utilities.Positions;
import spinnery.widget.api.*;

import java.util.List;

import static spinnery.common.utilities.Themes.DEFAULT_THEME;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class WAbstractWidget implements Tickable, WDrawableElement, WThemable, WStyleProvider, WEventListener {
	private WInterface linkedInterface;

	private WDrawableElement parent;

	private Position position = Position.origin();

	private Size size = Size.of(0, 0);

	private Text label = new LiteralText("");

	private boolean hidden = false;
	private boolean focused = false;
	private boolean held = false;

	private long heldSince = 0;

	private Identifier theme;
	private Style styleOverrides = new Style();

	public WInterface getInterface() {
		if (parent instanceof WAbstractWidget) {
			return ((WAbstractWidget) parent).getInterface();
		} else {
			return linkedInterface;
		}
	}

	public <W extends WAbstractWidget> W setInterface(WInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
		return (W) this;
	}

	/*
		Labelling
	 */

	public boolean hasLabel() {
		return !label.getString().isEmpty();
	}


	public Text getLabel() {
		return label;
	}


	public <W extends WAbstractWidget> W setLabel(Text label) {
		this.label = label;
		onLayoutChange();
		return (W) this;
	}


	public <W extends WAbstractWidget> W setLabel(String label) {
		this.label = new LiteralText(label);
		onLayoutChange();
		return (W) this;
	}


	public boolean isLabelShadowed() {
		return getStyle().asBoolean("label.shadow");
	}

	/*

	 */

	/*
		Styling
	 */

	@Override
	public Style getStyle() {
		Identifier widgetId = Widgets.getId(getClass());
		if (widgetId == null) {
			Class superClass = getClass().getSuperclass();
			while (superClass != Object.class) {
				widgetId = Widgets.getId(superClass);
				if (widgetId != null) break;
				superClass = superClass.getSuperclass();
			}
		}
		return Style.of(Themes.getStyle(getTheme(), widgetId)).mergeFrom(styleOverrides);
	}

	@Override

	public Identifier getTheme() {
		if (theme != null) return theme;
		if (parent != null && parent instanceof WThemable) return ((WThemable) parent).getTheme();
		if (linkedInterface != null && linkedInterface.getTheme() != null)
			return linkedInterface.getTheme();
		return DEFAULT_THEME;
	}


	public <W extends WAbstractWidget> W setTheme(Identifier theme) {
		this.theme = theme;
		return (W) this;
	}
	

	public <W extends WAbstractWidget> W setTheme(String theme) {
		return setTheme(new Identifier(theme));
	}

	public <W extends WAbstractWidget> W overrideStyle(String property, Object value) {
		styleOverrides.override(property, value);
		return (W) this;
	}

	/*

	 */

	/*
		Positioning
	 */

	public Position getPosition() {
		return position;
	}

	public <W extends WAbstractWidget> W setPosition(Position position) {
		if (!this.position.equals(position)) {
			this.position = position;
			onLayoutChange();
		}
		return (W) this;
	}


	public float getX() {
		return position.getX();
	}


	public float getWideX() {
		return getX() + getWidth();
	}


	public float getOffsetX() {
		return position.getOffsetX();
	}


	public float getY() {
		return position.getY();
	}


	public float getHighY() {
		return getY() + getHeight();
	}


	public float getOffsetY() {
		return position.getOffsetY();
	}

	public <W extends WAbstractWidget> W setY(float y) {
		return setPosition(Position.of(position).setY(y));
	}

	public <W extends WAbstractWidget> W setX(float x) {
		return setPosition(Position.of(position).setX(x));
	}

	public void center() {
		setPosition(Position.of(getPosition())
				.setX(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2)
				.setY(getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2));
	}

	public void centerX() {
		setPosition(Position.of(getPosition()).setX(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2));
	}

	public void centerY() {
		setPosition(Position.of(getPosition()).setY(getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2));
	}

	/*

	 */

	/*
		Relations
	 */

	public WDrawableElement getParent() {
		return parent;
	}

	public <W extends WAbstractWidget> W setParent(WDrawableElement parent) {
		this.parent = parent;
		return (W) this;
	}

	/*

	 */

	/*
		Dimensions
	 */

	public Size getSize() {
		return size;
	}

	public <W extends WAbstractWidget> W setSize(Size size) {
		if (!this.size.equals(size)) {
			this.size = size;
			onLayoutChange();
		}
		return (W) this;
	}

	public float getWidth() {
		return size.getWidth();
	}

	public float getHeight() {
		return size.getHeight();
	}

	public <W extends WAbstractWidget> W setHeight(float height) {
		return setSize(Size.of(size).setHeight(height));
	}

	public <W extends WAbstractWidget> W setWidth(float width) {
		return setSize(Size.of(size).setWidth(width));
	}

	/*

	 */

	/*
		Events
	 */

	@Override
	public void onKeyPressed(int keyCode, int character, int keyModifier) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onKeyPressed(keyCode, character, keyModifier);
			}
		}
	}

	@Override
	public void onKeyReleased(int keyCode, int character, int keyModifier) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onKeyReleased(keyCode, character, keyModifier);
			}
		}
	}

	@Override
	public void onCharTyped(char character, int keyCode) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onCharTyped(character, keyCode);
			}
		}
	}

	@Override
	public void onFocusGained() {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (((WAbstractWidget) widget).isFocused()) {
					widget.onFocusGained();
				}
			}
		}
	}


	@Override
	public void onFocusReleased() {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (!((WAbstractWidget) widget).isFocused()) {
					widget.onFocusReleased();
				}
			}
		}
	}

	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			}
		}

		held = false;
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}

		if (isWithinBounds(mouseX, mouseY)) {
			held = true;
			heldSince = System.currentTimeMillis();
		}
	}

	@Override
	public void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
			}
		}
	}

	@Override
	public void onMouseMoved(float mouseX, float mouseY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (widget instanceof WAbstractWidget) {
					WAbstractWidget updateWidget = ((WAbstractWidget) widget);
					boolean then = updateWidget.focused;
					updateWidget.updateFocus(mouseX, mouseY);
					boolean now = updateWidget.focused;

					if (then && !now) {
						updateWidget.onFocusReleased();
					} else if (!then && now) {
						updateWidget.onFocusGained();
					}

				}
				widget.onMouseMoved(mouseX, mouseY);
			}
		}
	}

	@Override
	public void onMouseScrolled(float mouseX, float mouseY, double deltaY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onMouseScrolled(mouseX, mouseY, deltaY);
			}
		}
	}

	@Override
	public void onDrawTooltip(float mouseX, float mouseY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onDrawTooltip(mouseX, mouseY);
			}
		}
	}

	/*

	 */

	/*
		Focus
	 */

	public void setFocus(boolean hasFocus) {
		if (!isFocused() && hasFocus) {
			this.focused = hasFocus;
		}
		if (isFocused() && !hasFocus) {
			this.focused = hasFocus;
		}
	}

	public boolean isFocused() {
		return !isHidden() && focused;
	}

	public boolean updateFocus(float positionX, float positionY) {
		if (isHidden()) {
			return false;
		}

		setFocus(isWithinBounds(positionX, positionY));
		return isFocused();
	}

	public boolean isWithinBounds(float positionX, float positionY) {
		return isWithinBounds(positionX, positionY);
	}

	/*

	 */

	/*
		Hidden
	 */

	public boolean isHidden() {
		if (parent instanceof WAbstractWidget) {
			WAbstractWidget parentWidget = (WAbstractWidget) parent;

			if (parentWidget.isHidden()) {
				return true;
			}
		}
		return hidden;
	}

	public <W extends WAbstractWidget> W setHidden(boolean isHidden) {
		this.hidden = isHidden;
		setFocus(false);
		return (W) this;
	}

	/*

	 */

	/*
		Held
	 */

	public boolean isHeld() {
		return held;
	}

	public long isHeldSince() {
		return heldSince;
	}

	/*
		Miscellaneous
	 */

	@Override
	public void onLayoutChange() {
		if (parent != null) parent.onLayoutChange();

		if (Spinnery.ENVIRONMENT == EnvType.CLIENT) {
			updateFocus(Positions.mouseX, Positions.mouseY);
		}
	}

	@Override
	public void tick() {
	}

	public void onAdded(WInterfaceProvider provider, WCollection parent) {
	}

	public void onRemoved(WInterfaceProvider provider, WCollection parent) {
	}

	/*

	 */
}
