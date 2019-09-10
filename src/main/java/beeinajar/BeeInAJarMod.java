package beeinajar;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BeeInAJarMod implements ModInitializer {
	public static final BeeInAJarBlock BEE_IN_A_JAR = new BeeInAJarBlock(FabricBlockSettings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).strength(2F, 2F).build());

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("beeinajar", "beeinajar"), BEE_IN_A_JAR);
		Registry.register(Registry.ITEM, new Identifier("beeinajar", "beeinajar"), new BlockItem(BEE_IN_A_JAR, new Item.Settings().group(ItemGroup.MISC)));
	}
}
