package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL11;
import spinnery.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WVerticalList extends WWidget implements WClient, WModifiableCollection, WVerticalScrollable {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	public static final int LABEL = 4;
	public List<List<WWidget>> listWidgets = new ArrayList<>();

	protected WVerticalScrollbar scrollbar;

	public WVerticalList(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setTheme("light");

		scrollbar = new WVerticalScrollbar(linkedInterface, this);
		scrollbar.setHidden(true);
		linkedInterface.add(scrollbar);
		updateScrollbar();
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.add(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.add(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.add(OUTLINE, WColor.of(rawTheme.get("outline")));
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}

	public void updateScrollbar() {
		int scrollBarOffset = 2 + (hasLabel() ? 16 : 0);
		int scrollBarWidth = 6;
		int scrollBarHeight = size.getY() - 3 - scrollBarOffset;
		int scrollBarX = position.getX() + size.getX() - scrollBarWidth - 3;
		int scrollBarY = position.getY() + scrollBarOffset;
		scrollbar.setPosition(WPosition.of(WType.FREE, scrollBarX, scrollBarY, getZ()));
		scrollbar.setSize(WSize.of(scrollBarWidth, scrollBarHeight));
	}

	@Override
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (isWithinBounds(mouseX, mouseY)) {
			scroll(0, deltaY * 2.5);
			super.onMouseScrolled(mouseX, mouseY, deltaY);
		}
	}

	@Override
	public void scroll(double deltaX, double deltaY) {
		if (getListWidgets().size() == 0) {
			return;
		}

		double scaledOffsetY = deltaY * 2.5;
		int top = getY() + 4 + (hasLabel() ? 17 : 0);
		int bottom = getY() + getHeight() - 9;

		boolean hitTop = getListWidgets().get(0).stream().anyMatch(widget ->
				widget.getY() + scaledOffsetY > top
		);

		boolean hitBottom = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
				widget.getY() + widget.getHeight() + scaledOffsetY <= bottom
		);

		///
		int topScroll = getAnchorTop();
		int topmostY = getListWidgets().get(0).stream().mapToInt(WWidget::getY).min().orElse(topScroll);
		int bottomScroll = getAnchorBottom();
		for (WWidget widget : getWidgets()) {
			int yOffset = widget.getY() - topmostY;
			if (scaledOffsetY > 0 && hitTop) {
				widget.setY(topScroll + yOffset);
			} else if (scaledOffsetY < 0 && hitBottom) {
				widget.setY(bottomScroll + yOffset);
			} else {
				widget.setY(widget.getY() + (int) scaledOffsetY);
			}
		}
		updateHidden();
	}

	public List<List<WWidget>> getListWidgets() {
		return listWidgets;
	}

	@Override
	public List<WWidget> getWidgets() {
		List<WWidget> widgets = new ArrayList<>();
		for (List<WWidget> widgetA : getListWidgets()) {
			widgets.addAll(widgetA);
		}
		return widgets;
	}

	@Override
	public List<WWidget> getAllWidgets() {
		List<WWidget> widgets = new ArrayList<>();
		for (List<WWidget> widgetA : getListWidgets()) {
			widgets.addAll(widgetA);
			if (widgetA instanceof WCollection) {
				widgets.addAll(((WCollection) widgetA).getAllWidgets());
			}
		}
		return widgets;
	}

	@Override
	public void setLabel(Text label) {
		super.setLabel(label);
		resetPositions();
		updateHidden();
		updateScrollbar();
	}

	public boolean hasScroller() {
		return !scrollbar.isHidden();
	}

	public void setScroller(boolean hasScroller) {
		scrollbar.setHidden(!hasScroller);
	}

	@Override
	public int getAnchorTop() {
		int startY = getY() + 4;
		if (hasLabel()) startY += 17;
		return startY;
	}

	@Override
	public int getAnchorBottom() {
		if (getHeight() >= getInnerHeight()) return getAnchorTop();
		// The final 5px value was determined empirically
		int topY = getAnchorTop() - (getInnerHeight() - getHeight()) - 4 - 5;
		if (hasLabel()) topY -= 17;
		return topY;
	}

	@Override
	public WSize getVisibleSize() {
		return WSize.of(getWidth() - 8, getAnchorBottom() - getAnchorTop());
	}

	@Override
	public WSize getInnerSize() {
		int startY = getAnchorTop();
		List<List<WWidget>> widgetLists = getListWidgets();
		for (WWidget widget : widgetLists.get(0)) {
			if (widget.getY() < startY) {
				startY = widget.getY();
			}
		}

		int endY = startY;
		for (WWidget widget : widgetLists.get(widgetLists.size() - 1)) {
			if (widget.getY() + widget.getHeight() > endY) {
				endY = widget.getY() + widget.getHeight();
			}
		}

		return WSize.of(getWidth(), endY - startY + 3); // 3px bottom padding to not hug the border
	}

	@Override
	public int getTopOffset() {
		int topY = getY() + (hasLabel() ? 16 : 0);
		int lowestY = getY() + (hasLabel() ? 16 : 0);
		for (WWidget widget : getListWidgets().get(0)) {
			if (widget.getY() < lowestY) {
				lowestY = widget.getY();
			}
		}
		return topY - lowestY;
	}

	@Override
	public void align() {
		super.align();
		resetPositions();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WWidget::getFocus)));
		return getFocus();
	}

	@Override
	public void center() {
		int oldX = getX();
		int oldY = getY();

		super.center();

		int newX = getX();
		int newY = getY();

		int offsetX = newX - oldX;
		int offsetY = newY - oldY;

		for (WWidget widget : getWidgets()) {
			widget.setX(widget.getX() + offsetX);
			widget.setY(widget.getY() + offsetY);
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawPanel(x, y, z, sX, sY, getColor(SHADOW), getColor(BACKGROUND), getColor(HIGHLIGHT), getColor(OUTLINE));

		if (hasLabel()) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getColor(LABEL).RGB);
			BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getColor(OUTLINE));
			BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getColor(SHADOW));
		}

		int rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y - 4) * scale) - (sY * scale)), (int) (sX * scale), (int) ((sY - 8 - (hasLabel() ? 13.75 : 0)) * scale));

		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.draw();
			}
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		scrollbar.draw();
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	public void resetPositions() {
		int y = getY() + (hasLabel() ? 20 : 8);

		for (List<WWidget> widgetA : getListWidgets()) {
			int x = getX() + 5;
			for (WWidget widgetB : widgetA) {
				widgetB.setX(x);
				widgetB.setY(y);
				x += widgetB.getWidth() + 2;
			}
			y += widgetA.get(0).getHeight() + 2;
		}
	}

	public void updateHidden() {
		for (List<WWidget> widgetB : getListWidgets()) {
			for (WWidget widgetC : widgetB) {
				widgetC.setHidden(!(isWithinBounds(widgetC.getX(), widgetC.getY())
						|| isWithinBounds(widgetC.getX() + widgetC.getWidth(), widgetC.getY() + widgetC.getHeight())));
			}
		}
	}

	@Override
	public void add(WWidget... widgetArray) {
		getListWidgets().add(Arrays.asList(widgetArray));
		resetPositions();
		updateHidden();
	}

	@Override
	public void remove(WWidget... widgetArray) {
		getListWidgets().remove(Arrays.asList(widgetArray));
		resetPositions();
		updateHidden();
	}

	@Override
	public boolean contains(WWidget... widgets) {
		for (List<WWidget> widgetList : getListWidgets()) {
			if (widgetList.containsAll(Arrays.asList(widgets))) {
				return true;
			}
		}
		return false;
	}
}
