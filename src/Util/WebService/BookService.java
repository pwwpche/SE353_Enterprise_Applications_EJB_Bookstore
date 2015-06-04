package Util.WebService;

import javax.json.JsonObject;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by pwwpche on 2015/5/16.
 */
@WebService
public interface BookService {
    @WebMethod
    public String getBookDetailJson(int bid);
}
