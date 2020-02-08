package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.client.BaseRenderer;
import spinnery.common.BaseContainer;
import spinnery.registry.NetworkRegistry;
import spinnery.util.MouseUtilities;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;
import spinnery.widget.api.WSlotAction;

import java.util.Set;

@WFocusedMouseListener
public class WSlot extends WAbstractWidget {
	protected int slotNumber;
	protected Identifier previewTexture;
	protected int maximumCount = 0;
	protected boolean overrideMaximumCount = false;
	protected int inventoryNumber;
	protected boolean skipRelease = false;
	protected int containerSyncId = 0;

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
						.setSlotNumber(slotNumber + y * arrayWidth + x)
						.setInventoryNumber(inventoryNumber);
			}
		}
	}

	public static void addHeadlessPlayerInventory(WInterface linkedInterface) {
		int temporarySlotNumber = 0;
		addHeadlessArray(linkedInterface, temporarySlotNumber, BaseContainer.PLAYER_INVENTORY, 9, 1);
		temporarySlotNumber = 9;
		addHeadlessArray(linkedInterface, temporarySlotNumber, BaseContainer.PLAYER_INVENTORY, 9, 3);
	}

	public static void addHeadlessArray(WModifiableCollection parent, int slotNumber, int inventoryNumber, int arrayWidth, int arrayHeight) {
		for (int y = 0; y < arrayHeight; ++y) {
			for (int x = 0; x < arrayWidth; ++x) {
				parent.createChild(WSlot.class)
						.setSlotNumber(slotNumber + y * arrayWidth + x)
						.setInventoryNumber(inventoryNumber);
			}
		}
	}

	public int getMaxCount() {
		return maximumCount;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		containerSyncId = getInterface().getContainer().syncId;

		PlayerEntity playerEntity = getInterface().getContainer().getPlayerInventory().player;

		boolean isDragging = (!getInterface().getContainer().getSingleSlots().isEmpty()
						   || !getInterface().getContainer().getSplitSlots().isEmpty())
						   && System.nanoTime() - MouseUtilities.lastNanos() > MouseUtilities.nanoDelay();

		if (isDragging && !skipRelease) {
			Set<WSlot> dragList = mouseButton == 0 ? getInterface().getContainer().getSplitSlots() : mouseButton == 1 ? getInterface().getContainer().getSingleSlots() : null;

			WSlotAction action = mouseButton == 0 ? WSlotAction.DRAG_SPLIT : mouseButton == 1 ? WSlotAction.DRAG_SINGLE : null;

			getInterface().getContainer().onSlotDrag(dragList.stream().map(WSlot::getSlotNumber).mapToInt(i -> i).toArray(), dragList.stream().map(WSlot::getInventoryNumber).mapToInt(i -> i).toArray(), action);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_DRAG_PACKET, NetworkRegistry.createSlotDragPacket(containerSyncId, dragList.stream().map(WSlot::getSlotNumber).mapToInt(i -> i).toArray(), dragList.stream().map(WSlot::getInventoryNumber).mapToInt(i -> i).toArray(), action));

			getInterface().getContainer().getSplitSlots().clear();
			getInterface().getContainer().getSingleSlots().clear();
			getInterface().getContainer().getPreviewStacks().clear();
		} else if (!skipRelease && mouseButton == 0 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), 0, WSlotAction.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), 0, WSlotAction.PICKUP));
		} else if (!skipRelease && mouseButton == 1 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), 1, WSlotAction.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), 1, WSlotAction.PICKUP));
		}

		skipRelease = false;

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		containerSyncId = getInterface().getContainer().syncId;

		MouseUtilities.lastNanos = System.nanoTime();

		PlayerEntity playerEntity = getInterface().getContainer().getPlayerInventory().player;

		if (mouseButton == 0 && Screen.hasShiftDown() && getInterface().getCachedWidgets().get(WSlot.class) != this) {
			getInterface().getCachedWidgets().put(WSlot.class, this);
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), 0, WSlotAction.QUICK_MOVE, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), 0, WSlotAction.QUICK_MOVE));
		} else if (mouseButton == 0 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
			skipRelease = true;
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), 0, WSlotAction.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), 0, WSlotAction.PICKUP));
		} else if (mouseButton == 1 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
			skipRelease = true;
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), 1, WSlotAction.PICKUP, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), 1, WSlotAction.PICKUP));
		} else if (mouseButton == 2) {
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), 2, WSlotAction.CLONE, playerEntity);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), 2, WSlotAction.CLONE));
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		containerSyncId = getInterface().getContainer().syncId;

		if (Screen.hasShiftDown() && mouseButton == 0) {
			getInterface().getContainer().onSlotAction(getSlotNumber(), getInventoryNumber(), mouseButton, WSlotAction.QUICK_MOVE, MinecraftClient.getInstance().player);
			ClientSidePacketRegistry.INSTANCE.sendToServer(NetworkRegistry.SLOT_CLICK_PACKET, NetworkRegistry.createSlotClickPacket(containerSyncId, getSlotNumber(), getInventoryNumber(), mouseButton, WSlotAction.QUICK_MOVE));
		} else if (!Screen.hasShiftDown() && getInterface().getCachedWidgets().get(WSlot.class) != this && (mouseButton == 0 || mouseButton == 1) && System.nanoTime() - MouseUtilities.lastNanos() > MouseUtilities.nanoDelay()) {
			getInterface().getCachedWidgets().put(WSlot.class, this);

			Set<WSlot> dragList = mouseButton == 0 ? getInterface().getContainer().getSplitSlots() : getInterface().getContainer().getSingleSlots();

			dragList.add(this);

			getInterface().getContainer().onSlotDrag(dragList.stream().mapToInt(WSlot::getSlotNumber).toArray(),
													 dragList.stream().mapToInt(WSlot::getInventoryNumber).toArray(),
												     mouseButton == 0 ? WSlotAction.DRAG_SPLIT_PREVIEW : WSlotAction.DRAG_SINGLE_PREVIEW);
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

		ItemStack stackA = getPreviewStack().isEmpty() ? getStack() : getPreviewStack();

		RenderSystem.enableLighting();
		BaseRenderer.getItemRenderer().renderGuiItem(stackA, 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2);
		BaseRenderer.getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, stackA, 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2, stackA.getCount() == 1 ? "" : withSuffix(stackA.getCount()));
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
	public <W extends WSlot> W setPreviewTexture(Identifier previewTexture) {
		this.previewTexture = previewTexture;
		return (W) this;
	}

	public ItemStack getStack() {
		try {
			return getLinkedInventory().getInvStack(getSlotNumber());
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}

	public ItemStack getPreviewStack() {
		return getInterface().getContainer().getPreviewStacks().getOrDefault(getSlotNumber() + getInventoryNumber(), ItemStack.EMPTY);
	}

	public <W extends WSlot> W setPreviewStack(ItemStack previewStack) {
		getInterface().getContainer().getPreviewStacks().put(getSlotNumber() + getInventoryNumber(), previewStack);
		return (W) this;
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

	public <W extends WSlot> W setStack(ItemStack stack) {
		try {
			getLinkedInventory().setInvStack(slotNumber, stack);
			if (!isOverrideMaximumCount()) {
				setMaximumCount(stack.getMaxCount());
			}
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
		}
		return (W) this;
	}

	public boolean isOverrideMaximumCount() {
		return overrideMaximumCount;
	}

	public <W extends WSlot> W setMaximumCount(int maximumCount) {
		this.maximumCount = maximumCount;
		return (W) this;
	}

	public <W extends WSlot> W setOverrideMaximumCount(boolean overrideMaximumCount) {
		this.overrideMaximumCount = overrideMaximumCount;
		return (W) this;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	public <W extends WSlot> W setInventoryNumber(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
		return (W) this;
	}

	public <W extends WSlot> W setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
		return (W) this;
	}
}
