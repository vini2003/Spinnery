package com.github.vini2003.spinnery.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import com.github.vini2003.spinnery.client.BaseRenderer;
import com.github.vini2003.spinnery.widget.WAbstractWidget;
import com.github.vini2003.spinnery.widget.WInterface;
import com.github.vini2003.spinnery.widget.WSlot;
import com.github.vini2003.spinnery.widget.api.WCollection;
import com.github.vini2003.spinnery.widget.api.WContextLock;
import com.github.vini2003.spinnery.widget.api.WInterfaceProvider;

import java.awt.*;

public class BaseContainerScreen<T extends BaseContainer> extends ContainerScreen<T> implements WInterfaceProvider {
	protected final WInterface clientInterface;
	protected int tooltipX = 0;
	protected int tooltipY = 0;
	protected WSlot drawSlot;

	/**
	 * Instantiates a BaseContainerScreen.
	 *
	 * @param name            Name to be used for Narrator.
	 * @param linkedContainer Container associated with screen.
	 * @param player          Player associated with screen.
	 */
	@OnlyIn(Dist.CLIENT)
	public BaseContainerScreen(ITextComponent name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		clientInterface = new WInterface(linkedContainer);
	}

	/**
	 * Method called for every frame, where all of Spinnery rendering happens.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(int mouseX, int mouseY, float tickDelta) {
		clientInterface.draw();

		if (getDrawSlot() != null && getContainer().getPlayerInventory().getItemStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), getTooltipX(), getTooltipY());
		}

		ItemStack stackA;

		if (getContainer().getPreviewCursorStack().isEmpty()
				&& getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).isEmpty()
				&& getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).isEmpty()) {
			stackA = getContainer().getPlayerInventory().getItemStack();
		} else {
			stackA = getContainer().getPreviewCursorStack();
		}

		RenderSystem.pushMatrix();
		RenderSystem.translatef(0, 0, 200);
		BaseRenderer.getItemRenderer().renderItemAndEffectIntoGUI(stackA, mouseX - 8, mouseY - 8);
		BaseRenderer.getItemRenderer().renderItemOverlayIntoGUI(BaseRenderer.getTextRenderer(), stackA, mouseX - 8, mouseY - 8, null);
		RenderSystem.popMatrix();
	}

	/**
	 * Method deprecated and unsupported by Spinnery.
	 */
	@Deprecated
	@Override
	@OnlyIn(Dist.CLIENT)
	protected void drawGuiContainerBackgroundLayer(float tick, int mouseX, int mouseY) {
	}

	/**
	 * Method deprecated and unsupported by Spinnery.
	 */
	@Deprecated
	@Override
	@OnlyIn(Dist.CLIENT)
	protected boolean hasClickedOutside(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return clientInterface.getAllWidgets().stream().noneMatch(widget -> widget.isWithinBounds((int) mouseX, (int) mouseY));
	}

	/**
	 * Method called when a tooltip should be drawn over something, however, currently not implemented.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		clientInterface.onDrawMouseoverTooltip(mouseX, mouseY);

		super.renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	 * Method called when the mouse is clicked.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button clicked.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseClicked((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	/**
	 * Method called when the mouse is released.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button released.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseReleased((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	/**
	 * Method called when the mouse is moved.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void mouseMoved(double mouseX, double mouseY) {
		clientInterface.onMouseMoved((int) mouseX, (int) mouseY);

		updateTooltip((int) mouseX, (int) mouseY);
	}

	/**
	 * Method called when the mouse is dragged.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button dragged.
	 * @param deltaX      Horizontal delta of mouse drag.
	 * @param deltaY      Vertical delta of mouse drag.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		getInterface().onMouseDragged((int) mouseX, (int) mouseY, mouseButton, (int) deltaX, (int) deltaY);

		return false;
	}

	/**
	 * Method called when the mouse wheel is scrolled.
	 *
	 * @param mouseX Horizontal position of the mouse cursor.
	 * @param mouseY Vertical position of the mouse cursor.
	 * @param deltaY Vertical delta of mouse scroll.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		getInterface().onMouseScrolled((int) mouseX, (int) mouseY, deltaY);

		return false;
	}

	/**
	 * Method called when a keyboard key is pressed.
	 *
	 * @param keyCode     Keycode associated with pressed key.
	 * @param character   Character associated with pressed key.
	 * @param keyModifier Modifier(s) associated with pressed key.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean keyPressed(int keyCode, int character, int keyModifier) {
		clientInterface.onKeyPressed(keyCode, character, keyModifier);

		if (keyCode == GLFW.GLFW_KEY_ESCAPE || Minecraft.getInstance().gameSettings.keyBindInventory.matchesKey(keyCode, character)) {
			if (clientInterface.getAllWidgets().stream().noneMatch(widget -> widget instanceof WContextLock && ((WContextLock) widget).isActive())) {
				Minecraft.getInstance().player.closeScreen();
				return true;
			}
		}

		return false;
	}

	/**
	 * Method called when a keyboard key is released.
	 *
	 * @param keyCode     Keycode associated with released key.
	 * @param character   Character associated with released key.
	 * @param keyModifier Modifier(s) associated with released key.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getInterface().onKeyReleased(character, keyCode, keyModifier);

		return false;
	}

	/**
	 * Method called when a key with a valid associated character is called.
	 *
	 * @param character Character associated with key pressed.
	 * @param keyCode   Keycode associated with key pressed.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean charTyped(char character, int keyCode) {
		getInterface().onCharTyped(character, keyCode);

		return super.charTyped(character, keyCode);
	}


	/**
	 * Return true by default for simplicity of use.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isPauseScreen() {
		return false;
	}

	/**
	 * Self-explanatory.
	 */
	@Override
	public void tick() {
		getInterface().tick();
		super.tick();
	}

