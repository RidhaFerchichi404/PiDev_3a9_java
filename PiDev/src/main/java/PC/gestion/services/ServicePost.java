package PC.gestion.services;

import PC.gestion.entities.Comment;
import PC.gestion.entities.Post;
import PC.gestion.utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePost implements IServicePost<Post> {
    private static Connection connection;
    public ServicePost() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Post post) throws SQLException {
        String sql = "INSERT INTO post (description, image, type) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, post.getDescription());
        statement.setString(2, post.getImage());
        statement.setString(3, post.getType());
        statement.executeUpdate();
    }

    @Override
    public void update(Post post) throws SQLException {
        String sql = "UPDATE post SET description = ?, image = ?, type = ? WHERE idp = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, post.getDescription());
        pst.setString(2, post.getImage());
        pst.setString(3, post.getType());
        pst.setInt(4, post.getIdp());
        pst.executeUpdate();
    }

    @Override
    public void delete(Post post) throws SQLException {
        String sql = "DELETE FROM post WHERE idp = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, post.getIdp());
        pst.executeUpdate();
    }

    @Override
    public ArrayList<Post> afficherAllPosts() throws SQLException {
        ArrayList<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post";
        Statement st = connection.createStatement();
        st.executeQuery(sql);
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            int id = rs.getInt("idp");
            String description = rs.getString("description");
            String image = rs.getString("image");
            String type = rs.getString("type");
            Post p = new Post(id, description, rs.getString("image"), rs.getString("type"));
            posts.add(p);
        }
        return posts;
    }

    public static ArrayList<Comment> getCommentsForPost(int postId) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM Comment WHERE idPost = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, postId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String commentText = resultSet.getString("comment");
            Date date = resultSet.getDate("date");
            int likes = resultSet.getInt("likes");
            int idPost = resultSet.getInt("idPost");
            Comment comment = new Comment(id, commentText, date, likes, idPost);
            comments.add(comment);
        }

        return comments;
    }
}
