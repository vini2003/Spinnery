package spinnery.widget.api;

import java.util.Collection;

/**
 * Generic interface that describes an event listener which delegates event processing. This interface is generally
 * used in collection widgets that pass interface events to their children and associated functional components
 * (e.g. scrollbar) in order to separate UI parent-child structure from the event processing hierarchy.
 */
public interface WDelegatedEventListener extends WEventListener {
	/**
	 * Returns a collection of event delegates to pass events to. The event provider is responsible for using the
	 * result of this method to pass events on; this is usually done automatically by
	 * {@link spinnery.widget.WInterface}, if you are implementing this interface in a widget.
	 *
	 * @return collection of event delegates
	 */
	Collection<? extends WEventListener> getEventDelegates();
}
