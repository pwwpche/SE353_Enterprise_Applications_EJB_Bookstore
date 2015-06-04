package bookstore.realm;

import com.sun.appserv.security.AppservPasswordLoginModule;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom module
 *
 * @author dgisbert
 */

public class MyCustomModule extends AppservPasswordLoginModule
{

    @EJB
    SecurityUtilInterface securityUtil;

    @Override
    protected void authenticateUser() throws LoginException
    {
        String str = String.valueOf(_passwd);
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, "username:" + _username + " password: " + str);

        try{
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            final Context context = new InitialContext(props);
            String ejbStr = "java:global/BookStore_war_exploded/SecurityUtil!bookstore.realm.SecurityUtilInterface";
            securityUtil = (SecurityUtilInterface) context.lookup(ejbStr);

        }catch (Exception e){
            e.printStackTrace();
        }

        securityUtil.authenticateUser(_username, _passwd);

        List<String> groups2 = securityUtil.getGroups(_username);
        String[] groups = groups2.toArray(new String[groups2.size()]);
        logger.log(Level.INFO, "setting up groups");
        for(int i = 0 ; i < groups.length ; i++){
            logger.log(Level.INFO, "Group idx=" + i + "name=" + groups[i]);
        }
        commitUserAuthentication(groups);
    }
}