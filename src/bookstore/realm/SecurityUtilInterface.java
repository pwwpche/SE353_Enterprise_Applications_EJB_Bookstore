package bookstore.realm;

import javax.ejb.Remote;
import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * Created by pwwpche on 2015/4/20.
 */
@Remote
public interface SecurityUtilInterface {
    public void authenticateUser(String username, char[] password) throws LoginException;
    public List<String> getGroups(String usid);
}
