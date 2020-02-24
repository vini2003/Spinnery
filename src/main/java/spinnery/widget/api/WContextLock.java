package spinnery.widget.api;

import spinnery.widget.WAbstractWidget;

public interface WContextLock {
	boolean isActive();

	<W extends WAbstractWidget> W setActive(boolean active);
}
