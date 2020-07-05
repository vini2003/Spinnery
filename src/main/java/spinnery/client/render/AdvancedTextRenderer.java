package spinnery.client.render;

import com.google.common.collect.Lists;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.*;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import spinnery.client.render.layer.SpinneryLayers;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class AdvancedTextRenderer {
    private static final Vector3f FORWARD_SHIFT = new Vector3f(0.0F, 0.0F, 0.03F);
    public final int fontHeight = 9;
    public final Random random = new Random();
    private Function<Identifier, FontStorage> fontStorageAccessor;
    private TextHandler handler;

    public AdvancedTextRenderer() {
    }

    public void setFontStorageAccessor(Function<Identifier, FontStorage> fontStorageAccessor) {
        this.fontStorageAccessor = fontStorageAccessor;
    }

    public void setHandler(TextHandler handler) {
        this.handler = handler;
    }

    private FontStorage getFontStorage(Identifier id) {
        return this.fontStorageAccessor.apply(id);
    }

    public int drawWithShadow(MatrixStack matrices, String text, float x, float y, float z, int color) {
        return this.draw(text, x, y, z, color, matrices, true, this.isRightToLeft());
    }

    public int drawWithShadow(MatrixStack matrices, String text, float x, float y, float z, int color, boolean rightToLeft) {
        RenderSystem.enableAlphaTest();
        return this.draw(text, x, y, z, color, matrices, true, rightToLeft);
    }

    public int drawWithShadow(MatrixStack matrices, StringRenderable text, float x, float y, float z, int color) {
        RenderSystem.enableAlphaTest();
        return this.draw(text, x, y, z, color, matrices, true);
    }

    public int draw(MatrixStack matrices, String text, float x, float y, float z, int color) {
        RenderSystem.enableAlphaTest();
        return this.draw(text, x, y, z, color, matrices, false, this.isRightToLeft());
    }

    public int draw(MatrixStack matrices, StringRenderable text, float x, float y, float z, int color) {
        RenderSystem.enableAlphaTest();
        return this.draw(text, x, y, z, color, matrices, false);
    }

    public void drawTrimmed(MatrixStack matrices, StringRenderable text, float x, float y, float z, int maxWidth, int color) {
        for(Iterator<StringRenderable> nextText = this.wrapLines(text, maxWidth).iterator(); nextText.hasNext(); y += 9) {
            this.draw(nextText.next(), x, y, z, color, matrices, false);
        }
    }

    public int draw(StringRenderable text, float x, float y, float z, int color, MatrixStack matrices, boolean shadow) {
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        int i = this.draw(text, x, y, z, color, shadow, matrices, immediate, false, 0, 15728880);
        immediate.draw();
        return i;
    }

    public int draw(String text, float x, float y, float z, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light) {
        return this.draw(text, x, y, z, color, shadow, matrices, vertexConsumers, seeThrough, backgroundColor, light, this.isRightToLeft());
    }

    public int draw(String text, float x, float y, float z, int color, MatrixStack matrices, boolean shadow, boolean mirror) {
        if (text == null) {
            return 0;
        } else {
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            int i = this.draw(text, x, y, z, color, shadow, matrices, immediate, false, 0, 15728880, mirror);
            immediate.draw();
            return i;
        }
    }

    public int draw(String text, float x, float y, float z, int color, boolean shadow, MatrixStack matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean rightToLeft) {
        return this.drawInternal(text, x, y, z, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light, rightToLeft);
    }

    public int draw(StringRenderable text, float x, float y, float z, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light) {
        return this.drawInternal(text, x, y, z, color, shadow, matrices, vertexConsumers, seeThrough, backgroundColor, light);
    }

    private static int tweakTransparency(int color) {
        return (color & -67108864) == 0 ? color | -16777216 : color;
    }

    private int drawInternal(String text, float x, float y, float z, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean mirror) {
        if (mirror) {
            text = this.mirror(text);
        }

        x /= 2;
        y /= 2;

        color = tweakTransparency(color);

        matrices.push();

        matrices.translate(x, y, z);

        Matrix4f matrix4f = matrices.peek().getModel().copy();

        if (shadow) {
            this.drawLayer(text, x, y, color, true, matrices, vertexConsumers, seeThrough, backgroundColor, light);
            matrix4f.addToLastColumn(FORWARD_SHIFT);
        }

        x = this.drawLayer(text, x, y, color, false, matrices, vertexConsumers, seeThrough, backgroundColor, light);

        matrices.pop();

        return (int)x + (shadow ? 1 : 0);
    }

    private int drawInternal(StringRenderable text, float x, float y, float z, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int backgroundColor, int light) {
        x /= 2;
        y /= 2;

        matrices.push();

        matrices.translate(x, y, z);

        color = tweakTransparency(color);

        if (shadow) {
            this.drawLayer(text, x, y, color, true, matrices, vertexConsumerProvider, seeThrough, backgroundColor, light);
            matrices.peek().getModel().addToLastColumn(FORWARD_SHIFT);
        }

        x = this.drawLayer(text, x, y, color, false, matrices, vertexConsumerProvider, seeThrough, backgroundColor, light);

        matrices.pop();

        return (int) x + (shadow ? 1 : 0);
    }

    private float drawLayer(String text, float x, float y, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light) {
        ShadowDrawer shadowDrawer = new ShadowDrawer(vertexConsumerProvider, x, y, color, shadow, matrices.peek().getModel(), seeThrough, light);
        TextVisitFactory.visitFormatted(text, Style.EMPTY, shadowDrawer);
        return shadowDrawer.drawLayer(underlineColor, x);
    }

    private float drawLayer(StringRenderable text, float x, float y, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light) {
        ShadowDrawer shadowDrawer = new ShadowDrawer(vertexConsumerProvider, x, y, color, shadow, matrices.peek().getModel(), seeThrough, light);
        TextVisitFactory.visitFormatted(text, Style.EMPTY, shadowDrawer);
        return shadowDrawer.drawLayer(underlineColor, x);
    }

    private void drawGlyph(GlyphRenderer glyphRenderer, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
        glyphRenderer.draw(italic, x, y, matrix, vertexConsumer, red, green, blue, alpha, light);

        if (bold) {
            glyphRenderer.draw(italic, x + weight, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        }
    }

    public int getWidth(String text) {
        return MathHelper.ceil(this.handler.getWidth(text));
    }

    public int getWidth(StringRenderable stringRenderable) {
        return MathHelper.ceil(this.handler.getWidth(stringRenderable));
    }

    public String trimToWidth(String text, int maxWidth, boolean backwards) {
        return backwards ? this.handler.trimToWidthBackwards(text, maxWidth, Style.EMPTY) : this.handler.trimToWidth(text, maxWidth, Style.EMPTY);
    }

    public String trimToWidth(String text, int maxWidth) {
        return this.handler.trimToWidth(text, maxWidth, Style.EMPTY);
    }

    public StringRenderable trimToWidth(StringRenderable text, int width) {
        return this.handler.trimToWidth(text, width, Style.EMPTY);
    }

    public int getStringBoundedHeight(String text, int maxWidth) {
        return 9 * this.handler.wrapLines(text, maxWidth, Style.EMPTY).size();
    }

    public List<StringRenderable> wrapLines(StringRenderable text, int width) {
        return this.handler.wrapLines(text, width, Style.EMPTY);
    }

    public boolean isRightToLeft() {
        return Language.getInstance().isRightToLeft();
    }

    public String mirror(String text) {
        try {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        } catch (ArabicShapingException var3) {
            return text;
        }
    }

    public TextHandler getTextHandler() {
        return this.handler;
    }

    @Environment(EnvType.CLIENT)
    class ShadowDrawer implements TextVisitFactory.CharacterVisitor {
        final VertexConsumerProvider vertexConsumers;
        private final boolean shadow;
        private final float brightnessMultiplier;
        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        private final Matrix4f matrix;
        private final boolean seeThrough;
        private final int light;
        private float x;
        private float y;
        private List<GlyphRenderer.Rectangle> rectangles;

        private void addRectangle(GlyphRenderer.Rectangle rectangle) {
            if (this.rectangles == null) {
                this.rectangles = Lists.newArrayList();
            }

            this.rectangles.add(rectangle);
        }

        public ShadowDrawer(VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, boolean seeThrough, int light) {
            this.vertexConsumers = vertexConsumers;
            this.x = x;
            this.y = y;
            this.shadow = shadow;
            this.brightnessMultiplier = shadow ? 0.25F : 1.0F;
            this.red = (float)(color >> 16 & 255) / 255.0F * this.brightnessMultiplier;
            this.green = (float)(color >> 8 & 255) / 255.0F * this.brightnessMultiplier;
            this.blue = (float)(color & 255) / 255.0F * this.brightnessMultiplier;
            this.alpha = (float)(color >> 24 & 255) / 255.0F;
            this.matrix = matrix;
            this.seeThrough = seeThrough;
            this.light = light;
        }

        public boolean onChar(int i, Style style, int j) {
            FontStorage fontStorage = getFontStorage(style.getFont());
            Glyph glyph = fontStorage.getGlyph(j);
            GlyphRenderer glyphRenderer = style.isObfuscated() && j != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(j);
            boolean bl = style.isBold();
            float f = this.alpha;
            TextColor textColor = style.getColor();
            float m;
            float n;
            float o;
            if (textColor != null) {
                int k = textColor.getRgb();
                m = (float)(k >> 16 & 255) / 255.0F * this.brightnessMultiplier;
                n = (float)(k >> 8 & 255) / 255.0F * this.brightnessMultiplier;
                o = (float)(k & 255) / 255.0F * this.brightnessMultiplier;
            } else {
                m = this.red;
                n = this.green;
                o = this.blue;
            }

            float s;
            float r;
            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                r = bl ? glyph.getBoldOffset() : 0.0F;
                s = this.shadow ? glyph.getShadowOffset() : 0.0F;
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.method_24045(this.seeThrough));
                drawGlyph(glyphRenderer, bl, style.isItalic(), r, this.x + s, this.y + s, this.matrix, vertexConsumer, m, n, o, f, this.light);
            }

            r = glyph.getAdvance(bl);
            s = this.shadow ? 1.0F : 0.0F;
            if (style.isStrikethrough()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + s - 1.0F, this.y + s + 4.5F, this.x + s + r, this.y + s + 4.5F - 1.0F, 0.01F, m, n, o, f));
            }

            if (style.isUnderlined()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + s - 1.0F, this.y + s + 9.0F, this.x + s + r, this.y + s + 9.0F - 1.0F, 0.01F, m, n, o, f));
            }

            this.x += r;
            return true;
        }

        public float drawLayer(int underlineColor, float x) {
            if (underlineColor != 0) {
                float a = (underlineColor >> 24 & 255) / 255.0F;
                float r = (underlineColor >> 16 & 255) / 255.0F;
                float g = (underlineColor >> 8 & 255) / 255.0F;
                float b = (underlineColor & 255) / 255.0F;

                this.addRectangle(new GlyphRenderer.Rectangle(x - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, r, g, b, a));
            }

            if (this.rectangles != null) {
                GlyphRenderer glyphRenderer = getFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(SpinneryLayers.getInterface());

                for (GlyphRenderer.Rectangle rectangle : rectangles) {
                    glyphRenderer.drawRectangle(rectangle, this.matrix, vertexConsumer, this.light);
                }
            }

            return this.x;
        }
    }
}
