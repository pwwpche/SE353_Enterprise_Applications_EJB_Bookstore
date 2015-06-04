package ejb.DAO;


import Entity.SaleEntity;
import Entity.UserEntity;
import Util.HibernateUtil;
import ejb.Utils.JsonBookGenerator;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import java.util.List;

/**
 * Created by pwwpche on 2015/4/19.
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SalesMgrBean {
    @EJB
    UserMgrBean userManagement;

    @EJB
    JsonBookGenerator jsonBookGenerator;
    public JsonObject addSaleToDatabase(String reqType, String reqData, String reqItem)
    {
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        reqType = intToType(Integer.valueOf(reqType));
        //Verify registration information
        //Check Username
        String hql;
        if(reqItem.equals("items")) {
            hql = "from SaleEntity as saleEntity where saleEntity." + reqType + "= :reqdata";
            Query query = session.createQuery(hql);
            System.out.println(hql);
            if (typeToInt(reqType) == 1) {
                long uid = userManagement.getUserFromDatabase("username", reqData).getUid();
                query.setLong("reqdata", uid);
            } else if (typeToInt(reqType) == 5) {
                query.setString("reqdata", reqData);
            } else {
                query.setInteger("reqdata", Integer.valueOf(reqData));
            }
            List res = query.list();
            if (res.size() == 0) {
                return Json.createObjectBuilder().build();
            } else {
                JsonObjectBuilder total = Json.createObjectBuilder().add("total", res.size());
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (Object re : res) {
                    SaleEntity sale = (SaleEntity) re;
                    JsonObjectBuilder builder = Json.createObjectBuilder()
                            .add("sid", sale.getSid())
                            .add("uid", sale.getUid())
                            .add("day", sale.getsDay())
                            .add("month", sale.getsMonth())
                            .add("year", sale.getsYear());
                    arrayBuilder.add(builder.build());
                }
                total.add("rows", arrayBuilder.build());
                total.add("item", reqItem);
                return total.build();
            }
        }
        else
        {
            hql = "select saleEntity."+ reqType +", sum(bookEntity.price) from SaleEntity saleEntity ,BookEntity bookEntity, BooksaleEntity booksale " +
                    "where saleEntity.sid = booksale.sid and booksale.bid = bookEntity.bid " +
                    "group by saleEntity." + reqType;
            Query query = session.createQuery(hql);
            System.out.println(hql);
            List<Object[]> res = query.list();
            if (res.size() == 0) {
               return Json.createObjectBuilder().build();
            } else {
                JsonObjectBuilder total = Json.createObjectBuilder().add("total", res.size());
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (Object[] result : res) {
                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    if(typeToInt(reqType) == 1) {
                        builder.add(reqType, (Long) result[0]);
                    }else{
                        builder.add(reqType, (Integer) result[0]);
                    }
                    builder.add("sumprice", (Double) result[1]);
                    arrayBuilder.add(builder.build());
                }
                total.add("rows",arrayBuilder.build());
                total.add("item", reqItem);
                return total.build();
            }
        }
    }


    public JsonObject dealSale(String username, double totalPrice){
        try{
            InitialContext ctx = new InitialContext();
            Queue queue = (Queue) ctx.lookup("OrderQueue");
            QueueConnectionFactory factory =
                    (QueueConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
            QueueConnection connection =  factory.createQueueConnection();
            QueueSession queueSession = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            MapMessage msg = queueSession.createMapMessage();
            UserEntity userEntity = userManagement.getUserFromDatabase("username", username);
            if(userEntity == null){
                throw new Exception("User not exists");
            }
            msg.setLong("uid", userEntity.getUid());
            msg.setDouble("totalPrice", totalPrice);
            QueueSender sender = queueSession.createSender(null);
            sender.send(queue, msg);
            queueSession.close();

        }catch (Exception e){
            if(e.getMessage().equals("User not exists")){
                return jsonBookGenerator.getErrorMessage("User not exists");
            }else{
                e.printStackTrace();
                return jsonBookGenerator.getErrorMessage("error");
            }
        }
        return jsonBookGenerator.getOkMessage();
    }

    private int typeToInt(String str)
    {
        if(str.equals("uid"))
            return 1;
        if(str.equals("day"))
            return 2;
        if(str.equals("month"))
            return 3;
        if(str.equals("year"))
            return 4;
        return 0;
    }

    private String intToType(int num)
    {
        if(num == 1)
            return "uid";
        if(num == 2)
            return "sDay";
        if(num == 3)
            return "sMonth";
        if(num == 4)
            return "sYear";
        return "";
    }

}
