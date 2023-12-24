package source.View;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
        showPerformances.setOnAction(e -> ManagerView.Performance(primaryStage,scene));

        menuPrfrmnc.getItems().add(showPerformances); // Add to the menuPrfrmnc menu

        menuBar.getMenus().addAll(menuRq, menuBook, menuPrfrmnc, menuFilter);

        Label lb = new Label("Welcome Manager!!!");

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, lb);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void Performance(Stage primaryStage,Scene scene) {
        GridPane gridPane = new GridPane();
        Scene scene1 = new Scene(gridPane, 800, 700);
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label("Choose librarian and timeframe:");
        gridPane.add(label, 0, 0);
        ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList("First", "Second", "Third"));
        gridPane.add(cb, 0, 1);
        ChoiceBox<String> cb1 = new ChoiceBox<>(FXCollections.observableArrayList("Daily", "Monthly", "Yearly"));
        gridPane.add(cb1, 1, 1);
        Button ok = new Button("OK");
        gridPane.add(ok, 1, 2);
        ok.setOnAction(e -> ManagerView.buttonOk(primaryStage,scene1));
        Button back = new Button("Back");
        gridPane.add(back,1,3);
        back.setOnAction(e->primaryStage.setScene(scene));
        primaryStage.setScene(scene1);
    }

    public static void buttonOk(Stage primaryStage,Scene scene) {

        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label startDateLabel = new Label("Start Date:");
        grid.add(startDateLabel, 0, 0);
        TextField startDate = new TextField();
        grid.add(startDate, 1, 0);

        Label endDateLabel = new Label("End Date:");
        grid.add(endDateLabel, 0, 1);
        TextField endDate = new TextField();
        grid.add(endDate, 1, 1);
        Button check = new Button("CHECK");
        grid.add(check,2,2);
        Button back = new Button("Back");
        grid.add(back,2,3);
        back.setOnAction(e->primaryStage.setScene(scene));

        Scene scene2 = new Scene(grid, 800, 700);

        primaryStage.setScene(scene2);

    }
}
