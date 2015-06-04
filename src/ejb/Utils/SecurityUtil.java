package ejb.Utils;

import Entity.UserEntity;
import Util.HibernateUtil;
import bookstore.realm.SecurityUtilInterface;
import ejb.DAO.UserMgrBean;
import org.hibernate.Session;

import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
/**
 * Utility class that implements a lookup method to locate the authorization service class that allows to authenticate users.
 *
 * @author dgisbert
 */
@Stateless
public class SecurityUtil implements SecurityUtilInterface
{
    @EJB
    UserMgrBean userMgrBean;

    public void authenticateUser(String username, char[] password) throws LoginException
    {
        System.out.println("Authenticating:  " + "username:" + username + " password: " + String.valueOf(password));
        //Setting up session

        Session session = HibernateUtil.getSessionFactory().openSession();
        UserEntity curUser = userMgrBean.getUserFromDatabase("username", username);
        if(curUser == null){
            System.out.println("User Not Exists");
            throw new LoginException("User Not Exists");
        }
        if(!curUser.getPasswd().equals(String.valueOf(password))){
            throw new LoginException("Password not correct");
        }
    }

    /**
     * Returns the groups of this user
     */

    public List<String> getGroups(String username)
    {
        System.out.println("In SecurityUtil, Getting groups");
        List<String> result = new Vector<String>();
        UserEntity userEntity = userMgrBean.getUserFromDatabase("username", username);
        assert (userEntity != null);
        if(userEntity.getPermission() == 0){
            result.add("admin2");
        }
        if(userEntity.getPermission() >= 0){
            result.add("allUser");
        }
        return result;
    }
}