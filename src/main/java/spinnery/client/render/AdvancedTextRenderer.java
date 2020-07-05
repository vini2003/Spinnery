package spinnery.client.render;

import com.google.common.collect.Lists;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class AdvancedTextRenderer {
    private static final Vector3f FORWARD_SHIFT = new Vector3f(0.0F, 0.0F, 0.03F);

    public final int fontHeight = 9;

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

    public int drawWithShadow(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color) {
        return this.draw(matrices, provider, text, x, y, z, color, true, this.isRightToLeft());
    }

    public int drawWithShadow(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color, boolean rightToLeft) {
        RenderSystem.enableAlphaTest();
        return this.draw(matrices, provider, text, x, y, z, color, true, rightToLeft);
    }

    public int drawWithShadow(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, float z, int color) {
        RenderSystem.enableAlphaTest();
        return this.draw(matrices, provider, text, x, y, z, color, true);
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color) {
        RenderSystem.enableAlphaTest();
        return this.draw(matrices, provider, text, x, y, z, color, false, this.isRightToLeft());
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, float z, int color) {
        RenderSystem.enableAlphaTest();
        return this.draw(matrices, provider, text, x, y, z, color, false);
    }

    public void drawTrimmed(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, float z, int maxWidth, int color) {
        for(Iterator<StringRenderable> nextText = this.wrapLines(text, maxWidth).iterator(); nextText.hasNext(); y += 9) {
            this.draw(matrices, provider, nextText.next(), x, y, z, color, false);
        }
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, float z, int color, boolean shadow) {
        return this.draw(matrices, provider, text, x, y, z, color, shadow, false, 0, 15728880);
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color, boolean shadow, boolean seeThrough, int backgroundColor, int light) {
        return this.draw(matrices, provider, text, x, y, z, color, shadow, seeThrough, backgroundColor, light, this.isRightToLeft());
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color, boolean shadow, boolean mirror) {
        return text == null ? 0 : this.draw(matrices, provider, text, x, y, z, color, shadow, false, 0, 15728880, mirror);
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color, boolean shadow, boolean seeThrough, int backgroundColor, int light, boolean rightToLeft) {
        return this.drawInternal(matrices, provider, text, x, y, z, color, shadow, seeThrough, backgroundColor, light, rightToLeft);
    }

    public int draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, float z, int color, boolean shadow, boolean seeThrough, int backgroundColor, int light) {
        return this.drawInternal(matrices, provider, text, x, y, z, color, shadow, seeThrough, backgroundColor, light);
    }

    private static int tweakTransparency(int color) {
        return (color & -67108864) == 0 ? color | -16777216 : color;
    }

    private int drawInternal(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, float z, int color, boolean shadow,  boolean seeThrough, int backgroundColor, int light, boolean mirror) {
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
            this.drawLayer(matrices, provider, text, x, y, color, true, seeThrough, backgroundColor, light);
            matrix4f.addToLastColumn(FORWARD_SHIFT);
        }

        x = this.drawLayer(matrices, provider, text, x, y, color, false, seeThrough, backgroundColor, light);

        matrices.pop();

        return (int)x + (shadow ? 1 : 0);
    }

    private int drawInternal(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, float z, int color, boolean shadow, boolean seeThrough, int backgroundColor, int light) {
        x /= 2;
        y /= 2;

        matrices.push();

        matrices.translate(x, y, z);

        color = tweakTransparency(color);

        if (shadow) {
            this.drawLayer(matrices, provider, text, x, y, color, true, seeThrough, backgroundColor, light);
            matrices.peek().getModel().addToLastColumn(FORWARD_SHIFT);
        }

        x = this.drawLayer(matrices, provider, text, x, y, color, false, seeThrough, backgroundColor, light);

        matrices.pop();

        return (int) x + (shadow ? 1 : 0);
    }

    private float drawLayer(MatrixStack matrices, VertexConsumerProvider.Immediate provider, String text, float x, float y, int color, boolean shadow, boolean seeThrough, int underlineColor, int light) {
        ShadowDrawer shadowDrawer = new ShadowDrawer(matrices, provider, x, y, color, shadow, seeThrough, light);
        TextVisitFactory.visitFormatted(text, Style.EMPTY, shadowDrawer);
        return shadowDrawer.drawLayer(underlineColor, x);
    }

    private float drawLayer(MatrixStack matrices, VertexConsumerProvider.Immediate provider, StringRenderable text, float x, float y, int color, boolean shadow, boolean seeThrough, int underlineColor, int light) {
        ShadowDrawer shadowDrawer = new ShadowDrawer(matrices, provider, x, y, color, shadow, seeThrough, light);
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
        private final VertexConsumerProvider.Immediate provider;

        private final MatrixStack matrices;

        private final boolean shadow;
        private final boolean seeThrough;

        private final float brightnessMultiplier;

        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;

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

        public ShadowDrawer(MatrixStack matrices, VertexConsumerProvider.Immediate provider, float x, float y, int color, boolean shadow, boolean seeThrough, int light) {
            this.provider = provider;

            this.matrices = matrices;

            this.shadow = shadow;
            this.seeThrough = seeThrough;

            this.brightnessMultiplier = shadow ? 0.25F : 1.0F;

            this.red = (color >> 16 & 255) / 255.0F * this.brightnessMultiplier;
            this.green = (color >> 8 & 255) / 255.0F * this.brightnessMultiplier;
            this.blue = (color & 255) / 255.0F * this.brightnessMultiplier;
            this.alpha = (color >> 24 & 255) / 255.0F;

            this.light = light;

            this.x = x;
            this.y = y;

            this.rectangles = new ArrayList<>();
        }

        public boolean onChar(int i, Style style, int code) {
            FontStorage fontStorage = getFontStorage(style.getFont());

            Glyph glyph = fontStorage.getGlyph(code);

            GlyphRenderer glyphRenderer = style.isObfuscated() && code != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(code);

            boolean isBold = style.isBold();

            TextColor textColor = style.getColor();

            float red;
            float green;
            float blue;
            float alpha = this.alpha;

            if (textColor != null) {
                int redGreenBlue = textColor.getRgb();

                red = (redGreenBlue >> 16 & 255) / 255.0F * this.brightnessMultiplier;
                green = (redGreenBlue >> 8 & 255) / 255.0F * this.brightnessMultiplier;
                blue = (redGreenBlue & 255) / 255.0F * this.brightnessMultiplier;
            } else {
                red = this.red;
                green = this.green;
                blue = this.blue;
            }

            float shadowOffset;
            float boldOffset;

            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                boldOffset = isBold ? glyph.getBoldOffset() : 0.0F;
                shadowOffset = this.shadow ? glyph.getShadowOffset() : 0.0F;

                VertexConsumer consumer = this.provider.getBuffer(glyphRenderer.method_24045(this.seeThrough));

                drawGlyph(glyphRenderer, isBold, style.isItalic(), boldOffset, this.x + shadowOffset, this.y + shadowOffset, this.matrices.peek().getModel(), consumer, red, green, blue, alpha, this.light);
            }

            boldOffset = glyph.getAdvance(isBold);
            shadowOffset = this.shadow ? 1.0F : 0.0F;

            if (style.isStrikethrough()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + shadowOffset - 1.0F, this.y + shadowOffset + 4.5F, this.x + shadowOffset + boldOffset, this.y + shadowOffset + 4.5F - 1.0F, 0.01F, red, green, blue, alpha));
            }

            if (style.isUnderlined()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + shadowOffset - 1.0F, this.y + shadowOffset + 9.0F, this.x + shadowOffset + boldOffset, this.y + shadowOffset + 9.0F - 1.0F, 0.01F, red, green, blue, alpha));
            }

            this.x += boldOffset;
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

                VertexConsumer consumer = this.provider.getBuffer(glyphRenderer.method_24045(this.seeThrough));

                for (GlyphRenderer.Rectangle rectangle : rectangles) {
                    glyphRenderer.drawRectangle(rectangle, this.matrices.peek().getModel(), consumer, this.light);
                }
            }

            return this.x;
        }
    }
}
