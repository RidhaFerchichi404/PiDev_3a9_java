package PC.gestion.entities;
import PC.gestion.services.IServicePost;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Post{
    private int idp;
    private String description;
    private Date dateU;
    private List<Comment> commentList;

    public Post() {
    }
    public Post(String description, Date dateU, List<Comment> comments) {
        this.description = description;
        this.dateU = dateU;
        this.commentList = comments;
    }
    public Post(int id, String description, Date date, List<Comment> commentList) {
        this.idp = id;
        this.description = description;
        this.dateU = date;
        this.commentList = (commentList != null) ? commentList : new ArrayList<>();
    }
    public int getIdp() {
        return idp;
    }
    public void setIdp(int idp) {
        this.idp = idp;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDateU() {
        return dateU;
    }
    public void setDateU(Date dateU) {
        this.dateU = dateU;
    }
    public String toString() {
        return "Post{" + "idp=" + idp + ", description=" + description + ", dateU=" + dateU + ", comments=" + commentList + '}';
    }

}

