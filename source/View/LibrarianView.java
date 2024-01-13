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

        Scene scene = new Scene(new VBox(), 800, 700);
        scene.setFill(Color.OLDLACE);
        BorderPane borderPane= new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menuRq = new Menu("Show Requestes");
        Menu menuBook = new Menu("Show Books");
        Menu menuPermission = new Menu("Request of Permission");
        MenuItem permission = new MenuItem("Make the request");
        MenuItem showRequestsItem = new MenuItem("Show Requests");
        showRequestsItem.setOnAction(e -> {
            try {
                Methods.getOrders(user);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        menuRq.getItems().add(showRequestsItem);
        menuPermission.getItems().add(permission);

        MenuItem showBooks = new MenuItem("Show books");
        showBooks.setOnAction(e -> {
            try {
                Methods.getBooks();
            } catch (ParseException e1) {

                e1.printStackTrace();
            }
        });

        menuBook.getItems().add(showBooks);

        menuBar.getMenus().addAll(menuRq, menuBook, menuPermission);

        Label lb = new Label("Welcome " + user.getUsername());
        lb.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: darkblue; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-color: #F0F8FF; " +
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




        primaryStage.setScene(scene);
        primaryStage.show();
    }

}