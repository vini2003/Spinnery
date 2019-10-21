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
				(int) (MinecraftClient.getInstance().window.getScaledWidth() / 2 - linkedWPanel.getSizeX() / 2) + 6,
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
				(int) (MinecraftClient.getInstance().window.getScaledHeight() / 2 - linkedWPanel.getSizeY() / 2) - 6,
				(int) linkedWPanel.getSizeY() - 72 - 6,
				 positionZ,
				 sizeX,
				 sizeY,
				 slotN,
				 linkedInventory,
				linkedWPanel);
	}

	public WSlot(int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		getLinkedPanel().getLinkedContainer().addSlot(internalSlot = new Slot(linkedInventory, slotNumber, (int) offsetX, (int) offsetY));

		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	@Override
	public void setPositionX(double positionX) {
		super.setPositionX(positionX);
		if (getSlot() != null) {
			if (getPositionX() < MinecraftClient.getInstance().window.getScaledWidth() / 2f - linkedWPanel.getSizeX() / 2) {
				getSlot().xPosition = (int) (-(Math.abs(positionX - (int) (MinecraftClient.getInstance().window.getScaledWidth() / 2 - linkedWPanel.getSizeX() / 2))) + 1);
			} else {
				getSlot().xPosition = (int) ((Math.abs(positionX - (int) (MinecraftClient.getInstance().window.getScaledWidth() / 2 - linkedWPanel.getSizeX() / 2))) + 1);
			}
		}
	}

	@Override
	public void setPositionY(double positionY) {
		super.setPositionY(positionY);
		if (getSlot() != null) {
			if (getPositionY() > MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeX() / 2) {
				getSlot().yPosition = (int) ((Math.abs(positionY + (int) (MinecraftClient.getInstance().window.getScaledHeight() / 2 - linkedWPanel.getSizeY() / 2))) - 3);
			} else {
				getSlot().yPosition = (int) (-(Math.abs(positionY + (int) (MinecraftClient.getInstance().window.getScaledHeight() / 2 - linkedWPanel.getSizeY() / 2))) - 3);
			}
		}
	}

	public Slot getSlot() {
		return internalSlot;
	}

	public void setSlot(Slot internalSlot) {
		this.internalSlot = internalSlot;
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawSlot(getPositionX(), getPositionY(), getPositionZ());
	}
}
