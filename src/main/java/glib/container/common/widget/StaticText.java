package glib.container.common.widget;

import glib.container.client.BaseRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StaticText extends Widget {
	protected Identifier texture;

	protected Text text;

	public StaticText(int positionX, int positionY, int positionZ, double sizeX, double sizeY, Text text, Panel linkedPanel) {
		setPositionX(positionX);
		setPositionY(positionY);
		setPositionZ(positionZ);

		setSizeX(sizeX);
		setSizeY(sizeY);

		setText(text);

		setLinkedPanel(linkedPanel);
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
