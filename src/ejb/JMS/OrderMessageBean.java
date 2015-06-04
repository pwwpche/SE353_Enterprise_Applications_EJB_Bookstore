package ejb.JMS;

import Entity.BookEntity;
import Entity.SaleEntity;
import Entity.UserEntity;
import Util.HibernateUtil;
import org.hibernate.Transaction;

import javax.ejb.*;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pwwpche on 2015/4/23.
 *
 */
@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty( propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty( propertyName = "destination",
                        propertyValue ="OrderQueue")
        }
)

public class OrderMessageBean implements MessageListener {
    static final Logger logger = Logger.getLogger("OrderMessageBean");

    public OrderMessageBean() {
    }

    @Override
    public void onMessage(Message inMessage) {
        try {
            if (inMessage instanceof MapMessage) {
                //Setting up session
                long uid = ((MapMessage) inMessage).getLong("uid");
                double totalPrice =((MapMessage) inMessage).getDouble("totalPrice");
                org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session.beginTransaction();
                SaleEntity saleEntity = new SaleEntity();
                saleEntity.setUid(uid);
                saleEntity.setTotprice(totalPrice);
                saleEntity.setsDay(Calendar.getInstance().get(Calendar.YEAR));
                saleEntity.setsMonth(Calendar.getInstance().get(Calendar.MONTH));
                saleEntity.setsDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                session.save(saleEntity);
                transaction.commit();
                session.close();
            } else {
                logger.log(Level.WARNING,
                        "Message of wrong type: {0}",
                        inMessage.getClass().getName());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    "onMessage: JMSException: {0}",
                    e.toString());
        }
    }
}
