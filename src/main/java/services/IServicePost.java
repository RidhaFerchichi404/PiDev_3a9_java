package services;

import entities.Post;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IServicePost<T> {
    void ajouter(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    ArrayList<T> afficherAllPosts() throws SQLException;
    Post getPostById(int postId) throws SQLException;
    String getUserNamePost(int idUser) throws SQLException;
    ArrayList<Post> sortPostsByMostComments() throws SQLException;
    List<Post> afficherPostsByType(String selectedType) throws SQLException;
}
