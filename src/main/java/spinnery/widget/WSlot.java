package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import spinnery.Spinnery;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WSlot extends WWidget {
	WSlot.Theme drawTheme;
	private int slotNumber;
	private ItemStack previewStack = ItemStack.EMPTY;
	private Inventory linkedInventory;
	private boolean ignoreOnRelease = false;

	public WSlot(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setTheme("default");

		setSlotNumber(slotNumber);
		setLinkedInventory(linkedInventory);
	}

	public static void addSingle(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		linkedWPanel.add(new WSlot(anchor, positionX, positionY, positionZ, sizeX, sizeY, slotNumber, linkedInventory, linkedWPanel));
	}

	public static void addArray(WAnchor anchor, int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		for (int y = 0; y < arrayY; ++ y) {
			for (int x = 0; x < arrayX; ++ x) {
				WSlot.addSingle(anchor, positionX + (int) (sizeX * x), positionY + (int) (sizeY * y), positionZ, sizeX, sizeY, slotNumber++, linkedInventory, linkedWPanel);
			}
		}
	}

	public static void addPlayerInventory(int positionZ, double sizeX, double sizeY, PlayerInventory linkedInventory, WPanel linkedWPanel) {
		int temporarySlotNumber = 0;
		addArray(
				WAnchor.MC_ORIGIN,
				9,
				1,
				4,
				(int) linkedWPanel.getSizeY() - 18 - 4,
				positionZ,
				sizeX,
				sizeY,
				temporarySlotNumber,
				linkedInventory,
				linkedWPanel);
		temporarySlotNumber = 9;
		addArray(
				WAnchor.MC_ORIGIN,
				9,
				3,
				4,
				(int) linkedWPanel.getSizeY() - 72 - 6,
				positionZ,
				sizeX,
				sizeY,
				temporarySlotNumber,
				linkedInventory,
				linkedWPanel);
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

	public Inventory getLinkedInventory() {
		return linkedInventory;
	}

	public void setLinkedInventory(Inventory linkedInventory) {
		this.linkedInventory = linkedInventory;
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		if (getFocus()) {
			PlayerEntity playerEntity = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player;

			if (! ignoreOnRelease && mouseButton == 0 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
				getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), 0, SlotActionType.PICKUP, playerEntity);
			} else if (! ignoreOnRelease && mouseButton == 1 && !Screen.hasShiftDown() && !playerEntity.inventory.getCursorStack().isEmpty()) {
				getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), 1, SlotActionType.PICKUP, playerEntity);
			}

			ignoreOnRelease = false;
		}

		getLinkedPanel().getLinkedContainer().getDragSlots().clear();

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus()) {
			PlayerEntity playerEntity = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player;

			if (mouseButton == 0 && Screen.hasShiftDown()) {
				getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), 0, SlotActionType.QUICK_MOVE, playerEntity);
			} else if (mouseButton == 0 && !Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
				ignoreOnRelease = true;
				getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), 0, SlotActionType.PICKUP, playerEntity);
			} else if (mouseButton == 1 && ! Screen.hasShiftDown() && playerEntity.inventory.getCursorStack().isEmpty()) {
				ignoreOnRelease = true;
				getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), 1, SlotActionType.PICKUP, playerEntity);
			} else if (mouseButton == 2) {
				getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), 2, SlotActionType.CLONE, playerEntity);
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (getFocus() && Screen.hasShiftDown() && mouseButton == 0) {
			PlayerEntity playerEntity = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player;

			getLinkedPanel().getLinkedContainer().onSlotClick(getSlotNumber(), mouseButton, SlotActionType.QUICK_MOVE, MinecraftClient.getInstance().player);
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
