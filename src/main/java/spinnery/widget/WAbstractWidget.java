package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import spinnery.Spinnery;
import spinnery.common.utilities.Themes;
import spinnery.common.utilities.Widgets;
import spinnery.common.utilities.Positions;
import spinnery.widget.api.*;

import java.util.Objects;

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
		setPosition(Position.of(getPosition()).setX(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2).setY(getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2));
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

	 */

	/*
		Network
	 */

	public boolean shouldSynchronize(Identifier packetId) {
		return false;
	}

	/*

	 */

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

	public void onAdded(WInterface linkedInterface, WCollection parent) {
	}

	public void onRemoved(WInterface linkedInterface, WCollection parent) {
	}

	public int hash() {
		return Objects.hash(getClass().getName() + "_" + getX() + "_" + getY() + "_" + getWidth() + "_" + getHeight());
	}

	/*

	 */
}
