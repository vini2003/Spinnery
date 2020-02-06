package spinnery.widget;

import net.minecraft.nbt.CompoundTag;
import spinnery.widget.api.WNetworked;

import java.util.function.BiConsumer;

public class WWidgetNetworked extends WAbstractWidget implements WNetworked {
	protected BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent;
	protected final int syncId;

	public WWidgetNetworked(int syncId) {
		this.syncId = syncId;
	}

	@Override
	public int getSyncId() {
		return syncId;
    }

    @Override
    public void onInterfaceEvent(Event event, CompoundTag payload) {
        if (this.consumerOnInterfaceEvent != null) {
            this.consumerOnInterfaceEvent.accept(event, payload);
        }
    }

    public BiConsumer<Event, CompoundTag> getOnInterfaceEvent() {
        return consumerOnInterfaceEvent;
    }

    public <W extends WWidgetNetworked> W setOnInterfaceEvent(BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent) {
		this.consumerOnInterfaceEvent = consumerOnInterfaceEvent;
		return (W) this;
    }
}
