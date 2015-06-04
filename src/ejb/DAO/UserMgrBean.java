package ejb.DAO;

import Entity.UserEntity;
import Util.HibernateUtil;
import ejb.Utils.MemCachedUtil;
import ejb.Utils.JsonBookGenerator;
import org.hibernate.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.json.*;
import java.util.List;

/**
 * Created by pwwpche on 2015/3/27.
 *
 */

@Stateless(name = "UserMgrBeanEJB")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserMgrBean {
    @EJB
    JsonBookGenerator jsonBookGenerator;
    @EJB
    MemCachedUtil memCachedUtil;

    public JsonObject addUserToDatabase(UserEntity user)
    {
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();

        if(user == null || getUserFromDatabase("username", String.valueOf(user.getUid())) != null)
        {
            System.out.println("User Exists");
            return jsonBookGenerator.getErrorMessage("User Exists");
        }

        //Write to database
        session.save(user);
        trans.commit();
        session.flush();
        session.close();
        return jsonBookGenerator.getOkMessage();
    }

    public JsonObject modifyUserToDatabase(UserEntity user)
    {
        if(user == null ||getUserFromDatabase("username", user.getUsername()) == null){
            System.out.println("User Not exists");
            return jsonBookGenerator.getErrorMessage("User Not exists");
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.saveOrUpdate(user);
        trans.commit();
        session.close();
        memCachedUtil.setObject("UserEntity:" + "username" + "=" + user.getUsername(), user);
        return jsonBookGenerator.getOkMessage();
    }

    public JsonObject removeUserFromDatabase(String username)
    {
        UserEntity user = getUserFromDatabase("username", username);

        if(user == null)
        {
            System.out.println("User Not exists");
            return jsonBookGenerator.getErrorMessage("User Not exists");
        }
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();

        session.delete(user);
        trans.commit();
        session.close();
        memCachedUtil.removeObject("UserEntity:" + "username" + "=" + username);
        return jsonBookGenerator.getOkMessage();
    }

    public UserEntity getUserFromDatabase(String opt, String val)
    {
        UserEntity userCached = (UserEntity) memCachedUtil.getObject("UserEntity:" + opt + "=" + val);
        if(userCached == null) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql;
            Query query;

            switch (opt) {
                case "username":
                    hql = "from UserEntity as userEntity where userEntity.username = :name";
                    query = session.createQuery(hql);
                    query.setLockOptions(LockOptions.READ);
                    query.setString("name", val);
                    break;
                case "uid":
                    hql = "from UserEntity as userEntity where userEntity.uid = :uid";
                    query = session.createQuery(hql);
                    query.setLockOptions(LockOptions.READ);
                    query.setString("uid", val);
                    break;
                default:
                    return null;
            }
            List res = query.list();
            if (res.size() == 0) {
                session.close();
                return null;
            } else {
                session.close();
                String keyCached = "UserEntity:" + opt + "=" + val;
                memCachedUtil.setObject(keyCached, res.get(0));
                return (UserEntity) res.get(0);
            }
        }else{
            return userCached;
        }
    }

    public JsonArray getAllUsers(){
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        //Convert from database format to JSON
        String hql = "from UserEntity";
        Query query = session.createQuery(hql);
        List resList = query.list();
        for (Object aResList : resList) {
            UserEntity curUser = (UserEntity) aResList;
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("uid", curUser.getUid())
                    .add("username", curUser.getUsername())
                    .add("password", curUser.getPasswd())
                    .add("email", curUser.getEmail())
                    .add("permission", curUser.getPermission())
                    .add("regDate", curUser.getRegDate().toString());
            arrayBuilder.add(builder.build());
        }

        //Send it back
        session.close();
        return arrayBuilder.build();
    }


}
