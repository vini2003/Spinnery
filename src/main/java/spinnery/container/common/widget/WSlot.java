package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

public class WSlot extends WWidget {
	public Slot internalSlot;
	public boolean isMemberOfList = false;

	public static void addSingle(WAlignment alignment, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		linkedWPanel.addWidget(new WSlot(alignment, positionX, positionY, positionZ, sizeX, sizeY, slotNumber, linkedInventory, linkedWPanel));
	}

	public static void addArray(WAlignment alignment, int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		for (int y = 0; y < arrayY; ++y) {
			for (int x = 0; x < arrayX; ++x) {
				WSlot.addSingle(alignment, positionX + (int) (sizeX * x), positionY + (int) (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, linkedInventory, linkedWPanel);
			}
		}
	}

	public static void addPlayerInventory(int positionZ, double sizeX, double sizeY, PlayerInventory linkedInventory, WPanel linkedWPanel) {
		int slotN = 0;
		addArray(
				WAlignment.PANEL_TOP_LEFT,
				9,
				1,
				4,
				(int) linkedWPanel.getSizeY() - 18 - 4,
				positionZ,
				sizeX,
				sizeY,
				slotN,
				linkedInventory,
				linkedWPanel);
		slotN = 9;
		addArray(
				 WAlignment.PANEL_TOP_LEFT,
				 9,
				 3,
				 4,
				(int) linkedWPanel.getSizeY() - 72 - 6,
				 positionZ,
				 sizeX,
				 sizeY,
				 slotN,
				 linkedInventory,
				 linkedWPanel);
	}

	public WSlot(WAlignment alignment, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAlignment(alignment);

		getLinkedPanel().getLinkedContainer().addSlot(internalSlot = new Slot(linkedInventory, slotNumber, positionX, positionY));

		setPositionX(getPositionX() + positionX);
		setPositionY(getPositionY() + positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);
	}

	/**
	 * This function is cursed.
	 * Please, if you have any idea how to fix it, tell me.
	 * @param positionX
	 */
	@Override
	public void setPositionX(double positionX) {
		if (!isHidden) {
			super.setPositionX(positionX);
			if (getSlot() != null) {
				if (getPositionX() < MinecraftClient.getInstance().window.getScaledWidth() / 2f - linkedWPanel.getSizeX() / 2f) {
					getSlot().xPosition = (int) (-(Math.abs(positionX - (MinecraftClient.getInstance().window.getScaledWidth() / 2f - linkedWPanel.getSizeX() / 2f))));
				} else {
					getSlot().xPosition = (int) ((Math.abs(positionX - (MinecraftClient.getInstance().window.getScaledWidth() / 2f - linkedWPanel.getSizeX() / 2f))));
				}
			}
		}
	}

	/**
	 * This function is cursed.
	 * Please, if you have any idea how to fix it, tell me.
	 * @param positionY
	 */
	@Override
	public void setPositionY(double positionY) {
		if (!isHidden) {
			super.setPositionY(positionY);
			if (getSlot() != null) {
				if (isMemberOfList) {
					if (getPositionY() < MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeX() / 2f) {
						getSlot().yPosition = (int) ((Math.abs(positionY + (MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeY() / 2f)))) - 3;
					} else {
						getSlot().yPosition = (int) ((Math.abs(positionY + (MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeY() / 2f)))) - 4;
					}
				} else {
					if (getPositionY() < MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeX() / 2f) {
						getSlot().yPosition = (int) ((Math.abs(positionY + (MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeY() / 2f)))) - 4;
					} else {
						getSlot().yPosition = (int) ((Math.abs(positionY + (MinecraftClient.getInstance().window.getScaledHeight() / 2f - linkedWPanel.getSizeY() / 2f)))) - 5;
					}
				}
			}
		}
	}

	@Override
	public void setHidden(boolean isHidden) {
		super.setHidden(isHidden);
		if (isHidden) {
			internalSlot.xPosition = Integer.MAX_VALUE;
			internalSlot.yPosition = Integer.MAX_VALUE;
		} else {
			setPositionX(getPositionX());
			setPositionY(getPositionY());
		}
	}

	@Override
	public boolean scanFocus(double mouseX, double mouseY) {
		return super.scanFocus(mouseX, mouseY);
	}

	public Slot getSlot() {
		return internalSlot;
	}

	public void setSlot(Slot internalSlot) {
		this.internalSlot = internalSlot;
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawSlot((int) getPositionX() - 1, (int) getPositionY() - 1, getPositionZ());
	}
}
