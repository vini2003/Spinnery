package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

import static net.fabricmc.fabric.api.network.ClientSidePacketRegistry.INSTANCE;

import static spinnery.registry.NetworkRegistry.SLOT_CLICK_PACKET;
import static spinnery.registry.NetworkRegistry.SLOT_DRAG_PACKET;

import static spinnery.registry.NetworkRegistry.createSlotClickPacket;
import static spinnery.registry.NetworkRegistry.createSlotDragPacket;

import static spinnery.util.MouseUtilities.nanoDelay;
import static spinnery.util.MouseUtilities.nanoInterval;
import static spinnery.util.MouseUtilities.nanoUpdate;

import static spinnery.widget.api.Action.*;

import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.Action;

import java.util.HashMap;

public class WSlot extends WAbstractWidget {
	protected int slotNumber;
	protected Identifier previewTexture;
	protected int maximumCount = 0;
	protected boolean overrideMaximumCount = false;
	protected int inventoryNumber;
	protected boolean skipRelease = false;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int MIDDLE = 2;

	@Environment(EnvType.CLIENT)
	public static void addPlayerInventory(Position position, Size size, WModifiableCollection parent) {
		addArray(position, size, parent, 9, BaseContainer.PLAYER_INVENTORY, 9, 3);
		addArray(position.add(0, size.getHeight() * 3 + 3, 0), size, parent, 0, BaseContainer.PLAYER_INVENTORY, 9, 1);
	}

	@Environment(EnvType.CLIENT)
	public static void addArray(Position position, Size size, WModifiableCollection parent, int slotNumber, int inventoryNumber, int arrayWidth, int arrayHeight) {
		for (int y = 0; y < arrayHeight; ++y) {
			for (int x = 0; x < arrayWidth; ++x) {
				parent.createChild(WSlot.class, position.add(size.getWidth() * x, size.getHeight() * y, 0), size)
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
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		if (button == MIDDLE) return;

		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		int[] slotNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getSlotNumber).toArray();
		int[] inventoryNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getInventoryNumber).toArray();

		boolean isDragging = container.isDragging() && nanoInterval() > nanoDelay();
		boolean isCursorEmpty = player.inventory.getCursorStack().isEmpty();

		if (!skipRelease && !Screen.hasShiftDown()) {
			if (isDragging) {
				container.onSlotDrag(slotNumbers, inventoryNumbers, Action.of(button, true));
				INSTANCE.sendToServer(SLOT_DRAG_PACKET, createSlotDragPacket(container.syncId, slotNumbers, inventoryNumbers, Action.of(button, true)));
			} else if (!isFocused()) {
				return;
			} else if ((button == LEFT || button == RIGHT) && !isCursorEmpty) {
				container.onSlotAction(slotNumber, inventoryNumber, button, PICKUP, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(container.syncId, slotNumber, inventoryNumber, button, PICKUP));
			}
		}

		container.flush();

		skipRelease = false;

		super.onMouseReleased(mouseX, mouseY, button);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int button) {
		if (!isFocused()) return;

		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		boolean isCursorEmpty = player.inventory.getCursorStack().isEmpty();

		if (nanoInterval() < nanoDelay() * 1.25f && button == LEFT) {
			skipRelease = true;
			container.onSlotAction(slotNumber, inventoryNumber, button, PICKUP_ALL, player);
			INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(container.syncId, slotNumber, inventoryNumber, button, PICKUP_ALL));
		} else {
			nanoUpdate();

			if (Screen.hasShiftDown()) {
				if (button == LEFT) {
					getInterface().getCachedWidgets().put(getClass(), this);
					container.onSlotAction(slotNumber, inventoryNumber, button, QUICK_MOVE, player);
					INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(container.syncId, slotNumber, inventoryNumber, button, QUICK_MOVE));
				}
			} else {
				if ((button == LEFT || button == RIGHT) && isCursorEmpty) {
					skipRelease = true;
					container.onSlotAction(slotNumber, inventoryNumber, button, PICKUP, player);
					INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(container.syncId, slotNumber, inventoryNumber, button, PICKUP));
				} else if (button == MIDDLE) {
					container.onSlotAction(slotNumber, inventoryNumber, button, CLONE, player);
					INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(container.syncId, slotNumber, inventoryNumber, button, CLONE));
				}
			}
		}

		super.onMouseClicked(mouseX, mouseY, button);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
		if (!isFocused() || button == MIDDLE) return;

		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		boolean isCached = getInterface().getCachedWidgets().get(getClass()) == this;

		int[] slotNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getSlotNumber).toArray();
		int[] inventoryNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getInventoryNumber).toArray();

		if (Screen.hasShiftDown()) {
			if (button == LEFT && !isCached) {
				getInterface().getCachedWidgets().put(getClass(), this);
				container.onSlotAction(slotNumber, inventoryNumber, button, QUICK_MOVE, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(container.syncId, slotNumber, inventoryNumber, button, QUICK_MOVE));
			}
		} else  {
			if ((button == LEFT || button == RIGHT) && nanoInterval() > nanoDelay()) {
				container.getDragSlots(button).add(this);
				container.onSlotDrag(slotNumbers, inventoryNumbers, Action.of(button, false));
			}
		}

		super.onMouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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

		if (isFocused()) {
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
		getInterface().getContainer().getPreviewStacks().putIfAbsent(getInventoryNumber(), new HashMap<>());
		return getInterface().getContainer().getPreviewStacks().get(getInventoryNumber()).getOrDefault(getSlotNumber(), ItemStack.EMPTY);
	}

	public <W extends WSlot> W setPreviewStack(ItemStack previewStack) {
		getInterface().getContainer().getPreviewStacks().putIfAbsent(getInventoryNumber(), new HashMap<>());
		getInterface().getContainer().getPreviewStacks().get(getInventoryNumber()).put(getSlotNumber(), previewStack);
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
