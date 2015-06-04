package bookstore.realm;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom Realm class
 *
 * @author dgisbert
 */

public class MyCustomRealm extends AppservRealm
{
    @EJB
    SecurityUtilInterface securityUtil;
    /** JAAS Context parameter name */ public static final String PARAM_JAAS_CONTEXT = "jaas-context";

    /** Authentication type description */

    public static final String AUTH_TPYE = "Authentication done by checking user at table USERS on database";

    @Override
    public void init(Properties properties) throws BadRealmException, NoSuchRealmException
    {
        String propJaasContext = properties.getProperty(PARAM_JAAS_CONTEXT);
        if (propJaasContext != null) setProperty(PARAM_JAAS_CONTEXT, propJaasContext);

        Logger logger = Logger.getLogger(this.getClass().getName());
        String realmName = this.getClass().getSimpleName();
        logger.log(Level.INFO, realmName + " started. ");
        logger.log(Level.INFO, realmName + ": " + getAuthType());
        for (Entry<Object, Object> property:properties.entrySet()) logger.log(Level.INFO, property.getKey() + ": " + property.getValue());
    }

    @Override
    public String getAuthType()
    {
        return AUTH_TPYE;
    }

    @Override
    public Enumeration<?> getGroupNames(String usid) throws InvalidOperationException, NoSuchUserException
    {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, "In MyRealm getting groups");
        try{
            final Context context = new InitialContext();
            String ejbStr = "java:global/BookStore_war_exploded/SecurityUtil!bookstore.realm.SecurityUtilInterface";
            securityUtil = (SecurityUtilInterface) context.lookup(ejbStr);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.enumeration(securityUtil.getGroups(usid));
    }
}