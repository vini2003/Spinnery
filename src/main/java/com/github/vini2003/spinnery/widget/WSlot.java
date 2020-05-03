package com.github.vini2003.spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import com.github.vini2003.spinnery.Spinnery;
import com.github.vini2003.spinnery.client.BaseRenderer;
import com.github.vini2003.spinnery.common.BaseContainer;
import com.github.vini2003.spinnery.widget.api.Action;
import com.github.vini2003.spinnery.widget.api.Position;
import com.github.vini2003.spinnery.widget.api.Size;
import com.github.vini2003.spinnery.widget.api.WModifiableCollection;

import java.util.*;
import java.util.function.BiConsumer;

import static com.github.vini2003.spinnery.registry.NetworkRegistry.*;
import static com.github.vini2003.spinnery.util.MouseUtilities.*;
import static com.github.vini2003.spinnery.widget.api.Action.*;

public class WSlot extends WAbstractWidget {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int MIDDLE = 2;
	protected int slotNumber;
	protected int inventoryNumber;
	protected int maximumCount = 0;
	protected boolean overrideMaximumCount = false;
	protected boolean skipRelease = false;
	protected boolean isLocked = false;
	protected boolean isWhitelist = false;
	protected ResourceLocation previewTexture;
	protected List<Item> acceptItems = new ArrayList<>();
	protected List<Item> denyItems = new ArrayList<>();
	protected List<Tag<Item>> acceptTags = new ArrayList<>();
	protected List<Tag<Item>> denyTags = new ArrayList<>();

	protected List<BiConsumer<Action, Action.Subtype>> consumers = new ArrayList<>();

	public void consume(Action action, Action.Subtype subtype) {
		consumers.forEach(actionConsumer -> actionConsumer.accept(action, subtype));
	}

	public <W extends WSlot> W addConsumer(BiConsumer<Action, Action.Subtype> consumer) {
		consumers.add(consumer);
		return (W) this;
	}

	public <W extends WSlot> W removeConsumer(BiConsumer<Action, Action.Subtype> consumer) {
		consumers.remove(consumer);
		return (W) this;
	}

	@OnlyIn(Dist.CLIENT)
	public static Collection<WSlot> addPlayerInventory(Position position, Size size, WModifiableCollection parent) {
		Collection<WSlot> set = addArray(position, size, parent, 9, BaseContainer.PLAYER_INVENTORY, 9, 3);
		set.addAll(addArray(position.add(0, size.getHeight() * 3 + 4, 0), size, parent, 0, BaseContainer.PLAYER_INVENTORY, 9, 1));
		return set;
	}

	@OnlyIn(Dist.CLIENT)
	public static Collection<WSlot> addArray(Position position, Size size, WModifiableCollection parent, int slotNumber, int inventoryNumber, int arrayWidth, int arrayHeight) {
		Collection<WSlot> set = new HashSet<>();
		for (int y = 0; y < arrayHeight; ++y) {
			for (int x = 0; x < arrayWidth; ++x) {
				set.add(parent.createChild(WSlot::new, position.add(size.getWidth() * x, size.getHeight() * y, 0), size)
						.setSlotNumber(slotNumber + y * arrayWidth + x)
						.setInventoryNumber(inventoryNumber));
			}
		}
		return set;
	}

	public static Collection<WSlot> addHeadlessPlayerInventory(WInterface linkedInterface) {
		Collection<WSlot> set = addHeadlessArray(linkedInterface, 0, BaseContainer.PLAYER_INVENTORY, 9, 1);
		set.addAll(addHeadlessArray(linkedInterface, 9, BaseContainer.PLAYER_INVENTORY, 9, 3));
		return set;
	}

	public static Collection<WSlot> addHeadlessArray(WModifiableCollection parent, int slotNumber, int inventoryNumber, int arrayWidth, int arrayHeight) {
		Collection<WSlot> set = new HashSet<>();
		for (int y = 0; y < arrayHeight; ++y) {
			for (int x = 0; x < arrayWidth; ++x) {
				set.add(parent.createChild(WSlot::new)
						.setSlotNumber(slotNumber + y * arrayWidth + x)
						.setInventoryNumber(inventoryNumber));
			}
		}
		return set;
	}

	public boolean isWhitelist() {
		return isWhitelist;
	}

	public <W extends WSlot> W setWhitelist() {
		this.isWhitelist = true;
		return (W) this;
	}

	public <W extends WSlot> W setBlacklist() {
		this.isWhitelist = false;
		return (W) this;
	}

	public <W extends WSlot> W accept(Tag<Item>... tags) {
		this.acceptTags.addAll(Arrays.asList(tags));
		return (W) this;
	}

