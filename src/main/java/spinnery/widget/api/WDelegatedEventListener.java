package spinnery.widget.api;

import java.util.Collection;

public interface WDelegatedEventListener extends WEventListener {
    Collection<? extends WEventListener> getEventDelegates();
}
