package spinnery.widget.api;

import com.google.common.collect.Lists;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;



  Every widget must implement this interface
 */
public interface WLayoutElement extends WPositioned, WSized, Comparable<WLayoutElement> {


	void draw(MatrixStack matrices, VertexConsumerProvider provider);



	default void drawTooltip(MatrixStack matrices, VertexConsumerProvider provider) {
		return;
	}

	default List<Text> getTooltip() {
		return Lists.newArrayList();
	}


	  this layout element. Such layout changes include e.g. changes in position and size of this element, or
	  in other ways, e.g. adding or removing a label.
	 */
	default void onLayoutChange() {
	}

	@Override
	default int compareTo(WLayoutElement element) {
		return Float.compare(element.getZ(), getZ());
	}
}
