package spinnery.client.render;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import spinnery.client.render.layer.SpinneryLayers;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AdvancedItemRenderer implements SynchronousResourceReloadListener {
    public static final Identifier ENCHANTED_ITEM_GLINT = new Identifier("textures/misc/enchanted_item_glint.png");

    private static Set<Item> WITHOUT_MODELS;

    private ItemModels models;
    private TextureManager textureManager;
    private ItemColors colorMap;

    public AdvancedItemRenderer() {
    }

    public static void setWithoutModels(Set<Item> withoutModels) {
        WITHOUT_MODELS = withoutModels;
    }

    public void setModels(ItemModels models) {
        this.models = models;
    }

    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public void setColorMap(ItemColors colorMap) {
        this.colorMap = colorMap;
    }

    public ItemModels getModels() {
        return this.models;
    }

    private void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
        Random random = new Random();
        long l = 42L;
        Direction[] var10 = Direction.values();
        int var11 = var10.length;

        for(int var12 = 0; var12 < var11; ++var12) {
            Direction direction = var10[var12];
            random.setSeed(42L);
            this.renderBakedItemQuads(matrices, vertices, model.getQuads((BlockState)null, direction, random), stack, light, overlay);
        }

        random.setSeed(42L);
        this.renderBakedItemQuads(matrices, vertices, model.getQuads((BlockState)null, (Direction)null, random), stack, light, overlay);
    }

    public void renderItem(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, int light, int overlay, BakedModel model) {
        if (!stack.isEmpty()) {
            matrices.push();
            boolean bl = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;
            if (stack.getItem() == Items.TRIDENT && bl) {
                model = this.models.getModelManager().getModel(new ModelIdentifier("minecraft:trident#inventory"));
            }

            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5D, -0.5D, -0.5D);
            if (model.isBuiltin() || stack.getItem() == Items.TRIDENT && !bl) {
                BuiltinModelItemRenderer.INSTANCE.render(stack, renderMode, matrices, provider, light, overlay);
            } else {
                boolean bl3;
                if (renderMode != ModelTransformation.Mode.GUI && !renderMode.method_29998() && stack.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)stack.getItem()).getBlock();
                    bl3 = !(block instanceof TransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    bl3 = true;
                }

                RenderLayer renderLayer = RenderLayers.getItemLayer(stack, bl3);
                VertexConsumer vertexConsumer4;
                if (stack.getItem() == Items.COMPASS && stack.hasGlint()) {
                    matrices.push();
                    MatrixStack.Entry entry = matrices.peek();
                    if (renderMode == ModelTransformation.Mode.GUI) {
                        entry.getModel().multiply(0.5F);
                    } else if (renderMode.method_29998()) {
                        entry.getModel().multiply(0.75F);
                    }

                    if (bl3) {
                        vertexConsumer4 = method_30115(provider, renderLayer, entry);
                    } else {
                        vertexConsumer4 = method_30114(provider, renderLayer, entry);
                    }

                    matrices.pop();
                } else if (bl3) {
                    vertexConsumer4 = method_29711(provider, renderLayer, true, stack.hasGlint());
                } else {
                    vertexConsumer4 = getArmorVertexConsumer(provider, renderLayer, true, stack.hasGlint());
                }

                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer4);
            }

            matrices.pop();
        }
    }

    public static VertexConsumer method_27952(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, boolean bl, boolean bl2) {
        return bl2 ? VertexConsumers.dual(vertexConsumerProvider.getBuffer(bl ? RenderLayer.getArmorGlint() : RenderLayer.getArmorEntityGlint()), vertexConsumerProvider.getBuffer(renderLayer)) : vertexConsumerProvider.getBuffer(renderLayer);
    }

    public static VertexConsumer method_30114(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, MatrixStack.Entry entry) {
        return VertexConsumers.dual(new TransformingVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayer.getGlint()), entry.getModel(), entry.getNormal()), vertexConsumerProvider.getBuffer(renderLayer));
    }

    public static VertexConsumer method_30115(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, MatrixStack.Entry entry) {
        return VertexConsumers.dual(new TransformingVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayer.getGlintDirect()), entry.getModel(), entry.getNormal()), vertexConsumerProvider.getBuffer(renderLayer));
    }

    public static VertexConsumer getArmorVertexConsumer(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint) {
        return glint ? VertexConsumers.dual(vertexConsumers.getBuffer(solid ? RenderLayer.getGlint() : RenderLayer.getEntityGlint()), vertexConsumers.getBuffer(layer)) : vertexConsumers.getBuffer(layer);
    }

    public static VertexConsumer method_29711(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, boolean bl, boolean bl2) {
        return bl2 ? VertexConsumers.dual(vertexConsumerProvider.getBuffer(bl ? RenderLayer.getGlintDirect() : RenderLayer.getEntityGlintDirect()), vertexConsumerProvider.getBuffer(renderLayer)) : vertexConsumerProvider.getBuffer(renderLayer);
    }

    private void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        boolean bl = !stack.isEmpty();
        MatrixStack.Entry entry = matrices.peek();
        Iterator var9 = quads.iterator();

        while(var9.hasNext()) {
            BakedQuad bakedQuad = (BakedQuad)var9.next();
            int i = -1;
            if (bl && bakedQuad.hasColor()) {
                i = this.colorMap.getColorMultiplier(stack, bakedQuad.getColorIndex());
            }

            float f = (float)(i >> 16 & 255) / 255.0F;
            float g = (float)(i >> 8 & 255) / 255.0F;
            float h = (float)(i & 255) / 255.0F;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }

    }

    public BakedModel getHeldItemModel(ItemStack stack, World world, LivingEntity entity) {
        Item item = stack.getItem();
        BakedModel bakedModel2;
        if (item == Items.TRIDENT) {
            bakedModel2 = this.models.getModelManager().getModel(new ModelIdentifier("minecraft:trident_in_hand#inventory"));
        } else {
            bakedModel2 = this.models.getModel(stack);
        }

        ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
        BakedModel bakedModel3 = bakedModel2.getOverrides().apply(bakedModel2, stack, clientWorld, entity);
        return bakedModel3 == null ? this.models.getModelManager().getMissingModel() : bakedModel3;
    }

    public void renderItem(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, ModelTransformation.Mode transformationType, int light, int overlay) {
        this.renderItem(matrices, provider, null, stack, transformationType, false, null, light, overlay);
    }

    public void renderItem(MatrixStack matrices, VertexConsumerProvider provider, LivingEntity entity, ItemStack item, ModelTransformation.Mode renderMode, boolean leftHanded, World world, int light, int overlay) {
        if (!item.isEmpty()) {
            BakedModel bakedModel = this.getHeldItemModel(item, world, entity);
            this.renderItem(matrices, provider, item, renderMode, leftHanded, light, overlay, bakedModel);
        }
    }

    public void renderGuiItemIcon(MatrixStack matrices, VertexConsumerProvider.Immediate provider, ItemStack stack, float x, float y, float z) {
        this.renderGuiItemModel(matrices, provider, stack, x, y, z, this.getHeldItemModel(stack, null, null));
    }

    protected void renderGuiItemModel(MatrixStack matrices, VertexConsumerProvider.Immediate provider, ItemStack stack, float x, float y, float z, BakedModel model) {
        matrices.push();

        matrices.translate(x, y, z);
        matrices.translate(8.0F, 8.0F, 0);

        matrices.scale(1.0F, -1.0F, 1.0F);
        matrices.scale(16f, 16f, 16f);

        this.textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        this.textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).setFilter(false, false);

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        boolean bl = !model.isSideLit();

        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        this.renderItem(matrices, provider, stack, ModelTransformation.Mode.GUI, false, 15728880, OverlayTexture.DEFAULT_UV, model);

        provider.draw();

        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();

        matrices.pop();
    }

    /**
     * Renders an item in a GUI with the player as the attached entity
     * for calculating model overrides.
     */
    public void renderInGuiWithOverrides(MatrixStack matrices, VertexConsumerProvider.Immediate provider, ItemStack stack, float x, float y, float z) {
        this.innerRenderInGui(matrices, provider, MinecraftClient.getInstance().player, stack, x, y, z);
    }

    /**
     * Renders an item in a GUI without an attached entity.
     */
    public void renderInGui(MatrixStack matrices, VertexConsumerProvider.Immediate provider, ItemStack stack, float x, float y, float z) {
        this.innerRenderInGui(matrices, provider, null, stack, x, y, z);
    }

    /**
     * Renders an item in a GUI with an attached entity.
     *
     * <p>The entity is used to calculate model overrides for the item.
     */
    public void renderInGuiWithOverrides(MatrixStack matrices, VertexConsumerProvider.Immediate provider, LivingEntity entity, ItemStack stack, float x, float y, float z) {
        this.innerRenderInGui(matrices, provider, entity, stack, x, y, z);
    }

    private void innerRenderInGui(MatrixStack matrices, VertexConsumerProvider.Immediate provider, LivingEntity entity, ItemStack itemStack, float x, float y, float z) {
        if (!itemStack.isEmpty()) {
            this.renderGuiItemModel(matrices, provider, itemStack, x, y, z, this.getHeldItemModel(itemStack, null, entity));
        }
    }

    /**
     * Renders the overlay for items in GUIs, including the damage bar and the item count.
     */
    public void renderGuiItemOverlay(MatrixStack matrices, VertexConsumerProvider.Immediate provider, net.minecraft.client.font.TextRenderer renderer, ItemStack stack, float x, float y, float z) {
        this.renderGuiItemOverlay(matrices, provider, renderer, stack, x, y, z, null);
    }

    /**
     * Renders the overlay for items in GUIs, including the damage bar and the item count.
     *
     * @param countLabel a label for the stack; if null, the stack count is drawn instead
     */
    public void renderGuiItemOverlay(MatrixStack matrices, VertexConsumerProvider.Immediate provider, TextRenderer renderer, ItemStack stack, float x, float y, float z, String countLabel) {
        matrices.push();

        if (!stack.isEmpty()) {
            if (stack.getCount() != 1 || countLabel != null) {
                String string = countLabel == null ? String.valueOf(stack.getCount()) : countLabel;

                spinnery.client.render.TextRenderer.pass().color(16777215).at(x + 19 - 1 - renderer.getWidth(string), y + 6 + 4, z).shadow(true).text(string).render(matrices, provider);

                provider.draw();
            }

            if (stack.isDamaged()) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableAlphaTest();
                RenderSystem.disableBlend();

                float f = stack.getDamage();
                float g = stack.getMaxDamage();
                float h = Math.max(0.0F, (g - f) / g);

                int i = Math.round(13.0F - f * 13.0F / g);
                int j = MathHelper.hsvToRgb(h / 3.0F, 1.0F, 1.0F);

                this.renderGuiQuad(matrices, provider, x + 2, y + 13, z, 13, 2, 0, 0, 0, 255);
                this.renderGuiQuad(matrices, provider, x + 2, y + 13, z, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);

                RenderSystem.enableBlend();
                RenderSystem.enableAlphaTest();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
            float k = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
            if (k > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                this.renderGuiQuad(matrices, provider, x, y + MathHelper.floor(16.0F * (1.0F - k)), z, 16, MathHelper.ceil(16.0F * k), 255, 255, 255, 127);

                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

        }

        matrices.pop();
    }

    private void renderGuiQuad(MatrixStack matrices, VertexConsumerProvider.Immediate provider, float x, float y, float z, int width, int height, int red, int green, int blue, int alpha) {
        VertexConsumer consumer = provider.getBuffer(SpinneryLayers.getInterface());

        consumer.vertex(matrices.peek().getModel(), x, y, z).color(red, green, blue, alpha).light(0x00f000f0).next();
        consumer.vertex(matrices.peek().getModel(), x, y + height, z).color(red, green, blue, alpha).light(0x00f000f0).next();
        consumer.vertex(matrices.peek().getModel(), x + width, y + height, z).color(red, green, blue, alpha).light(0x00f000f0).next();
        consumer.vertex(matrices.peek().getModel(), x + width, y, z).color(red, green, blue, alpha).light(0x00f000f0).next();

        provider.draw();
    }

    public void apply(ResourceManager manager) {
        this.models.reloadModels();
    }

    static {
        WITHOUT_MODELS = Sets.newHashSet(new Item[]{Items.AIR});
    }
}