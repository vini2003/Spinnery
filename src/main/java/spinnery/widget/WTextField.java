package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.WFocusedKeyboardListener;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;

@Environment(EnvType.CLIENT)
@WFocusedKeyboardListener
public class WTextField extends WAbstractTextEditor {
    @Override
    public WPosition getInnerAnchor() {
        return WPosition.of(this).add(4, 4, 0);
    }

    @Override
    public WSize getInnerSize() {
        return WSize.of(this).add(-8, -8);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <W extends WAbstractTextEditor> W setText(String text) {
        return (W) super.setText(text.replaceAll("\n", ""));
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
