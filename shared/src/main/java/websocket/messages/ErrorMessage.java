package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage =errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "serverMessageType=" + getServerMessageType() + ", " +
                "message=" + getErrorMessage() +
                '}';
    }
}
