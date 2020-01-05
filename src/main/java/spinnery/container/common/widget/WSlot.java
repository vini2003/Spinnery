package spinnery.container.common.widget;

import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;
import spinnery.SpinneryMod;
import spinnery.container.client.BaseRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

import java.util.List;

public class WSlot extends WWidget {
	public boolean isMemberOfList = false;
	public int slotNumber;
	public ItemStack previewStack = ItemStack.EMPTY;
	public Inventory linkedInventory;
	long lastClick = System.nanoTime();

	public static void addSingle(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		linkedWPanel.addWidget(new WSlot(anchor, positionX, positionY, positionZ, sizeX, sizeY, slotNumber, linkedInventory, linkedWPanel));
	}

	public static void addArray(WAnchor anchor, int arrayX, int arrayY, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		for (int y = 0; y < arrayY; ++y) {
			for (int x = 0; x < arrayX; ++x) {
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

	public WSlot(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, double sizeY, int slotNumber, Inventory linkedInventory, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAlignment(anchor);

		setPositionX(positionX + (anchor == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (anchor == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		this.slotNumber = slotNumber;
		this.linkedInventory = linkedInventory;

		setSizeX(sizeX);
		setSizeY(sizeY);

	}

	public void setStack(ItemStack stack) {
		try {
			linkedInventory.setInvStack(slotNumber, stack);
		} catch (ArrayIndexOutOfBoundsException exception) {
			SpinneryMod.logger.log(Level.ERROR, "Cannot access slot " + slotNumber + ", as it does exist in the inventory!");
		}
	}

	public ItemStack getStack() {
		try {
			return linkedInventory.getInvStack(slotNumber);
		} catch (ArrayIndexOutOfBoundsException exception) {
			SpinneryMod.logger.log(Level.ERROR, "Cannot access slot " + slotNumber + ", as it does exist in the inventory!");
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (hasFocus) {
			PlayerInventory playerInventory = MinecraftClient.getInstance().player.inventory;

			ItemStack stackA = playerInventory.getCursorStack().copy();
			ItemStack stackB = getStack().copy();

			if (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
				if (mouseButton == 0) {
					if (stackB.getCount() < stackB.getMaxCount()) {
						for (WWidget widget : linkedWPanel.getLinkedWidgets()) {
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
								} else if (widget instanceof WSlotList) {
									for (List listWidget : ((WSlotList) widget).listWidgets) {
										for (Object internalWidget : listWidget) {
											ItemStack stackC = ((WSlot) internalWidget).getStack();

											if (stackB.getCount() < stackB.getMaxCount() && stackB.isItemEqualIgnoreDamage(stackC)) {
												int quantityA = stackB.getMaxCount() - stackB.getCount();

												stackC.decrement(quantityA);

												if (stackC.getCount() <= 0) {
													stackB.increment(stackC.getCount());
													stackC = ItemStack.EMPTY;
												} else {
													stackB.increment(quantityA);
													stackC.decrement(quantityA);
												}
											} else {
												setStack(stackB);

												lastClick = System.nanoTime();

												return;
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				lastClick = System.nanoTime();
				if (mouseButton == 2) {
					if (MinecraftClient.getInstance().player.isCreative()) {
						playerInventory.setCursorStack(new ItemStack(stackB.getItem(), stackB.getMaxCount()));
						return;
					}
				} else if (stackA.isItemEqualIgnoreDamage(stackB)) {

					if (mouseButton == 0) {
						int quantityA = stackA.getCount(); // cursor
						int quantityB = stackB.getCount(); // slot

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
				} else if (!stackB.isEmpty() && stackA.isEmpty() && mouseButton == 1) {
					int quantityA = (int) Math.ceil(stackB.getCount() / 2f);

					stackA = new ItemStack(stackB.getItem(), quantityA);
					stackB.decrement(quantityA);
				} else if (stackB.isEmpty() && !stackA.isEmpty() && mouseButton == 1){
					stackB = new ItemStack(stackA.getItem(), 1);
					stackA.decrement(1);
				} else if (mouseButton == 0) {
					if (stackA.isEmpty()) {
						stackA = stackB.copy();
						stackB = ItemStack.EMPTY;
					} else {
						stackB = stackA.copy();
						stackA = ItemStack.EMPTY;
					}
				}
			}
			playerInventory.setCursorStack(stackA);
			setStack(stackB);

			lastClick = System.nanoTime();
		}
	}

	@Override
	public void onMouseDragged(double mouseX, double mouseY, int mouseButton, double dragOffsetX, double dragOffsetY) {
		if (isWithinBounds(mouseX, mouseY) && InputUtil.isKeyPressed(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			if (!getLinkedPanel().getLinkedContainer().dragSlots.contains(this)) {
				getLinkedPanel().getLinkedContainer().dragSlots.add(this);
			}
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, dragOffsetX, dragOffsetY);
	}

	@Override
	public void drawWidget() {
		BaseRenderer.drawBeveledPanel(positionX, positionY, positionZ, sizeX, sizeY, 0xFF373737, hasFocus ? 0xFF00C116 : 0xFF8b8b8b, 0xFFFFFFFF);
		
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		GuiLighting.enableForItems();
		itemRenderer.renderGuiItem(previewStack.isEmpty() ? getStack() : previewStack, 1 + (int) (positionX + (sizeX - 18) / 2), 1 + (int) (positionY + (sizeY - 18) / 2));
		itemRenderer.renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, previewStack.isEmpty() ? getStack() : previewStack, 1 + (int) (positionX + (sizeX - 18) / 2), 1 + (int) (positionY + (sizeY - 18) / 2));
		GuiLighting.disable();
	}
}
