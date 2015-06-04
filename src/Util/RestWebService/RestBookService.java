package Util.RestWebService;

import ejb.DAO.BookMgrBean;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/RestBookQuery")
public class RestBookService {
    @EJB
    BookMgrBean bookMgrBean;

    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {
        String output = "Book bid : " + msg;
        int bookid = 0;
        bookid = Integer.parseInt(msg);
        return Response.status(200).entity(output).build();
    }

}