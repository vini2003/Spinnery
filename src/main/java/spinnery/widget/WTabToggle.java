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
		float sY = getHeight() + 4;

		if (getToggleState()) {
			BaseRenderer.drawPanel(matrices, provider, x, y, z - 1, sX, sY, getStyle().asColor("shadow.off"), getStyle().asColor("background.off"), getStyle().asColor("highlight.off"), getStyle().asColor("outline.off"));
		} else {
			BaseRenderer.drawPanel(matrices, provider, x, y, z - 1, sX, sY, getStyle().asColor("shadow.on"), getStyle().asColor("background.on"), getStyle().asColor("highlight.on"), getStyle().asColor("outline.on"));
		}

		Item symbol = getSymbol();

		if (symbol != null) {
			BaseRenderer.getAdvancedItemRenderer().renderGuiItemIcon(matrices, provider, new ItemStack(symbol, 1), (int) x + 4, (int) y + 6, z);
		}

		if (label != null) {
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
