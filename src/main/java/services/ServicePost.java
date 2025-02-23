package services;

import entities.Comment;
import entities.Post;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePost implements IServicePost<Post> {
    private static Connection connection;
    public ServicePost() {
        this.connection = MyConnection.getInstance().getConnection();
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
            int idUser = rs.getInt("idUser");
            String description = rs.getString("description");
            String image = rs.getString("image");
            String type = rs.getString("type");
            Date date=rs.getDate("dateU");
            Post p = new Post(id, description, rs.getString("image"), rs.getString("type"),idUser,date);
            posts.add(p);
        }
        return posts;
    }

    @Override
    public Post getPostById(int postId) throws SQLException {
        String sql = "SELECT * FROM post WHERE idp = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, postId);
        ResultSet resultSet = statement.executeQuery();
        Post post = null;
        while (resultSet.next()) {
            int id = resultSet.getInt("idp");
            String description = resultSet.getString("description");
            String image = resultSet.getString("image");
            String type = resultSet.getString("type");
            post = new Post(id, description, image, type);
        }
        return post;
    }

    @Override
    public String getUserNamePost(int idUser) throws SQLException {
        System.out.println(idUser);
        String sql = "SELECT first_name FROM user WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, idUser);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("first_name");
                } else {
                    return null;
                }
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
            int idUser = resultSet.getInt("idUser");
            Comment comment = new Comment(id, commentText, date, likes, idPost ,idUser);
            comments.add(comment);
        }

        return comments;
    }

    @Override
    public ArrayList<Post> sortPostsByMostComments() throws SQLException {
        ArrayList<Post> posts = afficherAllPosts();
        posts.sort((p1, p2) -> {
            try {
                int commentsCount1 = getCommentsForPost(p1.getIdp()).size();
                int commentsCount2 = getCommentsForPost(p2.getIdp()).size();
                return Integer.compare(commentsCount2, commentsCount1);
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        });
        return posts;
    }

    @Override
    public List<Post> afficherPostsByType(String selectedType) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post WHERE type = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("idp");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                String type = resultSet.getString("type");
                int idUser = resultSet.getInt("idUser");
                Date date = resultSet.getDate("dateU");
                Post post = new Post(id, description, image, type, idUser, date);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
