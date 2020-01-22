package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;
import spinnery.registry.ResourceRegistry;
import spinnery.registry.ThemeRegistry;

import java.util.List;
import java.util.Map;

public class WInterface extends WWidget {
	protected BaseContainer linkedContainer;
	protected WWidgetHolder widgetHolder = new WWidgetHolder();

	protected boolean isClientside;
	protected Class<?> instanceType;

	protected WInterface.Theme drawTheme;

	public WInterface(BaseContainer linkedContainer) {
		setContainer(linkedContainer);
	}

	public WInterface(int positionX, int positionY, int positionZ, int sizeX, int sizeY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setClientside(true);

		setInstanceType(MinecraftClient.class);

		setTheme("default");
	}

	public WInterface(int positionX, int positionY, int positionZ, int sizeX, int sizeY, BaseContainer linkedContainer) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setContainer(linkedContainer);

		setClientside(false);

		if (getContainer().getLinkedWorld().isClient()) {
			setInstanceType(MinecraftClient.class);
		} else {
			setInstanceType(MinecraftServer.class);
		}

		setTheme("default");
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
			//
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getPositionX();
		int y = getPositionY();
		int z = getPositionZ();

		int sX = getSizeX();
		int sY = getSizeY();

		 BaseRenderer.drawPanel(x, y, z, sX, sY, getColor(SHADOW), getColor(BACKGROUND), getColor(HIGHLIGHT),getColor(OUTLINE));

		if (hasLabel()) {
			BaseRenderer.getTextRenderer().drawWithShadow(getLabel().asFormattedString(), x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2, positionY + 6, getColor(LABEL).RGB);
			BaseRenderer.drawRectangle(positionX, positionY + 16, positionZ, sizeX, 1, getColor(OUTLINE));
			BaseRenderer.drawRectangle(positionX + 1, positionY + 17, positionZ, sizeX - 2, 0.75, getColor(SHADOW));
		}

		for (WWidget widget : getWidgets()) {
			widget.draw();
		}
	}

	public static final int SHADOW = 0;
	public static final int BACKGROUND = 1;
	public static final int HIGHLIGHT = 2;
	public static final int OUTLINE = 3;
	public static final int LABEL = 4;

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WInterface.Theme theme = new WWidget.Theme();
		theme.add(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.add(BACKGROUND, WColor.of(rawTheme.get("background")));
		theme.add(HIGHLIGHT, WColor.of(rawTheme.get("highlight")));
		theme.add(OUTLINE, WColor.of(rawTheme.get("outline")));
		theme.add(LABEL, WColor.of(rawTheme.get("label")));
		return theme;
	}
}
