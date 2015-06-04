package ChatRoom.Message;

/**
 * Created by pwwpche on 2015/4/21.
 */
public class SystemMessage extends Message {
    private String name;
    private String action;

    public SystemMessage(String _name, String _action) {
        this.name = _name;
        this.action = _action;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String _action) {
        this.action = _action;
    }

    /* For logging purposes */
    @Override
    public String toString() {
        System.out.println("toString");
        if(action.equals("active")){
            return "[SystemMessage]" + "\n" + this.getName() + " is active";
        }else if(action.equals("inactive")){
            return "[SystemMessage]" + "\n" + this.getName() + " is inactive";
        }else {
            String str = "";
            for(int i = 0 ; i < 5 ; i++){
                str = str + "a";
            }
            return "[SystemMessage]" + "\n" + this.getName() + " is unknown message type - " + str;
        }
    }
}
