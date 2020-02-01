package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WTabToggle extends WToggle {
	Item symbol;
	Text name;

	public WTabToggle(WPosition position, WSize size, WInterface linkedInterface, Item symbol, Text name) {
		super(position, size, linkedInterface);
		setInterface(linkedInterface);
		setPosition(position);
		setSize(size);
		setSymbol(symbol);
		setName(name);
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
		BaseRenderer.drawText(isLabelShadowed(), name.asFormattedString(), x + 24, (int) (y + sY / 2 - 4.5), getStyle().asColor("label"));
	}

	public Item getSymbol() {
		return symbol;
	}

	public void setSymbol(Item symbol) {
		this.symbol = symbol;
	}
}
