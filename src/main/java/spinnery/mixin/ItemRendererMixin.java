package spinnery.mixin;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import spinnery.access.ItemRendererAccessor;

import java.util.Set;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements ItemRendererAccessor {
	@Shadow
	@Final
	private static Set<Item> WITHOUT_MODELS;

	@Shadow
	@Final
	private ItemModels models;

	@Shadow
	@Final
	private TextureManager textureManager;

	@Shadow
	@Final
	private ItemColors colorMap;

	@Override
	public Set<Item> spinnery_getWithoutModels() {
		return WITHOUT_MODELS;
	}

	@Override
	public ItemModels spinnery_getModels() {
		return models;
	}

	@Override
	public TextureManager spinnery_getTextureManager() {
		return textureManager;
	}

	@Override
	public ItemColors spinner_getColorMap() {
		return colorMap;
	}
}
