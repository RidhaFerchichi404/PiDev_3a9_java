/*package PC.gestion.main;

import PC.gestion.entities.Comment;
import PC.gestion.entities.Post;
import PC.gestion.services.ServiceComment;
import PC.gestion.services.ServicePost;
import PC.gestion.utils.MyConnection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        Scanner scanner = new Scanner(System.in);
        ServicePost servicePost = new ServicePost();
        ServiceComment serviceComment = new ServiceComment();
        boolean exit = false;

        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Add Post");
            System.out.println("2. Update Post");
            System.out.println("3. Delete Post");
            System.out.println("4. Show All Posts");
            System.out.println("5. Add Comment to Post");
            System.out.println("6. Delete Comment from Post");
            System.out.println("7. Modify Comment from Post");
            System.out.println("8. Show All Comments in Post");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    String dateStr = scanner.nextLine();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Post newPost = new Post(description,);
                    try {
                        servicePost.ajouter(newPost);
                        System.out.println("Post added successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.print("Enter post ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new description: ");
                    String newDescription = scanner.nextLine();
                    Post updatePost = new Post(updateId, newDescription, null, null);
                    try {
                        servicePost.update(updatePost);
                        System.out.println("Post updated successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.print("Enter post ID to delete: ");
                    int deleteId = scanner.nextInt();
                    Post deletePost = new Post(deleteId, null, null, null);
                    try {
                        servicePost.delete(deletePost);
                        System.out.println("Post deleted successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        List<Post> posts = servicePost.afficherAllPosts();
                        for (Post post : posts) {
                            System.out.println(post);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    System.out.print("Enter post ID to add comment: ");
                    int postId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter comment: ");
                    String commentText = scanner.nextLine();
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    String commentDateStr = scanner.nextLine();
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date commentDate = null;
                    try {
                        commentDate = dateFormat2.parse(commentDateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.print("Enter likes: ");
                    int likes = scanner.nextInt();
                    Comment newComment = new Comment(commentText, new java.sql.Date(commentDate.getTime()), likes, postId);
                    try {
                        serviceComment.ajouter(newComment);
                        System.out.println("Comment added successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.print("Enter comment ID to delete: ");
                    int commentIdToDelete = scanner.nextInt();
                    Comment deleteComment = new Comment();
                    deleteComment.setId(commentIdToDelete);
                    try {
                        serviceComment.delete(deleteComment);
                        System.out.println("Comment deleted successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    System.out.print("Enter comment ID to modify: ");
                    int commentIdToModify = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new comment: ");
                    String newCommentText = scanner.nextLine();
                    Comment updateComment = new Comment();
                    updateComment.setId(commentIdToModify);
                    updateComment.setComment(newCommentText);
                    try {
                        serviceComment.update(updateComment);
                        System.out.println("Comment updated successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    System.out.print("Enter post ID to show comments: ");
                    int postIdToShowComments = scanner.nextInt();
                    try {
                        ArrayList<Comment> comments = ServicePost.getCommentsForPost(postIdToShowComments);
                        for (Comment comment : comments) {
                            System.out.println(comment);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}*/