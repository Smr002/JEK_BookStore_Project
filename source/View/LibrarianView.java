package source.View;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Model.Book;
import source.Model.User;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LibrarianView {

    public static void showLibrarianView(Stage primaryStage, User user) {
        primaryStage.setTitle("Librarian MENU");

        Scene scene = new Scene(new VBox(), 700, 500);
        scene.setFill(Color.LIGHTGRAY);

        BorderPane borderPane= new BorderPane();
        try {
            InputStream inputStream = LibrarianView.class.getResourceAsStream("/images/library.png");
            if (inputStream != null) {
                Image backgroundImage = new Image(inputStream);
                ImageView backgroundImageView = new ImageView(backgroundImage);
               backgroundImageView.setFitHeight(700);
               backgroundImageView.setFitWidth(800);
                backgroundImageView.setPreserveRatio(false);
                borderPane.getChildren().add(backgroundImageView);
            } else {
                System.out.println("Image not found: /images/library.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MenuBar menuBar = new MenuBar();
        Menu menuRq = new Menu("Show Requests");
        Menu menuBook = new Menu("Show Books");
        Menu menuPermission = new Menu("Request a permission");
        MenuItem permission = new MenuItem("Request a permission");
        MenuItem showRequestsItem = new MenuItem("Show Requests");
        MenuItem haveAccess = new MenuItem("Check access");
        showRequestsItem.setOnAction(e -> {
            try {
                Methods.getOrders(primaryStage,user,scene);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        menuPermission.setOnMenuValidation(event -> {
            Methods.disableMenuItem(permission, user);
            System.out.println("User clicked Request of Permission");
        });

        permission.setOnAction(e -> {

            Methods.askPermissionView(primaryStage, scene, user);
        });

        menuRq.getItems().add(showRequestsItem);
        menuPermission.getItems().addAll(permission, haveAccess);

        MenuItem showBooks = new MenuItem("Show books");
        showBooks.setOnAction(e -> {
            try {
                Methods.getBooks();
            } catch (ParseException e1) {

                e1.printStackTrace();
            }
        });
        haveAccess.setOnAction(e -> {
            Methods.permission(user, primaryStage, scene);
        });
        menuBook.getItems().add(showBooks);

        menuBar.getMenus().addAll(menuRq, menuBook, menuPermission);

        Label lb = new Label("Welcome " + user.getUsername());
        lb.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #0066cc; " +  // Change text color to a shade of blue
                        "-fx-padding: 10px; " +
                        "-fx-background-color: #f0f0f0; " +  // Change background color to a light gray
                        "-fx-border-color: #4682B4; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-alignment: CENTER;");
        Button logoutButton=new Button("Logout");
        HBox menuBox= new HBox(menuBar);
        HBox logoutBox= new HBox(logoutButton);
        HBox hbox= new HBox(menuBox,logoutBox);
        HBox.setHgrow(menuBox, Priority.ALWAYS);
        borderPane.setTop(hbox);
        borderPane.setLeft(lb);
        logoutButton.setOnAction(e-> LoginScene.showLoginScene(primaryStage));
        scene.setRoot(borderPane);



        Methods.centerScene(primaryStage, scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}