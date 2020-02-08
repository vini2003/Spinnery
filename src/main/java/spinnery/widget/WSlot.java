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

import static spinnery.widget.api.WSlotAction.*;

import spinnery.util.MouseUtilities;
import spinnery.widget.api.WFocusedMouseListener;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;
import spinnery.widget.api.WSlotAction;

@WFocusedMouseListener
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
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		int id = container.syncId;
		int slot = getSlotNumber();
		int inventory = getInventoryNumber();

		int[] slotNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getSlotNumber).toArray();
		int[] inventoryNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getInventoryNumber).toArray();

		boolean isDragging = container.isDragging() && MouseUtilities.nanoInterval() > MouseUtilities.nanoDelay();
		boolean isCursorEmpty = player.inventory.getCursorStack().isEmpty();

		if (!skipRelease && !Screen.hasShiftDown()) {
			if (isDragging) {
				container.onSlotDrag(slotNumbers, inventoryNumbers, WSlotAction.of(button, true));
				INSTANCE.sendToServer(SLOT_DRAG_PACKET, createSlotDragPacket(id, slotNumbers, inventoryNumbers, WSlotAction.of(button, true)));
			} else if ((button == LEFT || button == RIGHT) && !isCursorEmpty) {
				container.onSlotAction(slot, inventory, button, PICKUP, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(id, slot, inventory, button, PICKUP));
			}
		}

		container.flush();

		skipRelease = false;

		super.onMouseReleased(mouseX, mouseY, button);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int button) {
		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		int id = container.syncId;
		int slot = getSlotNumber();
		int inventory = getInventoryNumber();

		boolean isCached = getInterface().getCachedWidgets().get(WSlot.class) == this;
		boolean isCursorEmpty = player.inventory.getCursorStack().isEmpty();

		if (MouseUtilities.nanoInterval() < MouseUtilities.nanoDelay() * 1.25f && button == LEFT) {
			skipRelease = true;
			container.onSlotAction(slot, inventory, 0, PICKUP_ALL, player);
			INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(id, slot, inventory, button, PICKUP_ALL));
			return;
		}

		MouseUtilities.lastNanos(System.nanoTime());

		if (Screen.hasShiftDown()) {
			if (button == LEFT && !isCached) {
				getInterface().getCachedWidgets().put(WSlot.class, this);
				container.onSlotAction(slot, inventory, button, QUICK_MOVE, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(id, slot, inventory, button, QUICK_MOVE));
			}
		} else {
			if ((button == LEFT || button == RIGHT) && isCursorEmpty) {
				skipRelease = true;
				container.onSlotAction(slot, inventory, button, PICKUP, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(id, slot, inventory, button, PICKUP));
			} else if (button == MIDDLE) {
				container.onSlotAction(slot, inventory, button, CLONE, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(id, slot, inventory, button, CLONE));
			}
		}

		super.onMouseClicked(mouseX, mouseY, button);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		int id = getInterface().getContainer().syncId;
		int slot = getSlotNumber();
		int inventory = getInventoryNumber();

		boolean isCached = getInterface().getCachedWidgets().get(WSlot.class) == this;

		int[] slotNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getSlotNumber).toArray();
		int[] inventoryNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getInventoryNumber).toArray();

		if (Screen.hasShiftDown()) {
			if (button == LEFT) {
				container.onSlotAction(slot, inventory, button, QUICK_MOVE, player);
				INSTANCE.sendToServer(SLOT_CLICK_PACKET, createSlotClickPacket(id, slot, inventory, button, QUICK_MOVE));
			}
		} else  {
			if (!isCached && (button == LEFT || button == RIGHT) && MouseUtilities.nanoInterval() > MouseUtilities.nanoDelay()) {
				getInterface().getCachedWidgets().put(WSlot.class, this);
				container.getDragSlots(button).add(this);
				container.onSlotDrag(slotNumbers, inventoryNumbers, WSlotAction.of(button, false));
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
