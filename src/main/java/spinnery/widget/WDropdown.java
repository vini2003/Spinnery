package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.client.BaseRenderer;
import spinnery.client.TextRenderer;
import spinnery.widget.api.*;

import java.util.*;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class WDropdown extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
	public enum HideBehavior {
		TOGGLE,
		ANYWHERE,
		ANYWHERE_EXCEPT_CHILD,
		ONLY_CHILD,
		INSIDE,
		INSIDE_EXCEPT_CHILD
	}

	public Set<WAbstractWidget> widgets = new HashSet<>();
	protected boolean state = false;
	protected WSize dropdownSize;
	protected WVirtualArea toggle;
	protected HideBehavior hideBehavior = HideBehavior.TOGGLE;

	@Override
	public void onLayoutChange() {
		toggle = new WVirtualArea(WPosition.of(this), WSize.of(getToggleWidth(), getToggleHeight()));
		updateChildren();
	}

	protected void updateChildren() {
		for (WAbstractWidget widget : widgets) {
			widget.getPosition().setOffset(0, getToggleHeight() + 2, 0);
			widget.setHidden(!getState());
		}
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}

	public boolean getState() {
		return state;
	}

	@SuppressWarnings("UnusedReturnValue")
	public <W extends WDropdown> W setState(boolean state) {
		this.state = state;
		return (W) this;
	}

	public HideBehavior	getHideBehavior() {
		return hideBehavior;
	}

	public <W extends WDropdown> W setHideBehavior(HideBehavior hideBehavior) {
		this.hideBehavior = hideBehavior;
		return (W) this;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean shouldOpen = isWithinBounds(mouseX, mouseY);
		boolean shouldClose = false;

		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (getState()) {
			switch (hideBehavior) {
				case TOGGLE:
					shouldClose = toggle.isWithinBounds(mouseX, mouseY);
					break;
				case ANYWHERE:
					shouldClose = true;
					break;
				case INSIDE:
					shouldClose = isWithinBounds(mouseX, mouseY);
					break;
				case ONLY_CHILD:
					shouldClose = (isWithinBounds(mouseX, mouseY) && !getFocus());
					break;
				case ANYWHERE_EXCEPT_CHILD:
					shouldClose = (!isWithinBounds(mouseX, mouseY) || getFocus());
					break;
				case INSIDE_EXCEPT_CHILD:
					shouldClose = (isWithinBounds(mouseX, mouseY) && getFocus());
					break;
			}
		}

		boolean shouldToggle = !getState() ? shouldOpen : shouldClose;

		if (shouldToggle && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			setState(!getState());
			updateChildren();
		}
	}

	@Override
	public void align() {
		super.align();
		updateChildren();
	}

	@Override
	public boolean updateFocus(int mouseX, int mouseY) {
		super.updateFocus(mouseX, mouseY);

		setFocus(isWithinBounds(mouseX, mouseY) && getAllWidgets().stream().noneMatch((WAbstractWidget::getFocus)));

		return getFocus();
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public int getWidth() {
		return Math.max(getToggleWidth(), state ? dropdownSize.getWidth() : 0);
	}

	@Override
	public int getHeight() {
		return getToggleHeight() + (state ? dropdownSize.getHeight() : 0);
	}

	public int getToggleWidth() {
		return super.getWidth();
	}

	public int getToggleHeight() {
		return super.getHeight();
	}

	public WSize getDropdownSize() {
		return dropdownSize;
	}

	public <W extends WDropdown> W setDropdownSize(WSize dropdownSize) {
		this.dropdownSize = dropdownSize;
		return (W) this;
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

		BaseRenderer.drawPanel(x, y, z, sX, sY + 1.75,
				getStyle().asColor("shadow"), getStyle().asColor("background"),
				getStyle().asColor("highlight"), getStyle().asColor("outline"));

		if (hasLabel()) {
			TextRenderer.pass().shadow(isLabelShadowed())
					.text(getLabel()).at(x + sX / 2 - TextRenderer.width(getLabel()) / 2, y + 6, z)
					.color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render();

			if (getState()) {
				BaseRenderer.drawRectangle(x, y + 16, z, sX, 1, getStyle().asColor("outline"));
				BaseRenderer.drawRectangle(x + 1, y + 17, z, sX - 2, 0.75, getStyle().asColor("shadow"));
			}
		}

		if (getState()) {
			for (WAbstractWidget widgetC : getAllWidgets()) {
				widgetC.draw();
			}
		}
	}

	@Override
	public void add(WAbstractWidget... widgetArray) {
		widgets.addAll(Arrays.asList(widgetArray));
		updateChildren();
	}

	@Override
	public void remove(WAbstractWidget... widgetArray) {
		widgets.removeAll(Arrays.asList(widgetArray));
		updateChildren();
	}

	@Override
	public boolean contains(WAbstractWidget... widgetArray) {
		return widgets.containsAll(Arrays.asList(widgetArray));
	}
}
