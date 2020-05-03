package com.github.vini2003.spinnery.widget.api;

import com.github.vini2003.spinnery.widget.WInterface;

/**
 * Generic interface for providing a widget interface. Widget interface providers are generally
 * various implementations of screens.
 */
public interface WInterfaceProvider {
	WInterface getInterface();
}
