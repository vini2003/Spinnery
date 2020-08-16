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
import spinnery.widget.api.listener.*;

import java.util.List;

import static spinnery.common.utilities.Themes.DEFAULT_THEME;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class WAbstractWidget implements Tickable, WLayoutElement, WThemable, WStyleProvider, WEventListener {
	protected WInterface linkedInterface;
	protected WLayoutElement parent;

	protected Position position = Position.origin();

	protected Size size = Size.of(0, 0);
	protected Size baseAutoSize = Size.of(0, 0);
	protected Size minimumAutoSize = Size.of(0, 0);
	protected Size maximumAutoSize = Size.of(Integer.MAX_VALUE, Integer.MAX_VALUE);

	protected Text label = new LiteralText("");

	protected boolean hidden = false;
	protected boolean focused = false;
	protected boolean held = false;

	protected long heldSince = 0;

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

	@Override
	public void tick() {
	}




	@Environment(EnvType.CLIENT)
	public boolean hasLabel() {
		return !label.getString().isEmpty();
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
		if (position != null) position.onLayoutChange();
		if (size != null) size.onLayoutChange();
		
		if (Spinnery.ENVIRONMENT == EnvType.CLIENT) {
			updateFocus(Positions.mouseX, Positions.mouseY);
		}
	}




	public <W extends WAbstractWidget> W setParent(WLayoutElement parent) {
		this.parent = parent;
		return (W) this;
	}

	@Environment(EnvType.CLIENT)
	public float getWidth() {
		return size.getWidth();
	}

	@Environment(EnvType.CLIENT)
	public float getHeight() {
		return size.getHeight();
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setHeight(float height) {
		return setSize(Size.of(size).setHeight(height));
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setWidth(float width) {
		return setSize(Size.of(size).setWidth(width));
	}




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
		setPosition(Position.of(getPosition()).setX(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2));
	}



	@Environment(EnvType.CLIENT)
	public void centerY() {
		setPosition(Position.of(getPosition()).setY(getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2));
	}



	  @param value    Value for property to be associated with.
	 */
	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W overrideStyle(String property, Object value) {
		styleOverrides.override(property, value);
		return (W) this;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (hidden) return;
	}

	@Override
	public void drawTooltip(MatrixStack matrices, VertexConsumerProvider provider) {
		List<Text> list = getTooltip();

		if (list.isEmpty()) return;

		int maxWidth = 0;

		for (Text text : list) {
			int newWidth = Texts.width(text);
			if (newWidth > maxWidth) maxWidth = newWidth;
		}

		int width = maxWidth;

		int height = list.size() * Texts.height() - 2;

		float x = Positions.mouseX + (Positions.mouseX > MinecraftClient.getInstance().getWindow().getScaledWidth() - (width + 8F) ? -width : 8F);
		float y = Positions.mouseY - 14F + 1F;

		Drawings.drawTooltip(matrices, provider, x, y + 1, width - 1, height - 1, Color.of(0xf0140617), Color.of(0xf0120418), Color.of(0xf0140617), Color.of(0xf0120412), Color.of(0x50270460), Color.of(0x50190333));

		for (Text text : list) {
			Texts.pass().text(text).at(x, y, 512F).scale(1F).shadow(true).color(Color.of(0xFFFCFCFC)).render(matrices, provider);
			y += Texts.height();
		}
	}

	// WLayoutElement




	@Environment(EnvType.CLIENT)
	public Size getSize() {
		return size;
	}




	@Environment(EnvType.CLIENT)
	public Size getBaseAutoSize() {
		return baseAutoSize;
	}




	@Environment(EnvType.CLIENT)
	public Size getMinimumAutoSize() {
		return minimumAutoSize;
	}




	@Environment(EnvType.CLIENT)
	public Size getMaximumAutoSize() {
		return maximumAutoSize;
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setSize(Size size) {
		if (!this.size.equals(size)) {
			this.size = size;
			onLayoutChange();
		}
		return (W) this;
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setMinimumAutoSize(Size minimumAutoSize) {
		if (!this.minimumAutoSize.equals(minimumAutoSize)) {
			this.minimumAutoSize = minimumAutoSize;
		}
		return (W) this;
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setMaximumAutoSize(Size maximumAutoSize) {
		if (!this.maximumAutoSize.equals(maximumAutoSize)) {
			this.maximumAutoSize = maximumAutoSize;
		}
		return (W) this;
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setBaseAutoSize(Size baseAutoSize) {
		if (!this.baseAutoSize.equals(baseAutoSize)) {
			this.baseAutoSize = baseAutoSize;

		}
		return (W) this;
	}


	  when {@link #isFocused()} return true.
	 *

	public boolean isFocusedKeyboardListener() {
		return false;
	}


	  when {@link #isFocused()} return true.
	 *

	public boolean isFocusedMouseListener() {
		return false;
	}


	  for any children widget event listeners.
	 *
	  @param character   Character associated with pressed key.

	@Environment(EnvType.CLIENT)
	@Override
	public void onKeyPressed(int keyCode, int character, int keyModifier) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveKeyboard(widget))
					widget.onKeyPressed(keyCode, character, keyModifier);
			}
		}
		if (runnableOnKeyPressed != null) {
			runnableOnKeyPressed.event(this, keyCode, character, keyModifier);
		}
	}


	  for any children widget event listeners.
	 *
	  @param character   Character associated with pressed key.

	@Environment(EnvType.CLIENT)
	@Override
	public void onKeyReleased(int keyCode, int character, int keyModifier) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveKeyboard(widget))
					widget.onKeyReleased(keyCode, character, keyModifier);
			}
		}
		if (runnableOnKeyReleased != null) {
			runnableOnKeyReleased.event(this, keyCode, character, keyModifier);
		}
	}


	  for any children widget event listeners.
	 *
	  @param keyCode   Keycode associated with key pressed.
	 */
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


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onFocusGained() {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget) && ((WAbstractWidget) widget).isFocused()) {
					widget.onFocusGained();
				}
			}
		}
		if (runnableOnFocusGained != null && isFocused()) {
			runnableOnFocusGained.event(this);
		}
	}


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onFocusReleased() {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget) && !((WAbstractWidget) widget).isFocused()) {
					widget.onFocusReleased();
				}
			}
		}
		if (runnableOnFocusReleased != null && !isFocused()) {
			runnableOnFocusReleased.event(this);
		}
	}


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onMouseReleased(mouseX, mouseY, mouseButton);
			}
		}
		if (runnableOnMouseReleased != null) {
			runnableOnMouseReleased.event(this, mouseX, mouseY, mouseButton);
		}

		held = false;
	}


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget))
					widget.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}

		if (runnableOnMouseClicked != null) {
			runnableOnMouseClicked.event(this, mouseX, mouseY, mouseButton);
		}

		if (isWithinBounds(mouseX, mouseY)) {
			held = true;
			heldSince = System.currentTimeMillis();
		}
	}


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
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


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
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
				if (EventUtilities.canReceiveMouse(widget)) widget.onMouseMoved(mouseX, mouseY);
			}
		}
		if (runnableOnMouseMoved != null) {
			runnableOnMouseMoved.event(this, mouseX, mouseY);
		}
	}



	  @param positionY The vertical (Y) position based on which to calculate focus.

	public boolean updateFocus(float positionX, float positionY) {
		if (isHidden()) {
			return false;
		}

		setFocus(isWithinBounds(positionX, positionY));
		return isFocused();
	}




	@Environment(EnvType.CLIENT)
	public boolean isHidden() {
		if (parent instanceof WAbstractWidget) {
			WAbstractWidget parentWidget = (WAbstractWidget) parent;

			if (parentWidget.isHidden()) {
				return true;
			}
		}
		return hidden;
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setHidden(boolean isHidden) {
		this.hidden = isHidden;
		setFocus(false);
		return (W) this;
	}




	@Environment(EnvType.CLIENT)
	public void setFocus(boolean hasFocus) {
		if (!isFocused() && hasFocus) {
			this.focused = hasFocus;
		}
		if (isFocused() && !hasFocus) {
			this.focused = hasFocus;
		}
	}




	@Environment(EnvType.CLIENT)
	public boolean isFocused() {
		return !isHidden() && focused;
	}




	@Environment(EnvType.CLIENT)
	public boolean isHeld() {
		return held;
	}




	@Environment(EnvType.CLIENT)
	public long isHeldSince() {
		return heldSince;
	}



	  @param positionY The vertical (Y) position based on which to calculate boundaries.

	@Environment(EnvType.CLIENT)
	public boolean isWithinBounds(float positionX, float positionY) {
		return isWithinBounds(positionX, positionY, 0);
	}


	  given a vertical and horizontal tolerance.
	 *
	  @param positionY The vertical (Y) position based on which to calculate boundaries.
	  @return True if within boundaries; False if not.
	 */
	@Environment(EnvType.CLIENT)
	public boolean isWithinBounds(float positionX, float positionY, float tolerance) {
		return positionX + tolerance > getX()
				&& positionX - tolerance < getWideX()
				&& positionY + tolerance > getY()
				&& positionY - tolerance < getHighY();
	}

	@Environment(EnvType.CLIENT)
	public float getX() {
		return position.getX();
	}

	@Environment(EnvType.CLIENT)
	public float getWideX() {
		return getX() + getWidth();
	}

	@Environment(EnvType.CLIENT)
	public float getOffsetX() {
		return position.getOffsetX();
	}

	@Environment(EnvType.CLIENT)
	public float getY() {
		return position.getY();
	}

	@Environment(EnvType.CLIENT)
	public float getHighY() {
		return getY() + getHeight();
	}

	@Environment(EnvType.CLIENT)
	public float getOffsetY() {
		return position.getOffsetY();
	}

	@Environment(EnvType.CLIENT)
	public float getZ() {
		return position.getZ();
	}

	@Environment(EnvType.CLIENT)
	public float getOffsetZ() {
		return position.getOffsetZ();
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setZ(float z) {
		return setPosition(Position.of(position).setZ(z));
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setY(float y) {
		return setPosition(Position.of(position).setY(y));
	}




	@Environment(EnvType.CLIENT)
	public <W extends WAbstractWidget> W setX(float x) {
		return setPosition(Position.of(position).setX(x));
	}


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseScrolled(float mouseX, float mouseY, double deltaY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				if (EventUtilities.canReceiveMouse(widget)) {
					widget.onMouseScrolled(mouseX, mouseY, deltaY);
				}
			}
		}
		if (runnableOnMouseScrolled != null) {
			runnableOnMouseScrolled.event(this, mouseX, mouseY, deltaY);
		}
	}


	  for any children widget event listeners.
	 */
	@Environment(EnvType.CLIENT)
	@Override
	public void onDrawTooltip(float mouseX, float mouseY) {
		if (this instanceof WDelegatedEventListener) {
			for (WEventListener widget : ((WDelegatedEventListener) this).getEventDelegates()) {
				widget.onDrawTooltip(mouseX, mouseY);
			}
		}
		if (runnableOnDrawTooltip != null) {
			runnableOnDrawTooltip.event(this, mouseX, mouseY);
		}
	}


	  for any children widget event listeners. Also dispatches

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
