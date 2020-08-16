package spinnery.widget.api;

import com.google.common.collect.Lists;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public interface WDrawableElement extends WPositioned, WSized {
	void draw(MatrixStack matrices, VertexConsumerProvider provider);

	default List<Text> getTooltip() {
		return Lists.newArrayList();
	}

	default void onLayoutChange() {
	}
}
