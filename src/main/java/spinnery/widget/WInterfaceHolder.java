package spinnery.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WInterfaceHolder {
	List<WInterface> heldInterfaces = new ArrayList<>();

	public List<WInterface> getInterfaces() {
		return heldInterfaces;
	}

	public boolean contains(WWidget... interfaces) {
		return heldInterfaces.containsAll(Arrays.asList(interfaces));
	}

	public void add(WInterface... interfaces) {
		heldInterfaces.addAll(Arrays.asList(interfaces));
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

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onMouseClicked(mouseX, mouseY, mouseButton);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onMouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}

		return false;
	}


	public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onMouseReleased(mouseX, mouseY, mouseButton);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onMouseReleased(mouseX, mouseY, mouseButton);

					}
				}
			}
		}
		return false;
	}


	public boolean onMouseDragged(int mouseX, int mouseY, int mouseButton, int deltaX, int deltaY) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

					}
				}
			}
		}

		return false;
	}


	public boolean onMouseScrolled(int mouseX, int mouseY, double deltaY) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onMouseScrolled(mouseX, mouseY, deltaY);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onMouseScrolled(mouseX, mouseY, deltaY);

					}
				}
			}
		}

		return false;
	}

	public void mouseMoved(int mouseX, int mouseY) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.scanFocus(mouseX, mouseY);
				widgetA.onMouseMoved(mouseX, mouseY);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.scanFocus(mouseX, mouseY);
						widgetB.onMouseMoved(mouseX, mouseY);

					}
				}
			}
		}
	}


	public boolean onKeyReleased(int character, int keyCode, int keyModifier) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onKeyReleased(keyCode);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onKeyReleased(keyCode);
					}
				}
			}
		}
		return false;
	}

	public boolean keyPressed(int character, int keyCode, int keyModifier) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onKeyPressed(character, keyCode, keyModifier);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onKeyPressed(character, keyCode, keyModifier);
					}
				}
			}
		}
		return false;
	}


	public boolean onCharTyped(char character, int keyCode) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onCharTyped(character);

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onCharTyped(character);
					}
				}
			}
		}

		return false;
	}

	public void drawMouseoverTooltip(int mouseX, int mouseY) {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.onDrawTooltip();

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.onDrawTooltip();
					}
				}
			}
		}
	}

	public void tick() {
		for (WInterface myInterface : getInterfaces()) {
			for (WWidget widgetA : myInterface.getWidgets()) {
				widgetA.tick();

				if (widgetA instanceof WCollection) {
					for (WWidget widgetB : ((WCollection) widgetA).getWidgets()) {
						widgetB.tick();
					}
				}
			}
		}
	}


	public void draw() {
		for (WInterface myInterface : getInterfaces()) {
			myInterface.draw();
		}
	}
}
