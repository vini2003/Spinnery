package spinnery.widget;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WButton extends WAbstractWidget {
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}
	}

	@Override
	public List<Text> getTooltip() {
		return Lists.newArrayList();
	}
}
