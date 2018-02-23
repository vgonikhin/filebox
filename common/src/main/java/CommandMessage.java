public class CommandMessage extends AbstractMessage {
    public static final int CM_AUTH_RIGHT = 23453634;
    public static final int CM_AUTH_WRONG = 32513854;

    private int type;
    Object[] attachment;

    public CommandMessage(int type, Object... attachment) {
        this.type = type;
        this.attachment = attachment;
    }

    public int getType() {
        return type;
    }

    public Object[] getAttachment() {
        return attachment;
    }
}
