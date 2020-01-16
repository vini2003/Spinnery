package spinnery.container.common.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.container.client.BaseRenderer;
import net.minecraft.text.Text;
import spinnery.registry.ResourceRegistry;

public class WStaticText extends WWidget {
	public class Theme extends WWidget.Theme {
		transient private WColor text;

		@SerializedName("text")
		private String rawText;

		public void build() {
			text = new WColor(rawText);
		}

		public WColor getText() {
			return text;
		}
	}

	WStaticText.Theme drawTheme;

	protected int hexColor;

	protected String text;

	public WStaticText(WAnchor anchor, int positionX, int positionY, int positionZ, String text, WPanel linkedWPanel) {
		setLinkedPanel(linkedWPanel);

		setAnchor(anchor);

		setPositionX(positionX + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionX() : 0));
		setPositionY(positionY + (getAnchor() == WAnchor.MC_ORIGIN ? getLinkedPanel().getPositionY() : 0));
		setPositionZ(positionZ);

		setSizeX(sizeX);

		setTheme("default");

		setText(text);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWStaticTextTheme();
	}

	@Override
	public void draw() {
		double x = getPositionX();
		double y = getPositionY();

		BaseRenderer.getTextRenderer().drawWithShadow(getText(), (int) x, (int) y, drawTheme.getText().RGB);
	}
}
