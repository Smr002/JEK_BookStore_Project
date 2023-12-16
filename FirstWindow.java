import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.swing.JOptionPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
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
    ///








    private void showLoginScene(Stage primaryStage, Scene scene) {
        GridPane grid2 = new GridPane();
        //Label typeLabel = new Label();
        Scene scene1 = new Scene(grid2, 500, 400);

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

        Scene loginScene = new Scene(grid, 500, 500);


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

            Librarian user = new Librarian(username, password);
            String type = user.Login();
          //  BorderPane layout= new BorderPane();
          //  MenuBar menuBar = new MenuBar();

            switch (type) {
                case "Librarian":
                    // showLibrarianView(primaryStage);
                    primaryStage.setTitle("Librarian MENU");
                   BorderPane layoutL= new BorderPane();

                    MenuBar menuBarL = new MenuBar();
                    Menu fileL = new Menu("File");
                    Menu requestL = new Menu("Request");
                    Menu showBooksL = new Menu("Show Books");
                    Menu option1L =new Menu("BILLS");
                    Menu option2L= new Menu("Option2");

                    layoutL.setTop(menuBarL);

                    menuBarL.getMenus().addAll(fileL,requestL,showBooksL,option1L,option2L);
                    MenuItem item1 = new MenuItem("Test1 L");
                    MenuItem item2 = new MenuItem("Test2 L");
                    MenuItem item3 = new MenuItem("Test3 L");
                    fileL.getItems().addAll(item1,item2,item3);

                    grid2.add(menuBarL, 0,0);

                    Button okbutton1 = new Button("OK");
                    grid2.add(okbutton1, 0, 2);

                    typeLabel.setText("Welcome Back Librarian!");
                    primaryStage.setScene(scene1);
                    break;
                case "Manager":
                    //showManagerView(primaryStage);
                    primaryStage.setTitle("Manager MENU");
                   BorderPane layoutM= new BorderPane();
                    //added menubar to manager view
                   MenuBar menuBarM = new MenuBar();
                    Menu fileM= new Menu("File");
                    Menu requestM = new Menu("Request");
                    Menu showBooksM = new Menu("Show Books");
                    Menu option1M =new Menu("Option1");
                    Menu option2M= new Menu("Option2");

                    layoutM.setTop(menuBarM);

                    menuBarM.getMenus().addAll(fileM,requestM,showBooksM,option1M,option2M);
 MenuItem item1M = new MenuItem("Test1 M");
 MenuItem item2M = new MenuItem("Test2 M");
 MenuItem item3M = new MenuItem("Test3 M");
 fileM.getItems().addAll(item1M,item2M,item3M);

                    grid2.add(menuBarM, 0,0);
                    //menuBar.prefHeight().bind(primaryStage.widthProperty());


                    Button okbutton2 = new Button("OK");
                    grid2.add(okbutton2, 0, 2);

                    typeLabel.setText("Welcome Back M!");
//typeLabel.setAlignment(Pos.CENTER);
                    primaryStage.setScene(scene1);
                    break;
                case "Administrator":
                    primaryStage.setTitle("Administrator MENU");
                    BorderPane layoutA= new BorderPane();

                    MenuBar menuBarA = new MenuBar();
                    Menu fileA = new Menu("File");
                    Menu requestA = new Menu("Request");
                    Menu showBooksA = new Menu("Show Books");
                    Menu showPerformance =new Menu("Performance");
                    Menu option2A= new Menu("Option2");

                    layoutA.setTop(menuBarA);

                    menuBarA.getMenus().addAll(fileA,requestA,showBooksA,showPerformance,option2A);
                    MenuItem item1A = new MenuItem("Test1 ad");
                    MenuItem item2A = new MenuItem("Test2 ad");
                    MenuItem item3A = new MenuItem("Test3 ad");
                    fileA.getItems().addAll(item1A,item2A,item3A);

                    grid2.add(menuBarA, 0,0);
                    //should make the menu fit
                    //menuBar.prefHeight().bind(primaryStage.widthProperty());


                    Button okbutton3 = new Button("OK");
                    grid2.add(okbutton3, 0, 2);

                    typeLabel.setText("Welcome Boss!");
                    primaryStage.setScene(scene1);
                    break;
                default:
                    //adding a popup when invalid input entered
                   // System.err.println("Unexpected user type: " + type);
                   JOptionPane.showMessageDialog(null,"Incorrect username or password","ERROR",JOptionPane.ERROR_MESSAGE);
                    break;

            }
        });

        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

        backButton.setOnAction(e -> primaryStage.setScene(scene));

        primaryStage.setScene(loginScene);


    }
}




