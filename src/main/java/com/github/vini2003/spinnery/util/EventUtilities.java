package com.github.vini2003.spinnery.util;

import com.github.vini2003.spinnery.widget.WAbstractWidget;
import com.github.vini2003.spinnery.widget.api.WEventListener;
import com.github.vini2003.spinnery.widget.api.WFocusedKeyboardListener;
import com.github.vini2003.spinnery.widget.api.WFocusedMouseListener;

public class EventUtilities {
	/**
	 * Returns whether a widget is eligible to receive a mouse event. If the widget is a focused mouse listener,
	 * returns true only if it is focused; otherwise, always returns true.
	 *
	 * @return true if mouse event should go through.
	 */
	@SuppressWarnings("deprecation")
	public static <T extends WEventListener> boolean canReceiveMouse(T target) {
		if (target instanceof WAbstractWidget) {
			WAbstractWidget widget = (WAbstractWidget) target;
			return !(widget.isFocusedMouseListener() || target.getClass().isAnnotationPresent(WFocusedMouseListener.class)) || widget.isFocused();
		}
		return true;
	}

	/**
	 * Returns whether a widget is eligible to receive a keyboard event. If the widget is a focused keyboard listener,
	 * returns true only if it is focused; otherwise, always returns true.
	 *
	 * @return true if mouse event should go through.
	 */
	@SuppressWarnings("deprecation")
	public static <T extends WEventListener> boolean canReceiveKeyboard(T target) {
		if (target instanceof WAbstractWidget) {
			WAbstractWidget widget = (WAbstractWidget) target;
			return !(widget.isFocusedKeyboardListener() || target.getClass().isAnnotationPresent(WFocusedKeyboardListener.class)) || widget.isFocused();
		}
		return true;
	}
}
