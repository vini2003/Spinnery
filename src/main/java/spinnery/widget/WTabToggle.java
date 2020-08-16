package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class WTabToggle extends WAbstractToggle {
	protected ItemConvertible symbol;

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		setToggled(true);
		if (parent instanceof WTabHolder.WTab) {
			WTabHolder.WTab tab = (WTabHolder.WTab) parent;
			if (tab.getParent() instanceof WTabHolder) {
				((WTabHolder) tab.getParent()).selectTab(tab.getNumber());
			}
		}
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		if (isToggled()) {
			Drawings.drawPanel(matrices, provider, x, y, z - 1, sX, sY, getStyle().asColor("shadow.off"), getStyle().asColor("background.off"), getStyle().asColor("highlight.off"), getStyle().asColor("outline.off"));

			WTabHolder parent = (WTabHolder) ((WTabHolder.WTab) getParent()).getParent();

			Drawings.drawQuad(matrices, provider, x + 1, y + sY - 4, z + 1, 2, 6, getStyle().asColor("highlight.off"));
			Drawings.drawQuad(matrices, provider, x + 1 + 2, y + sY - 4, z + 1, sX - 2 - 2, 8, getStyle().asColor("background.off"));
			Drawings.drawQuad(matrices, provider, x + sX - 3, y + sY - 4, z + 1, 1, 7, getStyle().asColor("shadow.off"));
			Drawings.drawQuad(matrices, provider, x + sX - 2, y + sY - 4, z + 1, 1, 6, getStyle().asColor("shadow.off"));
			Drawings.drawQuad(matrices, provider, x + sX - 2, y + sY + 2, z + 1, 1, 1, getStyle().asColor("highlight.off"));

			if (this.getX() == parent.getX()) {
				Drawings.drawQuad(matrices, provider, x, y + sY - 4, z + 1, 1, 6, getStyle().asColor("outline.off"));
				Drawings.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on")); // 6 -> 5 @ < getStyle()
			} else if (this.getWideX() == parent.getWideX()) {
				Drawings.drawQuad(matrices, provider, x + sX - 3, y + sY - 4, z + 1, 2, 8, getStyle().asColor("shadow.off"));
				Drawings.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
				Drawings.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z + 1, 1, 8, getStyle().asColor("outline.off"));
			} else {
				Drawings.drawQuad(matrices, provider, x, y + sY - 4, z + 1, 1, 5, getStyle().asColor("outline.off"));
				Drawings.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
			}
		} else {
			Drawings.drawPanel(matrices, provider, x, y, z - 1, sX, sY, getStyle().asColor("shadow.on"), getStyle().asColor("background.on"), getStyle().asColor("highlight.on"), getStyle().asColor("outline.on"));

			WTabHolder parent = (WTabHolder) ((WTabHolder.WTab) getParent()).getParent();

			Drawings.drawQuad(matrices, provider, x + 1, y + sY - 6, z - 1, 2, 6, getStyle().asColor("highlight.on"));
			Drawings.drawQuad(matrices, provider, x + 1 + 2, y + sY - 8, z - 1, sX - 2 - 2 - 2, 8, getStyle().asColor("background.on"));
			Drawings.drawQuad(matrices, provider, x + sX - 3, y + sY - 7, z - 1, 1, 7, getStyle().asColor("shadow.on"));
			Drawings.drawQuad(matrices, provider, x + sX - 2, y + sY - 6, z - 1, 1, 6, getStyle().asColor("shadow.on"));

			if (this.getX() == parent.getX()) {
				Drawings.drawQuad(matrices, provider, x + 1, y + sY, z + 5, 1, 1, getStyle().asColor("highlight.on"));
				Drawings.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 6, getStyle().asColor("outline.on"));
				Drawings.drawQuad(matrices, provider, x + sX - 1, y + sY - 5, z - 1, 1, 6, getStyle().asColor("outline.on"));
			} else if (this.getWideX() == parent.getWideX()) {
				Drawings.drawQuad(matrices, provider, x + sX - 3, y + sY - 8, z - 1, 2, 8, getStyle().asColor("shadow.on"));
				Drawings.drawQuad(matrices, provider, x + sX - 2, y + sY, z - 1, 1, 1, getStyle().asColor("shadow.on"));
				Drawings.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
				Drawings.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 8, getStyle().asColor("outline.on"));
			} else {
				Drawings.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
				Drawings.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
			}
		}

		Item symbol = getSymbol();

		if (symbol != null && sX >= 24) {
			Drawings.getAdvancedItemRenderer().renderGuiItemIcon(matrices, provider, new ItemStack(symbol, 1), (int) x + 4 + ((sX - 24) / 2), (int) y + 6, z + 100F);
		}

		if (label != null && sX >= Texts.width(label)) {
			Texts.pass().shadow(isLabelShadowed()).text(getLabel()).at(x + 8 + (symbol != null ? 16 : 0), y + sY / 2 - 4.5, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render(matrices, provider);
		}

		super.draw(matrices, provider);
	}

	public Item getSymbol() {
		if (symbol != null) {
			return symbol.asItem();
		} else {
			return null;
		}
	}

	public <W extends WTabToggle> W setSymbol(ItemConvertible symbol) {
		this.symbol = symbol;
		return (W) this;
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
