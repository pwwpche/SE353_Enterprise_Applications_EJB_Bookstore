package Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by pwwpche on 2014/5/4.
 */
@Entity
@Table(name = "book", schema = "", catalog = "reg")
public class BookEntity implements Serializable {
    private long bid;
    private String bookname;
    private String catagory;
    private double price;
    private String author;


    @Id
    @Column(name = "bid", nullable = false, insertable = true, updatable = true)
    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "bookname", nullable = true, insertable = true, updatable = true, length = 50)
    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    @Basic
    @Column(name = "catagory", nullable = true, insertable = true, updatable = true, length = 20)
    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    @Basic
    @Column(name = "price", nullable = true, insertable = true, updatable = true, precision = 2)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookEntity that = (BookEntity) o;

        if (bid != that.bid) return false;
        if (bookname != null ? !bookname.equals(that.bookname) : that.bookname != null) return false;
        if (catagory != null ? !catagory.equals(that.catagory) : that.catagory != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (bid ^ (bid >>> 32));
        result = 31 * result + (bookname != null ? bookname.hashCode() : 0);
        result = 31 * result + (catagory != null ? catagory.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "author", nullable = true, insertable = true, updatable = true, length = 20)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
