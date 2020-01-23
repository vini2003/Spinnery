package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;

import java.util.List;
import java.util.Map;

public class WInterface extends WWidget {
	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	public static final int LABEL = 4;
	protected BaseContainer linkedContainer;
	protected WWidgetHolder widgetHolder = new WWidgetHolder();
	protected boolean isClientside;
	protected Class<?> instanceType;

	public WInterface(BaseContainer linkedContainer) {
		setContainer(linkedContainer);
	}

	public WInterface(WPosition position) {
		setPosition(position);

		setSize(null);

		setClientside(true);

		setInstanceType(MinecraftClient.class);

		setTheme("light");
	}

	public WInterface(WPosition position, WSize size) {
		setPosition(position);

		setSize(size);

		setClientside(true);

		setInstanceType(MinecraftClient.class);

		setTheme("light");
	}

	public WInterface(WPosition position, WSize size, BaseContainer linkedContainer) {
		setPosition(position);

		setSize(size);

		setContainer(linkedContainer);

		setClientside(false);

		if (getContainer().getLinkedWorld().isClient()) {
			setInstanceType(MinecraftClient.class);
		} else {
			setInstanceType(MinecraftServer.class);
		}

		setTheme("light");
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

	public Class<?> getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(Class<?> instanceType) {
		this.instanceType = instanceType;
	}

	public boolean isClient() {
		return instanceType == MinecraftClient.class;
	}

	public boolean isServer() {
		return instanceType == MinecraftServer.class;
	}

	public Boolean isClientside() {
		return isClientside;
	}

	public void setClientside(Boolean clientside) {
		isClientside = clientside;
	}

	public BaseContainer getContainer() {
		return linkedContainer;
	}

	public void setContainer(BaseContainer linkedContainer) {
		this.linkedContainer = linkedContainer;
	}

	public List<WWidget> getWidgets() {
		return widgetHolder.getWidgets();
	}

	public WWidgetHolder getHolder() {
		return widgetHolder;
	}

	public boolean isDrawable() {
		return size != null;
	}

	public void add(WWidget... widgets) {
		for (WWidget widget : widgets) {
			if (widget instanceof WServer && isClientside()) {
				throw new RuntimeException("Cannot add server-side WWidget to non-server-side WIntertface!");
			}
		}

		getHolder().add(widgets);
	}

	public void remove(WWidget... WWidgets) {
		getHolder().remove(WWidgets);
	}

	@Override
	public void setTheme(String theme) {
		if (isClient()) {
			super.setTheme(theme);
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		if (isDrawable()) {
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
		}

		for (WWidget widget : getWidgets()) {
			widget.draw();
		}
	}
}
