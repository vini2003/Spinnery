package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import net.minecraft.text.Text;
import spinnery.container.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WDynamicText extends WWidget {
	public class Theme {
		@SerializedName("text")
		private String textColor;

		@SerializedName("top_left_outermost")
		private String topLeftOutermost;

		@SerializedName("top_left_innermost")
		private String topLeftInnermost;

		@SerializedName("background")
		private String background;

		@SerializedName("bottom_right_outermost")
		private String bottomRightOutermost;

		@SerializedName("bottom_right_innermost")
		private String bottomRightInnermost;

		public String getTextColor() {
			return textColor;
		}

		public String getTopLeftOutermost() {
			return topLeftOutermost;
		}

		public String getTopLeftInnermost() {
			return topLeftInnermost;
		}

		public String getBackground() {
			return background;
		}

		public String getBottomRightOutermost() {
			return bottomRightOutermost;
		}

		public String getBottomRightInnermost() {
			return bottomRightInnermost;
		}
	}

	protected int hexColor;

	protected Text text;

	public WDynamicText(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, Text text, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);

		setText(text);
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Text getText() {
		return text;
	}

	@Override
	public void drawWidget() {
		WDynamicText.Theme drawTheme = ResourceRegistry.get(getTheme()).getWDynamicTextTheme();

		BaseRenderer.getTextRenderer().drawStringBounded(getText().getString(), (int) getPositionX(), (int) getPositionY(), (int) getSizeX(), Integer.decode(drawTheme.getTextColor()));
	}
}
