package spinnery.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.access.GlyphRendererAccessor;

public class AdvancedGlyphRenderer extends GlyphRenderer {
    private final RenderLayer defaultLayer;
    private final RenderLayer seeThroughLayer;
    private final float uMin;
    private final float uMax;
    private final float vMin;
    private final float vMax;
    private final float xMin;
    private final float xMax;
    private final float yMin;
    private final float yMax;

    public AdvancedGlyphRenderer(RenderLayer defaultLayer, RenderLayer seeThroughLayer, float uMin, float uMax, float vMin, float vMax, float xMin, float xMax, float yMin, float yMax) {
        super(defaultLayer, seeThroughLayer, uMax, uMax, vMin, vMax, xMin, xMax, yMin, yMax);

        this.defaultLayer = defaultLayer;
        this.seeThroughLayer = seeThroughLayer;

        this.uMin = uMin;
        this.uMax = uMax;
        this.vMin = vMin;
        this.vMax = vMax;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public static AdvancedGlyphRenderer of(GlyphRenderer glyphRenderer) {
        GlyphRendererAccessor accessor = (GlyphRendererAccessor) glyphRenderer;
        return new AdvancedGlyphRenderer(accessor.spinnery_getField_21692(), accessor.spinnery_getField_21693(), accessor.spinnery_getUMin(), accessor.spinnery_getUMax(), accessor.spinnery_getVMin(), accessor.spinnery_getVMax(), accessor.spinnery_getXMin(), accessor.spinnery_getXMax(), accessor.spinnery_getYMin(), accessor.spinnery_getYMax());
    }

    public void draw(MatrixStack matrices, VertexConsumer consumer, float x, float y, float z, float red, float green, float blue, float alpha, int light, boolean italic) {
        float sX = x + this.xMin;
        float eX = x + this.xMax;
        float sY = this.yMin - 3.0F;
        float vY = this.yMax - 3.0F;
        float bY = y + sY;
        float tY = y + vY;
        float oSX = italic ? 1.0F - 0.25F * sY : 0.0F;
        float oEX = italic ? 1.0F - 0.25F * vY : 0.0F;

        consumer.vertex(sX + oSX, bY, z).color(red, green, blue, alpha).texture(this.uMin, this.vMin).light(light).next();
        consumer.vertex(sX + oEX, tY, z).color(red, green, blue, alpha).texture(this.uMin, this.vMax).light(light).next();
        consumer.vertex(eX + oEX, tY, z).color(red, green, blue, alpha).texture(this.uMax, this.vMax).light(light).next();
        consumer.vertex(eX + oSX, bY, z).color(red, green, blue, alpha).texture(this.uMax, this.vMin).light(light).next();
    }

    public void drawRectangle(MatrixStack matrices, VertexConsumer consumer, Rectangle rectangle, int light) {
        consumer.vertex(matrices.peek().getModel(), rectangle.xMin, rectangle.yMin, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.uMin, this.vMin).light(light).next();
        consumer.vertex(matrices.peek().getModel(), rectangle.xMax, rectangle.yMin, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.uMin, this.vMax).light(light).next();
        consumer.vertex(matrices.peek().getModel(), rectangle.xMax, rectangle.yMax, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.uMax, this.vMax).light(light).next();
        consumer.vertex(matrices.peek().getModel(), rectangle.xMin, rectangle.yMax, rectangle.zIndex).color(rectangle.red, rectangle.green, rectangle.blue, rectangle.alpha).texture(this.uMax, this.vMin).light(light).next();
    }

    public RenderLayer getLayer(boolean seeThrough) {
        return seeThrough ? this.seeThroughLayer : this.defaultLayer;
    }

    @Environment(EnvType.CLIENT)
    public static class Rectangle {
        protected final float xMin;
        protected final float yMin;
        protected final float xMax;
        protected final float yMax;
        protected final float zIndex;
        protected final float red;
        protected final float green;
        protected final float blue;
        protected final float alpha;

        public Rectangle(float xMin, float yMin, float xMax, float yMax, float zIndex, float red, float green, float blue, float alpha) {
            this.xMin = xMin;
            this.yMin = yMin;
            this.xMax = xMax;
            this.yMax = yMax;
            this.zIndex = zIndex;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}
