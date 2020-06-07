package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import org.lwjgl.glfw.GLFW;
import spinnery.client.render.BaseRenderer;
import spinnery.util.MouseUtilities;
import spinnery.widget.api.*;

import java.util.*;

@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public class WVerticalScrollableContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WVerticalScrollable, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	protected WVerticalScrollbar scrollbar;

	protected WVerticalArrowUp verticalArrowUp;
	protected WVerticalArrowDown verticalArrowDown;

	protected float scrollbarWidth = 12;

	protected float divisionSpace = 2;
	protected int borderSpace = 0;
	protected int fadeSpace = 12;

	protected float lastScrollX = 0;
	protected float lastScrollY = 0;

	protected float lastDragScrollY = 0;

	protected long lastDragScrollMilliseconds = 0;

	protected float offsetY = 0;

	protected float kineticScrollDelta = 0;

	protected float kineticReductionCoefficient = 1.1f;
	protected float kineticAccelerationCoefficient = 1.5f;

	protected float dragScrollAccelerationCoefficient = 0.0005f;

	protected boolean isDragScrolling = false;

	protected boolean hasFade = true;

	protected boolean hasSmoothing = true;

	protected boolean hasArrows = true;

	public WVerticalScrollableContainer() {
		scrollbar = new WVerticalScrollbar().setScrollable(this).setParent(this);
	}

	public boolean isScrollbarVisible() {
		return !scrollbar.isHidden();
	}

	public <W extends WVerticalScrollableContainer> W setScrollbarVisible(boolean visible) {
		scrollbar.setHidden(!visible);
		return (W) this;
	}

	public WVerticalScrollbar getScrollbar() {
		return scrollbar;
	}

	public WVerticalArrowUp getVerticalArrowUp() {
		return verticalArrowUp;
	}

	public void setVerticalArrowUp(WVerticalArrowUp verticalArrowUp) {
		this.verticalArrowUp = verticalArrowUp;
	}

	public WVerticalArrowDown getVerticalArrowDown() {
		return verticalArrowDown;
	}

	public void setVerticalArrowDown(WVerticalArrowDown verticalArrowDown) {
		this.verticalArrowDown = verticalArrowDown;
	}

	public void setScrollbar(WVerticalScrollbar scrollbar) {
		this.scrollbar = scrollbar;
	}

	public float getScrollbarWidth() {
		return scrollbarWidth;
	}

	public void setScrollbarWidth(float scrollbarWidth) {
		this.scrollbarWidth = scrollbarWidth;
	}

	public float getDivisionSpace() {
		return divisionSpace;
	}

	public void setDivisionSpace(float divisionSpace) {
		this.divisionSpace = divisionSpace;
	}

	public int getBorderSpace() {
		return borderSpace;
	}

	public void setBorderSpace(int borderSpace) {
		this.borderSpace = borderSpace;
	}

	public int getFadeSpace() {
		return fadeSpace;
	}

	public void setFadeSpace(int fadeSpace) {
		this.fadeSpace = fadeSpace;
	}

	public float getLastScrollX() {
		return lastScrollX;
	}

	public void setLastScrollX(float lastScrollX) {
		this.lastScrollX = lastScrollX;
	}

	public float getLastScrollY() {
		return lastScrollY;
	}

	public void setLastScrollY(float lastScrollY) {
		this.lastScrollY = lastScrollY;
	}

	public float getLastDragScrollY() {
		return lastDragScrollY;
	}

	public void setLastDragScrollY(float lastDragScrollY) {
		this.lastDragScrollY = lastDragScrollY;
	}

	public long getLastDragScrollMilliseconds() {
		return lastDragScrollMilliseconds;
	}

	public void setLastDragScrollMilliseconds(long lastDragScrollMilliseconds) {
		this.lastDragScrollMilliseconds = lastDragScrollMilliseconds;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public float getKineticScrollDelta() {
		return kineticScrollDelta;
	}

	public void setKineticScrollDelta(float kineticScrollDelta) {
		this.kineticScrollDelta = kineticScrollDelta;
	}

	public float getKineticReductionCoefficient() {
		return kineticReductionCoefficient;
	}

	public void setKineticReductionCoefficient(float kineticReductionCoefficient) {
		this.kineticReductionCoefficient = kineticReductionCoefficient;
	}

	public float getKineticAccelerationCoefficient() {
		return kineticAccelerationCoefficient;
	}

	public void setKineticAccelerationCoefficient(float kineticAccelerationCoefficient) {
		this.kineticAccelerationCoefficient = kineticAccelerationCoefficient;
	}

	public float getDragScrollAccelerationCoefficient() {
		return dragScrollAccelerationCoefficient;
	}

	public void setDragScrollAccelerationCoefficient(float dragScrollAccelerationCoefficient) {
		this.dragScrollAccelerationCoefficient = dragScrollAccelerationCoefficient;
	}

	public boolean isDragScrolling() {
		return isDragScrolling;
	}

	public void setDragScrolling(boolean dragScrolling) {
		isDragScrolling = dragScrolling;
	}

	public boolean hasFade() {
		return hasFade;
	}

	public void setHasFade(boolean hasFade) {
		this.hasFade = hasFade;
	}

	public boolean hasSmoothing() {
		return hasSmoothing;
	}

	public void setHasSmoothing(boolean hasSmoothing) {
		this.hasSmoothing = hasSmoothing;
	}

	public boolean hasArrows() {
		return hasArrows;
	}

	public void setHasArrows(boolean hasArrows) {
		this.hasArrows = hasArrows;
	}

	protected float getBottomWidgetY() {
		return (float) getWidgets().stream().mapToDouble(widget -> widget.getY() + widget.getHeight()).max().orElse(0);
	}

	protected float getBottomWidgetOffsetY() {
		return (float) getWidgets().stream().mapToDouble(widget -> widget.getPosition().getOffsetY() + widget.getHeight() + getDivisionSpace()).max().orElse(0);
	}

	@Override
	public void scroll(double deltaX, double deltaY) {
		if (getWidgets().isEmpty()) {
			return;
		}

		if (getUnderlyingHeight() <= getVisibleHeight()) {
			scrollToStart();
			return;
		}

		float bottomWidgetY = getBottomWidgetY();
		float bottomY = getBottomWidgetOffsetY();

		boolean hitTop = offsetY < -getDivisionSpace();
		boolean hitBottom = bottomWidgetY < getHighY();

		if ((!hitTop && deltaY > 0) || (!hitBottom && deltaY < 0)) {
			offsetY = (float) Math.min(Math.max(0, offsetY - deltaY), bottomY - getHeight() + 1);

			kineticScrollDelta = offsetY - deltaY >= bottomY + (2 * getDivisionSpace()) ? 0 : kineticScrollDelta;

			updateChildren();
		}
	}

	public void updateChildren() {
		for (WAbstractWidget widget : getWidgets()) {
			widget.getPosition().setY(-offsetY + widget.getPosition().getOffsetY() + getY());
			boolean startContained = isWithinBounds(widget.getX(), widget.getY(), 1) || isWithinBounds(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(), 1);
			widget.setHidden(!startContained);
		}
	}

	public void updateChildrenFocus() {
		for (WAbstractWidget widget : getAllWidgets()) {
			if (widget.isWithinBounds(lastScrollX, lastScrollY)) {
				widget.onFocusGained();
			} else {
				widget.onFocusReleased();
			}
		}
	}

	public void scrollToStart() {
		offsetY = 0;
		updateChildren();
	}

	public void scrollToEnd() {
		offsetY = getBottomWidgetY();
		updateChildren();
	}

	public void updateScrollbar() {
		float scrollBarWidth = getScrollbarWidth();
		float scrollBarHeight = getHeight();

		float scrollBarOffsetX = getWidth() - scrollBarWidth - getBorderSpace();
		float scrollBarOffsetY = getBorderSpace();

		if (hasArrows) {
			scrollBarOffsetY += scrollbarWidth - 1;
			scrollBarHeight -= scrollbarWidth * 2;

			scrollBarHeight = Math.abs(scrollBarHeight);

			if (verticalArrowUp == null) verticalArrowUp =  new WVerticalArrowUp().setScrollable(this).setPosition(Position.of(this, scrollBarOffsetX, 0, 0)).setSize(Size.of(scrollBarWidth));
			else verticalArrowUp.setPosition(Position.of(this, scrollBarOffsetX, 0, 0)).setSize(Size.of(scrollBarWidth));
			if (verticalArrowDown == null) verticalArrowDown = new WVerticalArrowDown().setScrollable(this).setPosition(Position.of(this, scrollBarOffsetX, scrollBarHeight + scrollbarWidth - 2, 0)).setSize(Size.of(scrollBarWidth));
			else verticalArrowDown.setPosition(Position.of(this, scrollBarOffsetX, scrollBarHeight + scrollBarWidth - 2, 0)).setSize(Size.of(scrollBarWidth));
		} else {
			verticalArrowUp = null;
			verticalArrowDown = null;
		}

		scrollbar.setPosition(Position.of(this, scrollBarOffsetX, scrollBarOffsetY, 0));
		scrollbar.setSize(Size.of(scrollBarWidth, scrollBarHeight - (2 * getBorderSpace())));
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return ImmutableSet.<WAbstractWidget>builder().addAll(widgets).add(scrollbar).add(verticalArrowUp).add(verticalArrowDown).build();
	}

	@Override
	public Size getUnderlyingSize() {
		Set<WAbstractWidget> widgets = getWidgets();

		float topmostY = getStartAnchorY();
		float bottommostY = topmostY;
		for (WAbstractWidget widget : widgets) {
			if (widget.getPosition().getOffsetY() < topmostY) {
				topmostY = widget.getPosition().getOffsetY();
			}
			if (widget.getPosition().getOffsetY() + widget.getHeight() > bottommostY) {
				bottommostY = widget.getPosition().getOffsetY() + widget.getHeight();
			}
		}

		return Size.of(getVisibleWidth(), bottommostY - topmostY);
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public List<WLayoutElement> getOrderedWidgets() {
		return orderedWidgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgetArray) {
		return widgets.containsAll(Arrays.asList(widgetArray));
	}

	@Override
	public void add(WAbstractWidget... widgetArray) {
		widgets.addAll(Arrays.asList(widgetArray));

		onLayoutChange();
	}

	public void addSequentially(WAbstractWidget... widgetArray) {
		float maxY = 0;
		float maxX = 0;

		for (WAbstractWidget widget : getWidgets()) {
			if (widget.getPosition().getOffsetY() > maxY) {
				maxY = widget.getPosition().getOffsetY() + widget.getHeight();
			}
		}

		for (WAbstractWidget widget : widgetArray) {
			widget.setPosition(Position.of(this));

			widget.getPosition().setOffsetX(maxX + getDivisionSpace());
			widget.getPosition().setOffsetY(maxY + getDivisionSpace());

			maxX += widget.getWidth() + getDivisionSpace();
		}

		widgets.addAll(Arrays.asList(widgetArray));

		onLayoutChange();
	}

	@Override
	public void remove(WAbstractWidget... widgetArray) {
		widgets.removeAll(Arrays.asList(widgetArray));

		for (WAbstractWidget widgetA : widgetArray) {
			if (widgets.stream().noneMatch(widgetB -> widgetA != widgetB && widgetA.getY() == widgetB.getY())) {
				for (WAbstractWidget widgetC : widgets) {
					if (widgetC.getPosition().getOffsetY() > widgetA.getPosition().getOffsetY()) {
						widgetC.getPosition().setOffsetY(widgetC.getPosition().getOffsetY() - widgetC.getHeight() - getDivisionSpace());
					}
				}
			}

			float bottomY = getBottomWidgetOffsetY();

			if (offsetY + getHeight() > bottomY) {
				offsetY = bottomY - getHeight();
			}

			updateChildren();
			updateChildrenFocus();
		}

		onLayoutChange();
	}

	@Override
	public Size getVisibleSize() {
		return Size.of(getWidth() - (!scrollbar.isHidden() ? scrollbar.getWidth() : 0), getHeight());
	}

	@Override
	public float getStartAnchorY() {
		return getY();
	}

	@Override
	public float getEndAnchorY() {
		if (getVisibleHeight() > getUnderlyingHeight()) return getStartAnchorY();
		return getStartAnchorY() - (getUnderlyingHeight() - getVisibleHeight());
	}

	@Override
	public float getStartOffsetY() {
		return offsetY;
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();

		updateScrollbar();
		recalculateCache();
	}

	@Override
	public void recalculateCache() {
		orderedWidgets = new ArrayList<>(getWidgets());

		Collections.sort(orderedWidgets);
		Collections.reverse(orderedWidgets);
	}

	@Override
	public boolean updateFocus(float positionX, float positionY) {
		setFocus(isWithinBounds(positionX, positionY) && getWidgets().stream().noneMatch((WAbstractWidget::isFocused)));

		return isFocused();
	}

	@Override
	public void onKeyPressed(int keyCode, int character, int keyModifier) {
		if (isWithinBounds(MouseUtilities.mouseX, MouseUtilities.mouseY)) {
			if (keyCode == GLFW.GLFW_KEY_UP) {
				if (hasSmoothing()) {
					kineticScrollDelta += 0.75;
				} else {
					scroll(0, 2.5);
				}
			} else if (keyCode == GLFW.GLFW_KEY_DOWN) {
				if (hasSmoothing()) {
					kineticScrollDelta -= 0.75;
				} else {
					scroll(0, -2.5);
				}
			}
		}

		super.onKeyPressed(keyCode, character, keyModifier);
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (isWithinBounds(mouseX, mouseY)) {
			if (mouseButton == 2) {
				isDragScrolling = true;

				lastDragScrollY = mouseY;
				lastDragScrollMilliseconds = System.currentTimeMillis();

				MouseUtilities.enableDragCursor();
			}
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (mouseButton == 2) {
			isDragScrolling = false;

			lastDragScrollY = 0;
			lastDragScrollMilliseconds = 0;

			MouseUtilities.enableArrowCursor();
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseScrolled(float mouseX, float mouseY, double deltaY) {
		if (isWithinBounds(mouseX, mouseY)) {
			if (hasSmoothing()) {
				kineticScrollDelta += deltaY;
			}

			scroll(0, deltaY);

			lastScrollX = mouseX;
			lastScrollY = mouseY;
		}

		super.onMouseScrolled(mouseX, mouseY, deltaY);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (isDragScrolling()) {
			scroll(0, Math.pow(5, Math.abs(((MouseUtilities.mouseY - lastDragScrollY) / 100))) * ((System.currentTimeMillis() - lastDragScrollMilliseconds) * dragScrollAccelerationCoefficient) * (lastDragScrollY - MouseUtilities.mouseY > 0 ? 1 : -1));
		}


		if (kineticScrollDelta > 0.05 || kineticScrollDelta < -0.05) {
			kineticScrollDelta = kineticScrollDelta / getKineticReductionCoefficient();

			scroll(0, kineticScrollDelta * kineticReductionCoefficient * getKineticAccelerationCoefficient());

			updateChildrenFocus();
		} else {
			kineticScrollDelta = 0;

			lastScrollX = 0;
			lastScrollY = 0;
		}

		BaseRenderer.enableCropping();

		for (WAbstractWidget widget : getWidgets()) {
			BaseRenderer.crop((int) getX(), (int) getY(), (int) getWideX(), (int) getHighY());

			widget.draw();
		}

		BaseRenderer.disableCropping();

		if (hasFade()) {
			Color fadeOut = getStyle().asColor("background");
			fadeOut = Color.of("0x00" + Integer.toHexString((int) (fadeOut.R * 255)) + Integer.toHexString((int) (fadeOut.G * 255)) + Integer.toHexString((int) (fadeOut.B * 255)));

			if (offsetY > 1) {
				BaseRenderer.drawGradient(getX(), getY() - 1, getWideX(), getY() + getFadeSpace(), getZ(), getStyle().asColor("background"), fadeOut);
			}

			if (getBottomWidgetY() > getHighY()) {
				BaseRenderer.drawGradient(getX(), getHighY() - getFadeSpace() , getWideX(), getHighY() + 1, getZ(), fadeOut, getStyle().asColor("background"));
			}
		}

		scrollbar.draw();


		if (hasArrows()) {
			verticalArrowUp.draw();
			verticalArrowDown.draw();
		}
	}

	@Override
	public void tick() {
		if (hasArrows) {
			verticalArrowUp.tick();
			verticalArrowDown.tick();
		}
	}
}
