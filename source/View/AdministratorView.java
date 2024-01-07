package source.View;

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

public class AdministratorView {

    public static void showAdministratorView(Stage primaryStage) {
        primaryStage.setTitle("Administrator MENU");

        Scene scene = new Scene(new VBox(), 800, 700);
        scene.setFill(Color.OLDLACE);

        MenuBar menuBar = new MenuBar();
        Menu menuRq = new Menu("Show Requestes");
        Menu menuBook = new Menu("Show Books");
        Menu menuPrfrmnc = new Menu("Performances or Filter");
        Menu menuManage = new Menu("Manage the employes");

        MenuItem registeringUsers = new MenuItem("Registering Users");
        MenuItem modifyUsers = new MenuItem("Modify Users");
        MenuItem deleteThem = new MenuItem("Delete Them");

        MenuItem showRequestsItem = new MenuItem("Show Requests");
        showRequestsItem.setOnAction(e -> {
            Label label = new Label("Requests are being shown.");
            ((VBox) scene.getRoot()).getChildren().add(label);
        });

        menuRq.getItems().add(showRequestsItem);
        menuManage.getItems().addAll(registeringUsers, modifyUsers, deleteThem);

        MenuItem showBooks = new MenuItem("Show books");
        showBooks.setOnAction(e -> {

        });

        registeringUsers.setOnAction(e -> Methods.registering(primaryStage, scene));
        modifyUsers.setOnAction(e -> Methods.modify(primaryStage, scene));
        deleteThem.setOnAction(e -> Methods.delete(primaryStage, scene));
        menuBook.getItems().add(showBooks);

        MenuItem showPerformances = new MenuItem("Show Performances or Filters");
        showPerformances.setOnAction(e -> Methods.Performance(primaryStage, scene));

        menuPrfrmnc.getItems().add(showPerformances);

        menuBar.getMenus().addAll(menuRq, menuBook, menuPrfrmnc, menuManage);

        Label lb = new Label("Welcome Administrator!!!");
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
