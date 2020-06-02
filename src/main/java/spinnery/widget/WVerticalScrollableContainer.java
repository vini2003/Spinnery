package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import spinnery.client.BaseRenderer;
import spinnery.util.MouseUtilities;
import spinnery.widget.api.*;

import java.util.*;

import static spinnery.util.MouseUtilities.mouseY;

@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public class WVerticalScrollableContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WVerticalScrollable, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	protected WVerticalScrollbar scrollbar;

	protected int scrollbarWidth = 12;

	protected int divisionSpace = 2;
	protected int borderSpace = 0;
	protected int fadeSpace = 12;

	protected int lastScrollX = 0;
	protected int lastScrollY = 0;

	protected int lastDragScrollY = 0;

	protected long lastDragScrollMilliseconds = 0;

	protected double offsetY = 0;

	protected float kineticScrollDelta = 0;

	protected double kineticReductionCoefficient = 1.5;
	protected double kineticAccelerationCoefficient = 1.5;

	protected double dragScrollAccelerationCoefficient = 0.0035;

	protected boolean isDragScrolling = false;

	protected boolean hasFade = true;

	protected boolean hasSmoothing = true;

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

	public void setScrollbar(WVerticalScrollbar scrollbar) {
		this.scrollbar = scrollbar;
	}

	public int getScrollbarWidth() {
		return scrollbarWidth;
	}

	public void setScrollbarWidth(int scrollbarWidth) {
		this.scrollbarWidth = scrollbarWidth;
	}

	public int getDivisionSpace() {
		return divisionSpace;
	}

	public void setDivisionSpace(int divisionSpace) {
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

	public int getLastScrollX() {
		return lastScrollX;
	}

	public void setLastScrollX(int lastScrollX) {
		this.lastScrollX = lastScrollX;
	}

	public int getLastScrollY() {
		return lastScrollY;
	}

	public void setLastScrollY(int lastScrollY) {
		this.lastScrollY = lastScrollY;
	}

	public int getLastDragScrollY() {
		return lastDragScrollY;
	}

	public void setLastDragScrollY(int lastDragScrollY) {
		this.lastDragScrollY = lastDragScrollY;
	}

	public long getLastDragScrollMilliseconds() {
		return lastDragScrollMilliseconds;
	}

	public void setLastDragScrollMilliseconds(long lastDragScrollMilliseconds) {
		this.lastDragScrollMilliseconds = lastDragScrollMilliseconds;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	public float getKineticScrollDelta() {
		return kineticScrollDelta;
	}

	public void setKineticScrollDelta(float kineticScrollDelta) {
		this.kineticScrollDelta = kineticScrollDelta;
	}

	public double getKineticReductionCoefficient() {
		return kineticReductionCoefficient;
	}

	public void setKineticReductionCoefficient(double kineticReductionCoefficient) {
		this.kineticReductionCoefficient = kineticReductionCoefficient;
	}

	public double getKineticAccelerationCoefficient() {
		return kineticAccelerationCoefficient;
	}

	public void setKineticAccelerationCoefficient(double kineticAccelerationCoefficient) {
		this.kineticAccelerationCoefficient = kineticAccelerationCoefficient;
	}

	public double getDragScrollAccelerationCoefficient() {
		return dragScrollAccelerationCoefficient;
	}

	public void setDragScrollAccelerationCoefficient(double dragScrollAccelerationCoefficient) {
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

	protected int getBottomWidgetY() {
		return getWidgets().stream().mapToInt(widget -> widget.getY() + widget.getHeight()).max().orElse(0);
	}

	protected int getBottomWidgetOffsetY() {
		return getWidgets().stream().mapToInt(widget -> widget.getPosition().getOffsetY() + widget.getHeight() + getDivisionSpace()).max().orElse(0);
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

		int bottomWidgetY = getBottomWidgetY();
		int bottomY = getBottomWidgetOffsetY();

		boolean hitTop = offsetY < -getDivisionSpace();
		boolean hitBottom = bottomWidgetY < getHighY();

		if ((!hitTop && deltaY > 0) || (!hitBottom && deltaY < 0)) {
			offsetY = Math.min(Math.max(0, offsetY - deltaY), bottomY - getHeight() + 1);

			kineticScrollDelta = offsetY - deltaY >= bottomY + (2 * getDivisionSpace()) ? 0 : kineticScrollDelta;

			updateChildren();
		}
	}

	public void updateChildren() {
		for (WAbstractWidget widget : getWidgets()) {
			widget.getPosition().setY((int) -offsetY + widget.getPosition().getOffsetY() + getY());
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
		int scrollBarWidth = getScrollbarWidth();
		int scrollBarHeight = getHeight();

		int scrollBarOffsetX = getWidth() - scrollBarWidth - getBorderSpace();
		int scrollBarOffsetY = getBorderSpace();

		scrollbar.setPosition(Position.of(this, scrollBarOffsetX, scrollBarOffsetY, 0));
		scrollbar.setSize(Size.of(scrollBarWidth, scrollBarHeight - (2 * getBorderSpace())));
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return ImmutableSet.<WAbstractWidget>builder().addAll(widgets).add(scrollbar).build();
	}

	@Override
	public Size getUnderlyingSize() {
		Set<WAbstractWidget> widgets = getWidgets();

		int topmostY = getStartAnchorY();
		int bottommostY = topmostY;
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
		int maxY = 0;
		int maxX = 0;

		for (WAbstractWidget widget : getAllWidgets()) {
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

			int bottomY = getWidgets().stream().mapToInt(widget -> widget.getPosition().getOffsetY() + widget.getHeight() + getDivisionSpace()).max().orElse(0);

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
	public int getStartAnchorY() {
		return getY();
	}

	@Override
	public int getEndAnchorY() {
		if (getVisibleHeight() > getUnderlyingHeight()) return getStartAnchorY();
		return getStartAnchorY() - (getUnderlyingHeight() - getVisibleHeight());
	}

	@Override
	public int getStartOffsetY() {
		return (int) offsetY;
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
	public boolean updateFocus(int positionX, int positionY) {
		setFocus(isWithinBounds(positionX, positionY) && getWidgets().stream().noneMatch((WAbstractWidget::isFocused)));

		return isFocused();
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
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
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 2) {
			isDragScrolling = false;

			lastDragScrollY = 0;
			lastDragScrollMilliseconds = 0;

			MouseUtilities.enableArrowCursor();
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (hasSmoothing()) {
			kineticScrollDelta += deltaY;
		}

		scroll(0, deltaY);

		lastScrollX = mouseX;
		lastScrollY = mouseY;

		super.onMouseScrolled(mouseX, mouseY, deltaY);
	}

	@Override
	public void tick() {
		if (kineticScrollDelta > 0.05 || kineticScrollDelta < -0.05) {
			kineticScrollDelta = (float) (kineticScrollDelta / getKineticReductionCoefficient());

			scroll(0, kineticScrollDelta * kineticReductionCoefficient * getKineticAccelerationCoefficient());
			updateChildrenFocus();
		} else {
			kineticScrollDelta = 0;
			lastScrollX = 0;
			lastScrollY = 0;
		}

		if (isDragScrolling()) {
			scroll(0, Math.pow(5, Math.abs(((float) (mouseY - lastDragScrollY) / 100))) * ((System.currentTimeMillis() - lastDragScrollMilliseconds) * dragScrollAccelerationCoefficient) * (lastDragScrollY - mouseY > 0 ? 1 : -1));
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		BaseRenderer.enableCropping();

		for (WAbstractWidget widget : getWidgets()) {
			BaseRenderer.crop(getX(), getY(), getWideX(), getHighY());

			widget.draw();
		}

		BaseRenderer.disableCropping();

		if (hasFade()) {
			Color fadeOut = getStyle().asColor("background");
			fadeOut = Color.of("0x00" + Integer.toHexString((int) (fadeOut.R * 255)) + Integer.toHexString((int) (fadeOut.G * 255)) + Integer.toHexString((int) (fadeOut.B * 255)));

			if (offsetY > 1) {
				BaseRenderer.drawGradient(getX(), getY(), getWideX(), getY() + getFadeSpace(), getZ(), getStyle().asColor("background"), fadeOut);
			}

			if (getBottomWidgetY() > getHighY()) {
				BaseRenderer.drawGradient(getX(), getHighY() - getFadeSpace() , getWideX(), getHighY(), getZ(), fadeOut, getStyle().asColor("background"));
			}
		}

		scrollbar.draw();
	}
}
