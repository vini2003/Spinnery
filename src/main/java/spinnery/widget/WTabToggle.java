package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.client.utility.ScissorArea;
import spinnery.widget.api.Color;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class WTabToggle extends WAbstractToggle {
	protected ItemConvertible symbol;

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		setToggleState(true);
		if (parent instanceof WTabHolder.WTab) {
			WTabHolder.WTab tab = (WTabHolder.WTab) parent;
			if (tab.getParent() instanceof WTabHolder) {
				((WTabHolder) tab.getParent()).selectTab(tab.getNumber());
			}
		}
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		if (getToggleState()) {
			BaseRenderer.drawPanel(matrices, provider, x, y, z - 1, sX, sY, getStyle().asColor("shadow.off"), getStyle().asColor("background.off"), getStyle().asColor("highlight.off"), getStyle().asColor("outline.off"));

			WTabHolder parent = (WTabHolder) ((WTabHolder.WTab) getParent()).getParent();

			BaseRenderer.drawQuad(matrices, provider, x + 1, y + sY - 4, z + 1, 2, 6, getStyle().asColor("highlight.off"));
			BaseRenderer.drawQuad(matrices, provider, x + 1 + 2, y + sY - 4, z + 1, sX - 2 - 2, 8, getStyle().asColor("background.off"));
			BaseRenderer.drawQuad(matrices, provider, x + sX - 3, y + sY - 4, z + 1, 1, 7, getStyle().asColor("shadow.off"));
			BaseRenderer.drawQuad(matrices, provider, x + sX - 2, y + sY - 4, z + 1, 1, 6, getStyle().asColor("shadow.off"));
			BaseRenderer.drawQuad(matrices, provider, x + sX - 2, y + sY + 2, z + 1, 1, 1, getStyle().asColor("highlight.off"));

			if (this.getX() == parent.getX()) {
				BaseRenderer.drawQuad(matrices, provider, x, y + sY - 4, z + 1, 1, 6, getStyle().asColor("outline.off"));
				BaseRenderer.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 6, getStyle().asColor("outline.on"));
			} else if (this.getWideX() == parent.getWideX()) {
				BaseRenderer.drawQuad(matrices, provider, x + sX - 3, y + sY - 4, z + 1, 2, 8, getStyle().asColor("shadow.off"));
				BaseRenderer.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
				BaseRenderer.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z + 1, 1, 8, getStyle().asColor("outline.off"));
			} else {
				BaseRenderer.drawQuad(matrices, provider, x, y + sY - 4, z + 1, 1, 5, getStyle().asColor("outline.off"));
				BaseRenderer.drawQuad(matrices, provider, x + sX -1, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
			}
		} else {
			BaseRenderer.drawPanel(matrices, provider, x, y, z - 1, sX, sY, getStyle().asColor("shadow.on"), getStyle().asColor("background.on"), getStyle().asColor("highlight.on"), getStyle().asColor("outline.on"));

			WTabHolder parent = (WTabHolder) ((WTabHolder.WTab) getParent()).getParent();

			BaseRenderer.drawQuad(matrices, provider, x + 1, y + sY - 6, z - 1, 2, 6, getStyle().asColor("highlight.on"));
			BaseRenderer.drawQuad(matrices, provider, x + 1 + 2, y + sY - 8, z - 1, sX - 2 - 2 - 2, 8, getStyle().asColor("background.on"));
			BaseRenderer.drawQuad(matrices, provider, x + sX - 3, y + sY - 7, z - 1, 1, 7, getStyle().asColor("shadow.on"));
			BaseRenderer.drawQuad(matrices, provider, x + sX - 2, y + sY - 6, z - 1, 1, 6, getStyle().asColor("shadow.on"));

			if (this.getX() == parent.getX()) {
				BaseRenderer.drawQuad(matrices, provider, x + 1, y + sY, z + 5, 1, 1, getStyle().asColor("highlight.on"));
				BaseRenderer.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 6, getStyle().asColor("outline.on"));
				BaseRenderer.drawQuad(matrices, provider, x + sX - 1, y + sY - 5, z - 1, 1, 6, getStyle().asColor("outline.on"));
			} else if (this.getWideX() == parent.getWideX()) {
				BaseRenderer.drawQuad(matrices, provider, x + sX - 3, y + sY - 8, z - 1, 2, 8, getStyle().asColor("shadow.on"));
				BaseRenderer.drawQuad(matrices, provider, x + sX - 2, y + sY, z - 1, 1, 1, getStyle().asColor("shadow.on"));
				BaseRenderer.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
				BaseRenderer.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 8, getStyle().asColor("outline.on"));
			} else {
				BaseRenderer.drawQuad(matrices, provider, x, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
				BaseRenderer.drawQuad(matrices, provider, x + sX - 1, y + sY - 4, z - 1, 1, 5, getStyle().asColor("outline.on"));
			}
		}

		Item symbol = getSymbol();

		if (symbol != null && sX >= 24) {
			BaseRenderer.getAdvancedItemRenderer().renderGuiItemIcon(matrices, provider, new ItemStack(symbol, 1), (int) x + 4 + ((sX - 24) / 2), (int) y + 6, z + 100F);
		}

		if (label != null && sX >= TextRenderer.width(label)) {
			TextRenderer.pass().shadow(isLabelShadowed()).text(getLabel()).at(x + 8 + (symbol != null ? 16 : 0), y + sY / 2 - 4.5, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render(matrices, provider);
		}
	}

	public Item getSymbol() {
		if(symbol != null) {
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
