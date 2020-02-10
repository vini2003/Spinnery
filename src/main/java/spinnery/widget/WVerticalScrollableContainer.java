package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WDrawableCollection;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WVerticalScrollable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public class WVerticalScrollableContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WVerticalScrollable, WDelegatedEventListener {
	protected Set<WAbstractWidget> widgets = new HashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	protected WVerticalScrollbar scrollbar;

	protected int yOffset = 0;
	protected float scrollKineticDelta = 0;
	protected int bottomSpace = 0;

	public WVerticalScrollableContainer() {
		scrollbar = WWidgetFactory.buildDetached(WVerticalScrollbar.class).scrollable(this).setParent(this);
	}

	public int getBottomSpace() {
		return bottomSpace;
	}

	public <W extends WVerticalScrollableContainer> W setBottomSpace(int bottomSpace) {
		this.bottomSpace = bottomSpace;
		return (W) this;
	}

	@Override
	public void scroll(double deltaX, double deltaY) {
		if (getWidgets().size() == 0) {
			return;
		}

		boolean hitTop = yOffset - deltaY < 0;
		boolean hitBottom = yOffset - deltaY > getMaxOffsetY();

		if (hitTop || hitBottom) {
			scrollKineticDelta = 0;
		}

		if (deltaY > 0 && hitTop) {
			scrollToStart();
		} else if (deltaY < 0 && hitBottom) {
			scrollToEnd();
		} else {
			yOffset -= deltaY;
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

	public <W extends WVerticalScrollableContainer> W setScrollbarVisible(boolean visible) {
		scrollbar.setHidden(!visible);
		return (W) this;
	}

	protected int getMaxY() {
		int max = widgets.stream().mapToInt(w -> w.getPosition().getRelativeY() + w.getHeight()).max().orElse(0);
		if (max == 0) return getStartAnchorY();
		return getStartAnchorY() + max - getVisibleHeight() + bottomSpace;
	}

	@Override
	public Size getUnderlyingSize() {
		Set<WAbstractWidget> widgets = getWidgets();

		int topmostY = getStartAnchorY();
		int bottommostY = topmostY;
		for (WAbstractWidget widget : widgets) {
			if (widget.getPosition().getRelativeY() < topmostY) {
				topmostY = widget.getPosition().getRelativeY();
			}
			if (widget.getPosition().getRelativeY() + widget.getHeight() > bottommostY) {
				bottommostY = widget.getPosition().getRelativeY() + widget.getHeight();
			}
		}

		return Size.of(getVisibleWidth(), bottommostY - topmostY);
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
		return Size.of(getWidth() - (!scrollbar.isHidden() ? scrollbar.getWidth() : 0), getHeight());
	}	@Override
	public int getStartAnchorY() {
		return getY();
	}

	public int getMaxOffsetY() {
		return getMaxY() - getStartAnchorY();
	}	@Override
	public int getEndAnchorY() {
		if (getVisibleHeight() > getUnderlyingHeight()) return getStartAnchorY();
		return getStartAnchorY() - (getUnderlyingHeight() - getVisibleHeight());
	}

	public void scrollToEnd() {
		yOffset = getMaxOffsetY();
		updateChildren();
	}

	@Override
	public void add(WAbstractWidget... widgetArray) {
		widgets.addAll(Arrays.asList(widgetArray));
		onLayoutChange();
	}

	@Override
	public void onLayoutChange() {
		scrollToStart();
		updateScrollbar();
		recalculateCache();
	}	@Override
	public int getStartOffsetY() {
		return yOffset;
	}

	public void scrollToStart() {
		yOffset = 0;
		updateChildren();
	}

	public void updateScrollbar() {
		int scrollBarWidth = 6;
		int scrollBarHeight = getHeight();
		int scrollBarOffsetX = getWidth() - scrollBarWidth;
		int scrollBarOffsetY = 0;
		scrollbar.setPosition(Position.of(this, scrollBarOffsetX, scrollBarOffsetY, 0));
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
			w.getPosition().setOffsetY(-yOffset);
			boolean startContained = isWithinBounds(w.getX(), w.getY(), 1)
					|| isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
			w.setHidden(!startContained);
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
	}

	@Override
	public void tick() {
		if (scrollKineticDelta > 0.05 || scrollKineticDelta < -0.05) {
			scrollKineticDelta = (float) (scrollKineticDelta / 1.25);
			scroll(0, scrollKineticDelta);
		} else {
			scrollKineticDelta = 0;
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

		for (WAbstractWidget widget : getWidgets()) {
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
			scroll(0, deltaY);
		}
		super.onMouseScrolled(mouseX, mouseY, deltaY);
	}






}
