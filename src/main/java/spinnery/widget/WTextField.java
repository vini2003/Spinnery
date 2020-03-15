package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;

@Environment(EnvType.CLIENT)
public class WTextField extends WAbstractTextEditor {
	protected Integer fixedLength;

	public Integer getFixedLength() {
		return fixedLength;
	}

	@SuppressWarnings("unchecked")
	public <W extends WTextField> W setFixedLength(Integer fixedLength) {
		this.fixedLength = fixedLength;
		return (W) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <W extends WAbstractTextEditor> W setText(String text) {
		String finalText = text.replaceAll("\n", "");
		if (fixedLength != null && fixedLength >= 0 && fixedLength < finalText.length()) {
			finalText = finalText.substring(0, fixedLength);
		}
		return (W) super.setText(finalText);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		int sX = getWidth();
		int sY = getHeight();

		BaseRenderer.drawBeveledPanel(x, y, z, sX, sY, getStyle().asColor("top_left"), getStyle().asColor("background"), getStyle().asColor("bottom_right"));

		renderField();
	}
}
