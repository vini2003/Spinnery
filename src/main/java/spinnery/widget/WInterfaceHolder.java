package spinnery.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WInterfaceHolder implements WModifiableCollection {
	List<WInterface> heldInterfaces = new ArrayList<>();

	public List<WInterface> getInterfaces() {
		return heldInterfaces;
	}

	@Override
	public boolean contains(WWidget... interfaces) {
		return heldInterfaces.containsAll(Arrays.asList(interfaces));
	}

	@Override
	public void add(WWidget... interfaces) {
		for (WWidget widget : interfaces) {
			if (!(widget instanceof WInterface)) continue;
			heldInterfaces.add((WInterface) widget);
		}
	}

	public void remove(WWidget... interfaces) {
		heldInterfaces.removeAll(Arrays.asList(interfaces));
	}

	public List<WWidget> getWidgets() {
		List<WWidget> widgets = new ArrayList<>();

		for (WInterface myInterface : getInterfaces()) {
			widgets.addAll(myInterface.getWidgets());
		}

		return widgets;
	}

	public List<WWidget> getAllWidgets() {
		List<WWidget> widgets = new ArrayList<>();
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgets.add(widgetA);
				if (widgetA instanceof WCollection) {
					widgets.addAll(((WCollection) widgetA).getAllWidgets());
				}
			}
		}
		return widgets;
	}

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		return false;
	}

	public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseReleased(mouseX, mouseY, mouseButton);
		}
		return false;
	}


	public boolean onMouseDragged(int mouseX, int mouseY, int mouseButton, int deltaX, int deltaY) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
		}
		return false;
	}


	public boolean onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseScrolled(mouseX, mouseY, deltaY);
		}
		return false;
	}

	public void mouseMoved(int mouseX, int mouseY) {
		for (WWidget widget : getAllWidgets()) {
			widget.updateFocus(mouseX, mouseY);
			if (widget instanceof WFocusedMouseListener && !widget.getFocus()) continue;
			widget.onMouseMoved(mouseX, mouseY);
		}
	}


	public boolean onKeyReleased(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedKeyboardListener && !widget.getFocus()) continue;
			widget.onKeyReleased(keyCode);
		}
		return false;
	}

	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedKeyboardListener && !widget.getFocus()) continue;
			widget.onKeyPressed(character, keyCode, keyModifier);
		}
		return false;
	}


	public boolean onCharTyped(char character, int keyCode) {
		for (WWidget widget : getAllWidgets()) {
			if (widget instanceof WFocusedKeyboardListener && !widget.getFocus()) continue;
			widget.onCharTyped(character);
		}
		return false;
	}

	public void drawMouseoverTooltip(int mouseX, int mouseY) {
		for (WWidget widget : getAllWidgets()) {
			widget.onDrawTooltip();
		}
	}

	public void tick() {
		for (WWidget widget : getAllWidgets()) {
			widget.tick();
		}
	}


	public void draw() {
		for (WInterface myInterface : getInterfaces()) {
			myInterface.draw();
		}
	}
}
