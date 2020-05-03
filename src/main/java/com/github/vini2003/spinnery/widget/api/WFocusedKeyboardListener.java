package com.github.vini2003.spinnery.widget.api;

import com.github.vini2003.spinnery.widget.WAbstractWidget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies that the annotated widget only listens for keyboard events when focused, that is, when
 * {@link WAbstractWidget#isFocused()} returns true. This behavior is not inherited by child
 * classes!
 *
 * @deprecated Replaced with {@link WAbstractWidget#isFocusedKeyboardListener()}.
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface WFocusedKeyboardListener {
}
