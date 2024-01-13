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
        GridPane grid2 = new GridPane();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ImageView logo = new ImageView("images/jek_logo.png");
        logo.setFitWidth(200);
        logo.setPreserveRatio(true);
        grid.add(logo, 0, 0, 2, 1);
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);
        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 0, 3);
        Button backButton = new Button("Back");
        grid.add(backButton, 1, 3);

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