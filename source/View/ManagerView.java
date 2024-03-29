package source.View;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Model.User;

public class ManagerView {

    public static void showManagerView(Stage primaryStage, User user) {
        primaryStage.setTitle(user.getUsername() + " MENU");
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox, 700, 500);
        scene.setFill(Color.OLDLACE);
        BorderPane borderPane = new BorderPane();
        try {
            InputStream inputStream = ManagerView.class.getResourceAsStream("/images/library.png");
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
        Menu menuBook = new Menu("Show Books");
        Menu menuPrfrmnc = new Menu("Performances");
        Menu menuAddBook = new Menu("Add Book");
        Menu menuPermission = new Menu("Request a permission");
        MenuItem permission = new MenuItem("Request a permission");
        MenuItem haveAccess = new MenuItem("Check access");

        menuPermission.setOnMenuValidation(event -> {
            Methods.disableMenuItem(permission, user);
            System.out.println("User clicked Request of Permission");
        });

        permission.setOnAction(e -> {

            Methods.askPermissionView(primaryStage, scene, user);
        });

        haveAccess.setOnAction(e -> {
            Methods.permission(user, primaryStage, scene);
        });
        menuPermission.getItems().addAll(permission, haveAccess);

        MenuItem addBook = new MenuItem("Add Book");
        MenuItem showBooks = new MenuItem("Show books");
        showBooks.setOnAction(e -> {
            try {
                Methods.getBooks();
            } catch (ParseException e1) {

                e1.printStackTrace();
            }
        });

        menuBook.getItems().add(showBooks);
        menuAddBook.getItems().add(addBook);

        MenuItem showPerformances = new MenuItem("Show Performances or Filters");
        showPerformances.setOnAction(e -> Methods.Performance(primaryStage, scene));

        addBook.setOnAction(e -> Methods.addBook(primaryStage, scene));
        menuPrfrmnc.getItems().add(showPerformances);

        menuBar.getMenus().addAll(menuBook, menuPrfrmnc, menuAddBook, menuPermission);

        Label lb = new Label("Welcome " + user.getUsername() + "!!!");
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
        Button logoutButton = new Button("Logout");
        HBox menuBox = new HBox(menuBar);
        HBox logoutBox = new HBox(logoutButton);
        HBox hbox = new HBox(menuBox, logoutBox);

        HBox.setHgrow(menuBox, Priority.ALWAYS);

        borderPane.setTop(hbox);
        borderPane.setLeft(lb);
        scene.setRoot(borderPane);
        Methods.showALertBook();
        logoutButton.setOnAction(e -> {

            LoginScene.showLoginScene(primaryStage);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
