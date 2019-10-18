package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

public class ItemSlot extends Widget {
	public Slot internalSlot;

	public static void addSingle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, Panel linkedPanel) {
		linkedPanel.addWidget(new ItemSlot(positionX, positionY, positionZ, sizeX, sizeY, slotNumber, linkedInventory, linkedPanel));
	}

	public static void addArray(int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, Panel linkedPanel) {
		for (int y = 0; y < arrayY; ++y) {
			for (int x = 0; x < arrayX; ++x) {
				ItemSlot.addSingle(positionX + (int) (sizeX * x), positionY + (int) (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, linkedInventory, linkedPanel);
			}
		}
	}

	public static void addPlayerInventory(int positionZ, double sizeX, double sizeY, PlayerInventory linkedInventory, Panel linkedPanel) {
		int slotN = 0;
		addArray(9,
				1,
				6,
				(int) linkedPanel.getSizeY() - 18 - 3,
				positionZ,
				sizeX,
				sizeY,
				slotN,
				linkedInventory,
				linkedPanel);
		slotN = 9;
		addArray(9,
				 3,
				 6,
				(int) linkedPanel.getSizeY() - 72 - 6,
				 positionZ,
				 sizeX,
				 sizeY,
				 slotN,
				 linkedInventory,
				 linkedPanel);
	}

	public ItemSlot(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, Panel linkedPanel) {
		double fuckSlots1 = MinecraftClient.getInstance().window.getScaledWidth();
		double fuckSlots2 = MinecraftClient.getInstance().window.getScaledHeight();

		double fuckSlots3 = fuckSlots1 / 2 - linkedPanel.getSizeX() / 2;
		double fuckSlots4 = fuckSlots2 / 2 - linkedPanel.getSizeY() / 2;

		setPositionX(fuckSlots3 - 2 + positionX);
		setPositionY(fuckSlots4 + 0.5 + positionY);
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
