package spinnery.widget.implementation;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

import java.util.List;

public class WTooltipItem extends WItem {
	@Override
	public List<Text> getTooltip() {
		return super.getStack().getTooltip(null, TooltipContext.Default.NORMAL);
	}
}
