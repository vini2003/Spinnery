package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;
import spinnery.SpinneryMod;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

import java.util.List;

public class WSlot extends WWidget {
	WSlot.Theme drawTheme;
	private int slotNumber;
	private ItemStack previewStack = ItemStack.EMPTY;
	private Inventory linkedInventory;

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
			SpinneryMod.logger.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}

	public void setStack(ItemStack stack) {
		try {
			getLinkedInventory().setInvStack(getSlotNumber(), stack);
		} catch (ArrayIndexOutOfBoundsException exception) {
			SpinneryMod.logger.log(Level.ERROR, "Cannot access slot " + getSlotNumber() + ", as it does exist in the inventory!");
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
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (getFocus() && !Screen.hasShiftDown()) {
			ItemStack stackA = getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().getCursorStack().copy();
			ItemStack stackB = getStack().copy();

			if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
				if (mouseButton == 0) {
					if (stackB.getCount() < stackB.getMaxCount()) {
						for (WWidget widget : getLinkedPanel().getLinkedWidgets()) {
							if (widget != this) {
								if (widget instanceof WSlot) {
									ItemStack stackC = ((WSlot) widget).getStack();

									if (stackB.getCount() < stackB.getMaxCount() && stackB.getItem() == stackC.getItem()) {
										int quantityA = stackB.getMaxCount() - stackB.getCount();

										int quantityB = stackC.getCount() - quantityA;

										if (quantityB <= 0) {
											stackB.increment(stackC.getCount());
											stackC.decrement(stackC.getCount());
										} else {
											stackB.increment(quantityA);
											stackC.decrement(quantityA);
										}
									}
								} else if (widget instanceof WList) {
									for (List listWidget : ((WList) widget).getListWidgets()) {
										for (Object internalWidget : listWidget) {
											if (internalWidget instanceof WSlot) {
												ItemStack stackC = ((WSlot) internalWidget).getStack();

												if (stackB.getCount() < stackB.getMaxCount() && stackB.getItem() == stackC.getItem()) {
													int quantityA = stackB.getMaxCount() - stackB.getCount();

													int quantityB = stackC.getCount() - quantityA;

													if (quantityB <= 0) {
														stackB.increment(stackC.getCount());
														stackC.decrement(stackC.getCount());
													} else {
														stackB.increment(quantityA);
														stackC.decrement(quantityA);
													}

												} else {
													setStack(stackB);
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				if (mouseButton == 2) {
					if (getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().player.isCreative()) {
						getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().setCursorStack(new ItemStack(stackB.getItem(), stackB.getMaxCount()));
						return;
					}
				} else if (stackA.isItemEqualIgnoreDamage(stackB)) {
					if (mouseButton == 0) {
						int quantityA = stackA.getCount(); // Cursor
						int quantityB = stackB.getCount(); // WSlot

						if (quantityA <= stackB.getMaxCount() - quantityB) {
							stackB.increment(quantityA);
							stackA.decrement(quantityA);
						} else {
							int quantityC = stackB.getMaxCount() - quantityB;

							stackB.increment(quantityC);
							stackA.decrement(quantityC);
						}
					} else if (mouseButton == 1) {
						stackA.decrement(1);
						stackB.increment(1);
					}
				} else if (! stackB.isEmpty() && stackA.isEmpty() && mouseButton == 1) {
					int quantityA = (int) Math.ceil(stackB.getCount() / 2f);

					stackA = new ItemStack(stackB.getItem(), quantityA);
					stackB.decrement(quantityA);
				} else if (stackB.isEmpty() && ! stackA.isEmpty() && mouseButton == 1) {
					stackB = new ItemStack(stackA.getItem(), 1);
					stackA.decrement(1);
				} else if (mouseButton == 0) {
					if (stackB.isEmpty()) {
						stackB = stackA.copy();
						stackA = ItemStack.EMPTY;
					} else {
						ItemStack stackC = stackB.copy();
						stackB = stackA.copy();
						stackA = stackC.copy();
					}
				}
			}
			getLinkedPanel().getLinkedContainer().getLinkedPlayerInventory().setCursorStack(stackA);
			setStack(stackB);
		} else if (getFocus() && Screen.hasShiftDown()) {
			for (WWidget wWidget : getLinkedPanel().getLinkedWidgets()) {
				if (wWidget instanceof WSlot && ((WSlot) wWidget).linkedInventory != linkedInventory) {
					ItemStack stackA = getStack();
					ItemStack stackB = ((WSlot) wWidget).getStack();

					if (!stackB.isEmpty() && stackA.isItemEqual(stackB)) {
						// stackA = 32
						// stackB = 48

						int maxA = stackA.getMaxCount();
						int maxB = stackB.getMaxCount();

						int countA = stackA.getCount();
						int countB = stackB.getCount();

						int availableB = maxB - countB;

						stackA.setCount(Math.max(countA - availableB, 0));
						stackB.increment(Math.min(countA, availableB));
						break;
					} else if (stackB.isEmpty()) {
						stackB = stackA.copy();
						stackA = ItemStack.EMPTY;
						break;
					}




				}
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (isWithinBounds(mouseX, mouseY) && Screen.hasShiftDown()) {
			if (!getLinkedPanel().getLinkedContainer().getDragSlots().contains(this)) {
				getLinkedPanel().getLinkedContainer().getDragSlots().add(this);
			}
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
