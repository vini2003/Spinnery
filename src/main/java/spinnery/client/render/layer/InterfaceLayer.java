package spinnery.client.render.layer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class InterfaceLayer extends RenderLayer {
	public InterfaceLayer(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	private static final RenderLayer INTERFACE = RenderLayer.of("spinnery", VertexFormats.POSITION_COLOR_LIGHT, 7, 256, RenderLayer.MultiPhaseParameters.builder().texture(NO_TEXTURE).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false));

	public static RenderLayer getInterface() {
		return INTERFACE;
	}
}
