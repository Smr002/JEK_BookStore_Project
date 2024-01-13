package source.View;

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
        primaryStage.setTitle(user.getRole() + " MENU");
VBox vbox= new VBox();
        Scene scene = new Scene(vbox, 800, 700);
        scene.setFill(Color.OLDLACE);
BorderPane borderPane= new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menuRq = new Menu("Show Requestes");
        Menu menuBook = new Menu("Show Books");
        Menu menuPrfrmnc = new Menu("Performances or Filter");
        Menu menuAddBook = new Menu("Add Book");

        MenuItem showRequestsItem = new MenuItem("Show Requests");
        showRequestsItem.setOnAction(e -> {
            try {
                Methods.getOrders(user);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        menuRq.getItems().add(showRequestsItem);
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

        menuBar.getMenus().addAll(menuRq, menuBook, menuPrfrmnc, menuAddBook);

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
        Button logoutButton= new Button("Logout");
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
