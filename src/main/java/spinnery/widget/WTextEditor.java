package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WTextEditor extends WAbstractTextEditor {
	private final PartitionedTexture texture = new PartitionedTexture(Spinnery.identifier("textures/widget/text_background.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F);

	// Keep track of which lines are "wrapped" and which are hard newlines
	protected final List<Boolean> newLine = new ArrayList<>();
	protected boolean lineWrap;

	public boolean getLineWrap() {
		return lineWrap;
	}

	@SuppressWarnings("unchecked")
	public <W extends WTextEditor> W setLineWrap(boolean lineWrap) {
		this.lineWrap = lineWrap;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		float sX = getWidth();
		float sY = getHeight();

		texture.draw(matrices, provider, x, y, sX, sY);

		if (lineWrap && xOffset != 0) xOffset = 0;

		renderField(matrices, provider);
	}

	protected boolean hasNewline(int index) {
		return lineWrap ? newLine.get(index) : true;
	}

	@Override
	protected int getNewlinedLineLength(int index) {
		return getLineLength(index) + (hasNewline(index) ? 1 : 0);
	}

	@Override
	protected int getStringIndex(Cursor cursor) {
		int i = 0;
		for (int y = 0; y < cursor.y; y++) {
			i += getNewlinedLineLength(y);
		}
		i += cursor.x;
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WTextEditor setText(String text) {
		if (lineWrap) {
			lines.clear();
			newLine.clear();
			this.text = text;
			StringBuilder currentLine = new StringBuilder();
			int lineWidth = 0;
			for (char c : text.toCharArray()) {
				lineWidth += Math.round(Texts.width(c) * scale);
				if (lineWidth > getInnerSize().getWidth() || c == '\n') {
					lines.add(currentLine.toString());
					newLine.add(c == '\n');
					lineWidth = Math.round(Texts.width(c) * scale);
					currentLine = new StringBuilder();
				}
				if (c != '\n') currentLine.append(c);
			}
			String finalLine = currentLine.toString();
			if (!finalLine.isEmpty() || text.endsWith("\n")) {
				newLine.add(text.endsWith("\n"));
				lines.add(finalLine);
				newLine.add(true);
			}
		} else {
			super.setText(text);
		}
		return this;
	}

	@Override
	protected void processKeyActions(int keyPressed, int character, int keyModifier) {
		if (keyPressed == GLFW.GLFW_KEY_BACKSPACE && !hasSelection()) {
			int lineLength = getLineLength(cursor.y);
			String deleted = deleteText(cursor.copy().left(), cursor);
			if (deleted.equals("")) {
				cursor.left();
				deleteText(cursor.copy().left(), cursor);
			}
			if (deleted.equals("\n")) {
				for (int i = 0; i < lineLength; i++) {
					cursor.left();
				}
			}
			cursor.left();
			return;
		} else if (keyPressed == GLFW.GLFW_KEY_DELETE && !hasSelection()) {
			String deleted = deleteText(cursor, cursor.copy().right());
			if (deleted.equals("")) {
				deleteText(cursor, cursor.copy().right().right());
			}
			return;
		}
		super.processKeyActions(keyPressed, character, keyModifier);
	}


}
