package co.com.bancolombia.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NotifySubscriber {
    Logger log = LoggerFactory.getLogger(NotifySubscriber.class);

    static NotifySubscriber createChain() {
        var defaultHandler = new DefaultNotificationHandler();
        var serviceHandler = new ServiceNotification(defaultHandler);
        return new UserNotification(serviceHandler);
    }

    default void doLog(Object o) {
        log.info("Notified from {}", o.getClass().getName());
    }

    void dispatchNotification(String message);

}
