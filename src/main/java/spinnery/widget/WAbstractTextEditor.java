package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WPadded;
import spinnery.widget.api.Padding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public abstract class WAbstractTextEditor extends WAbstractWidget implements WPadded {
    protected String text = "";
    protected final List<String> lines = new ArrayList<>();
    protected double scale = 1.0;
    protected boolean editable = true;
    protected boolean active = false;
    protected int xOffset = 0;
    protected int yOffset = 0;
    protected int cursorTick = 20;

    @SuppressWarnings("UnusedReturnValue")
    protected class Cursor {
        protected int x;
        protected int y;

        public Cursor(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Cursor left() {
            x--;
            if (x < 0) {
                int prevY = y;
                up();
                if (y != prevY) {
                    x = getLineLength(y);
                } else {
                    x = 0;
                }
            }
            return this;
        }

        public Cursor right() {
            x++;
            if (x > getLineLength(y)) {
                int prevY = y;
                down();
                if (y != prevY) {
                    x = 0;
                } else {
                    x = getLineLength(y);
                }
            }
            return this;
        }

        public Cursor up() {
            y = Math.max(y - 1, 0);
            if (x > getLineLength(y)) {
                x = getLineLength(y);
            }
            return this;
        }

        public Cursor down() {
            y = Math.min(y + 1, lines.size() - 1);
            if (x > getLineLength(y)) {
                x = getLineLength(y);
            }
            return this;
        }

        public boolean present() {
            return x != -1 && y != -1;
        }

        public Cursor assign(Cursor cursor) {
            x = cursor.x;
            y = cursor.y;
            return this;
        }

        public Cursor copy() {
            return new Cursor(x, y);
        }

        public boolean lessThan(Cursor cursor) {
            return getStringIndex(this) < getStringIndex(cursor);
        }

        public boolean greaterThan(Cursor cursor) {
            return getStringIndex(this) > getStringIndex(cursor);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cursor cursor = (Cursor) o;
            return x == cursor.x &&
                    y == cursor.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    protected final Cursor cursor = new Cursor(0, 0);
    protected final Pair<Cursor, Cursor> selection = new Pair<>(new Cursor(-1, -1), new Cursor(-1, -1));
    protected final Cursor mouseClick = new Cursor(0, 0);

    public WAbstractTextEditor() {
        setText("");
    }

    protected boolean hasSelection() {
        return selection.getLeft().present() && selection.getRight().present();
    }

    protected void clearSelection() {
        selection.getLeft().assign(new Cursor(-1, -1));
        selection.getRight().assign(new Cursor(-1, -1));
    }

    protected int getLineLength(int index) {
        if (lines.isEmpty()) return 0;
        if (index < 0 || index > lines.size() - 1) return 0;
        return lines.get(index).length();
    }

    protected int getNewlinedLineLength(int index) {
        return getLineLength(index) + 1;
    }

    protected int getTextHeight() {
        return (int) Math.round(TextRenderer.height() * scale);
    }

    protected int getLineOffset() {
        return -yOffset / getTextHeight();
    }

    protected int getVisibleLines() {
        return Math.max(1, getInnerSize().getHeight() / getTextHeight());
    }

    protected boolean isLineVisible(int line) {
        int lineOffset = getLineOffset();
        return line >= lineOffset && line <= lineOffset + getVisibleLines();
    }

    protected int getVisibleColumns() {
        return getInnerSize().getWidth() / getXOffsetStep();
    }

    protected Cursor getCursorFromMouse(int mouseX, int mouseY) {
        Position innerPos = getInnerAnchor();
        int x = -1;
        int y = -1;
        int lineOffset = getLineOffset();
        int cH = getTextHeight();
        int offsetMouseX = -xOffset + mouseX;
        for (int i = 0; i < getVisibleLines(); i++) {
            if (mouseY >= innerPos.getY() + (cH + 2) * i && mouseY <= innerPos.getY() + (cH + 2) * i + cH) {
                if (lineOffset + i > lines.size() - 1) {
                    y = lines.size() - 1;
                    if (offsetMouseX >= innerPos.getX() + getLineWidth(y)) {
                        x = getLineLength(y);
                    }
                    break;
                }
                y = lineOffset + i;
                if (offsetMouseX >= innerPos.getX() + getLineWidth(y)) {
                    x = getLineLength(y);
                    break;
                }
            }
        }
        for (int j = 0; j < getLineLength(y); j++) {
            if (offsetMouseX + 2 >= innerPos.getX() + getXOffset(y, j) && offsetMouseX + 2 <= innerPos.getX() + getXOffset(y, j + 1)) {
                x = j;
                break;
            }
        }
        return new Cursor(x, y);
    }

    protected int getLineWidth(int lineIndex) {
        if (lineIndex > lines.size() - 1) {
            return 0;
        }
        return (int) (TextRenderer.width(lines.get(lineIndex)) * scale);
    }

    protected int getXOffset(int lineIndex, int charIndex) {
        if (charIndex < 0 || lines.isEmpty() || lineIndex > lines.size() - 1) return 0;
        String line = lines.get(lineIndex);
        int endIndex = Math.min(charIndex, line.length());
        return (int) (TextRenderer.width(line.substring(0, endIndex)) * scale);
    }

    protected Pair<Integer, Integer> getSelectedChars(int lineIndex) {
        if (!selection.getLeft().present() || !selection.getRight().present()) return null;
        if (lineIndex < selection.getLeft().y || lineIndex > selection.getRight().y) return null;
        int left = selection.getLeft().y == lineIndex ? selection.getLeft().x : 0;
        int right = selection.getRight().y == lineIndex ? selection.getRight().x : getLineLength(lineIndex);
        return new Pair<>(left, right);
    }

    protected String getTextSegment(Cursor start, Cursor end) {
        int startIndex = getStringIndex(start);
        int endIndex = getStringIndex(end);
        if (startIndex < 0 || startIndex > text.length() || endIndex < 0 || endIndex > text.length()) return "";
        return text.substring(startIndex, endIndex);
    }

    protected int getStringIndex(Cursor cursor) {
        int i = 0;
        for (int y = 0; y < cursor.y; y++) {
            i += getNewlinedLineLength(y);
        }
        i += cursor.x;
        return i;
    }

    @SuppressWarnings("unchecked")
    public <W extends WAbstractTextEditor> W setText(String text) {
        lines.clear();
        this.text = text;
        lines.addAll(Arrays.asList(text.split("\n", -1)));
        return (W) this;
    }

    public <W extends WAbstractTextEditor> W setText(Text text) {
        return setText(text.asFormattedString());
    }

    public String getText() {
        return text;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLine(int index) {
        if (index < 0 || index > lines.size() - 1) return "";
        return lines.get(index);
    }

    protected void insertText(String insert) {
        int cursorIndex = getStringIndex(cursor);
        setText(text.substring(0, cursorIndex) + insert + text.substring(cursorIndex));
    }

    protected String deleteText(Cursor start, Cursor end) {
        int startIndex = getStringIndex(start);
        int endIndex = getStringIndex(end);
        if (endIndex == 0 || startIndex > text.length() || endIndex > text.length()) return "";
        String deleted = text.substring(startIndex, endIndex);
        setText(text.substring(0, startIndex) + text.substring(endIndex));
        return deleted;
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    @Override
    public void tick() {
        if (cursorTick > 0) {
            --cursorTick;
        } else {
            cursorTick = 20;
        }
        super.tick();
    }

    @Override
    public void onCharTyped(char character, int keyCode) {
        if (active) {
            if (hasSelection()) {
                cursor.assign(selection.getLeft());
                deleteText(selection.getLeft(), selection.getRight());
                clearSelection();
            }
            insertText(String.valueOf(character));
            int prevY = cursor.y;
            cursor.right();
            if (cursor.y != prevY) {
                cursor.right();
            }
            onCursorMove();
        }
        cursorTick = 20;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHidden() && editable && mouseButton == 0) {
            active = isWithinBounds(mouseX, mouseY);
            cursorTick = 20;
            if (active) {
                Cursor mousePos = getCursorFromMouse(mouseX, mouseY);
                if (mousePos.present()) {
                    mouseClick.assign(mousePos);
                    cursor.assign(mouseClick);
                    onCursorMove();
                }
                if (hasSelection()) clearSelection();
            }
        }
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        if (!isHidden() && editable && mouseButton == 0 && active) {
            cursorTick = 20;
            Cursor mousePos = getCursorFromMouse(mouseX, mouseY);
            boolean cursorUpdated = false;
            if (mousePos.present()) {
                if (mousePos.lessThan(mouseClick)) {
                    selection.getLeft().assign(mousePos);
                    selection.getRight().assign(mouseClick);
                } else {
                    selection.getLeft().assign(mouseClick);
                    selection.getRight().assign(mousePos);
                }
                cursor.assign(mousePos);
                cursorUpdated = true;
            }
            if (mouseY > getInnerAnchor().getY() + getInnerSize().getHeight()) {
                cursor.down();
                selection.getRight().down();
                cursorUpdated = true;
            } else if (mouseY < getInnerAnchor().getY()) {
                cursor.up();
                selection.getLeft().up();
                cursorUpdated = true;
            }
            if (cursorUpdated) onCursorMove();
        }
    }

    @Override
    public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
        int cH = getTextHeight();
        int textHeight = (lines.size() - 1) * cH;
        if (textHeight <= getInnerSize().getHeight()) return;
        yOffset += deltaY * cH;
        if (yOffset > 0) yOffset = 0;
        if (yOffset < -textHeight) yOffset = -textHeight;
    }

    /**
     * When injecting custom key action handling logic, you should overload this function
     * and not {@link #onKeyPressed(int, int, int)}!
     */
    // TODO: Comment selection expansion/contraction logic
    // *******************
    // * BLESS THIS MESS *
    // *******************
    protected void processKeyActions(int keyPressed, int character, int keyModifier) {
        int prevY;
        Cursor prevCursor;
        switch (keyPressed) {
            case GLFW.GLFW_KEY_ENTER:
                insertText("\n");
                cursor.right();
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_C:
                if (Screen.hasControlDown() && hasSelection()) {
                    MinecraftClient.getInstance().keyboard.setClipboard(
                            getTextSegment(selection.getLeft(), selection.getRight()));
                }
                break;
            case GLFW.GLFW_KEY_V:
                if (Screen.hasControlDown()) {
                    String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();
                    if (hasSelection()) {
                        cursor.assign(selection.getLeft());
                        deleteText(selection.getLeft(), selection.getRight());
                        clearSelection();
                    }
                    insertText(clipboard);
                    prevY = cursor.y;
                    for (int i = 0; i < clipboard.length(); i++) {
                        cursor.right();
                    }
                    if (cursor.y != prevY) cursor.right();
                    onCursorMove();
                }
                break;
            case GLFW.GLFW_KEY_D:
                if (Screen.hasControlDown()) {
                    cursor.x = 0;
                    cursor.y = 0;
                    setText("");
                    onCursorMove();
                }
                break;
            case GLFW.GLFW_KEY_X:
                if (Screen.hasControlDown()) {
                    MinecraftClient.getInstance().keyboard.setClipboard(
                            getTextSegment(selection.getLeft(), selection.getRight()));
                    cursor.assign(selection.getLeft());
                    onCursorMove();
                    deleteText(selection.getLeft(), selection.getRight());
                    clearSelection();
                }
                break;
            case GLFW.GLFW_KEY_A:
                if (Screen.hasControlDown()) {
                    selection.getLeft().assign(new Cursor(0, 0));
                    selection.getRight().assign(new Cursor(getLineLength(lines.size() - 1), lines.size() - 1));
                    cursor.assign(selection.getRight());
                    onCursorMove();
                }
                break;
            case GLFW.GLFW_KEY_BACKSPACE:
                if (hasSelection()) {
                    cursor.assign(selection.getLeft());
                    deleteText(selection.getLeft(), selection.getRight());
                    clearSelection();
                } else {
                    int lineLength = getLineLength(cursor.y);
                    String deleted = deleteText(cursor.copy().left(), cursor);
                    if (deleted.equals("\n")) {
                        for (int i = 0; i < lineLength; i++) {
                            cursor.left();
                        }
                    }
                    cursor.left();
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_DELETE:
                if (hasSelection()) {
                    cursor.assign(selection.getLeft());
                    onCursorMove();
                    deleteText(selection.getLeft(), selection.getRight());
                    clearSelection();
                } else {
                    deleteText(cursor, cursor.copy().right());
                }
                break;
            case GLFW.GLFW_KEY_LEFT:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getRight().assign(cursor);
                        selection.getLeft().assign(cursor);
                    }
                }
                prevY = cursor.y;
                cursor.left();
                if (Screen.hasShiftDown()) {
                    if (cursor.y != prevY) cursor.left();
                    if (cursor.lessThan(selection.getLeft())) {
                        selection.getLeft().left();
                        if (cursor.y != prevY) selection.getLeft().left();
                    } else if (cursor.greaterThan(selection.getLeft())) {
                        selection.getRight().left();
                        if (cursor.y != prevY) selection.getRight().left();
                    }
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_RIGHT:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getRight().assign(cursor);
                        selection.getLeft().assign(cursor);
                    }
                }
                prevY = cursor.y;
                cursor.right();
                if (Screen.hasShiftDown()) {
                    if (cursor.y != prevY) cursor.right();
                    if (cursor.greaterThan(selection.getRight())) {
                        selection.getRight().right();
                        if (cursor.y != prevY) selection.getRight().right();
                    } else if (cursor.lessThan(selection.getRight())) {
                        selection.getLeft().right();
                        if (cursor.y != prevY) selection.getLeft().right();
                    } else {
                        clearSelection();
                    }
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_UP:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getRight().assign(cursor);
                    } else {
                        if (cursor.equals(selection.getLeft())) {
                            selection.getLeft().assign(selection.getRight());
                        }
                    }
                }
                prevCursor = cursor.copy();
                cursor.up();
                if (Screen.hasShiftDown()) {
                    if (cursor.lessThan(selection.getLeft())) {
                        if (!prevCursor.lessThan(selection.getRight())) {
                            selection.getRight().assign(selection.getLeft());
                        }
                        selection.getLeft().assign(cursor);
                    } else {
                        if (!hasSelection()) {
                            selection.getRight().assign(prevCursor);
                            selection.getLeft().assign(cursor);
                        } else {
                            selection.getRight().assign(cursor);
                        }
                    }
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_DOWN:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getLeft().assign(cursor);
                    } else {
                        if (cursor.equals(selection.getLeft())) {
                            selection.getLeft().assign(selection.getRight());
                        }
                    }
                }
                cursor.down();
                if (Screen.hasShiftDown()) {
                    if (cursor.greaterThan(selection.getRight())) {
                        selection.getRight().assign(cursor);
                    } else if (cursor.lessThan(selection.getRight())) {
                        selection.getLeft().assign(cursor);
                    }
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_HOME:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getRight().assign(cursor);
                    } else {
                        if (!cursor.lessThan(selection.getRight())) {
                            selection.getRight().assign(selection.getLeft());
                        }
                    }
                }
                cursor.x = 0;
                if (Screen.hasControlDown()) cursor.y = 0;
                if (Screen.hasShiftDown()) {
                    if (!cursor.lessThan(selection.getRight())) {
                        selection.getRight().assign(cursor);
                    } else {
                        selection.getLeft().assign(cursor);
                    }
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_END:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getLeft().assign(cursor);
                    } else {
                        if (!cursor.greaterThan(selection.getLeft())) {
                            selection.getLeft().assign(selection.getRight());
                        }
                    }
                }
                if (Screen.hasControlDown()) cursor.y = lines.size() - 1;
                cursor.x = getLineLength(cursor.y);
                if (Screen.hasShiftDown()) {
                    selection.getRight().assign(cursor);
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_PAGE_UP:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getRight().assign(cursor);
                    } else {
                        if (cursor.equals(selection.getRight())) {
                            selection.getRight().assign(selection.getLeft());
                        }
                    }
                }
                for (int i = 0; i < Math.min(lines.size() - 1, getVisibleLines()); i++) {
                    cursor.up();
                }
                if (Screen.hasShiftDown()) {
                    selection.getLeft().assign(cursor);
                }
                onCursorMove();
                break;
            case GLFW.GLFW_KEY_PAGE_DOWN:
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection();
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.getLeft().assign(cursor);
                    } else {
                        if (cursor.equals(selection.getLeft())) {
                            selection.getLeft().assign(selection.getRight());
                        }
                    }
                }
                for (int i = 0; i < Math.min(lines.size() - 1, getVisibleLines()); i++) {
                    cursor.down();
                }
                if (Screen.hasShiftDown()) {
                    selection.getRight().assign(cursor);
                }
                onCursorMove();
                break;
        }
    }

    @Override
    public void onKeyPressed(int keyPressed, int character, int keyModifier) {
        if (!active) return;
        processKeyActions(keyPressed, character, keyModifier);
        if (selection.getLeft().equals(selection.getRight())) {
            clearSelection();
        }
        cursorTick = 20;
    }

    @Override
    public boolean isFocused() {
        return super.isFocused() || active;
    }

    protected int getXOffsetStep() {
        return (int) (TextRenderer.width('m') * scale);
    }

    protected void onCursorMove() {
        Position innerPos = getInnerAnchor();
        Size innerSize = getInnerSize();
        int innerX = innerPos.getX();
        int innerY = innerPos.getY();
        int innerWidth = innerSize.getWidth();
        int innerHeight = innerSize.getHeight();
        int lineOffset = getLineOffset();
        int cH = getTextHeight();
        int cursorX = innerX + getXOffset(cursor.y, cursor.x) - 1;
        int cursorY = innerY + (cH + 2) * (cursor.y - lineOffset) - 2;
        int offsetCursorX = cursorX + xOffset;
        int yRenderOffset = yOffset + lineOffset * cH;
        int offsetCursorStartY = cursorY + yRenderOffset;
        int offsetCursorEndY = cursorY + yRenderOffset + cH;
        if (offsetCursorX > innerX + innerWidth) {
            xOffset -= (offsetCursorX - (innerX + innerWidth)) + 2;
        } else if (offsetCursorX < innerX) {
            int compensateOffset = xOffset + (innerX - offsetCursorX);
            int widthOffset = xOffset + innerWidth;
            xOffset = Math.min(0, Math.max(widthOffset, compensateOffset));
        }
        if (offsetCursorEndY > innerY + innerHeight) {
            yOffset -= offsetCursorEndY - (innerY + innerHeight);
        } else if (offsetCursorStartY < innerY) {
            yOffset = Math.min(0, yOffset + innerY - (offsetCursorStartY + 2));
        }
    }

    @Override
    public Padding getPadding() {
        return getStyle().asPadding("padding");
    }

    protected void renderField() {
        int z = getZ();

        Position innerPos = getInnerAnchor();
        Size innerSize = getInnerSize();
        int innerX = innerPos.getX();
        int innerY = innerPos.getY();
        int innerWidth = innerSize.getWidth();
        int innerHeight = innerSize.getHeight();

        if (isEmpty() && !active) {
            TextRenderer.pass().text(getLabel()).at(innerX, innerY, z).scale(scale)
                    .shadow(getStyle().asBoolean("label.shadow")).shadowColor(getStyle().asColor("label.shadow_color"))
                    .color(getStyle().asColor("label.color"))
                    .render();
            return;
        }

        double glScale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        int rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) ((innerX - 1) * glScale), (int) (rawHeight - (innerY * glScale + innerHeight * glScale)), (int) (innerWidth * glScale), (int) (innerHeight * glScale));

        int lineOffset = getLineOffset();
        int cH = getTextHeight();
        int cursorX = innerX + getXOffset(cursor.y, cursor.x) - 1;
        int cursorY = innerY + (cH + 2) * (cursor.y - lineOffset) - 2;
        int yRenderOffset = yOffset + lineOffset * cH;

        RenderSystem.pushMatrix();
        RenderSystem.translatef(xOffset, yRenderOffset, 0f);
        for (int i = lineOffset; i < lineOffset + getVisibleLines(); i++) {
            if (i < 0 || !isLineVisible(i) || i > lines.size() - 1) continue;
            int adjustedI = i - lineOffset;
            String line = lines.get(i);
            TextRenderer.pass().text(line).at(innerX, innerY + (cH + 2) * adjustedI, z).scale(scale)
                    .shadow(getStyle().asBoolean("text.shadow")).shadowColor(getStyle().asColor("text.shadow_color"))
                    .color(getStyle().asColor("text.color"))
                    .render();

            Pair<Integer, Integer> selectedChars = getSelectedChars(i);
            if (selectedChars != null) {
                int selW = getXOffset(i, selectedChars.getRight()) - getXOffset(i, selectedChars.getLeft());
                BaseRenderer.drawRectangle(innerX + getXOffset(i, selectedChars.getLeft()),
                        innerY + (cH + 2) * adjustedI, z, selW, cH, getStyle().asColor("highlight"));
            }
        }
        if (active && cursorTick > 10) {
            BaseRenderer.drawRectangle(cursorX, cursorY, z, 1, cH + 2,
                    getStyle().asColor("cursor"));
        }
        RenderSystem.popMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public double getScale() {
        return scale;
    }

    public <W extends WAbstractTextEditor> W setScale(double scale) {
        this.scale = scale;
        return (W) this;
    }

    public boolean isEditable() {
        return editable;
    }

    public <W extends WAbstractTextEditor> W setEditable(boolean editable) {
        this.editable = editable;
        return (W) this;
    }

    public boolean isActive() {
        return active;
    }

    public <W extends WAbstractTextEditor> W setActive(boolean active) {
        this.active = active;
        return (W) this;
    }
}
