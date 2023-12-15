import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FirstWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JEK-BOOKSTORE");

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 700, 500);

        borderPane.setTop(createTopLabel());
        Button rightButton = new Button("Login");

        borderPane.setRight(rightButton);

        borderPane.setBottom(createBottomImageView());

        borderPane.setLeft(createLeftImageView());

        StackPane centerStackPane = createCenterStackPane();
        borderPane.setCenter(centerStackPane);

        rightButton.setOnAction(e -> showLoginScene(primaryStage, scene));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createTopLabel() {
        Label labelTop = new Label("JEK-BOOKSTORE");
        labelTop.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        labelTop.setStyle("-fx-text-fill: darkblue;");
        return labelTop;
    }

    private ImageView createBottomImageView() {
        Image bottomImage = new Image("Kronika_ne_gure.png");
        ImageView bottomImageView = new ImageView(bottomImage);
        bottomImageView.setFitHeight(180);
        bottomImageView.setTranslateX(10);
        bottomImageView.setPreserveRatio(true);
        return bottomImageView;
    }

    private ImageView createLeftImageView() {
        Image leftImage = new Image("Kodi_i_Da_Vicit.png");
        ImageView leftImageView = new ImageView(leftImage);
        leftImageView.setFitWidth(150);
        leftImageView.setPreserveRatio(true);
        return leftImageView;
    }

    private StackPane createCenterStackPane() {
        Image centerImage1 = new Image("Shoku_Zylo.png");
        ImageView centerImageView1 = new ImageView(centerImage1);
        centerImageView1.setFitHeight(200);
        centerImageView1.setTranslateY(-47);
        centerImageView1.setTranslateX(-50);
        centerImageView1.setPreserveRatio(true);

        Image centerImage2 = new Image("Me_ty_Pa_ty.png");
        ImageView centerImageView2 = new ImageView(centerImage2);
        centerImageView2.setFitHeight(200);
        centerImageView2.setTranslateY(225);
        centerImageView2.setTranslateX(-50);
        centerImageView2.setPreserveRatio(true);

        StackPane centerStackPane = new StackPane(centerImageView1, centerImageView2);
        return centerStackPane;
    }

    private void showLoginScene(Stage primaryStage, Scene scene) {
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
        grid.add(loginButton, 2, 1);
        Button backButton = new Button("Back");
        grid.add(backButton, 1, 2);

        Label typeLabel1 = new Label();
        grid.add(typeLabel1, 0, 0);

        Scene loginScene = new Scene(grid, 500, 400);

        GridPane grid2 = new GridPane();
        grid2.setAlignment(javafx.geometry.Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(25, 25, 25, 25));

        Button logoutButton = new Button("Logout");
        grid2.add(logoutButton, 1, 2);

        Label typeLabel = new Label();
        grid2.add(typeLabel, 0, 0);

        Scene scene1 = new Scene(grid2, 500, 400);

        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
           
            Librarian user = new Librarian(username, password);
            String type = user.Login();  

          
            ChoiceBox<String> choiceBox = new ChoiceBox<>();
            choiceBox.getItems().addAll("Request", "Show Books");
            grid2.add(choiceBox, 0, 1);

            Button okbutton = new Button("OK");
            grid2.add(okbutton,0,2);

            typeLabel.setText("Welcome Back " + type +"!");
            primaryStage.setScene(scene1);
        });

        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

        backButton.setOnAction(e -> primaryStage.setScene(scene));

        primaryStage.setScene(loginScene);
    }
}





