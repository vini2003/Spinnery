package spinnery.widget.api;

import java.util.Collection;


  used in collection widgets that pass interface events to their children and associated functional components
 /
public interface WDelegatedEventListener extends WEventListener {

	  result of this method to pass events on; this is usually done automatically by


	Collection<? extends WEventListener> getEventDelegates();
}
