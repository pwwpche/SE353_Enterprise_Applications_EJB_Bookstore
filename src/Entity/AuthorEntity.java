package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by pwwpche on 2014/5/4.
 */
@Entity
@Table(name = "author", schema = "", catalog = "reg")
public class AuthorEntity implements Serializable {
    private String authorname;
    private String nation;

    @Id
    @Column(name = "authorname", nullable = false, insertable = true, updatable = true, length = 20)
    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    @Basic
    @Column(name = "nation", nullable = true, insertable = true, updatable = true, length = 20)
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorEntity that = (AuthorEntity) o;

        if (authorname != null ? !authorname.equals(that.authorname) : that.authorname != null) return false;
        if (nation != null ? !nation.equals(that.nation) : that.nation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = authorname != null ? authorname.hashCode() : 0;
        result = 31 * result + (nation != null ? nation.hashCode() : 0);
        return result;
    }
}
