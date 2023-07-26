package co.com.bancolombia.usecase;

import co.com.bancolombia.model.EventWrapper;

public interface EventAnswer {

    static EventAnswer of(NotifySubscriber notifySubscriber) {
        return new EventCollector(notifySubscriber);
    }

    EventWrapper getEvents(String message);
}
