package spinnery.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.widget.WCollection;
import spinnery.widget.WInterface;
import spinnery.widget.WInterfaceHolder;
import spinnery.widget.WSlot;
import spinnery.widget.WWidget;

public class BaseContainerScreen<T extends BaseContainer> extends ContainerScreen<T> {
	int tooltipX = 0;
	int tooltipY = 0;
	WSlot drawSlot;

	WInterfaceHolder clientHolder = new WInterfaceHolder();

	@Environment(EnvType.CLIENT)
	public BaseContainerScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(linkedContainer, player.inventory, name);
		this.x = Integer.MIN_VALUE;
		this.y = Integer.MIN_VALUE;
		this.containerHeight = Integer.MAX_VALUE;
		this.containerWidth = Integer.MIN_VALUE;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void render(int mouseX, int mouseY, float tick) {
		getHolder().draw();

		drawTooltip();

		if (!playerInventory.getCursorStack().isEmpty()) {
			RenderSystem.translatef(0, 0, 100);
			MinecraftClient.getInstance().getItemRenderer().renderGuiItem(playerInventory.getCursorStack(), mouseX - 8, mouseY - 8);
			MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, playerInventory.getCursorStack(), mouseX - 8, mouseY - 8);
		}
	}

	@Environment(EnvType.CLIENT)
	public WInterfaceHolder getHolder() {
		return clientHolder;
	}

	@Environment(EnvType.CLIENT)
	public void drawTooltip() {
		if (getDrawSlot() != null && getLinkedContainer().getLinkedPlayerInventory().getCursorStack().isEmpty() && !getDrawSlot().getStack().isEmpty()) {
			this.renderTooltip(getDrawSlot().getStack(), getTooltipX(), getTooltipY());
		}
	}

	@Environment(EnvType.CLIENT)
	public WSlot getDrawSlot() {
		return drawSlot;
	}

	@Environment(EnvType.CLIENT)
	public T getLinkedContainer() {
		return super.container;
	}

	@Environment(EnvType.CLIENT)
	public int getTooltipX() {
		return tooltipX;
	}

	@Environment(EnvType.CLIENT)
	public void setTooltipX(int tooltipX) {
		this.tooltipX = tooltipX;
	}

	@Environment(EnvType.CLIENT)
	public int getTooltipY() {
		return tooltipY;
	}

	@Environment(EnvType.CLIENT)
	public void setTooltipY(int tooltipY) {
		this.tooltipY = tooltipY;
	}

	@Environment(EnvType.CLIENT)
	public void setDrawSlot(WSlot drawSlot) {
		this.drawSlot = drawSlot;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void drawMouseoverTooltip(int mouseX, int mouseY) {
		getHolder().drawMouseoverTooltip(mouseX, mouseY);

		super.drawMouseoverTooltip(mouseX, mouseY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected void drawBackground(float tick, int mouseX, int mouseY) {
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		getHolder().onMouseClicked((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int int_1, int int_2, int int_3) {
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
		getHolder().onMouseDragged((int) mouseX, (int) mouseY, mouseButton, (int) deltaX, (int) deltaY);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		getHolder().onMouseReleased((int) mouseX, (int) mouseY, mouseButton);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		getHolder().keyPressed(character, keyCode, keyModifier);

		if (character == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.player.closeContainer();
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		getHolder().tick();
		super.tick();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaY) {
		getHolder().onMouseScrolled((int) mouseX, (int) mouseY, deltaY);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean keyReleased(int character, int keyCode, int keyModifier) {
		getHolder().onKeyReleased(character, keyCode, keyModifier);

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean charTyped(char character, int keyCode) {
		getHolder().onCharTyped(character, keyCode);

		return super.charTyped(character, keyCode);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void mouseMoved(double mouseX, double mouseY) {
		getHolder().mouseMoved((int) mouseX, (int) mouseY);

		updateTooltip((int) mouseX, (int) mouseY);
	}

	@Environment(EnvType.CLIENT)
	public void updateTooltip(int mouseX, int mouseY) {
		setDrawSlot(null);
		for (WWidget widgetA : getHolder().getWidgets()) {
			if (widgetA.getFocus() && widgetA instanceof WSlot) {
				setDrawSlot((WSlot) widgetA);
				setTooltipX(mouseX);
				setTooltipY(mouseY);
			} else if (widgetA instanceof WCollection) {
				for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
					if (widgetB.updateFocus(mouseX, mouseY) && widgetB instanceof WSlot) {
						setDrawSlot((WSlot) widgetB);
						setTooltipX(mouseX);
						setTooltipY(mouseY);
					}
				}
			}
		}
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		for (WInterface wInterface : getLinkedContainer().getHolder().getInterfaces()) {
			wInterface.align();
			wInterface.onAlign();
			for (WWidget widgetA : wInterface.getWidgets()) {
				widgetA.align();
				widgetA.onAlign();
			}
		}
		super.resize(client, width, height);
	}
}
