package ejb.Utils;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;

/**
 * Created by pwwpche on 2015/3/27.
 */

@Stateful(mappedName = "CartBean")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CartBean implements Cart {
    @EJB
    JsonBookGenerator jsonBookGenerator;

    public class Book {
        public int bid;
        public String bookname;
        public double price;
        public int quantity;

        public Book() {
            bid = 0;
            bookname = "";
            price = 0;
            quantity = 1;
        }

        public Book(int _bid, String _bookName, double _price) {
            bid = _bid;
            bookname = _bookName;
            price = _price;
            quantity = 1;
        }

        public int getBid() {
            return bid;
        }

        public void cartAdd() {
            quantity++;
        }

        public void cartRemove() {
            if (quantity > 0) {
                quantity--;
            }
        }

        public String getName() {
            return bookname;
        }


    }

    String username;
    String uid;
    ArrayList<Book> bookList = new ArrayList<Book>();

    @Override
    public void addBook(int bid, String bookName, double price) {
        boolean found = false;
        for (Book aBookList : bookList) {
            if (aBookList.getBid() == bid) {
                found = true;
                aBookList.cartAdd();
                break;
            }
        }

        if (!found) {
            bookList.add(new Book(bid, bookName, price));
        }
    }

    @Override
    public void removeBook(int bid) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBid() == bid) {
                bookList.get(i).cartRemove();
                if (bookList.get(i).quantity == 0) {
                    bookList.remove(i);
                }
                break;
            }
        }

    }

    @Override
    public String toJSONStr() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Book book : bookList) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("bid", book.bid);
            objectBuilder.add("bookname", book.bookname);
            objectBuilder.add("quantity", book.quantity);
            objectBuilder.add("price", book.price);
            arrayBuilder.add(objectBuilder.build());
        }
        return arrayBuilder.build().toString();
    }

    @Override
    public double getTotalPrice() {
        double total = 0;
        for(Book book : bookList){
            total += book.price * book.quantity;
        }
        return total;
    }

    @Override
    public void clear() {
        bookList = new ArrayList<Book>();
    }
}

