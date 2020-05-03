package com.github.vini2003.spinnery.debug;

import net.minecraft.nbt.CompoundNBT;
import com.github.vini2003.spinnery.widget.WButton;
import com.github.vini2003.spinnery.widget.api.WNetworked;

public class WNetworkedButton extends WButton implements WNetworked {
	@Override
	public int getSyncId() {
		return 0;
	}

	@Override
	public void onInterfaceEvent(Event event, CompoundNBT payload) {
		if (event == Event.MOUSE_CLICK) {
			System.out.println("Serverside button click!");
		}
	}
}
