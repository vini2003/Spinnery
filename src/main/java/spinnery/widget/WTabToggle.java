package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

public class WTabToggle extends WToggle {
	public WTabToggle(Item symbol, Text name, WAnchor anchor, int positionX, int positionY, int positionZ, int sizeX, int sizeY, WInterface linkedPanel) {
		super(anchor, positionX, positionY, positionZ, sizeX, sizeY, linkedPanel);

		setInterface(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSymbol(symbol);
		setName(name);

		setSizeX(sizeX);
		setSizeY(sizeY);

		//setTheme("default");
	}

	Item symbol;
	Text name;

	public Item getSymbol() {
		return symbol;
	}

	public void setSymbol(Item symbol) {
		this.symbol = symbol;
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
		int x = getPositionX();
		int y = getPositionY();
		int z = getPositionZ();

		int sX = getSizeX();
		int sY = getSizeY() + 4;

		if (!getToggleState()) {
			BaseRenderer.drawPanel(x, y, z, sX, sY, new WColor("0xff262626"), new WColor("0xff393939"), new WColor("0xff474747"), new WColor("0xff000000"));
		} else {
			BaseRenderer.drawPanel(x, y, z, sX, sY, new WColor("0xff545454"), new WColor("0xff444444"), new WColor("0xff353535"), new WColor("0xff000000"));
		}

		RenderSystem.enableLighting();
		BaseRenderer.getItemRenderer().renderGuiItemIcon(new ItemStack(getSymbol(), 1), x + 4, y + 4);
		RenderSystem.disableLighting();
		BaseRenderer.getTextRenderer().draw(name.asFormattedString(), x + 24, (int) (y + sY / 2 - 4.5), new WColor("0xffffff").RGB);
	}
}
