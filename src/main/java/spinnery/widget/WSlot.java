package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.client.BaseRenderer;
import spinnery.registry.NetworkRegistry;
import spinnery.registry.ResourceRegistry;

public class WSlot extends WWidget {
	protected WSlot.Theme drawTheme;
	protected int slotNumber;
	protected ItemStack previewStack = ItemStack.EMPTY;
	protected int inventoryNumber;
	protected boolean ignoreOnRelease = false;

	public WSlot(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, int inventoryNumber, WPanel linkedPanel) {
		setLinkedPanel(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");

		setSlotNumber(slotNumber);
		setInventoryNumber(inventoryNumber);
	}

	public static void addSingle(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, int inventoryNumber, WPanel linkedPanel) {
		linkedPanel.add(new WSlot(anchor, positionX, positionY, positionZ, sizeX, sizeY, slotNumber, inventoryNumber, linkedPanel));
	}

	public static void addArray(WAnchor anchor, int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, int inventoryNumber, WPanel linkedPanel) {
		for (int y = 0; y < arrayY; ++ y) {
			for (int x = 0; x < arrayX; ++ x) {
				WSlot.addSingle(anchor, positionX + (int) (sizeX * x), positionY + (int) (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, inventoryNumber, linkedPanel);
			}
		}
	}

	public static void addPlayerInventory(int positionZ, double sizeX, double sizeY, int inventoryNumber, WPanel linkedPanel) {
		int temporarySlotNumber = 0;
		addArray(
				WAnchor.MC_ORIGIN,
				9,
				1,
				4,
				(int) linkedPanel.getSizeY() - 18 - 4,
				positionZ,
				sizeX,
				sizeY,
				temporarySlotNumber,
				inventoryNumber,
				linkedPanel);
		temporarySlotNumber = 9;
		addArray(
				WAnchor.MC_ORIGIN,
				9,
				3,
				4,
				(int) linkedPanel.getSizeY() - 72 - 6,
				positionZ,
				sizeX,
				sizeY,
				temporarySlotNumber,
				inventoryNumber,
				linkedPanel);
	}

	public ItemStack getStack() {
		try {
			return getLinkedInventory().getInvStack(getSlotNumber());
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.logger.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}

	public void setStack(ItemStack stack) {
		try {
			getLinkedInventory().setInvStack(getSlotNumber(), stack);
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.logger.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
		}
	}

	public ItemStack getPreviewStack() {
		return previewStack;
	}

	public void setPreviewStack(ItemStack previewStack) {
		this.previewStack = previewStack;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public Inventory getLinkedInventory() {
		return getLinkedPanel().getLinkedContainer().getLinkedInventories().get(inventoryNumber);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (getFocus()) {
			PlayerEntity playerEntity = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player;

			if (! ignoreOnRelease && mouseButton == 0 && ! Screen.hasShiftDown() && ! playerEntity.inventory.getCursorStack().isEmpty()) {
				getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP));
			} else if (! ignoreOnRelease && mouseButton == 1 && ! Screen.hasShiftDown() && ! playerEntity.inventory.getCursorStack().isEmpty()) {
				getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP));
			}

			ignoreOnRelease = false;
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus()) {
			PlayerEntity playerEntity = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player;

			if (mouseButton == 0 && Screen.hasShiftDown()) {
				getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.QUICK_MOVE, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.QUICK_MOVE));
			} else if (mouseButton == 0 && ! Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
				ignoreOnRelease = true;
				getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP));
			} else if (mouseButton == 1 && ! Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
				ignoreOnRelease = true;
				getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP));
			} else if (mouseButton == 2) {
				getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 2, SlotActionType.CLONE, playerEntity);
				ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 2, SlotActionType.CLONE));
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (getFocus() && Screen.hasShiftDown() && mouseButton == 0) {
			PlayerEntity playerEntity = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player;

			getLinkedPanel().getLinkedContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), mouseButton, SlotActionType.QUICK_MOVE, MinecraftClient.getInstance().player);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), mouseButton, SlotActionType.QUICK_MOVE));
		}

		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWSlotTheme();
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

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, drawTheme.getTopLeft(), getFocus() ? drawTheme.getBackgroundFocused() : drawTheme.getBackgroundUnfocused(), drawTheme.getBottomRight());

		RenderSystem.enableLighting();

		BaseRenderer.getItemRenderer().renderGuiItem(getPreviewStack().isEmpty() ? getStack() : getPreviewStack(), 1 + (int) (x + (sX - 18) / 2), 1 + (int) (y + (sY - 18) / 2));
		BaseRenderer.getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, getPreviewStack().isEmpty() ? getStack() : getPreviewStack(), 1 + (int) (x + (sX - 18) / 2), 1 + (int) (y + (sY - 18) / 2));

		RenderSystem.disableLighting();
	}

	public static class Theme {
		transient private WColor topLeft;
		transient private WColor bottomRight;
		transient private WColor backgroundFocused;
		transient private WColor backgroundUnfocused;

		@SerializedName("top_left")
		private String rawTopLeft;

		@SerializedName("bottom_right")
		private String rawBottomRight;

		@SerializedName("background_focused")
		private String rawBackgroundFocused;

		@SerializedName("background_unfocused")
		private String rawBackgroundUnfocused;

		public void build() {
			topLeft = new WColor(rawTopLeft);
			bottomRight = new WColor(rawBottomRight);
			backgroundFocused = new WColor(rawBackgroundFocused);
			backgroundUnfocused = new WColor(rawBackgroundUnfocused);
		}


		public WColor getTopLeft() {
			return this.topLeft;
		}

		public WColor getBottomRight() {
			return this.bottomRight;
		}

		public WColor getBackgroundFocused() {
			return this.backgroundFocused;
		}

		public WColor getBackgroundUnfocused() {
			return this.backgroundUnfocused;
		}
	}
}
