package spinnery.widget;

import net.minecraft.nbt.CompoundTag;
import spinnery.widget.api.WNetworked;

import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public abstract class WNetworkedWidget extends WAbstractWidget implements WNetworked {
	protected BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent;
	protected int syncId;

	@Override
	public int getSyncId() {
		return syncId;
    }

    public <W extends WNetworkedWidget> W setSyncId(int syncId) {
        this.syncId = syncId;
        return (W) this;
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

    public <W extends WNetworkedWidget> W setOnInterfaceEvent(BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent) {
		this.consumerOnInterfaceEvent = consumerOnInterfaceEvent;
		return (W) this;
    }
}
