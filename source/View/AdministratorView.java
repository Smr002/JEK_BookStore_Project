package source.View;

import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Model.User;

public class AdministratorView {

    public static void showAdministratorView(Stage primaryStage, User user) {
        primaryStage.setTitle(user.getUsername() + " MENU");

        Scene scene = new Scene(new VBox(), 700, 500);
        scene.setFill(Color.OLDLACE);
        BorderPane borderPane= new BorderPane();
        try {
            InputStream inputStream = AdministratorView.class.getResourceAsStream("/images/library.png");
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
        Menu menuRq = new Menu("Requests");
        Menu menuBook = new Menu("Show Books");
        Menu menuPrfrmnc = new Menu("Performances");
        Menu menuManage = new Menu("Employees");
        Menu menuAddBook = new Menu("Add Book");
        Menu menuFinance = new Menu("Finance");
        Menu menuPermission = new Menu("Permission");
        Menu statisticsMenu=new Menu("Statistics");

        MenuItem registeringUsers = new MenuItem("Registering Users");
        MenuItem modifyUsers = new MenuItem("Modify Users");
        MenuItem deleteThem = new MenuItem("Delete Them");
        MenuItem addBook = new MenuItem("Add Book");
        MenuItem finance = new MenuItem("Show Finance");
        MenuItem permission = new MenuItem("Check permission requests");
        MenuItem showRequestsItem = new MenuItem("Show Requests");
        MenuItem statistics= new MenuItem("Sold Books Statistics");
        showRequestsItem.setOnAction(e -> {
            try {
                Methods.getOrders(primaryStage,user,scene);
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
        statisticsMenu.getItems().add(statistics);
        statistics.setOnAction(e-> Methods.bookStatistics(primaryStage,scene));

        menuBar.getMenus().addAll(menuRq, menuBook, menuPrfrmnc, menuManage, menuAddBook, menuFinance, menuPermission,statisticsMenu);

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
        logoutButton.setOnAction(e -> LoginScene.showLoginScene(primaryStage));
        scene.setRoot(borderPane);
        Methods.showALertBook();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
