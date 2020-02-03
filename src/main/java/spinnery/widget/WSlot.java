package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;
import spinnery.registry.NetworkRegistry;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

@WFocusedMouseListener
public class WSlot extends WWidget {
	protected int slotNumber;
	protected Identifier previewTexture;
	protected int maximumCount = 0;
	protected boolean overrideMaximumCount = false;
	protected int inventoryNumber;
	protected boolean ignoreOnRelease = false;

	public WSlot slot(int slotNumber) {
		this.slotNumber = slotNumber;
		return this;
	}

	public WSlot inventory(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
		return this;
	}

	public WSlot maximum(int maximumCount) {
		overrideMaximumCount = true;
		this.maximumCount = maximumCount;
		return this;
	}

	public WSlot preview(Identifier previewTexture) {
		this.previewTexture = previewTexture;
		return this;
	}

	@Environment(EnvType.CLIENT)
	public static void addPlayerInventory(WPosition position, WSize size, WModifiableCollection parent) {
		addArray(position, size, parent, 9, BaseContainer.PLAYER_INVENTORY, 9, 3);
		addArray(position.add(0, size.getHeight() * 3 + 3, 0), size, parent, 0, BaseContainer.PLAYER_INVENTORY, 9, 1);
	}

	@Environment(EnvType.CLIENT)
	public static void addArray(WPosition position, WSize size, WModifiableCollection parent, int slotNumber, int inventoryNumber, int arrayWidth, int arrayHeight) {
		for (int y = 0; y < arrayHeight; ++y) {
			for (int x = 0; x < arrayWidth; ++x) {
				parent.createChild(WSlot.class, WPosition.of(position.getX() + (size.getWidth() * x), position.getY() + (size.getHeight() * y), position.getZ()), size)
						.slot(slotNumber + y * arrayWidth + x)
						.inventory(inventoryNumber);
			}
		}
	}

	public static void addHeadlessPlayerInventory(WInterface linkedInterface, int inventoryNumber) {
		int temporarySlotNumber = 0;
		addHeadlessArray(linkedInterface, temporarySlotNumber, inventoryNumber, 9, 1);
		temporarySlotNumber = 9;
		addHeadlessArray(linkedInterface, temporarySlotNumber, inventoryNumber, 9, 3);
	}

	public static void addHeadlessArray(WModifiableCollection parent, int slotNumber, int inventoryNumber, int arrayWidth, int arrayHeight) {
		for (int y = 0; y < arrayHeight; ++y) {
			for (int x = 0; x < arrayWidth; ++x) {
				parent.createChild(WSlot.class).slot(slotNumber + y * arrayWidth + x).inventory(inventoryNumber);
			}
		}
	}

	public int getMaxCount() {
		return maximumCount;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		PlayerEntity playerEntity = getInterface().getContainer().getLinkedPlayerInventory().player;

		if (!ignoreOnRelease && mouseButton == 0 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
			if (Spinnery.IS_DEBUG) {
				System.out.println("\n" + SlotActionType.PICKUP + "\n");
			}
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP));
		} else if (!ignoreOnRelease && mouseButton == 1 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
			if (Spinnery.IS_DEBUG) {
				System.out.println("\n" + SlotActionType.PICKUP + "\n");
			}
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP));
		}

		ignoreOnRelease = false;

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		PlayerEntity playerEntity = getInterface().getContainer().getLinkedPlayerInventory().player;

		if (mouseButton == 0 && Screen.hasShiftDown() && getInterface().cachedWidgets.get(WSlot.class) != this) {
			getInterface().cachedWidgets.put(WSlot.class, this);
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.QUICK_MOVE, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.QUICK_MOVE));
		} else if (mouseButton == 0 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
			ignoreOnRelease = true;
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 0, SlotActionType.PICKUP));
		} else if (mouseButton == 1 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
			ignoreOnRelease = true;
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 1, SlotActionType.PICKUP));
		} else if (mouseButton == 2) {
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), 2, SlotActionType.CLONE, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), 2, SlotActionType.CLONE));
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (Screen.hasShiftDown() && mouseButton == 0) {
			if (Spinnery.IS_DEBUG) {
				System.out.println("\n" + SlotActionType.QUICK_MOVE + "\n");
			}
			getInterface().getContainer().onSlotClicked(getSlotNumber(), getInventoryNumber(), mouseButton, SlotActionType.QUICK_MOVE, MinecraftClient.getInstance().player);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(getSlotNumber(), getInventoryNumber(), mouseButton, SlotActionType.QUICK_MOVE));
		}

		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, getStyle().asColor("top_left"), getStyle().asColor("background.unfocused"), getStyle().asColor("bottom_right"));

		if (getFocus()) {
			BaseRenderer.drawRectangle(x + 1, y + 1, z, sX - 2, sY - 2, getStyle().asColor("background.focused"));
		}

		if (hasPreviewTexture()) {
			BaseRenderer.drawImage(x + 1, y + 1, z, sX - 2, sY - 2, getPreviewTexture());
		}

		RenderSystem.enableLighting();
		BaseRenderer.getItemRenderer().renderGuiItem(getStack(), 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2);
		BaseRenderer.getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, getStack(), 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2, getStack().getCount() == 1 ? "" : withSuffix(getStack().getCount()));
		RenderSystem.disableLighting();
	}

	@Environment(EnvType.CLIENT)
	public boolean hasPreviewTexture() {
		return previewTexture != null;
	}

	@Environment(EnvType.CLIENT)
	public Identifier getPreviewTexture() {
		return previewTexture;
	}

	@Environment(EnvType.CLIENT)
	public void setPreviewTexture(Identifier previewTexture) {
		this.previewTexture = previewTexture;
	}

	public ItemStack getStack() {
		try {
			return getLinkedInventory().getInvStack(getSlotNumber());
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}

	@Environment(EnvType.CLIENT)
	private static String withSuffix(long value) {
		if (value < 1000) return "" + value;
		int exp = (int) (Math.log(value) / Math.log(1000));
		return String.format("%.1f%c", value / Math.pow(1000, exp), "KMGTPE".charAt(exp - 1));
	}

	public Inventory getLinkedInventory() {
		return getInterface().getContainer().getInventories().get(inventoryNumber);
	}

	public void setStack(ItemStack stack) {
		try {
			getLinkedInventory().setInvStack(slotNumber, stack);
			if (!isOverrideMaximumCount()) {
				setMaximumCount(stack.getMaxCount());
			}
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
		}
	}

	public boolean isOverrideMaximumCount() {
		return overrideMaximumCount;
	}

	public void setMaximumCount(int maximumCount) {
		this.maximumCount = maximumCount;
	}

	public void setOverrideMaximumCount(boolean overrideMaximumCount) {
		this.overrideMaximumCount = overrideMaximumCount;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}
}
