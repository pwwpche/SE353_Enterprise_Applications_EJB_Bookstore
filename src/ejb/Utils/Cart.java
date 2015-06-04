package ejb.Utils;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by pwwpche on 2015/3/27.
 */

@Remote
public interface Cart {
    public void addBook(int bid, String bookName, double price);
    public void removeBook(int bid);
    public String toJSONStr();
    public double getTotalPrice();
    public void clear();
}
