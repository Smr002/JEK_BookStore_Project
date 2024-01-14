package source.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import source.Model.User;
import source.Main.Main;

public class LoginScene {
    public static void showLoginScene(Stage primaryStage) {

        grid.add(typeLabel1, 0, 0);

        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            User user = new User(null, username, password);

            String type = user.Login(username, password);
            switch (type) {
                case "Librarian":
                    LibrarianView.showLibrarianView(primaryStage, user);
                    break;
                case "Manager":
                    ManagerView.showManagerView(primaryStage, user);
                    break;
                case "Administrator":
                    AdministratorView.showAdministratorView(primaryStage, user);
                    break;
                default:
                    showAlert("Invalid Login", "Incorrect username or password");
                    break;
            }
        });

        backButton.setOnAction(e -> {
            Main.showMainScene(primaryStage);
            primaryStage.close();
        });

        Scene loginScene = new Scene(grid, 500, 400);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}