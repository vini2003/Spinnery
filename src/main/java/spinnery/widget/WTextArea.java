package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WTextArea extends WAbstractTextEditor {
	// Keep track of which lines are "wrapped" and which are hard newlines
	protected final List<Boolean> newLine = new ArrayList<>();
	protected boolean lineWrap;

	public boolean getLineWrap() {
		return lineWrap;
	}

	@SuppressWarnings("unchecked")
	public <W extends WTextArea> W setLineWrap(boolean lineWrap) {
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
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		BaseRenderer.drawBeveledPanel(matrices, provider, x, y, z, sX, sY, getStyle().asColor("top_left"), getStyle().asColor("background"), getStyle().asColor("bottom_right"));

		if (lineWrap && xOffset != 0) xOffset = 0;

		renderField(matrices, provider);
	}    // Essentially this function checks if a given line is a true "new" line, or if it's wrapped.

	// This is useful e.g. for line numbering
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
	public WTextArea setText(String text) {
		if (lineWrap) {
			lines.clear();
			newLine.clear();
			this.text = text;
			StringBuilder currentLine = new StringBuilder();
			int lineWidth = 0;
			for (char c : text.toCharArray()) {
				lineWidth += Math.round(TextRenderer.width(c) * scale);
				if (lineWidth > getInnerSize().getWidth() || c == '\n') {
					lines.add(currentLine.toString());
					newLine.add(c == '\n');
					lineWidth = (int) Math.round(TextRenderer.width(c) * scale);
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
