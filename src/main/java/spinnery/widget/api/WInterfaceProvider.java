package spinnery.widget.api;

import spinnery.widget.WInterface;

/**
 * Generic interface for providing a widget interface. Widget interface providers are generally
 * various implementations of screens.
 */
public interface WInterfaceProvider {
	WInterface getInterface();
}
