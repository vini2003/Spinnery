package spinnery.access;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.Item;

import java.util.Set;

public interface ItemRendererAccessor {
    Set<Item> spinnery_getWithoutModels();

    ItemModels spinnery_getModels();

    TextureManager spinnery_getTextureManager();

    ItemColors spinner_getColorMap();
}
