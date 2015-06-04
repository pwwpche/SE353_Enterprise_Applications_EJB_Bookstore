package Entity;

import javax.persistence.*;

/**
 * Created by pwwpche on 2014/5/4.
 */
@Entity
@Table(name = "sale", schema = "", catalog = "reg")
public class SaleEntity {
    private long sid;
    private Long uid;
    private Integer sYear;
    private Integer sMonth;
    private Integer sDay;
    private double totprice;

    @Id
    @Column(name = "sid", nullable = false, insertable = true, updatable = true)
    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    @Basic
    @Column(name = "uid", nullable = true, insertable = true, updatable = true)
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "s_year", nullable = true, insertable = true, updatable = true)
    public Integer getsYear() {
        return sYear;
    }

    public void setsYear(Integer sYear) {
        this.sYear = sYear;
    }

    @Basic
    @Column(name = "s_month", nullable = true, insertable = true, updatable = true)
    public Integer getsMonth() {
        return sMonth;
    }

    public void setsMonth(Integer sMonth) {
        this.sMonth = sMonth;
    }

    @Basic
    @Column(name = "s_day", nullable = true, insertable = true, updatable = true)
    public Integer getsDay() {
        return sDay;
    }

    public void setsDay(Integer sDay) {
        this.sDay = sDay;
    }

    @Basic
    @Column(name = "totprice", nullable = true, insertable = true, updatable = true, precision = 2)
    public double getTotprice() {
        return totprice;
    }

    public void setTotprice(double totprice) {
        this.totprice = totprice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SaleEntity that = (SaleEntity) o;

        if (sid != that.sid) return false;
        if (sDay != null ? !sDay.equals(that.sDay) : that.sDay != null) return false;
        if (sMonth != null ? !sMonth.equals(that.sMonth) : that.sMonth != null) return false;
        if (sYear != null ? !sYear.equals(that.sYear) : that.sYear != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (sid ^ (sid >>> 32));
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (sYear != null ? sYear.hashCode() : 0);
        result = 31 * result + (sMonth != null ? sMonth.hashCode() : 0);
        result = 31 * result + (sDay != null ? sDay.hashCode() : 0);

        return result;
    }
}
