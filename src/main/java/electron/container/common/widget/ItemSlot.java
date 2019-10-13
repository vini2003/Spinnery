package electron.container.common.widget;

import electron.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;

public class ItemSlot extends Widget {
	public Slot internalSlot;

	public ItemSlot(int x, int y, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, Panel linkedPanel) {
		this.linkedPanel = linkedPanel;
		this.positionX = x;
		this.positionY = y;
		super.offsetX = x  + 2;
		super.offsetY = y - 1;
		this.sizeX = sizeX;
		this.sizeY = sizeY;

		if (linkedInventory != null && linkedPanel != null) {
			alignWithContainerEdge();

			linkedPanel.container.addSlot(internalSlot = new Slot(linkedInventory, slotNumber, offsetX, offsetY));
		}

	}

	@Override
	public void draw() {
		BaseRenderer.drawSlot(positionX, positionY);
	}
}
