package PC.gestion.gui;

import PC.gestion.entities.Comment;
import PC.gestion.services.ServiceComment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.SQLException;

public class ajouterComments2 {

    @FXML
    private Button BTNmodifier;

    @FXML
    private Button BTNsupprimer;

    @FXML
    private DatePicker DatePicker;

    @FXML
    private ListView<Comment> LVcomments;

    @FXML
    private TextField TFcomment;

    @FXML
    private TextField TFnbLikes;

    private int postId;

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @FXML
    public void initialize() {
        LVcomments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleCommentSelection());
        loadComments();
    }

    private void loadComments() {
        ServiceComment serviceComment = new ServiceComment();
        try {
            ObservableList<Comment> comments = FXCollections.observableArrayList(serviceComment.getCommentsByPostId(postId));
            System.out.println("Comments loaded: " + comments.size());
            LVcomments.setItems(comments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCommentSelection() {
        Comment selectedComment = LVcomments.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            TFcomment.setText(selectedComment.getComment());
            TFnbLikes.setText(String.valueOf(selectedComment.getLikes()));
            DatePicker.setValue(selectedComment.getDate().toLocalDate());
        }
    }

    @FXML
    private void handleModifyComment() {
        Comment selectedComment = LVcomments.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            selectedComment.setComment(TFcomment.getText());
            selectedComment.setLikes(Integer.parseInt(TFnbLikes.getText()));
            selectedComment.setDate(Date.valueOf(DatePicker.getValue()));

            ServiceComment serviceComment = new ServiceComment();
            try {
                serviceComment.update(selectedComment);
                loadComments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteComment() {
        Comment selectedComment = LVcomments.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            ServiceComment serviceComment = new ServiceComment();
            try {
                serviceComment.delete(selectedComment);
                loadComments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
