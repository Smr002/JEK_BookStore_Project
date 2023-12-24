package source.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LibrarianView {
    public static void showLibrarianView(Stage primaryStage) {
        primaryStage.setTitle("Librarian MENU");

        BorderPane layout = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("BOOKS");

        layout.setTop(menuBar);

        menuBar.getMenus().addAll(file);

        Button okButton = new Button("OK");
        layout.setCenter(okButton);

        Button logoutButton = new Button("Logout");
        layout.setBottom(logoutButton);

        logoutButton.setOnAction(e -> {
       
            LoginScene.showLoginScene(primaryStage);
        });

        primaryStage.setScene(new Scene(layout, 800, 700));
        primaryStage.show();
    }
}
