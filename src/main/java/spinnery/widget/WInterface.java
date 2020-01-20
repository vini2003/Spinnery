package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;
import spinnery.registry.ResourceRegistry;

import java.util.List;

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
			drawTheme = ResourceRegistry.get(getTheme()).getWInterfaceTheme();
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		double x = getPositionX();
		double y = getPositionY();
		double z = getPositionZ();

		double sX = getSizeX();
		double sY = getSizeY();

		BaseRenderer.drawPanel(x, y, z, sX, sY, drawTheme.getShadow(), drawTheme.getBackground(), drawTheme.getHighlight(), drawTheme.getOutline());

		if (hasLabel()) {
			BaseRenderer.getTextRenderer().drawWithShadow(getLabel().asFormattedString(), (int) (x + sX / 2 - BaseRenderer.getTextRenderer().getStringWidth(getLabel().asFormattedString()) / 2), (int) (positionY + 6), drawTheme.getLabel().RGB);
			BaseRenderer.drawRectangle(positionX, positionY + 16, positionZ, sizeX, 1, drawTheme.getOutline());
			BaseRenderer.drawRectangle(positionX + 1, positionY + 17, positionZ, sizeX - 2, 0.75, drawTheme.getShadow());
		}

		for (WWidget widget : getWidgets()) {
			widget.draw();
		}
	}

	@Override
	public void tick() {
		for (WWidget widget : getWidgets()) {
			widget.tick();
		}
	}

	public class Theme extends WWidget.Theme {
		transient private WColor shadow;
		transient private WColor background;
		transient private WColor highlight;
		transient private WColor outline;
		transient private WColor label;

		@SerializedName("shadow")
		private String rawShadow;

		@SerializedName("background")
		private String rawBackground;

		@SerializedName("highlight")
		private String rawHighlight;

		@SerializedName("outline")
		private String rawOutline;

		@SerializedName("label")
		private String rawLabel;

		public void build() {
			shadow = new WColor(rawShadow);
			background = new WColor(rawBackground);
			highlight = new WColor(rawHighlight);
			outline = new WColor(rawOutline);
			label = new WColor(rawLabel);
		}

		public WColor getShadow() {
			return shadow;
		}

		public WColor getBackground() {
			return background;
		}

		public WColor getHighlight() {
			return highlight;
		}

		public WColor getOutline() {
			return outline;
		}

		public WColor getLabel() {
			return label;
		}
	}
}
