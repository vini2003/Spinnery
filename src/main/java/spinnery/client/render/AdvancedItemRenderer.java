package spinnery.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import spinnery.client.render.layer.SpinneryLayers;

import java.util.List;
import java.util.Random;

public class AdvancedItemRenderer implements SynchronousResourceReloadListener {
	private ItemModels models;
	private TextureManager textureManager;
	private ItemColors colorMap;

	private static final Random random = new Random(42L);

	public AdvancedItemRenderer() {
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
		for (Direction direction : Direction.values()) {
			this.renderBakedItemQuads(matrices, vertices, model.getQuads(null, direction, random), stack, light, overlay);
		}

		this.renderBakedItemQuads(matrices, vertices, model.getQuads(null, null, random), stack, light, overlay);
	}

	public void renderItem(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, int light, int overlay, BakedModel model) {
		if (!stack.isEmpty()) {
			matrices.push();

			boolean isConcern = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;

			if (stack.getItem() == Items.TRIDENT && isConcern) {
				model = this.models.getModelManager().getModel(new ModelIdentifier("minecraft:trident#inventory"));
			}

			model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

			matrices.translate(-0.5D, -0.5D, -0.5D);

			if (model.isBuiltin() || stack.getItem() == Items.TRIDENT && !isConcern) {
				BuiltinModelItemRenderer.INSTANCE.render(stack, renderMode, matrices, provider, light, overlay);
			} else {
				boolean isSolid;

				if (renderMode != ModelTransformation.Mode.GUI && !renderMode.method_29998() && stack.getItem() instanceof BlockItem) {
					Block block = ((BlockItem) stack.getItem()).getBlock();
					isSolid = !(block instanceof TransparentBlock) && !(block instanceof StainedGlassPaneBlock);
				} else {
					isSolid = true;
				}

				RenderLayer layer = RenderLayers.getItemLayer(stack, isSolid);

				VertexConsumer consumer;

				if (stack.getItem() == Items.COMPASS && stack.hasGlint()) {
					matrices.push();

					MatrixStack.Entry entry = matrices.peek();

					if (renderMode == ModelTransformation.Mode.GUI) {
						entry.getModel().multiply(0.5F);
					} else if (renderMode.method_29998()) {
						entry.getModel().multiply(0.75F);
					}

					if (isSolid) {
						consumer = getDirectGlintVertexConsumer(provider, layer, entry);
					} else {
						consumer = getGlintVertexConsumer(provider, layer, entry);
					}

					matrices.pop();
				} else if (isSolid) {
					consumer = getDirectAndEntityGlintVertexConsumer(provider, layer, true, stack.hasGlint());
				} else {
					consumer = getArmorVertexConsumer(provider, layer, true, stack.hasGlint());
				}

				this.renderBakedItemModel(model, stack, light, overlay, matrices, consumer);

				if (provider instanceof VertexConsumerProvider.Immediate) {
					((VertexConsumerProvider.Immediate) provider).draw();
				}
			}

			matrices.pop();
		}
	}

	public static VertexConsumer getArmorAndEntityGlintVertexConsumer(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, boolean bl, boolean bl2) {
		return bl2 ? VertexConsumers.dual(vertexConsumerProvider.getBuffer(bl ? RenderLayer.getArmorGlint() : RenderLayer.getArmorEntityGlint()), vertexConsumerProvider.getBuffer(renderLayer)) : vertexConsumerProvider.getBuffer(renderLayer);
	}

