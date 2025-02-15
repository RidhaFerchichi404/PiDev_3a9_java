package PC.gestion.services;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IServiceComment<T> {
    void ajouter(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    ArrayList<T> afficherAllComments() throws SQLException;
    ArrayList<T> getCommentsByPostId(int postId) throws SQLException;
}

