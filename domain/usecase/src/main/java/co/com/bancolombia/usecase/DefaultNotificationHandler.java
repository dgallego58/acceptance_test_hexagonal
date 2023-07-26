package co.com.bancolombia.usecase;

public class DefaultNotificationHandler implements NotifySubscriber {


    @Override
    public void dispatchNotification(String message) {
        if (message.isEmpty()) {
            doLog(this);
            throw new InvalidMessage("El mensaje no puede estar vacío");
        }
    }

    public static class InvalidMessage extends RuntimeException {
        public InvalidMessage(String message) {
            super(message);
        }
    }
}
