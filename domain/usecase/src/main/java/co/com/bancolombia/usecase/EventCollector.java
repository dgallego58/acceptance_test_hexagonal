package co.com.bancolombia.usecase;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import co.com.bancolombia.model.EventWrapper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class EventCollector implements EventAnswer {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EventCollector.class);
    private final NotifySubscriber notifySubscriber;

    public EventCollector(NotifySubscriber notifySubscriber) {
        this.notifySubscriber = notifySubscriber;
    }

    @Override
    public EventWrapper getEvents(String message) {
        log.info("CALLING EVENT");
        Logger logger = (Logger) LoggerFactory.getLogger(NotifySubscriber.class);
        var eventAppender = new ListAppender<ILoggingEvent>();
        eventAppender.start();
        logger.addAppender(eventAppender);
        var events = new ArrayList<String>();

        notifySubscriber.dispatchNotification(message);
        eventAppender.list
                .stream()
                .map(ILoggingEvent::getMessage)
                .forEach(events::add);
        return new EventWrapper(events);
    }
}
