import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Test extends Application {
    public static void main(String[] args) {
         launch(args);

    }

 @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Login");

      
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
        grid.add(loginButton, 1, 2);

        Scene loginScene = new Scene(grid, 300, 200);

   
        GridPane grid2 = new GridPane();
        grid2.setAlignment(javafx.geometry.Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(25, 25, 25, 25));

        Scene scene1 = new Scene(grid2, 300, 200);

        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            Librarian user = new Librarian(username, password);
            String type = user.Login();
            Label lb1 = new Label("You are: " + type);
            grid2.add(lb1, 0, 0);
            primaryStage.setScene(scene1);
        });
       
        primaryStage.setScene(loginScene);

        primaryStage.show();

    }
}
