package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

/**
 * Generic interface that describes a context lock for an object,
 * allowing actions to be taken based on the lock state.
 */
public interface WContextLock {
	boolean isActive();

	<W extends WAbstractWidget> W setActive(boolean active);
}
