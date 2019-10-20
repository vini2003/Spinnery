package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

public class WSlot extends WWidget {
	public Slot internalSlot;

	public static void addSingle(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		linkedWPanel.addWidget(new WSlot(positionX, positionY, positionZ, sizeX, sizeY, slotNumber, linkedInventory, linkedWPanel));
	}

	public static void addArray(int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		for (int y = 0; y < arrayY; ++y) {
			for (int x = 0; x < arrayX; ++x) {
				WSlot.addSingle(positionX + (int) (sizeX * x), positionY + (int) (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, linkedInventory, linkedWPanel);
			}
		}
	}

	public static void addPlayerInventory(int positionZ, double sizeX, double sizeY, PlayerInventory linkedInventory, WPanel linkedWPanel) {
		int slotN = 0;
		addArray(9,
				1,
				6,
				(int) linkedWPanel.getSizeY() - 18 - 3,
				positionZ,
				sizeX,
				sizeY,
				slotN,
				linkedInventory,
				linkedWPanel);
		slotN = 9;
		addArray(9,
				 3,
				 6,
				(int) linkedWPanel.getSizeY() - 72 - 6,
				 positionZ,
				 sizeX,
				 sizeY,
				 slotN,
				 linkedInventory,
				linkedWPanel);
	}

	public WSlot(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		double fuckSlots1 = MinecraftClient.getInstance().window.getScaledWidth();
		double fuckSlots2 = MinecraftClient.getInstance().window.getScaledHeight();

		double fuckSlots3 = fuckSlots1 / 2 - linkedWPanel.getSizeX() / 2;
		double fuckSlots4 = fuckSlots2 / 2 - linkedWPanel.getSizeY() / 2;

		setPositionX(fuckSlots3 - 2 + positionX);
		setPositionY(fuckSlots4 + 0.5 + positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setLinkedPanel(linkedWPanel);

		getLinkedPanel().getLinkedContainer().addSlot(internalSlot = new Slot(linkedInventory, slotNumber, (int) offsetX, (int) offsetY));

		getSlot().xPosition = positionX;
		getSlot().yPosition = positionY;;
	}

	@Override
	public void alignWithContainerCenter() {
		getSlot().xPosition = (int) (linkedWPanel.getSizeX() / 2);
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
