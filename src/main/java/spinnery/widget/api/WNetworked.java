package spinnery.widget.api;

import net.minecraft.nbt.CompoundTag;

public interface WNetworked {
	enum Event {
		MOUSE_CLICK,
		MOUSE_RELEASE,
		MOUSE_DRAG,
		MOUSE_SCROLL,
		FOCUS,
		KEY_PRESS,
		KEY_RELEASE,
		CHAR_TYPE,
	}

    int getSyncId();
    void onInterfaceEvent(Event event, CompoundTag payload);
    default void appendPayload(Event event, CompoundTag payload) {}
}
