package PC.gestion.gui;

import PC.gestion.entities.Comment;
import PC.gestion.entities.Post;
import PC.gestion.services.ServiceComment;
import PC.gestion.services.ServicePost;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.sql.Date;
import java.sql.SQLException;

public class ajouterCommentsAdmin {

    @FXML
    private Button BTNmodifier;

    @FXML
    private Button BTNsupprimer;


    @FXML
    private FlowPane FPcomments;

    @FXML
    private FlowPane FPposts;

    @FXML
    private TextField TFcomment;

    @FXML
    private TextField TFnbLikes;

    private int postId;
    private Comment selectedComment;
    private  int commentId;

    public void setPostId(int postId) {
        this.postId = postId;
        loadComments();
    }

    public void setCommentId( int commentId) {
        this.commentId = commentId;
    }

    public void setSelectedComment(String comment) {
        this.selectedComment = findCommentByText(comment);
        if (this.selectedComment != null) {
            TFcomment.setText(this.selectedComment.getComment());
            TFnbLikes.setText(String.valueOf(this.selectedComment.getLikes()));
        }
    }



    @FXML
    public void initialize() {
        loadComments();
        loadPosts();
        FPcomments.setOnMouseClicked(event -> handleCommentSelection());
        FPposts.setOnMouseClicked(event -> handlePostSelection());
    }

    private void loadComments() {
        if (postId == 0) {
            return;
        }
        ServiceComment serviceComment = new ServiceComment();
        try {
            ObservableList<Comment> comments = FXCollections.observableArrayList(serviceComment.getCommentsByPostId(postId));
            System.out.println("Comments loaded: " + comments.size());
            FPcomments.getChildren().clear();
            for (Comment comment : comments) {
                HBox commentBox = createCommentBox(comment);
                FPcomments.getChildren().add(commentBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createCommentBox(Comment comment) {
        Label commentLabel = new Label(comment.getComment());
        HBox hBox = new HBox(commentLabel);
        hBox.setSpacing(10);
        return hBox;
    }

    private HBox createPostBox(Post post) {
        Label postLabel = new Label(post.getDescription());
        ImageView postImageView = new ImageView(new Image("file:" + post.getImage()));
        postImageView.setFitHeight(50);
        postImageView.setFitWidth(50);
        HBox hBox = new HBox(postImageView, postLabel);
        hBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        hBox.setSpacing(10);
        return hBox;
    }

    private void loadPosts() {
        ServicePost servicePost = new ServicePost();
        try {
            ObservableList<Post> posts = FXCollections.observableArrayList(servicePost.afficherAllPosts());
            System.out.println("Posts loaded: " + posts.size());
            FPposts.getChildren().clear();
            for (Post post : posts) {
                HBox postBox = createPostBox(post);
                FPposts.getChildren().add(postBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePostSelection() {
        for (javafx.scene.Node node : FPposts.getChildren()) {
            node.getStyleClass().remove("selected-post");
            node.setOnMouseClicked(event -> {
                HBox selectedBox = (HBox) node;
                for (javafx.scene.Node child : selectedBox.getChildren()) {
                    if (child instanceof Label) {
                        Label selectedLabel = (Label) child;
                        Post selectedPost = findPostByDescription(selectedLabel.getText());
                        if (selectedPost != null) {
                            setPostId(selectedPost.getIdp());
                            selectedBox.getStyleClass().add("highlight");
                        }
                        break;
                    }
                }
            });
        }
    }

    private Post findPostByDescription(String description) {
        ServicePost servicePost = new ServicePost();
        try {
            for (Post post : servicePost.afficherAllPosts()) {
                if (post.getDescription().equals(description)) {
                    return post;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void handleCommentSelection() {
        for (javafx.scene.Node node : FPcomments.getChildren()) {
            node.setOnMouseClicked(event -> {
                HBox selectedBox = (HBox) node;
                for (javafx.scene.Node child : selectedBox.getChildren()) {
                    if (child instanceof Label) {
                        Label selectedLabel = (Label) child;
                        Comment selectedComment = findCommentByText(selectedLabel.getText());
                        if (selectedComment != null) {
                            commentId = selectedComment.getId();
                            TFcomment.setText(selectedComment.getComment());
                            TFnbLikes.setText(String.valueOf(selectedComment.getLikes()));
                            selectedBox.getStyleClass().add("highlight");
                        }
                        break;
                    }
                }
            });
        }
    }

    @FXML
    private void handleModifyComment() {
        if (selectedComment != null) {
            selectedComment.setComment(TFcomment.getText());
            selectedComment.setLikes(Integer.parseInt(TFnbLikes.getText()));
            selectedComment.setIdPost(postId);
            selectedComment.setId(commentId);

            ServiceComment serviceComment = new ServiceComment();
            try {
                serviceComment.update(selectedComment);
                loadComments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Comment not found");
        }
    }

    private Comment findCommentByText(String text) {
        ServiceComment serviceComment = new ServiceComment();
        try {
            for (Comment comment : serviceComment.getCommentsByPostId(postId)) {
                if (comment.getComment().equals(text)) {
                    return comment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void handleDeleteComment() {
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
