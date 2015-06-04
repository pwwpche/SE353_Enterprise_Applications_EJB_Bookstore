package ejb.DAO;

/**
 * Created by pwwpche on 2015/3/26.
 *
 */
import Entity.BookEntity;
import Util.HibernateUtil;
import ejb.Utils.JsonBookGenerator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.naming.InitialContext;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BookMgrBean  {
    @EJB
    JsonBookGenerator jsonBookGenerator;

    public JsonObject addBookToDatabase(BookEntity book)
    {
        //TODO: How to add author to list?
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        if(session.get(BookEntity.class, book.getBid()) != null){
            System.out.println("Book Exists");
            return jsonBookGenerator.getErrorMessage("Book Exists");
        }
        session.save(book);     //Return value is book bid
        transaction.commit();
        session.close();
        return jsonBookGenerator.getOkMessage();
    }

    public JsonObject modifyBookToDatabase(BookEntity book)
    {
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        BookEntity bookEntity = (BookEntity)session.get(BookEntity.class, book.getBid());
        Transaction transaction = session.beginTransaction();
        if(bookEntity != null){
            session.saveOrUpdate(bookEntity);
        }else{
            System.out.println("Book Not exists");
            return jsonBookGenerator.getErrorMessage("Book Not exists");
        }
        transaction.commit();
        session.close();

        //Exit registration
        return jsonBookGenerator.getOkMessage();
    }



    public BookEntity getBookFromDatabase(long bid)
    {
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        BookEntity book = (BookEntity)session.get(BookEntity.class, bid);
        if(book == null){
            System.out.println("Book Not exists");
            return null;
        }
        session.close();
        return book;
    }

    public JsonObject getBookDetailInJson(long bid){
        BookEntity bookEntity = getBookFromDatabase(bid);
        if(bookEntity != null){
            return jsonBookGenerator.buildBookJson(bookEntity);
        }else{
            return jsonBookGenerator.getErrorMessage("Book not exist!");
        }
    }

    public JsonObject removeBookFromDatabase(int bid)
    {
        try{
            InitialContext ctx = new InitialContext();
            Queue queue = (Queue) ctx.lookup("MDBQueue");
            QueueConnectionFactory factory =
                    (QueueConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
            QueueConnection connection =  factory.createQueueConnection();
            QueueSession queueSession = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            MapMessage msg = queueSession.createMapMessage();
            msg.setLong("bid", bid);
            QueueSender sender = queueSession.createSender(null);

            sender.send(queue, msg);
            queueSession.close();

        }catch (Exception e){
            if(e.getMessage().equals("Book Not Exist")){
                return jsonBookGenerator.getErrorMessage("Book Not Exist");
            }else{
                e.printStackTrace();
                return jsonBookGenerator.getErrorMessage("error");
            }
        }
        return jsonBookGenerator.getOkMessage();
    }

    public JsonArray queryAllBook(){
        //Setting up session
        Session session = HibernateUtil.getSessionFactory().openSession();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        //Convert from database format to JSON
        String hql = "from BookEntity";
        List resList = session.createQuery(hql).list();
        for (Object aResList : resList) {
            BookEntity curBook = (BookEntity) aResList;
            arrayBuilder.add(jsonBookGenerator.buildBookJson(curBook));
        }
        //Send it back
        session.close();
        return arrayBuilder.build();
    }
}
