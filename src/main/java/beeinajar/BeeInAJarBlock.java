package beeinajar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;

public class BeeInAJarBlock extends Block {
    BeeInAJarBlock(Settings blockSettings) {
        super(blockSettings);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}