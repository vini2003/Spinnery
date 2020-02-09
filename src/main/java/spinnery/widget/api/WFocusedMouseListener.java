package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies that the annotated widget only listens for mouse events when focused, that is, when
 * {@link WAbstractWidget#isFocused()} returns <tt>true</tt>. This behavior is not inherited by child
 * classes!
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WFocusedMouseListener {
}
