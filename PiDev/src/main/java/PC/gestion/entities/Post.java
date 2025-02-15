package PC.gestion.entities;
import PC.gestion.services.IServicePost;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Post{
    private int idp;
    private String description;
    private String image;
    private String type;
    private int idUser;


    public Post() {
    }
    public Post(String description,String image,String type,int idUser) {
        this.description = description;
        this.image = image;
        this.type = type;
        this.idUser = idUser;
    }
    public Post(String description,String image,String type) {
        this.description = description;
        this.image = image;
        this.type = type;
    }
    public Post(int id, String description, String image, String type) {
        this.idp = id;
        this.description = description;
        this.image = image;
        this.type = type;
    }

    public Post(int id, String description, String image, String type,int idUser) {
        this.idp = id;
        this.description = description;
        this.image = image;
        this.type = type;
        this.idUser = idUser;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser){
        this.idUser = idUser;
    }

    public String toString() {
        return "Post{" + "idp=" + idp + ", description=" + description + ", image=" + image + ", type=" + type + ", idUser=" + idUser + '}';
    }

}

