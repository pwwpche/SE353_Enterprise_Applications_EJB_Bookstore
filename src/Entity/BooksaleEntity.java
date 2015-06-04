package Entity;

import javax.persistence.*;

/**
 * Created by pwwpche on 2014/5/6.
 */
@Entity
@Table(name = "booksale", schema = "", catalog = "reg")
public class BooksaleEntity {
    private long sid;
    private Long bid;
    private Integer quantity;

    @Id
    @Column(name = "sid", nullable = false, insertable = true, updatable = true)
    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    @Basic
    @Column(name = "bid", nullable = true, insertable = true, updatable = true)
    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "quantity", nullable = true, insertable = true, updatable = true)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooksaleEntity that = (BooksaleEntity) o;

        if (sid != that.sid) return false;
        if (bid != null ? !bid.equals(that.bid) : that.bid != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (sid ^ (sid >>> 32));
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
