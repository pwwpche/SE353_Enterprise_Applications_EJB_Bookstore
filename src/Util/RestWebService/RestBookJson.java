package Util.RestWebService;

import ejb.DAO.BookMgrBean;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("RestBookJson")
public class RestBookJson {
    @EJB
    BookMgrBean bookMgrBean;

    @GET
    @Path("/book/{param}")
    public String getMsg(@PathParam("param") String msg) {
        String output = "Book bid : " + msg;
        int bookid = 0;
        bookid = Integer.parseInt(msg);
        return bookMgrBean.getBookDetailInJson(bookid).toString();
    }
}