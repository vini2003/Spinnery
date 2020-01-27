package spinnery.widget;

import net.minecraft.nbt.CompoundTag;

import java.util.function.BiConsumer;

public abstract class WWidgetSynced extends WWidget implements WSynced {
    protected BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent;
    protected final int syncId;

    public WWidgetSynced(int syncId) {
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

    public void setOnInterfaceEvent(BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent) {
        this.consumerOnInterfaceEvent = consumerOnInterfaceEvent;
    }
}
