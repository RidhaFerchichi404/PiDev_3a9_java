package PC.gestion.services;

import PC.gestion.entities.Post;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IServicePost<T> {
    void ajouter(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    ArrayList<T> afficherAllPosts() throws SQLException;
    Post getPostById(int postId) throws SQLException;
}
