package electron.container.common.widget;

import electron.container.common.BaseContainer;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Panel extends Widget {
	protected BaseContainer container;

	protected List<Widget> linkedWidgets = new ArrayList<>();

	public Panel(BaseContainer container) {
		this.container = container;
	}

	public void alignWithContainerEdge() {
		this.setPositionX(MinecraftClient.getInstance().window.getScaledWidth() / 2 - this.getSizeX() / 2);
		this.setPositionY(MinecraftClient.getInstance().window.getScaledHeight() / 2 - this.getSizeY() / 2);
	}

	/**
	 * Add Widget to 'linkedWidgets'.
	 * @param widget Widget to add.
	 */
	public <W extends Widget> W addWidget(W widget) {
		if (!this.linkedWidgets.contains(widget)) {
			widget.linkedPanel = this;
			this.linkedWidgets.add(widget);
		}
		return widget;
	}

	/**
	 * Remove Widget from 'linkedWidgets'.
	 * @param widget Widget to remove.
	 */
	public <W extends Widget> W removeWidget(W widget) {
		if (this.linkedWidgets.contains(widget)) {
			this.linkedWidgets.remove(widget);
		}
		return widget;
	}

	/**
	 * Find focused Widget in 'linkedWidgets'.
	 */
	public Optional<? extends Widget> getFocusedWidget() {
		Widget[] widgetArray = { null };
		linkedWidgets.forEach((widget) -> {
			if (widget.getFocus()) {
				widgetArray[0] = widget;
			}
		});
		if (widgetArray[0] != null) {
			return Optional.of(widgetArray[0]);
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Get 'linkedWidgets'.
	 */
	public List<? extends Widget> getLinkedWidgets() {
		return linkedWidgets;
	}

	@Override
	public void tick() {
		linkedWidgets.forEach(Widget::tick);
	}
}
