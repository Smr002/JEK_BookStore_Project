package source.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import source.Model.User;
import source.Main.Main;

public class LoginScene {
    public static void showLoginScene(Stage primaryStage) {
        GridPane grid2 = new GridPane();
        // Label typeLabel = new Label();
        Scene scene1 = new Scene(grid2, 800, 700);

        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 0);
        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 0);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 2, 1);
        Button backButton = new Button("Back");
        grid.add(backButton, 1, 2);

        Label typeLabel1 = new Label();
        grid.add(typeLabel1, 0, 0);

        grid2.setAlignment(Pos.TOP_LEFT);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(5, 5, 5, 5));

        Button logoutButton = new Button("Logout");
        grid2.add(logoutButton, 5, 2);

        Label typeLabel = new Label();
        grid2.add(typeLabel, 5, 4);

        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            User user = new User(null,username, password);

            String type = user.Login(username, password);
            switch (type) {
                case "Librarian":
                    LibrarianView.showLibrarianView(primaryStage);
                    break;
                case "Manager":
                    ManagerView.showManagerView(primaryStage);
                    break;
                case "Administrator":
                    AdministratorView.showAdministratorView(primaryStage);
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

        Scene loginScene = new Scene(grid, 800, 700);
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