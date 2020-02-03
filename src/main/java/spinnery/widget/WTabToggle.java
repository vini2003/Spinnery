package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedMouseListener;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WTabToggle extends WToggle {
	Item symbol;
	Text name;

	public WTabToggle symbol(Item symbol) {
		this.symbol = symbol;
		return this;
	}

	public WTabToggle name(Text name) {
		this.name = name;
		return this;
	}

	public WTabToggle build() {
		return this;
	}

	public Text getName() {
		return name;
	}

	public void setName(Text name) {
		this.name = name;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!getToggleState()) {
			super.onMouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight() + 4;

		if (!getToggleState()) {
			BaseRenderer.drawPanel(x, y, z - 1, sX, sY, getStyle().asColor("shadow.off"), getStyle().asColor("background.off"), getStyle().asColor("highlight.off"), getStyle().asColor("outline.off"));
		} else {
			BaseRenderer.drawPanel(x, y, z - 1, sX, sY, getStyle().asColor("shadow.on"), getStyle().asColor("background.on"), getStyle().asColor("highlight.on"), getStyle().asColor("outline.on"));
		}

		RenderSystem.enableLighting();
		BaseRenderer.getItemRenderer().renderGuiItemIcon(new ItemStack(getSymbol(), 1), x + 4, y + 4);
		RenderSystem.disableLighting();
		TextRenderer.pass().shadow(isLabelShadowed()).text(name).at(x + 24, y + sY / 2 - 4.5, z)
				.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
	}

	public Item getSymbol() {
		return symbol;
	}

	public void setSymbol(Item symbol) {
		this.symbol = symbol;
	}
}
