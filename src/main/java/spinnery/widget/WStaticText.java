package spinnery.widget;

import com.google.gson.annotations.SerializedName;
import spinnery.client.BaseRenderer;
import spinnery.registry.ResourceRegistry;

public class WStaticText extends WWidget {
	protected int hexColor;
	protected String text;
	protected WStaticText.Theme drawTheme;

	public WStaticText(WAnchor anchor, int positionX, int positionY, int positionZ, String text, WPanel linkedPanel) {
		setLinkedPanel(linkedPanel);

		setAnchor(anchor);

		setAnchoredPositionX(positionX);
		setAnchoredPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);

		setTheme("default");

		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void setTheme(String theme) {
		super.setTheme(theme);
		drawTheme = ResourceRegistry.get(getTheme()).getWStaticTextTheme();
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		double x = getPositionX();
		double y = getPositionY();

		BaseRenderer.getTextRenderer().drawWithShadow(getText(), (int) x, (int) y, drawTheme.getText().RGB);
	}

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
}
