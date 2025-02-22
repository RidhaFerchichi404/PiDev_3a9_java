package PC.gestion.gui;

import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GestionPCUser {

    @FXML
    private FlowPane FPposts;

    @FXML
    private ComboBox<String> ComboBox;

    @FXML
    public void initialize() {
         ServicePost servicePost = new ServicePost();
         try {
             List<Post> posts = servicePost.afficherAllPosts();
             displayPosts(posts);
         } catch (SQLException e) {
             e.printStackTrace();
         }

         ComboBox.setOnAction(event -> {
             String selectedType = ComboBox.getValue();
             if (selectedType != null && !selectedType.isEmpty()) {
                 List<Post> filteredPosts = servicePost.afficherPostsByType(selectedType);
                 displayPosts(filteredPosts);
             }else if (selectedType.isEmpty()) {
            try {
                List<Post> allPosts = servicePost.afficherAllPosts();
                displayPosts(allPosts);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
         });
    }

    public void handleAscendentSort(ActionEvent actionEvent) {
                ServicePost servicePost = new ServicePost();
                try {
                    List<Post> posts = servicePost.afficherAllPosts();
                    posts.sort((p1, p2) -> p1.getType().compareToIgnoreCase(p2.getType()));
                    FPposts.getChildren().clear();
                    for (Post post : posts) {
                        VBox vBox = new VBox(10);
                        vBox.setAlignment(Pos.CENTER);
                        vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                        ImageView imageView = new ImageView(new Image("file:" + post.getImage()));
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);

                        Label labelType = new Label(post.getType());

                        vBox.getChildren().addAll(imageView, labelType);
                        FPposts.getChildren().add(vBox);
                        vBox.setOnMouseClicked(event -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
                                Parent root = loader.load();
                                afficherPostsUser controller = loader.getController();
                                controller.setPost(post);
                                Stage stage = (Stage) FPposts.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
    }

    public void handleDescendentSort(ActionEvent actionEvent) {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.afficherAllPosts();
            posts.sort((p1, p2) -> p2.getType().compareToIgnoreCase(p1.getType()));
            FPposts.getChildren().clear();
            for (Post post : posts) {
                VBox vBox = new VBox(10);
                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                ImageView imageView = new ImageView(new Image("file:" + post.getImage()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                Label labelType = new Label(post.getType());

                vBox.getChildren().addAll(imageView, labelType);
                FPposts.getChildren().add(vBox);
                vBox.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
                        Parent root = loader.load();
                        afficherPostsUser controller = loader.getController();
                        controller.setPost(post);
                        Stage stage = (Stage) FPposts.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleMostCommented(ActionEvent actionEvent) {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.sortPostsByMostComments();
            FPposts.getChildren().clear();
            for (Post post : posts) {
                VBox vBox = new VBox(10);
                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                ImageView imageView = new ImageView(new Image("file:" + post.getImage()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                Label labelType = new Label(post.getType());

                vBox.getChildren().addAll(imageView, labelType);
                FPposts.getChildren().add(vBox);
                vBox.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
                        Parent root = loader.load();
                        afficherPostsUser controller = loader.getController();
                        controller.setPost(post);
                        Stage stage = (Stage) FPposts.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void displayPosts(List<Post> posts) {
        FPposts.getChildren().clear();
        for (Post post : posts) {
            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

            ImageView imageView = new ImageView(new Image("file:" + post.getImage()));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            Label labelType = new Label(post.getType());

            vBox.getChildren().addAll(imageView, labelType);
            FPposts.getChildren().add(vBox);
            vBox.setOnMouseClicked(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
                    Parent root = loader.load();
                    afficherPostsUser controller = loader.getController();
                    controller.setPost(post);
                    Stage stage = (Stage) FPposts.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}


