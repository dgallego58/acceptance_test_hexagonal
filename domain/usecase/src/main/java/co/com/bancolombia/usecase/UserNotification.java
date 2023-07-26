package co.com.bancolombia.usecase;


public class UserNotification implements NotifySubscriber {

    private final NotifySubscriber notifySubscriber;

    public UserNotification(NotifySubscriber notifySubscriber) {
        this.notifySubscriber = notifySubscriber;
    }

    @Override
    public void dispatchNotification(String message) {
        if (!message.contains("SERVICE")) {
            //si no es para service, entonces notifique al siguiente
            notifySubscriber.dispatchNotification(message);
        }
        doLog(this);
    }

}
