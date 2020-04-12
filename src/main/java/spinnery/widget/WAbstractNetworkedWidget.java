package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import spinnery.registry.NetworkRegistry;
import spinnery.widget.api.WNetworked;

import java.util.function.BiConsumer;

/**
 * A WAbstractNetworkedWidget is a default implementation of the
 * {@link WNetworked} interface, providing utility methods for
 * its usage.
 */
@SuppressWarnings("unchecked")
public abstract class WAbstractNetworkedWidget extends WAbstractWidget implements WNetworked {
	protected BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent;
	protected int syncId;

	/**
	 * Retrieves the synchronization ID of this widget, which for most purposes should be unique, but the same in client and server.
	 *
	 * @return The synchronization ID of this widget.
	 */
	@Override
	public int getSyncId() {
		return syncId;
	}

	/**
	 * Sets the synchronization ID of this widget, which for most purposes should be unique, but the same in client and server.
	 *
	 * @param syncId The synchronization ID to be used by this widget.
	 */
	public <W extends WAbstractNetworkedWidget> W setSyncId(int syncId) {
		this.syncId = syncId;
		return (W) this;
	}

	/**
	 * Dispatches the event attached to this widget when an event and a payload are received.
	 *
	 * @param event   Event received.
	 * @param payload Payload received.
	 */
	@Override
	public void onInterfaceEvent(Event event, CompoundTag payload) {
		if (this.consumerOnInterfaceEvent != null) {
			this.consumerOnInterfaceEvent.accept(event, payload);
		}
	}

	/**
	 * Retrieves the event attached to this widget for when an event and a payload are received.
	 *
	 * @return The event attached to this widget.
	 */
	public BiConsumer<Event, CompoundTag> getOnInterfaceEvent() {
		return consumerOnInterfaceEvent;
	}

	/**
	 * Sets the event attached to this widget for when an event and a payload are received.
	 *
	 * @param consumerOnInterfaceEvent The event attached to this payload for when an event and a payload are received.
	 */
	public <W extends WAbstractNetworkedWidget> W setOnInterfaceEvent(BiConsumer<Event, CompoundTag> consumerOnInterfaceEvent) {
		this.consumerOnInterfaceEvent = consumerOnInterfaceEvent;
		return (W) this;
	}

	/**
	 * Dispatches a custom event with no pre-existing {@link WNetworked.Event} attached.
	 *
	 * @param payload Payload for the custom event.
	 */
	@Environment(EnvType.CLIENT)
	public void sendCustomEvent(CompoundTag payload) {
		NetworkRegistry.createCustomInterfaceEventPacket(this, payload);
	}
}
