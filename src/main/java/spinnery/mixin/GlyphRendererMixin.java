package spinnery.mixin;

import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import spinnery.access.GlyphRendererAccessor;

@Mixin(GlyphRenderer.class)
public class GlyphRendererMixin implements GlyphRendererAccessor {
    @Shadow
    @Final
    private float yMax;

    @Shadow
    @Final
    private float yMin;

    @Shadow
    @Final
    private float xMax;

    @Shadow
    @Final
    private float xMin;

    @Shadow
    @Final
    private float vMax;

    @Shadow
    @Final
    private float vMin;

    @Shadow
    @Final
    private float uMax;

    @Shadow
    @Final
    private float uMin;

    @Shadow
    @Final
    private RenderLayer field_21693;

    @Shadow
    @Final
    private RenderLayer field_21692;

    @Override
    public RenderLayer spinnery_getField_21692() {
        return field_21692;
    }

    @Override
    public RenderLayer spinnery_getField_21693() {
        return field_21693;
    }

    @Override
    public float spinnery_getUMin() {
        return uMin;
    }

    @Override
    public float spinnery_getUMax() {
        return uMax;
    }

    @Override
    public float spinnery_getVMin() {
        return vMin;
    }

    @Override
    public float spinnery_getVMax() {
        return vMax;
    }

    @Override
    public float spinnery_getXMin() {
        return xMin;
    }

    @Override
    public float spinnery_getXMax() {
        return xMax;
    }

    @Override
    public float spinnery_getYMin() {
        return yMin;
    }

    @Override
    public float spinnery_getYMax() {
        return yMax;
    }
}