	public <W extends WSlot> W accept(Item... stacks) {
		this.acceptItems.addAll(Arrays.asList(stacks));
		return (W) this;
	}

	public <W extends WSlot> W refuse(Tag<Item>... tags) {
		this.denyTags.addAll(Arrays.asList(tags));
		return (W) this;
	}

	public <W extends WSlot> W refuse(Item... items) {
		this.denyItems.addAll(Arrays.asList(items));
		return (W) this;
	}

	public boolean accepts(ItemStack... stacks) {
		if (!(Arrays.stream(stacks).allMatch(stack -> getLinkedInventory().isItemValidForSlot(slotNumber, stack)))) {
			return false;
		}
		if (isWhitelist) {
			return Arrays.stream(stacks).allMatch(stack ->
					(acceptItems.contains(stack.getItem()) || acceptTags.stream().anyMatch(tag -> tag.contains(stack.getItem()))));
		} else {
			return Arrays.stream(stacks).noneMatch(stack ->
					(denyItems.contains(stack.getItem()) || denyTags.stream().anyMatch(tag -> tag.contains(stack.getItem()))));
		}
	}

	public boolean refuses(ItemStack... stacks) {
		return !accepts(stacks);
	}

	public int getMaxStackSize() {
		return maximumCount;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
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

		if (hasPreviewTexture()) {
			BaseRenderer.drawImage(x + 1, y + 1, z, sX - 2, sY - 2, getPreviewTexture());
		}

		ItemStack stackA = getPreviewStack().isEmpty() ? getStack() : getPreviewStack();

		RenderSystem.translatef(0, 0, +250);
		RenderSystem.translatef(0, 0, -150);
		RenderSystem.enableLighting();
		BaseRenderer.getItemRenderer().renderItemAndEffectIntoGUI(stackA, 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2);
		BaseRenderer.getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, stackA, 1 + x + (sX - 18) / 2, 1 + y + (sY - 18) / 2, stackA.getCount() == 1 ? "" : withSuffix(stackA.getCount()));
		RenderSystem.disableLighting();
		RenderSystem.translatef(0, 0, +150);