	public static VertexConsumer getGlintVertexConsumer(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, MatrixStack.Entry entry) {
		return VertexConsumers.dual(new TransformingVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayer.getGlint()), entry.getModel(), entry.getNormal()), vertexConsumerProvider.getBuffer(renderLayer));
	}

	public static VertexConsumer getDirectGlintVertexConsumer(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, MatrixStack.Entry entry) {
		return VertexConsumers.dual(new TransformingVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayer.getGlintDirect()), entry.getModel(), entry.getNormal()), vertexConsumerProvider.getBuffer(renderLayer));
	}

	public static VertexConsumer getArmorVertexConsumer(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint) {
		return glint ? VertexConsumers.dual(vertexConsumers.getBuffer(solid ? RenderLayer.getGlint() : RenderLayer.getEntityGlint()), vertexConsumers.getBuffer(layer)) : vertexConsumers.getBuffer(layer);
	}

	public static VertexConsumer getDirectAndEntityGlintVertexConsumer(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer, boolean bl, boolean bl2) {
		return bl2 ? VertexConsumers.dual(vertexConsumerProvider.getBuffer(bl ? RenderLayer.getGlintDirect() : RenderLayer.getEntityGlintDirect()), vertexConsumerProvider.getBuffer(renderLayer)) : vertexConsumerProvider.getBuffer(renderLayer);
	}

	private void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
		boolean isNotEmpty = !stack.isEmpty();

		MatrixStack.Entry entry = matrices.peek();

		for (BakedQuad quad : quads) {
			int color = -1;

			if (isNotEmpty && quad.hasColor()) {
				color = this.colorMap.getColorMultiplier(stack, quad.getColorIndex());
			}

			float r = (float) (color >> 16 & 255) / 255.0F;
			float g = (float) (color >> 8 & 255) / 255.0F;
			float b = (float) (color & 255) / 255.0F;

			vertices.quad(entry, quad, r, g, b, light, overlay);
		}
	}

	public BakedModel getHeldItemModel(ItemStack stack, World world, LivingEntity entity) {
		Item item = stack.getItem();

		BakedModel baseModel;

		if (item == Items.TRIDENT) {
			baseModel = this.models.getModelManager().getModel(new ModelIdentifier("minecraft:trident_in_hand#inventory"));
		} else {
			baseModel = this.models.getModel(stack);
		}

		ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
		BakedModel overriddenModel = baseModel.getOverrides().apply(baseModel, stack, clientWorld, entity);
		return overriddenModel == null ? this.models.getModelManager().getMissingModel() : overriddenModel;
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

	public void renderGuiItemIcon(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, float x, float y, float z) {
		this.renderGuiItemModel(matrices, provider, stack, x, y, z, this.getHeldItemModel(stack, null, null));
	}

	protected void renderGuiItemModel(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, float x, float y, float z, BakedModel model) {
		matrices.push();

		matrices.translate(x, y, z);
		matrices.translate(8.0F, 8.0F, 0);

		matrices.scale(1.0F, -1.0F, 1.0F);
		matrices.scale(16f, 16f, 0.001F);

		this.textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		this.textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).setFilter(false, false);

		boolean isSideNotLit = !model.isSideLit();

		if (isSideNotLit) {
			DiffuseLighting.disableGuiDepthLighting();
		}

		this.renderItem(matrices, provider, stack, ModelTransformation.Mode.GUI, false, 0x00f000f0, OverlayTexture.DEFAULT_UV, model);

		if (isSideNotLit) {
			DiffuseLighting.enableGuiDepthLighting();
		}

		matrices.pop();
	}

	/**
	 * Renders an item in a GUI with the player as the attached entity
	 * for calculating model overrides.
	 */
	public void renderInGuiWithOverrides(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, float x, float y, float z) {
		this.innerRenderInGui(matrices, provider, MinecraftClient.getInstance().player, stack, x, y, z);
	}

	/**
	 * Renders an item in a GUI without an attached entity.
	 */
	public void renderInGui(MatrixStack matrices, VertexConsumerProvider provider, ItemStack stack, float x, float y, float z) {
		this.innerRenderInGui(matrices, provider, null, stack, x, y, z);
	}

	/**
	 * Renders an item in a GUI with an attached entity.
	 *
	 * <p>The entity is used to calculate model overrides for the item.
	 */
	public void renderInGuiWithOverrides(MatrixStack matrices, VertexConsumerProvider provider, LivingEntity entity, ItemStack stack, float x, float y, float z) {
		this.innerRenderInGui(matrices, provider, entity, stack, x, y, z);
	}

	private void innerRenderInGui(MatrixStack matrices, VertexConsumerProvider provider, LivingEntity entity, ItemStack itemStack, float x, float y, float z) {
		if (!itemStack.isEmpty()) {
			this.renderGuiItemModel(matrices, provider, itemStack, x, y, z, this.getHeldItemModel(itemStack, null, entity));
		}
	}

	/**
	 * Renders the overlay for items in GUIs, including the damage bar and the item count.
	 */
	public void renderGuiItemOverlay(MatrixStack matrices, VertexConsumerProvider provider, net.minecraft.client.font.TextRenderer renderer, ItemStack stack, float x, float y, float z) {
		this.renderGuiItemOverlay(matrices, provider, renderer, stack, x, y, z, null);
	}

	/**
	 * Renders the overlay for items in GUIs, including the damage bar and the item count.
	 *
	 * @param countLabel a label for the stack; if null, the stack count is drawn instead
	 */
	public void renderGuiItemOverlay(MatrixStack matrices, VertexConsumerProvider provider, TextRenderer renderer, ItemStack stack, float x, float y, float z, String countLabel) {
		if (!stack.isEmpty()) {
			if (stack.getCount() != 1 || countLabel != null) {
				String string = countLabel == null ? String.valueOf(stack.getCount()) : countLabel;

				spinnery.client.render.TextRenderer.pass().color(16777215).at(x + 19 - 1 - renderer.getWidth(string), y + 6 + 4, z).shadow(true).text(string).render(matrices, provider);
			}

			if (stack.isDamaged()) {
				float damage = stack.getDamage();
				float maximumDamage = stack.getMaxDamage();
				float damageColor = Math.max(0.0F, (maximumDamage - damage) / maximumDamage);

				int width = Math.round(13.0F - damage * 13.0F / maximumDamage);
				int color = MathHelper.hsvToRgb(damageColor / 3.0F, 1.0F, 1.0F);


				this.renderGuiQuad(matrices, provider, x + 2, y + 13, z, 13, 2, 0, 0, 0, 255);
				this.renderGuiQuad(matrices, provider, x + 2, y + 13, z, width, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);

				RenderSystem.enableDepthTest();
			}

			ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;

			float cooldown = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());

			if (cooldown > 0.0F) {
				this.renderGuiQuad(matrices, provider, x, y + MathHelper.floor(16.0F * (1.0F - cooldown)), z, 16, MathHelper.ceil(16.0F * cooldown), 255, 255, 255, 127);

				RenderSystem.enableDepthTest();
			}

		}
	}

	private void renderGuiQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, int width, int height, int red, int green, int blue, int alpha) {
		VertexConsumer consumer = provider.getBuffer(SpinneryLayers.getFlat());

		consumer.vertex(matrices.peek().getModel(), x, y, z).color(red, green, blue, alpha).light(0x00f000f0).next();
		consumer.vertex(matrices.peek().getModel(), x, y + height, z).color(red, green, blue, alpha).light(0x00f000f0).next();
		consumer.vertex(matrices.peek().getModel(), x + width, y + height, z).color(red, green, blue, alpha).light(0x00f000f0).next();
		consumer.vertex(matrices.peek().getModel(), x + width, y, z).color(red, green, blue, alpha).light(0x00f000f0).next();
	}

	public void apply(ResourceManager manager) {
		this.models.reloadModels();
	}
}