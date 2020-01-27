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
public class WList extends WWidget implements WClient, WModifiableCollection, WFocusedMouseListener {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	public static final int LABEL = 4;
	public List<List<WWidget>> listWidgets = new ArrayList<>();

	public WList(WPosition position, WSize size, WInterface linkedInterface) {
		setInterface(linkedInterface);

		setPosition(position);

		setSize(size);

		setTheme("light");
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.put(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.put(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.put(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.put(OUTLINE, WColor.of(rawTheme.get("outline")));
		theme.put(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}

	@Override
	public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		if (getListWidgets().size() == 0) {
			return;
		}

		double scaledOffsetY = deltaY * 2.5;

		boolean hitTop = getListWidgets().get(0).stream().anyMatch(widget ->
				widget.getY() + scaledOffsetY > getY() + 4
		);

		boolean hitBottom = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
				widget.getY() + widget.getHeight() + scaledOffsetY <= getY() + getHeight() - 6
		);

		if ((scaledOffsetY > 0 && !hitTop) || (scaledOffsetY < 0 && !hitBottom)) {
			for (WWidget widget : getWidgets()) {
				widget.setY(widget.getY() + (int) scaledOffsetY);
				widget.setHidden(!(isWithinBounds(widget.getX(), widget.getY()) || isWithinBounds(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight())));
			}
		}

		super.onMouseScrolled(mouseX, mouseY, deltaY);
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
		updatePositions();
		updateHidden();
	}

	@Override
	public void align() {
		super.align();

		updatePositions();
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

		BaseRenderer.drawPanel(x, y, z, sX, sY, getResourceAsColor(SHADOW), getResourceAsColor(BACKGROUND), getResourceAsColor(HIGHLIGHT), getResourceAsColor(OUTLINE));

		if (hasLabel()) {
			BaseRenderer.drawText(isLabelShadowed(), getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, y + 6, getResourceAsColor(LABEL).RGB);
			BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getResourceAsColor(OUTLINE));
			BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getResourceAsColor(SHADOW));
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
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	public void updatePositions() {
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
				widgetC.setHidden(!isWithinBounds(widgetC.getX(), widgetC.getY()));
			}
		}
	}

	@Override
	public void add(WWidget... widgetArray) {
		getListWidgets().add(Arrays.asList(widgetArray));
		updatePositions();
		updateHidden();
	}

	@Override
	public void remove(WWidget... widgetArray) {
		getListWidgets().remove(Arrays.asList(widgetArray));
		updatePositions();
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
