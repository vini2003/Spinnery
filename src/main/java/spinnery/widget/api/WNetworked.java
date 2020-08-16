package spinnery.widget.api;

import net.minecraft.nbt.CompoundTag;


  processing. These widgets may also be created serverside; to that end, they should implement the
  Additionally, after the default payload has been constructed but before the packet has been sent, the
  the compound NBT tag.
 */
public interface WNetworked {

	  and server. The value may not be constant; however, the process that returns the ID must be strictly deterministic.
	 *

	int getSyncId();

	void onInterfaceEvent(Event event, CompoundTag payload);


	  the packet has been sent.
	 *
	  @param payload payload NBT tag
	 */
	default void appendPayload(Event event, CompoundTag payload) {
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
