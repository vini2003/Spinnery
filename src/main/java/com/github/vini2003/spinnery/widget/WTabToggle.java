package com.github.vini2003.spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import com.github.vini2003.spinnery.client.BaseRenderer;
import com.github.vini2003.spinnery.client.TextRenderer;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class WTabToggle extends WAbstractToggle {
	protected IItemProvider symbol;

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
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

		Item symbol = getSymbol();

		if(symbol != null) {
			RenderSystem.enableLighting();
			BaseRenderer.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(symbol, 1), x + 4, y + 4);
		}
		RenderSystem.disableLighting();
		TextRenderer.pass().shadow(isLabelShadowed()).text(getLabel()).at(x + 8 + (symbol != null ? 16 : 0), y + sY / 2 - 4.5, z)
				.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();
	}

	public Item getSymbol() {
		if(symbol != null) {
			return symbol.asItem();
		} else {
			return null;
		}
	}

	public <W extends WTabToggle> W setSymbol(IItemProvider symbol) {
		this.symbol = symbol;
		return (W) this;
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}
