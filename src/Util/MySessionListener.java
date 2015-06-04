package Util;

/**
 * Created by pwwpche on 2014/5/6.
 *
 */

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Map;


@WebListener()
public class MySessionListener implements HttpSessionListener {
    public static Map<String,HttpSession>  session_map = new  HashMap<String,HttpSession>();

    public void  sessionCreated(HttpSessionEvent sessionEvent) {
        String session_id=sessionEvent.getSession().getId();
        System. out .println( "new Session,id is: " +session_id );

        session_map .put(session_id, sessionEvent.getSession());
    }

    public void  sessionDestroyed(HttpSessionEvent sessionEvent) {
        System. out .println( "Session removed,id is: " + sessionEvent.getSession().getId());
        session_map .remove(sessionEvent.getSession());
    }

    public static  HttpSession getSessionById(String session_id){
        System. out .println("Session get,id is: " + session_id);
        if(session_map .get(session_id) == null){
            return null;
        }else{
            return session_map.get(session_id);
        }
    }

    public static void  removeSessionById(String session_id){
        session_map .remove(session_id);
    }

    public MySessionListener() {

    }


}
