package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import spinnery.registry.ResourceRegistry;

public class WStaticText extends WWidget {
	public class Theme {
		@SerializedName("text")
		private String textColor;

		public String getTextColor() {
			return textColor;
		}
	}

	protected int hexColor;

	protected Text text;

	public WStaticText(WAnchor anchor, int positionX, int positionY, int positionZ, double sizeX, Text text, WPanel linkedWPanel) {
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
		WStaticText.Theme drawTheme = ResourceRegistry.get(getTheme()).getWStaticTextTheme();

		BaseRenderer.getTextRenderer().drawStringBounded(getText().getString(), (int) getPositionX(), (int) getPositionY(), (int) getSizeX(), Integer.decode(drawTheme.getTextColor()));
	}
}
