package Util.WebService;

import ejb.DAO.BookMgrBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by pwwpche on 2015/5/16.
 */

@WebService

public class BookServiceImpl implements BookService {
    @EJB
    BookMgrBean bookMgr;

    @Override
    public String getBookDetailJson(int bid) {
        if(bookMgr == null) {
            String ejbStr = "java:global/BookStore_war_exploded/BookMgrBean!ejb.DAO.BookMgrBean";
            try {
                bookMgr = (BookMgrBean) new InitialContext().lookup(ejbStr);
                return bookMgr.getBookDetailInJson(bid).toString();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        if(bookMgr != null){
            return bookMgr.getBookDetailInJson(bid).toString();
        }else {
            return "error";
        }
    }

}
