package Locale;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by pwwpche on 2015/5/8.
 */
public class LangBundle {

    public LangBundle(){
    }

    public String getStr(String key, HttpSession session){
        if(session.getAttribute("Language") == null){
            session.setAttribute("Language", Locale.ENGLISH);
        }
        ResourceBundle resStrings = ResourceBundle.getBundle("Locale.LoginString", (Locale) session.getAttribute("Language"));
        return resStrings.getString(key);
    }

    public void changeLanguage(HttpSession session){
        if(session.getAttribute("Language") == null){
            session.setAttribute("Language", Locale.ENGLISH);
        }
        Locale lang = (Locale) session.getAttribute("Language");
        if(lang.equals( Locale.CHINA)){
            session.setAttribute("Language", Locale.ENGLISH);
        }else{
            session.setAttribute("Language", Locale.CHINA);
        }
    }
}

