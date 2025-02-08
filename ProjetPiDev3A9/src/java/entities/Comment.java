import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Comment implements IServiceComment<Comment> {
    private int id;
    private String comment;
    private String author;
    private String date;
    private int likes;

    public Comment(int id, String comment, String author, String date, int likes) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.date = date;
        this.likes = likes;
    }
    public Comment(String comment, String author, String date, int likes) {
        this.comment = comment;
        this.author = author;
        this.date = date;
        this.likes = likes;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDate() {
        return date;
    }
}
