package spinnery.widget;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.List;

public class WTooltipItem extends WItem {
	@Override
	public List<Text> getTooltip() {
		return super.getStack().getTooltip(null, TooltipContext.Default.NORMAL);
	}
}
