package ChatRoom.Message;

/**
 * Created by pwwpche on 2015/4/21.
 */
public class TextMessage extends Message {

    private String name;
    private String message;

    public TextMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /* For logging purposes */
    @Override
    public String toString() {
        return "[TextMessage]" + "\n" + this.getName() + " : " + this.getMessage();
    }
}

