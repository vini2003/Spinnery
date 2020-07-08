package spinnery.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.client.render.BaseRenderer;
import spinnery.common.container.BaseContainer;
import spinnery.common.utility.MouseUtilities;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;
import spinnery.widget.api.WContextLock;
import spinnery.widget.api.WInterfaceProvider;

import javax.print.DocFlavor;

public class BaseContainerScreen<T extends BaseContainer> extends HandledScreen<T> implements WInterfaceProvider {
	protected final WInterface clientInterface;
	protected float tooltipX = 0;
	protected float tooltipY = 0;
	protected WSlot drawSlot;

	protected int minX = Integer.MAX_VALUE;
	protected int minY = Integer.MAX_VALUE;
	protected int maxX = Integer.MIN_VALUE;
	protected int maxY = Integer.MIN_VALUE;

	protected boolean requiresRecalculation = true;

	/**
	 * Instantiates a BaseContainerScreen.
	 *
	 * @param name            Name to be used for Narrator.
	 * @param linkedContainer Container associated with screen.
	 * @param player          Player associated with screen.
	 */
	@Environment(EnvType.CLIENT)
	public BaseContainerScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		clientInterface = new WInterface(linkedContainer);
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
	}

	/**
	 * Method called for every frame, where all of Spinnery rendering happens.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		VertexConsumerProvider.Immediate provider =  MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();

		clientInterface.draw(matrices, provider);

		ItemStack stackA;

		if (getContainer().getPreviewCursorStack().isEmpty()
				&& getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).isEmpty()
				&& getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).isEmpty()) {
			stackA = getContainer().getPlayerInventory().getCursorStack();
		} else {
			stackA = getContainer().getPreviewCursorStack();
		}

		matrices.push();

		BaseRenderer.getAdvancedItemRenderer().renderInGui(matrices, provider, stackA, mouseX - 8, mouseY - 8, Integer.MAX_VALUE);
		BaseRenderer.getAdvancedItemRenderer().renderGuiItemOverlay(matrices, provider, BaseRenderer.getDefaultTextRenderer(), stackA, (mouseX - 8), mouseY - 8, Integer.MAX_VALUE);

		matrices.pop();

		provider.draw();

		if (getDrawSlot() != null && getContainer().getPlayerInventory().getCursorStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(matrices, getDrawSlot().getStack(), (int) getTooltipX(), (int) getTooltipY());
		}

		super.render(matrices, mouseX, mouseY, tickDelta);
	}

	/**
	 * Method deprecated and unsupported by Spinnery.
	 */
	@Deprecated
	@Override
	@Environment(EnvType.CLIENT)
	protected void drawBackground(MatrixStack matrices, float tick, int mouseX, int mouseY) {
	}

	/**
	 * Method deprecated and unsupported by Spinnery.
	 */
	@Deprecated
	@Override
	@Environment(EnvType.CLIENT)
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
	}

	/**
	 * Method called when a tooltip should be drawn over something, however, currently not implemented.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void drawMouseoverTooltip(MatrixStack matrices, int mouseX, int mouseY) {
		clientInterface.onDrawMouseoverTooltip(mouseX, mouseY);

		super.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	/**
	 * Method called when the mouse is clicked.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button clicked.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseClicked((float) mouseX, (float) mouseY, mouseButton);

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Method called when the mouse is released.
	 *
	 * @param mouseX      Horizontal position of mouse cursor.
	 * @param mouseY      Vertical position of mouse cursor.
	 * @param mouseButton Mouse button released.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		getInterface().onMouseReleased((float) mouseX, (float) mouseY, mouseButton);

		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	/**
	 * Method called when the mouse is moved.
	 *
	 * @param mouseX Horizontal position of mouse cursor.
	 * @param mouseY Vertical position of mouse cursor.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public void mouseMoved(double mouseX, double mouseY) {
		clientInterface.onMouseMoved((float) mouseX, (float) mouseY);

		updateTooltip((float) mouseX, (float) mouseY);

		MouseUtilities.mouseX = (float) mouseX;
		MouseUtilities.mouseY = (float) mouseY;

		super.mouseMoved(mouseX, mouseY);
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
	@Environment(EnvType.CLIENT)
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		getInterface().onMouseDragged((float) mouseX, (float) mouseY, mouseButton, deltaX, deltaY);

		return super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	/**
	 * Method called when the mouse wheel is scrolled.
	 *
	 * @param mouseX Horizontal position of the mouse cursor.
	 * @param mouseY Vertical position of the mouse cursor.
	 * @param deltaY Vertical delta of mouse scroll.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		getInterface().onMouseScrolled((float) mouseX, (float) mouseY, deltaY);

		return super.mouseScrolled(mouseX, mouseY, deltaY);
	}

	/**
	 * Method called when a keyboard key is pressed.
	 *
	 * @param keyCode     Keycode associated with pressed key.
	 * @param character   Character associated with pressed key.
	 * @param keyModifier Modifier(s) associated with pressed key.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyPressed(int keyCode, int character, int keyModifier) {
		clientInterface.onKeyPressed(keyCode, character, keyModifier);

		if (keyCode == GLFW.GLFW_KEY_ESCAPE || MinecraftClient.getInstance().options.keyInventory.matchesKey(keyCode, character)) {
			if (clientInterface.getAllWidgets().stream().noneMatch(widget -> widget instanceof WContextLock && ((WContextLock) widget).isActive())) {
				MinecraftClient.getInstance().player.closeHandledScreen();
				return true;
			}
		}

		return super.keyPressed(keyCode, character, keyModifier);
	}

	/**
	 * Method called when a keyboard key is released.
	 *
	 * @param keyCode     Keycode associated with released key.
	 * @param character   Character associated with released key.
	 * @param keyModifier Modifier(s) associated with released key.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getInterface().onKeyReleased(character, keyCode, keyModifier);

		return super.keyReleased(character, keyCode, keyModifier);
	}

	/**
	 * Method called when a key with a valid associated character is called.
	 *
	 * @param character Character associated with key pressed.
	 * @param keyCode   Keycode associated with key pressed.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public boolean charTyped(char character, int keyCode) {
		getInterface().onCharTyped(character, keyCode);

		return super.charTyped(character, keyCode);
	}


	/**
	 * Return true by default for simplicity of use.
	 */
	@Override
	@Environment(EnvType.CLIENT)
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
	@Environment(EnvType.CLIENT)
	public T getContainer() {
		return super.handler;
	}

	/**
	 * Retrieves the interface associated with this screen.
	 *
	 * @return Interface associated with this screen.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public WInterface getInterface() {
		return clientInterface;
	}

	/**
	 * Retrieves WSlot of which the tooltip will be rendered.
	 *
	 * @return WSlot of which the tooltip will be rendered.
	 */
	@Environment(EnvType.CLIENT)
	public WSlot getDrawSlot() {
		return drawSlot;
	}

	/**
	 * Sets the WSlot of which the tooltip will be rendered.
	 *
	 * @param drawSlot WSlot of which the tooltip will be rendered.
	 */
	@Environment(EnvType.CLIENT)
	public <S extends BaseContainerScreen> S setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
		return (S) this;
	}

	/**
	 * Retrieves the horizontal position at which the tooltip will be drawn.
	 *
	 * @return Horizontal position at which the tooltip will be drawn.
	 */
	@Environment(EnvType.CLIENT)
	public float getTooltipX() {
		return tooltipX;
	}

	/**
	 * Retrieves the vertical position at which the tooltip will be drawn.
	 *
	 * @return Vertical position at which the tooltip will be drawn.
	 */
	@Environment(EnvType.CLIENT)
	public float getTooltipY() {
		return tooltipY;
	}

	/**
	 * Sets the horizontal position at which the tooltip will be drawn.
	 *
	 * @param tooltipX Horizontal position at which the tooltip will be drawn.
	 */
	@Environment(EnvType.CLIENT)
	public <S extends BaseContainerScreen> S setTooltipX(float tooltipX) {
		this.tooltipX = tooltipX;
		return (S) this;
	}

	/**
	 * Sets the vertical position at which the tooltip will be drawn.
	 *
	 * @param tooltipY Vertical position at which the tooltip will be drawn.
	 */
	@Environment(EnvType.CLIENT)
	public <S extends BaseContainerScreen> S setTooltipY(float tooltipY) {
		this.tooltipY = tooltipY;
		return (S) this;
	}

	/**
	 * Method called when the Minecraft window is resized.
	 *
	 * @param client MinecraftClient whose window was resized.
	 * @param width  Width of window after resizing.
	 * @param height Height of window after resizing.
	 */
	@Override
	public void resize(MinecraftClient client, int width, int height) {
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
	@Environment(EnvType.CLIENT)
	public void updateTooltip(float mouseX, float mouseY) {
		setDrawSlot(null);
		for (WAbstractWidget widgetA : getInterface().getAllWidgets()) {
			if (widgetA.isFocused() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);

				setTooltipX(mouseX);
				setTooltipY(mouseY);
			}
		}
	}

	@Override
	protected void init() {
		super.init();

		tryRecalculatingDimensions();
	}

	public void tryRecalculatingDimensions() {
		if (requiresRecalculation) {
			for (WAbstractWidget widget : getInterface().getAllWidgets()) {
				if (widget.getX() < minX) minX = (int) widget.getX();
				if (widget.getY() < minY) minY = (int) widget.getY();
				if (widget.getX() + widget.getWidth() > maxX) maxX = (int) (widget.getX() + widget.getWidth());
				if (widget.getY() + widget.getHeight() > maxY) maxY = (int) (widget.getY() + widget.getHeight());
			}
			requiresRecalculation = false;
		}
	}

	public void setRequiresRecalculation(boolean requiresRecalculation) {
		this.requiresRecalculation = requiresRecalculation;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}
}
