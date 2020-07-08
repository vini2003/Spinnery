package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.utility.ScissorArea;
import spinnery.widget.api.*;

import java.util.*;

// TODO: Add smoothing.
@SuppressWarnings("unchecked")
@Deprecated // Will be fixed soon!
public class WHorizontalScrollableContainer extends WAbstractWidget implements WModifiableCollection, WHorizontalScrollable, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();


	protected WHorizontalScrollbar scrollbar;

	protected float xOffset = 0;
	protected float scrollKineticDelta = 0;
	protected float rightSpace = 0;

	protected float lastScrollX = 0;
	protected float lastScrollY = 0;

	public WHorizontalScrollableContainer() {
		scrollbar = new WHorizontalScrollbar().scrollable(this).setParent(this).setInterface(linkedInterface);;
	}

	public float getRightSpace() {
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

	protected float getMaxX() {
		float max = (float) widgets.stream().mapToDouble(w -> w.getPosition().getRelativeX() + w.getWidth()).max().orElse(0);
		if (max == 0) return getStartAnchorX();
		return getStartAnchorX() + max - getVisibleWidth() + rightSpace;
	}

	@Override
	public Size getUnderlyingSize() {
		Set<WAbstractWidget> widgets = getWidgets();

		float leftmostX = getStartAnchorX();
		float rightmostX = leftmostX;
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
	public float getStartAnchorX() {
		return getX();
	}

	public float getMaxOffsetX() {
		return getMaxX() - getStartAnchorX();
	}

	@Override
	public float getEndAnchorX() {
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
	}

	@Override
	public float getStartOffsetX() {
		return xOffset;
	}

	public void scrollToStart() {
		xOffset = 0f;
		updateChildren();
	}

	public void updateScrollbar() {
		float scrollBarWidth = getVisibleWidth();
		float scrollBarHeight = 6f;
		float scrollBarOffsetX = 0f;
		float scrollBarOffsetY = getHeight() - scrollBarHeight;
		scrollbar.setPosition(Position.of(this, scrollBarOffsetX, scrollBarOffsetY, scrollbar.getPosition().getRelativeZ()));
		scrollbar.setSize(Size.of(scrollBarWidth, scrollBarHeight));
	}

	public void updateChildren() {
		for (WAbstractWidget w : getWidgets()) {
			w.getPosition().setOffsetX(-xOffset);
			boolean startContained = isWithinBounds(w.getX(), w.getY(), 1) || isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
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
	public void remove(WAbstractWidget... widgetArray) {
		widgets.removeAll(Arrays.asList(widgetArray));
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
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		ScissorArea area = new ScissorArea(this);

		for (WLayoutElement widget : widgets) {
			widget.draw(matrices, provider);
		}

		area.destroy();

		scrollbar.draw(matrices, provider);
	}

	@Override
	public boolean updateFocus(float positionX, float positionY) {
		setFocus(isWithinBounds(positionX, positionY) && getWidgets().stream().noneMatch((WAbstractWidget::isFocused)));
		return isFocused();
	}

	@Override
	public void onMouseScrolled(float mouseX, float mouseY, double deltaY) {
		if (isWithinBounds(mouseX, mouseY)) {
			scrollKineticDelta += deltaY;
			scroll(deltaY * 5, 0);
		}
		lastScrollX = mouseX;
		lastScrollY = mouseY;
		super.onMouseScrolled(mouseX, mouseY, deltaY);
	}
}
