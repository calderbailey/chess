package websocket.messages;


public class NotificationMessage extends ServerMessage {
    private String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message =message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "serverMessageType=" + getServerMessageType() + ", " +
                "message=" + getMessage() +
                '}';
    }
}
