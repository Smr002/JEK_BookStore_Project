package source.View;

import java.text.ParseException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Model.User;

public class AdministratorView {

    public static void showAdministratorView(Stage primaryStage, User user) {
        primaryStage.setTitle(user.getRole() + " MENU");

        Scene scene = new Scene(new VBox(), 800, 700);
        scene.setFill(Color.OLDLACE);

        MenuBar menuBar = new MenuBar();
        Menu menuRq = new Menu("Show Requestes");
        Menu menuBook = new Menu("Show Books");
        Menu menuPrfrmnc = new Menu("Performances or Filter");
        Menu menuManage = new Menu("Manage the employes");
        Menu menuAddBook = new Menu("Add Book");
        Menu menuFinance = new Menu("Finance");
        Menu menuPermission = new Menu("Permission");

        MenuItem registeringUsers = new MenuItem("Registering Users");
        MenuItem modifyUsers = new MenuItem("Modify Users");
        MenuItem deleteThem = new MenuItem("Delete Them");
        MenuItem addBook = new MenuItem("Add Book");
        MenuItem finance = new MenuItem("Show Finance");
        MenuItem permission = new MenuItem("Check the permission & give permission");

        MenuItem showRequestsItem = new MenuItem("Show Requests");
        showRequestsItem.setOnAction(e -> {
            try {
                Methods.getOrders(user);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        menuRq.getItems().add(showRequestsItem);
        menuManage.getItems().addAll(registeringUsers, modifyUsers, deleteThem);

        MenuItem showBooks = new MenuItem("Show books");
        showBooks.setOnAction(e -> {
            try {
                Methods.getBooks();
            } catch (ParseException e1) {

                e1.printStackTrace();
            }

        });

        registeringUsers.setOnAction(e -> Methods.registering(primaryStage, scene));
        modifyUsers.setOnAction(e -> Methods.modify(primaryStage, scene));
        deleteThem.setOnAction(e -> Methods.delete(primaryStage, scene));
        menuBook.getItems().add(showBooks);
        menuAddBook.getItems().add(addBook);
        menuFinance.getItems().add(finance);
        finance.setOnAction(e -> Methods.finance(primaryStage, scene));

        MenuItem showPerformances = new MenuItem("Show Performances or Filters");
        showPerformances.setOnAction(e -> Methods.Performance(primaryStage, scene));

        menuPrfrmnc.getItems().add(showPerformances);
        addBook.setOnAction(e -> Methods.addBook(primaryStage, scene));
        menuPermission.getItems().add(permission);
        permission.setOnAction(e -> Methods.approvePermission(primaryStage, scene, user));
        menuBar.getMenus().addAll(menuRq, menuBook, menuPrfrmnc, menuManage, menuAddBook, menuFinance, menuPermission);

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

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, lb);

        Methods.showALertBook();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
