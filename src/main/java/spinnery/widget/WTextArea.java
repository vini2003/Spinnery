package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedKeyboardListener;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@WFocusedKeyboardListener
public class WTextArea extends WAbstractTextEditor {
    // Keep track of which lines are "wrapped" and which are hard newlines
    protected final List<Boolean> newLine = new ArrayList<>();
    protected boolean lineWrap;

    public WTextArea setLineWrap(boolean lineWrap) {
        this.lineWrap = lineWrap;
        return this;
    }

    public boolean getLineWrap() {
        return lineWrap;
    }

    @Override
    public WPosition getInnerAnchor() {
        return WPosition.of(this).add(4, 4, 0);
    }

    @Override
    public WSize getInnerSize() {
        return WSize.of(this).add(-8, -8);
    }

    // Essentially this function checks if a given line is a true "new" line, or if it's wrapped.
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

        if (lineWrap && xOffset != 0) xOffset = 0;
        renderField();
    }
}
