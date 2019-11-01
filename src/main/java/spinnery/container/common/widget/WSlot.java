package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

/**
 * Represents a slot widget.
 */
public class WSlot extends WWidget {
	protected Slot internalSlot;

	public static void addSingle(WPanel linkedWPanel, WAlignment alignment, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory) {
		linkedWPanel.addWidget(new WSlot(linkedWPanel, alignment, positionX, positionY, positionZ, sizeX,	sizeY, slotNumber, linkedInventory));
	}

	public static void addArray(WPanel linkedWPanel, WAlignment alignment, int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory) {
		for (int y = 0; y < arrayY; ++y) {
			for (int x = 0; x < arrayX; ++x) {
				WSlot.addSingle(linkedWPanel, alignment, positionX + (int) (sizeX * x), positionY + (int) (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, linkedInventory);
			}
		}
	}

	public static void addPlayerInventory(WPanel linkedWPanel, int positionZ, double sizeX, double sizeY, PlayerInventory linkedInventory) {
		int slotN = 0;
		addArray(
				linkedWPanel,
				WAlignment.PANEL_TOP_LEFT,
				9,
				1,
				4,
				(int) linkedWPanel.getSizeY() - 18 - 4,
				positionZ,
				sizeX,
				sizeY,
				slotN,
				linkedInventory
		);
		slotN = 9;
		addArray(
				linkedWPanel,
				 WAlignment.PANEL_TOP_LEFT,
				 9,
				 3,
				 4,
				(int) linkedWPanel.getSizeY() - 72 - 6,
				 positionZ,
				 sizeX,
				 sizeY,
				 slotN,
				 linkedInventory
		);
	}

	public WSlot(WPanel linkedWPanel, WAlignment alignment, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory) {
		setLinkedPanel(linkedWPanel);

		setAlignment(alignment);

		getLinkedPanel().getLinkedContainer().addSlot(internalSlot = new Slot(linkedInventory, slotNumber, positionX + 1, positionY + 1));

		setPositionX(getPositionX() + positionX);
		setPositionY(getPositionY() + positionY);
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
				getSlot().yPosition = (int) ((Math.abs(positionY + (MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeY() / 2))) - 4);
			} else {
				getSlot().yPosition = (int) (-(Math.abs(positionY +(MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeY() / 2))) + 1);
			}
		}
	}

	@Override
	public boolean isFocused(double mouseX, double mouseY) {
		return super.isFocused(mouseX, mouseY);
	}

	public Slot getSlot() {
		return internalSlot;
	}

	public void setSlot(Slot internalSlot) {
		this.internalSlot = internalSlot;
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawSlot((int) getPositionX(), (int) getPositionY(), getPositionZ());
	}
}
