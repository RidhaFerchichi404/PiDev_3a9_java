package PC.gestion.entities;

import PC.gestion.services.IServiceComment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

public class Comment {
    private int id;
    private String comment;
    private Date date;
    private int likes;
    private int idPost;
    private int idUser;

    public Comment() {
    }
    public Comment(String comment, Date date, int likes, int idPost, int idUser) {
        this.comment = comment;
        this.date = date;
        this.likes = likes;
        this.idPost = idPost;
        this.idUser = idUser;
    }
    public Comment(int id, String comment, Date date, int likes, int idPost, int idUser) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.likes = likes;
        this.idPost = idPost;
        this.idUser = idUser;
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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public int getIdPost() {
        return idPost;
    }
    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }
    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
       return "Comment [id=" + id + ", comment=" + comment + ", date=" + date + ", likes=" + likes + ", idPost=" + idPost + ", idUser=" + idUser + "]";
    }
}
