package spinnery.container.common.widget;

import spinnery.container.client.BaseRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Represents a static (unchanging) text widget.
 */
public class WStaticText extends WWidget {
	// protected Identifier texture;  // ??

	protected Text text;

	public WStaticText(WPanel linkedPanel, int positionX, int positionY, int positionZ, double sizeX, double sizeY, Text text) {
		setPosition(positionX, positionY, positionZ);
		setSize(sizeX, sizeY);

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
		BaseRenderer.textRenderer.drawStringBounded(getText().getString(), (int) getPositionX(), (int) getPositionY(), (int) getSizeX(), (int) getSizeY());
	}
}
