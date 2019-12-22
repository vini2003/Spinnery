package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WStaticText extends WWidget {
	protected Identifier texture;

	protected Text text;

	public WStaticText(int positionX, int positionY, int positionZ, double sizeX, double sizeY, Text text, WPanel linkedWPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setText(text);

		setLinkedPanel(linkedWPanel);
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Text getText() {
		return text;
	}

	@Override
	public void drawWidget() {
		BaseRenderer.getTextRenderer().drawStringBounded(getText().getString(), (int) getPositionX(), (int) getPositionY(), (int) getSizeX(), (int) getSizeY());
	}
}
