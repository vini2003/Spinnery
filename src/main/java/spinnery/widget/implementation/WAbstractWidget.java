package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import spinnery.Spinnery;
import spinnery.common.utilities.Themes;
import spinnery.common.utilities.Widgets;
import spinnery.common.utilities.Positions;
import spinnery.common.utilities.miscellaneous.Position;
import spinnery.common.utilities.miscellaneous.Size;
import spinnery.common.utilities.miscellaneous.Style;
import spinnery.widget.declaration.collection.WCollection;
import spinnery.widget.declaration.event.WEventConsumer;
import spinnery.widget.declaration.position.WPositioned;
import spinnery.widget.declaration.size.WSized;
import spinnery.widget.declaration.style.WStyled;
import spinnery.widget.declaration.theme.WThemed;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static spinnery.common.utilities.Themes.DEFAULT_THEME;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class WAbstractWidget implements Tickable, WPositioned, WSized, WThemed, WStyled, WEventConsumer {
	private WInterface linkedInterface;

	private WAbstractWidget parent;

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
		if (parent != null && parent instanceof WThemed) return ((WThemed) parent).getTheme();
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

	public WAbstractWidget getParent() {
		return parent;
	}

	public <W extends WAbstractWidget> W setParent(WAbstractWidget parent) {
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
		return getX() > positionX && positionX < getWideX() && getY() > positionY && positionY < getHighY();
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
		Events
	 */

	public interface NoArgumentConsumer {
		void accept();
	}

	public interface SingleArgumentConsumer<T> {
		void accept(T t);
	}

	public interface DoubleArgumentConsumer<T, U> {
		void accept(T t, U u);
	}

	public interface TripleArgumentConsumer<T, U, V> {
		void accept(T t, U u, V v);
	}

	public interface QuadrupleArgumentConsumer<T, U, V, W> {
		void accept(T t, U u, V v, W w);
	}

	public interface QuintupleArgumentConsumer<T, U, V, W, X> {
		void accept(T t, U u, V v, W w, X x);
	}

	private final Event<TripleArgumentConsumer<Integer, Integer, Integer>> keyPressEvent = EventFactory.createArrayBacked(TripleArgumentConsumer.class, (listeners) -> (keyCode, scanCode, keyModifiers) -> {
		for (TripleArgumentConsumer<Integer, Integer, Integer> listener : listeners) {
			listener.accept(keyCode, scanCode, keyModifiers);
		}
	});

	public Event<TripleArgumentConsumer<Integer, Integer, Integer>> getKeyPressEvent() {
		return keyPressEvent;
	}

	@Override
	public void onKeyPressed(int keyCode, int scanCode, int keyModifiers) {
		WEventConsumer.super.onKeyReleased(keyCode, scanCode, keyModifiers);
		keyPressEvent.invoker().accept(keyCode, scanCode, keyModifiers);
	}

	private final Event<TripleArgumentConsumer<Integer, Integer, Integer>> keyReleaseEvent = EventFactory.createArrayBacked(TripleArgumentConsumer.class, (listeners) -> (keyCode, scanCode, keyModifiers) -> {
		for (TripleArgumentConsumer<Integer, Integer, Integer> listener : listeners) {
			listener.accept(keyCode, scanCode, keyModifiers);
		}
	});

	public Event<TripleArgumentConsumer<Integer, Integer, Integer>> getKeyReleaseEvent() {
		return keyReleaseEvent;
	}

	@Override
	public void onKeyReleased(int keyCode, int scanCode, int keyModifiers) {
		WEventConsumer.super.onKeyReleased(keyCode, scanCode, keyModifiers);
		keyReleaseEvent.invoker().accept(keyCode, scanCode, keyModifiers);
	}

	private final Event<DoubleArgumentConsumer<Character, Integer>> charTypeEvent = EventFactory.createArrayBacked(DoubleArgumentConsumer.class, (listeners) -> (character, keyCode) -> {
		for (DoubleArgumentConsumer<Character, Integer> listener : listeners) {
			listener.accept(character, keyCode);
		}
	});

	public Event<DoubleArgumentConsumer<Character, Integer>> getCharTypeEvent() {
		return charTypeEvent;
	}

	@Override
	public void onCharTyped(char character, int keyCode) {
		WEventConsumer.super.onCharTyped(character, keyCode);
		charTypeEvent.invoker().accept(character, keyCode);
	}

	private final Event<TripleArgumentConsumer<Float, Float, Integer>> mouseReleaseEvent = EventFactory.createArrayBacked(TripleArgumentConsumer.class, (listeners) -> (x, y, button) -> {
		for (TripleArgumentConsumer<Float, Float, Integer> listener : listeners) {
			listener.accept(x, y, button);
		}
	});

	public Event<TripleArgumentConsumer<Float, Float, Integer>> getMouseReleaseEvent() {
		return mouseReleaseEvent;
	}

	@Override
	public void onMouseReleased(float x, float y, int button) {
		WEventConsumer.super.onMouseReleased(x, y, button);
		mouseReleaseEvent.invoker().accept(x, y, button);
	}

	private final Event<TripleArgumentConsumer<Float, Float, Integer>> mouseClickEvent = EventFactory.createArrayBacked(TripleArgumentConsumer.class, (listeners) -> (x, y, button) -> {
		for (TripleArgumentConsumer<Float, Float, Integer> listener : listeners) {
			listener.accept(x, y, button);
		}
	});

	public Event<TripleArgumentConsumer<Float, Float, Integer>> getMouseClickEvent() {
		return mouseClickEvent;
	}

	@Override
	public void onMouseClicked(float x, float y, int button) {
		WEventConsumer.super.onMouseClicked(x, y, button);
		mouseClickEvent.invoker().accept(x, y, button);
	}

	private final Event<QuintupleArgumentConsumer<Float, Float, Integer, Double, Double>> mouseDragEvent = EventFactory.createArrayBacked(QuintupleArgumentConsumer.class, (listeners) -> (x, y, button, deltaX, deltaY) -> {
		for (QuintupleArgumentConsumer<Float, Float, Integer, Double, Double> listener : listeners) {
			listener.accept(x, y, button, deltaX, deltaY);
		}
	});

	public Event<QuintupleArgumentConsumer<Float, Float, Integer, Double, Double>> getMouseDragEvent() {
		return mouseDragEvent;
	}

	@Override
	public void onMouseDragged(float x, float y, int button, double deltaX, double deltaY) {
		WEventConsumer.super.onMouseDragged(x, y, button, deltaX, deltaY);
		mouseDragEvent.invoker().accept(x, y, button, deltaX, deltaY);
	}

	private final Event<DoubleArgumentConsumer<Float, Float>> mouseMoveEvent = EventFactory.createArrayBacked(DoubleArgumentConsumer.class, (listeners) -> (x, y) -> {
		for (DoubleArgumentConsumer<Float, Float> listener : listeners) {
			listener.accept(x, y);
		}
	});

	public Event<DoubleArgumentConsumer<Float, Float>> getMouseMoveEvent() {
		return mouseMoveEvent;
	}

	@Override
	public void onMouseMoved(float x, float y) {
		WEventConsumer.super.onMouseMoved(x, y);
		mouseMoveEvent.invoker().accept(x, y);
	}

	private final Event<TripleArgumentConsumer<Float, Float, Double>> mouseScrollEvent = EventFactory.createArrayBacked(TripleArgumentConsumer.class, (listeners) -> (x, y, deltaY) -> {
		for (TripleArgumentConsumer<Float, Float, Double> listener : listeners) {
			listener.accept(x, y, deltaY);
		}
	});

	public Event<TripleArgumentConsumer<Float, Float, Double>> getMouseScrollEvent() {
		return mouseScrollEvent;
	}

	@Override
	public void onMouseScrolled(float x, float y, double deltaY) {
		WEventConsumer.super.onMouseScrolled(x, y, deltaY);
		mouseScrollEvent.invoker().accept(x, y, deltaY);
	}

	private final Event<NoArgumentConsumer> focusGainEvent = EventFactory.createArrayBacked(NoArgumentConsumer.class, (listeners) -> () -> {
		for (NoArgumentConsumer listener : listeners) {
			listener.accept();
		}
	});

	public Event<NoArgumentConsumer> getFocusGainEvent() {
		return focusGainEvent;
	}

	@Override
	public void onFocusGained() {
		WEventConsumer.super.onFocusGained();
		focusGainEvent.invoker().accept();
	}

	private final Event<NoArgumentConsumer> focusReleaseEvent = EventFactory.createArrayBacked(NoArgumentConsumer.class, (listeners) -> () -> {
		for (NoArgumentConsumer listener : listeners) {
			listener.accept();
		}
	});

	public Event<NoArgumentConsumer> getFocusReleaseEvent() {
		return focusReleaseEvent;
	}

	@Override
	public void onFocusReleased() {
		WEventConsumer.super.onFocusReleased();
		focusReleaseEvent.invoker().accept();
	}

	/*

	 */

	/*
		Miscellaneous
	 */

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

	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
	}

	public int hash() {
		return Objects.hash(getClass().getName() + "_" + getX() + "_" + getY() + "_" + getWidth() + "_" + getHeight());
	}

	public List<Text> getTooltip() {
		return Collections.emptyList();
	}

	/*

	 */
}
