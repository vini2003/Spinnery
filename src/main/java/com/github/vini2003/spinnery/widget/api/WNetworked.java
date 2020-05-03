package com.github.vini2003.spinnery.widget.api;

import net.minecraft.nbt.CompoundNBT;

/**
 * Generic interface representing a networked widget. Networked widgets send interface events to the server for
 * processing. These widgets may also be created serverside; to that end, they should implement the
 * {@link #onInterfaceEvent(Event, CompoundNBT)} method, which is called whenever an event is received.
 * Additionally, after the default payload has been constructed but before the packet has been sent, the
 * {@link #appendPayload(Event, CompoundNBT)} method is called on this object, where custom data may be appended to
 * the compound NBT tag.
 */
public interface WNetworked {
	/**
	 * Returns the synchronisation ID for this widget, which is used to uniquely identify widgets between the client
	 * and server. The value may not be constant; however, the process that returns the ID must be strictly deterministic.
	 *
	 * @return synchronisation ID
	 */
	int getSyncId();

	void onInterfaceEvent(Event event, CompoundNBT payload);

	/**
	 * Appends custom data to the packet payload. Called after the default payload has been constructed, but before
	 * the packet has been sent.
	 *
	 * @param event   interface event
	 * @param payload payload NBT tag
	 */
	default void appendPayload(Event event, CompoundNBT payload) {
	}

	enum Event {
		CUSTOM,
		MOUSE_CLICK,
		MOUSE_RELEASE,
		MOUSE_DRAG,
		MOUSE_SCROLL,
		FOCUS,
		KEY_PRESS,
		KEY_RELEASE,
		CHAR_TYPE,
	}
}
