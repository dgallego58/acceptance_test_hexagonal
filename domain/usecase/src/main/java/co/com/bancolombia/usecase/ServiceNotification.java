package co.com.bancolombia.usecase;

public class ServiceNotification implements NotifySubscriber {

    private final NotifySubscriber notifySubscriber;

    public ServiceNotification(NotifySubscriber notifySubscriber) {
        this.notifySubscriber = notifySubscriber;
    }

    @Override
    public void dispatchNotification(String message) {
        if (!message.contains("USER")) {
            notifySubscriber.dispatchNotification(message);
        }
        doLog(this);
    }
}
