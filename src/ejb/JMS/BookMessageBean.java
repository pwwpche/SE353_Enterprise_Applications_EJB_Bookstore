package ejb.JMS;

import Entity.BookEntity;
import Util.HibernateUtil;
import ejb.DAO.BookMgrBean;
import org.hibernate.*;

import javax.ejb.*;
import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pwwpche on 2015/4/22.
 *
 */

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty( propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty( propertyName = "destination",
                        propertyValue ="MDBQueue")
        }
)
public class BookMessageBean implements MessageListener {
    static final Logger logger = Logger.getLogger("BookMessageBean");
    @EJB
    BookMgrBean bookMgrBean;

    public BookMessageBean() {
    }

    @Override
    public void onMessage(Message inMessage) {
        try {
            if (inMessage instanceof MapMessage) {
                //Setting up session
                long bid = ((MapMessage) inMessage).getLong("bid");
                org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session.beginTransaction();
                BookEntity book = (BookEntity)session.get(BookEntity.class, bid);
                if(book != null){
                    session.delete(book);
                }else{
                    transaction.commit();
                    session.close();
                    System.out.println("Book Not exists");
                    throw new Exception("Book Not Exists");
                }
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