		if (isFocused()) {
			BaseRenderer.drawRectangle(x + 1, y + 1, z + 1, sX - 2, sY - 2, getStyle().asColor("overlay"));
		}
		RenderSystem.translatef(0, 0, -250);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onMouseReleased(int mouseX, int mouseY, int button) {
		if (button == MIDDLE || isLocked()) return;

		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		int[] slotNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getSlotNumber).toArray();
		int[] inventoryNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getInventoryNumber).toArray();

		boolean isDragging = container.isDragging() && nanoInterval() > nanoDelay();
		boolean isCursorEmpty = player.inventory.getItemStack().isEmpty();

		if (!skipRelease && !Screen.hasShiftDown()) {
			if (isDragging) {
				container.onSlotDrag(slotNumbers, inventoryNumbers, Action.of(button, true));
				INSTANCE.sendToServer(createSlotDragPacket(container.windowId, slotNumbers, inventoryNumbers, Action.of(button, true)));
			} else if (!isFocused()) {
				return;
			} else if ((button == LEFT || button == RIGHT) && !isCursorEmpty) {
				container.onSlotAction(slotNumber, inventoryNumber, button, PICKUP, player);
				INSTANCE.sendToServer(createSlotClickPacket(container.windowId, slotNumber, inventoryNumber, button, PICKUP));
			}
		}

		container.flush();

		skipRelease = false;

		super.onMouseReleased(mouseX, mouseY, button);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onMouseClicked(int mouseX, int mouseY, int button) {
		if (!isFocused() || isLocked()) return;

		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		boolean isCursorEmpty = player.inventory.getItemStack().isEmpty();

		if (nanoInterval() < nanoDelay() * 1.25f && button == LEFT) {
			skipRelease = true;
			container.onSlotAction(slotNumber, inventoryNumber, button, PICKUP_ALL, player);
			INSTANCE.sendToServer(createSlotClickPacket(container.windowId, slotNumber, inventoryNumber, button, PICKUP_ALL));
		} else {
			nanoUpdate();

			if (Screen.hasShiftDown()) {
				if (button == LEFT) {
					getInterface().getCachedWidgets().put(getClass(), this);
					container.onSlotAction(slotNumber, inventoryNumber, button, QUICK_MOVE, player);
					INSTANCE.sendToServer(createSlotClickPacket(container.windowId, slotNumber, inventoryNumber, button, QUICK_MOVE));
				}
			} else {
				if ((button == LEFT || button == RIGHT) && isCursorEmpty) {
					skipRelease = true;
					container.onSlotAction(slotNumber, inventoryNumber, button, PICKUP, player);
					INSTANCE.sendToServer(createSlotClickPacket(container.windowId, slotNumber, inventoryNumber, button, PICKUP));
				} else if (button == MIDDLE) {
					container.onSlotAction(slotNumber, inventoryNumber, button, CLONE, player);
					INSTANCE.sendToServer(createSlotClickPacket(container.windowId, slotNumber, inventoryNumber, button, CLONE));
				}
			}
		}

		super.onMouseClicked(mouseX, mouseY, button);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onMouseDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
		if (!isFocused() || button == MIDDLE || isLocked()) return;

		PlayerEntity player = getInterface().getContainer().getPlayerInventory().player;
		BaseContainer container = getInterface().getContainer();

		boolean isCached = getInterface().getCachedWidgets().get(getClass()) == this;

		int[] slotNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getSlotNumber).toArray();
		int[] inventoryNumbers = container.getDragSlots(button).stream().mapToInt(WSlot::getInventoryNumber).toArray();

		if (Screen.hasShiftDown()) {
			if (button == LEFT && !isCached) {
				getInterface().getCachedWidgets().put(getClass(), this);
				container.onSlotAction(slotNumber, inventoryNumber, button, QUICK_MOVE, player);
				INSTANCE.sendToServer(createSlotClickPacket(container.windowId, slotNumber, inventoryNumber, button, QUICK_MOVE));
			}
		} else {
			if ((button == LEFT || button == RIGHT) && nanoInterval() > nanoDelay()) {
				if (!container.getDragSlots(button).isEmpty()) {
					ItemStack stackA = container.getDragSlots(button).iterator().next().getStack();
					ItemStack stackB = getStack();

					if ((stackA.getItem() != stackB.getItem() || stackA.getTag() != stackB.getTag())) return;
				}

				container.getDragSlots(button).add(this);
				container.onSlotDrag(slotNumbers, inventoryNumbers, Action.of(button, false));
			}
		}

		super.onMouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	public boolean isLocked() {
		return isLocked;
	}

	public <W extends WSlot> W setLocked(boolean isLocked) {
		this.isLocked = isLocked;
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

	@OnlyIn(Dist.CLIENT)
	public boolean hasPreviewTexture() {
		return previewTexture != null;
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getPreviewTexture() {
		return previewTexture;
	}

	@OnlyIn(Dist.CLIENT)
	public <W extends WSlot> W setPreviewTexture(ResourceLocation previewTexture) {
		this.previewTexture = previewTexture;
		return (W) this;
	}

	public ItemStack getPreviewStack() {
		getInterface().getContainer().getPreviewStacks().putIfAbsent(getInventoryNumber(), new HashMap<>());
		return getInterface().getContainer().getPreviewStacks().get(getInventoryNumber()).getOrDefault(getSlotNumber(), ItemStack.EMPTY);
	}

	public ItemStack getStack() {
		try {
			ItemStack stackA = getLinkedInventory().getStackInSlot(getSlotNumber());
			;
			if (!isOverrideMaximumCount()) {
				setMaximumCount(stackA.getMaxStackSize());
			}
			return stackA;
		} catch (ArrayIndexOutOfBoundsException exception) {
			Spinnery.LOGGER.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static String withSuffix(long value) {
		if (value < 1000) return "" + value;
		int exp = (int) (Math.log(value) / Math.log(1000));
		return String.format("%.1f%c", value / Math.pow(1000, exp), "KMGTPE".charAt(exp - 1));
	}

	public IInventory getLinkedInventory() {
		return getInterface().getContainer().getInventories().get(inventoryNumber);
	}

	public <W extends WSlot> W setStack(ItemStack stack) {
		try {
			getLinkedInventory().setInventorySlotContents(slotNumber, stack);
			if (!isOverrideMaximumCount()) {
				setMaximumCount(stack.getMaxStackSize());
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

	public <W extends WSlot> W setPreviewStack(ItemStack previewStack) {
		getInterface().getContainer().getPreviewStacks().putIfAbsent(getInventoryNumber(), new HashMap<>());
		getInterface().getContainer().getPreviewStacks().get(getInventoryNumber()).put(getSlotNumber(), previewStack);
		return (W) this;
	}

	public static <W extends WSlot> W setStack(WSlot slot, ItemStack stack) {
		slot.setStack(stack);
		return (W) slot;
	}

	public void acceptStack(ItemStack itemStack) {
		setStack(itemStack);
	}
}
