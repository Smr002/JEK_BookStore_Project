package source.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ManagerView {
    public static void showManagerView(Stage primaryStage) {
        primaryStage.setTitle("Manager MENU");

        BorderPane layout = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");

        layout.setTop(menuBar);

        menuBar.getMenus().addAll(file);

        Button okButton = new Button("OK");
        layout.setCenter(okButton);

        primaryStage.setScene(new Scene(layout, 800, 700));
        primaryStage.show();
    }
}