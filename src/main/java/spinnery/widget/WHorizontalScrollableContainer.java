package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WDrawableCollection;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WHorizontalScrollable;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class WHorizontalScrollableContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WHorizontalScrollable, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	protected WHorizontalScrollbar scrollbar;

	protected int xOffset = 0;
	protected float scrollKineticDelta = 0;
	protected int rightSpace = 0;

	protected int lastScrollX = 0;
	protected int lastScrollY = 0;

	public WHorizontalScrollableContainer() {
		scrollbar = WWidgetFactory.buildDetached(WHorizontalScrollbar.class).scrollable(this).setParent(this);
	}

	public int getRightSpace() {
		return rightSpace;
	}

	public <W extends WHorizontalScrollableContainer> W setRightSpace(int rightSpace) {
		this.rightSpace = rightSpace;
		return (W) this;
	}

	@Override
	public void scroll(double deltaX, double deltaY) {
		if (getWidgets().size() == 0) {
			return;
		}

		if (getUnderlyingWidth() <= getVisibleWidth()) {
			scrollToStart();
			return;
		}

		boolean hitLeft = xOffset - deltaX < 0;
		boolean hitRight = xOffset - deltaX > getMaxOffsetX();

		if (hitRight || hitLeft) {
			scrollKineticDelta = 0;
		}

		if (deltaX > 0 && hitLeft) {
			scrollToStart();
		} else if (deltaX < 0 && hitRight) {
			scrollToEnd();
		} else {
			xOffset -= deltaX;
		}

		updateChildren();
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		Set<WAbstractWidget> delegates = new HashSet<>(widgets);
		delegates.add(scrollbar);
		return ImmutableSet.copyOf(delegates);
	}

	public boolean getScrollbarVisible() {
		return !scrollbar.isHidden();
	}

	public <W extends WHorizontalScrollableContainer> W setScrollbarVisible(boolean visible) {
		scrollbar.setHidden(!visible);
		return (W) this;
	}

	protected int getMaxX() {
		int max = widgets.stream().mapToInt(w -> w.getPosition().getRelativeX() + w.getWidth()).max().orElse(0);
		if (max == 0) return getStartAnchorX();
		return getStartAnchorX() + max - getVisibleWidth() + rightSpace;
	}

	@Override
	public Size getUnderlyingSize() {
		Set<WAbstractWidget> widgets = getWidgets();

		int leftmostX = getStartAnchorX();
		int rightmostX = leftmostX;
		for (WAbstractWidget widget : widgets) {
			if (widget.getX() < leftmostX) {
				leftmostX = widget.getX();
			}
			if (widget.getX() + widget.getWidth() > rightmostX) {
				rightmostX = widget.getX() + widget.getWidth();
			}
		}

		return Size.of(rightmostX - leftmostX, getVisibleHeight());
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgetArray) {
		return widgets.containsAll(Arrays.asList(widgetArray));
	}

	@Override
	public Size getVisibleSize() {
		return Size.of(getWidth(), getHeight() - (!scrollbar.isHidden() ? scrollbar.getHeight() : 0));
	}

	@Override
	public int getStartAnchorX() {
		return getX();
	}

	public int getMaxOffsetX() {
		return getMaxX() - getStartAnchorX();
	}	@Override
	public int getEndAnchorX() {
		if (getWidth() > getUnderlyingWidth()) return getStartAnchorX();
		return getStartAnchorX() - (getUnderlyingWidth() - getVisibleWidth());
	}

	public void scrollToEnd() {
		xOffset = getMaxOffsetX();
		updateChildren();
	}

	@Override
	public void add(WAbstractWidget... widgetArray) {
		widgets.addAll(Arrays.asList(widgetArray));
		onLayoutChange();
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
		scrollToStart();
		updateScrollbar();
		recalculateCache();
	}

	@Override
	public int getStartOffsetX() {
		return xOffset;
	}

	public void scrollToStart() {
		xOffset = 0;
		updateChildren();
	}

	public void updateScrollbar() {
		int scrollBarWidth = getVisibleWidth();
		int scrollBarHeight = 6;
		int scrollBarOffsetX = 0;
		int scrollBarOffsetY = getHeight() - scrollBarHeight;
		scrollbar.setPosition(Position.of(this, scrollBarOffsetX, scrollBarOffsetY, scrollbar.getPosition().getRelativeZ()));
		scrollbar.setSize(Size.of(scrollBarWidth, scrollBarHeight));
	}

	@Override
	public void recalculateCache() {
		orderedWidgets = new ArrayList<>(getWidgets());
		Collections.sort(orderedWidgets);
		Collections.reverse(orderedWidgets);
	}

	public void updateChildren() {
		for (WAbstractWidget w : getWidgets()) {
			w.getPosition().setOffsetX(-xOffset);
			boolean startContained = isWithinBounds(w.getX(), w.getY(), 1)
					|| isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
			w.setHidden(!startContained);
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

	@Override
	public List<WLayoutElement> getOrderedWidgets() {
		return orderedWidgets;
	}

	@Override
	public void remove(WAbstractWidget... widgetArray) {
		widgets.removeAll(Arrays.asList(widgetArray));
		onLayoutChange();
		onLayoutChange();
	}

	@Override
	public void tick() {
		if (scrollKineticDelta > 0.05 || scrollKineticDelta < -0.05) {
			scrollKineticDelta = (float) (scrollKineticDelta / 1.10);
			scroll(scrollKineticDelta, 0);
			updateChildrenFocus();
		} else {
			scrollKineticDelta = 0;
			lastScrollX = 0;
			lastScrollY = 0;
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();

		int sX = getWidth();
		int sY = getHeight();

		int rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - (y * scale + sY * scale)), (int) (sX * scale), (int) (sY * scale));

		for (WLayoutElement widget : getOrderedWidgets()) {
			widget.draw();
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		scrollbar.draw();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WAbstractWidget::isFocused)));
		return isFocused();
	}

	@Override
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (isWithinBounds(mouseX, mouseY)) {
			scrollKineticDelta += deltaY;
			scroll(deltaY * 5, 0);
		}
		lastScrollX = mouseX;
		lastScrollY = mouseY;
		super.onMouseScrolled(mouseX, mouseY, deltaY);
	}
}