	/**
	 * Retrieves the container associated with this screen.
	 *
	 * @return Container associated with this screen.
	 */
	@OnlyIn(Dist.CLIENT)
	public T getContainer() {
		return super.container;
	}

	/**
	 * Retrieves the interface associated with this screen.
	 *
	 * @return Interface associated with this screen.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public WInterface getInterface() {
		return clientInterface;
	}

	/**
	 * Retrieves WSlot of which the tooltip will be rendered.
	 *
	 * @return WSlot of which the tooltip will be rendered.
	 */
	@OnlyIn(Dist.CLIENT)
	public WSlot getDrawSlot() {
		return drawSlot;
	}

	/**
	 * Sets the WSlot of which the tooltip will be rendered.
	 *
	 * @param drawSlot WSlot of which the tooltip will be rendered.
	 */
	@OnlyIn(Dist.CLIENT)
	public <S extends BaseContainerScreen> S setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
		return (S) this;
	}

	/**
	 * Retrieves the horizontal position at which the tooltip will be drawn.
	 *
	 * @return Horizontal position at which the tooltip will be drawn.
	 */
	@OnlyIn(Dist.CLIENT)
	public int getTooltipX() {
		return tooltipX;
	}

	/**
	 * Retrieves the vertical position at which the tooltip will be drawn.
	 *
	 * @return Vertical position at which the tooltip will be drawn.
	 */
	@OnlyIn(Dist.CLIENT)
	public int getTooltipY() {
		return tooltipY;
	}

	/**
	 * Sets the horizontal position at which the tooltip will be drawn.
	 *
	 * @param tooltipX Horizontal position at which the tooltip will be drawn.
	 */
	@OnlyIn(Dist.CLIENT)
	public <S extends BaseContainerScreen> S setTooltipX(int tooltipX) {
		this.tooltipX = tooltipX;
		return (S) this;
	}

	/**
	 * Sets the vertical position at which the tooltip will be drawn.
	 *
	 * @param tooltipY Vertical position at which the tooltip will be drawn.
	 */
	@OnlyIn(Dist.CLIENT)
	public <S extends BaseContainerScreen> S setTooltipY(int tooltipY) {
		this.tooltipY = tooltipY;
		return (S) this;
	}

	/**
	 * Method called when the Minecraft window is resized.
	 *
	 * @param client Minecraft whose window was resized.
	 * @param width  Width of window after resizing.
	 * @param height Height of window after resizing.
	 */
	@Override
	public void resize(Minecraft client, int width, int height) {
		getInterface().onAlign();
		super.resize(client, width, height);
	}

	/**
	 * Method called whenever the mouse is moved,
	 * which updates information of the tooltip
	 * to be rendered.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	@OnlyIn(Dist.CLIENT)
	public void updateTooltip(int mouseX, int mouseY) {
		setDrawSlot(null);
		for (WAbstractWidget widgetA : getInterface().getWidgets()) {
			if (widgetA.isFocused() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);
				setTooltipX(mouseX);
				setTooltipY(mouseY);
			} else if (widgetA instanceof WCollection) {
				for (WAbstractWidget widgetB : ((WCollection) widgetA).getWidgets()) {
					if (widgetB.updateFocus(mouseX, mouseY) && widgetB instanceof WSlot) {
						setDrawSlot((WSlot) widgetB);
						setTooltipX(mouseX);
						setTooltipY(mouseY);
					}
				}
			}
		}
	}
}
