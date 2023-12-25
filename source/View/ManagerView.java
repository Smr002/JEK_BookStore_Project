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

public class ManagerView {

    public static void showManagerView(Stage primaryStage) {
        primaryStage.setTitle("Manager MENU");

        Scene scene = new Scene(new VBox(), 800, 700);
        scene.setFill(Color.OLDLACE);

        MenuBar menuBar = new MenuBar();
        Menu menuRq = new Menu("Show Requestes");
        Menu menuBook = new Menu("Show Books");
        Menu menuPrfrmnc = new Menu("Performances");
        Menu menuFilter = new Menu("Filter");

        MenuItem showRequestsItem = new MenuItem("Show Requests");
        showRequestsItem.setOnAction(e -> {
            Label label = new Label("Requests are being shown.");
            ((VBox) scene.getRoot()).getChildren().add(label);
        });

        menuRq.getItems().add(showRequestsItem);

        MenuItem showBooks = new MenuItem("Show books");
        showBooks.setOnAction(e -> {
            // Handle "Show Books" action
        });

        menuBook.getItems().add(showBooks);

        MenuItem showPerformances = new MenuItem("Show Performances");
        showPerformances.setOnAction(e -> Methods.Performance(primaryStage, scene));

        menuPrfrmnc.getItems().add(showPerformances);

        menuBar.getMenus().addAll(menuRq, menuBook, menuPrfrmnc, menuFilter);

        Label lb = new Label("Welcome Manager!!!");

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, lb);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

	
 
}
