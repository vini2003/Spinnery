package spinnery.client.render.layer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class SpinneryLayers extends RenderLayer {
	public SpinneryLayers(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer get(Identifier texture) {
		RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DISABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).lightmap(DISABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).build(true);
		return of("entity_cutout", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, 7, 256, true, true, multiPhaseParameters);
	}

	private static final RenderLayer FLAT = of("spinnery", VertexFormats.POSITION_COLOR_LIGHT, 7, 256, RenderLayer.MultiPhaseParameters.builder()
			.texture(NO_TEXTURE)
			.cull(ENABLE_CULLING)
			.lightmap(ENABLE_LIGHTMAP)
			.shadeModel(SMOOTH_SHADE_MODEL)
			.depthTest(ALWAYS_DEPTH_TEST)
			.transparency(TRANSLUCENT_TRANSPARENCY)
			.alpha(ONE_TENTH_ALPHA)
			.layering(VIEW_OFFSET_Z_LAYERING).build(false));

	private static final RenderLayer TOOLTIP = of(
			"blade_tooltip",
			VertexFormats.POSITION_COLOR_LIGHT, 7, 256,
			MultiPhaseParameters.builder()
					.texture(NO_TEXTURE)
					.cull(ENABLE_CULLING)
					.lightmap(ENABLE_LIGHTMAP)
					.shadeModel(SMOOTH_SHADE_MODEL)
					.depthTest(ALWAYS_DEPTH_TEST)
					.transparency(NO_TRANSPARENCY)
					.alpha(ONE_TENTH_ALPHA)
					.layering(VIEW_OFFSET_Z_LAYERING)
					.build(false));


	public static RenderLayer getFlat() {
		return FLAT;
	}

	public static RenderLayer getTooltip() {
		return TOOLTIP;
	}
}
