package PC.gestion.services;

import PC.gestion.entities.Comment;
import PC.gestion.utils.MyConnection;

import java.util.ArrayList;
import java.sql.*;

public class ServiceComment implements IServiceComment<Comment> {

    private Connection connection;
    public ServiceComment() {
        this.connection = MyConnection.getInstance().getCnx();
    }
    @Override
    public void ajouter(Comment comment) throws SQLException {
        String sql = "INSERT INTO comment (comment, date, likes, idPost, idUser) VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, comment.getComment());
        statement.setDate(2, (Date) comment.getDate());
        statement.setInt(3, comment.getLikes());
        statement.setInt(4, comment.getIdPost());
        statement.setInt(5, 1);
        statement.executeUpdate();
    }

    @Override
    public void update(Comment comment) throws SQLException {
        String query = "UPDATE comment SET comment = ?, likes = ?, idPost = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment.getComment());
            preparedStatement.setInt(2, comment.getLikes());
            preparedStatement.setInt(3, comment.getIdPost());
            preparedStatement.setInt(4, comment.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Comment comment) throws SQLException {
        String sql = "DELETE FROM comment WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, comment.getId());
        pst.executeUpdate();
    }

    @Override
    public ArrayList<Comment> afficherAllComments() throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comment";
        Statement st = connection.createStatement();
        st.executeQuery(sql);
        ResultSet rs = st.getResultSet();
        while (rs.next()) {
            int id = rs.getInt("id");
            String comment = rs.getString("comment");
            Date date = rs.getDate("date");
            int likes = rs.getInt("likes");
            int idPost = rs.getInt("idPost");
            int idUser = rs.getInt("idUser");
            Comment c = new Comment(id, comment, date, likes, idPost, idUser);
            comments.add(c);
        }
        return comments;
    }

    @Override
    public ArrayList<Comment> getCommentsByPostId(int postId) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comment WHERE idPost = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, postId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String comment = rs.getString("comment");
            Date date = rs.getDate("date");
            int likes = rs.getInt("likes");
            int idPost = rs.getInt("idPost");
            int idUser = rs.getInt("idUser");
            Comment c = new Comment(id, comment, date, likes, idPost, idUser);
            comments.add(c);
        }
        return comments;
    }
}
