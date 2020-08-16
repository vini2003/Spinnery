package spinnery.widget.declaration.event;

import spinnery.common.utilities.Networks;
import spinnery.widget.implementation.WAbstractWidget;
import spinnery.widget.declaration.collection.WCollection;

public interface WEventConsumer {
	default void onKeyPressed(int keyCode, int scanCode, int keyModifiers) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onKeyPressed(keyCode, scanCode, keyModifiers);

				if (widget.shouldSynchronize(Networks.KEY_PRESS)) {
					Networks.toServer(Networks.KEY_PRESS, Networks.ofKeyPress(widget.getInterface().getHandler().syncId, widget.hash(), keyCode, scanCode, keyModifiers));
				}
			}
		}
	}

	default void onKeyReleased(int keyCode, int scanCode, int keyModifiers) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onKeyReleased(keyCode, scanCode, keyModifiers);

				if (widget.shouldSynchronize(Networks.KEY_RELEASE)) {
					Networks.toServer(Networks.KEY_RELEASE, Networks.ofKeyRelease(widget.getInterface().getHandler().syncId, widget.hash(), keyCode, scanCode, keyModifiers));
				}
			}
		}
	}

	default void onCharTyped(char character, int keyCode) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onCharTyped(character, keyCode);

				if (widget.shouldSynchronize(Networks.CHAR_TYPE)) {
					Networks.toServer(Networks.CHAR_TYPE, Networks.ofCharType(widget.getInterface().getHandler().syncId, widget.hash(), character, keyCode));
				}
			}
		}
	}

	default void onMouseReleased(float x, float y, int button) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onMouseReleased(x, y, button);

				if (widget.shouldSynchronize(Networks.MOUSE_RELEASE)) {
					Networks.toServer(Networks.MOUSE_RELEASE, Networks.ofMouseRelease(widget.getInterface().getHandler().syncId, widget.hash(), x, y, button));
				}
			}
		}
	}

	default void onMouseClicked(float x, float y, int button) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onMouseClicked(x, y, button);

				if (widget.shouldSynchronize(Networks.MOUSE_CLICK)) {
					Networks.toServer(Networks.MOUSE_CLICK, Networks.ofMouseClick(widget.getInterface().getHandler().syncId, widget.hash(), x, y, button));
				}
			}
		}
	}

	default void onMouseDragged(float x, float y, int button, double deltaX, double deltaY) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onMouseDragged(x, y, button, deltaX, deltaY);

				if (widget.shouldSynchronize(Networks.MOUSE_DRAG)) {
					Networks.toServer(Networks.MOUSE_DRAG, Networks.ofMouseDrag(widget.getInterface().getHandler().syncId, widget.hash(), x, y, button, deltaX, deltaY));
				}
			}
		}
	}

	default void onMouseMoved(float x, float y) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				boolean then = widget.isFocused();

				widget.updateFocus(x, y);

				boolean now = widget.isFocused();

				if (then && !now) {
					widget.onFocusReleased();

					if (widget.shouldSynchronize(Networks.FOCUS_RELEASE)) {
						Networks.toServer(Networks.FOCUS_RELEASE, Networks.ofFocusRelease(widget.getInterface().getHandler().syncId, widget.hash()));
					}
				} else if (!then && now) {
					widget.onFocusGained();

					if (widget.shouldSynchronize(Networks.FOCUS_GAIN)) {
						Networks.toServer(Networks.FOCUS_GAIN, Networks.ofFocusGain(widget.getInterface().getHandler().syncId, widget.hash()));
					}
				}

				widget.onMouseMoved(x, y);

				if (widget.shouldSynchronize(Networks.MOUSE_MOVE)) {
					Networks.toServer(Networks.MOUSE_MOVE, Networks.ofMouseMove(widget.getInterface().getHandler().syncId, widget.hash(), x, y));
				}
			}
		}
	}

	default void onMouseScrolled(float x, float y, double deltaY) {
		if (this instanceof WCollection) {
			for (WAbstractWidget widget : ((WCollection) this).getWidgets()) {
				widget.onMouseScrolled(x, y, deltaY);

				if (widget.shouldSynchronize(Networks.MOUSE_SCROLL)) {
					Networks.toServer(Networks.MOUSE_SCROLL, Networks.ofMouseScroll(widget.getInterface().getHandler().syncId, widget.hash(), x, y, deltaY));
				}
			}
		}
	}

	default void onFocusGained() {
	}

	default void onFocusReleased() {
	}
}
