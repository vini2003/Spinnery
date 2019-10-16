package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;

public class ItemSlot extends Widget {
	public Slot internalSlot;

	public ItemSlot(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, Panel linkedPanel) {
		double fuckSlots1 = MinecraftClient.getInstance().window.getScaledWidth();
		double fuckSlots2 = MinecraftClient.getInstance().window.getScaledHeight();

		double fuckSlots3 = fuckSlots1 / 2 - linkedPanel.getSizeX() / 2;
		double fuckSlots4 = fuckSlots2 / 2 - linkedPanel.getSizeX() / 2;

		double fuckSlots5 = fuckSlots3 + positionX + 1;
		double fuckSlots6 = (int) (fuckSlots4 + positionY + sizeX / 2);

		setPositionX(fuckSlots5);
		setPositionY(fuckSlots6);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLinkedPanel(linkedPanel);

		getLinkedPanel().getLinkedContainer().addSlot(internalSlot = new Slot(linkedInventory, slotNumber, (int) offsetX, (int) offsetY));

		getSlot().xPosition = positionX;
		getSlot().yPosition = positionY;
	}

	@Override
	public void alignWithContainerCenter() {
		getSlot().xPosition = (int) (linkedPanel.getSizeX() / 2);
		super.alignWithContainerCenter();
	}

	public Slot getSlot() {
		return internalSlot;
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawSlot(positionX, positionY, positionZ);
	}
}
