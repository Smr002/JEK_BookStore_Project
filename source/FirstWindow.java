package source;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.io.File;
import java.text.ParseException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FirstWindow extends Application  {
    private Label cartLabel = new Label("Cart is Empty");
    private VBox allBooksVBox = new VBox(40);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JEK-BOOKSTORE");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: red");
        Scene scene = new Scene(borderPane, 700, 500);

        Button rightButton = new Button("Login");
        GridPane labelGrid = new GridPane();
        GridPane gp2 = new GridPane();
        borderPane.setRight(gp2);
        borderPane.setTop(labelGrid);
        gp2.add(rightButton, 0, 0);
        labelGrid.add(createTopLabel(), 0, 0);
        labelGrid.setAlignment(Pos.TOP_LEFT);

        int booksPerRow = 3;
        int numberOfRows = 4;

        for (int i = 0; i < numberOfRows; i++) {
            int startIdx = i * booksPerRow;
            int endIdx = Math.min((i + 1) * booksPerRow, getImagePaths().size());
            List<String> rowImagePaths = getImagePaths().subList(startIdx, endIdx);
            HBox bookRow = createBookRow(rowImagePaths.toArray(new String[0]));
            bookRow.setAlignment(Pos.BASELINE_LEFT);
            bookRow.setStyle("-fx-border-color: blue");
            allBooksVBox.getChildren().add(bookRow);
        }

        ScrollPane scrollPane = new ScrollPane(allBooksVBox);
        scrollPane.setPrefViewportWidth(200);
        ScrollPane sx = new ScrollPane();
        sx.setPrefViewportWidth(300);

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(sx);

        sx.setContent(cartLabel);

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

    private List<String> getImagePaths() {
        List<String> imagePaths = new ArrayList<>();
        File folder = new File("images");

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        imagePaths.add(file.toURI().toString());
                    }
                }
            }
        }

        return imagePaths;
    }

    private HBox createBookRow(String... imagePaths) {
        HBox bookRow = new HBox(70);

        for (String imagePath : imagePaths) {
            ImageView bookImageView = createBookImageView(imagePath);

            Button addToCartButton = new Button("Add to Cart");
            addToCartButton.setStyle("-fx-background-color: red");
            addToCartButton.setStyle("-fx-background-radius: 6");

            Label textLabel = new Label("This is test\n" +
                    "Description\n" +
                    "Isbn: ");
            textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
            textLabel.setStyle("-fx-text-fill: black;");
            textLabel.setStyle("-fx-background-color: yellow");
            textLabel.setStyle("-fx-border-color: black");

            TextField quantityTextField = new TextField();
            quantityTextField.setPromptText("Quantity");

            HBox quantityAndButtonBox = new HBox(10);
            quantityAndButtonBox.getChildren().addAll(quantityTextField, addToCartButton);

            VBox bookContainer = new VBox(10);
            bookContainer.getChildren().addAll(bookImageView, textLabel, quantityAndButtonBox);
            bookContainer.setAlignment(Pos.CENTER_LEFT);

            bookRow.getChildren().add(bookContainer);

            // Pass quantityTextField to handleAddToCart method
            addToCartButton.setOnAction(e -> handleAddToCart(imagePath, quantityTextField));
        }

        return bookRow;
    }

    private ImageView createBookImageView(String imagePath) {
        Image bookImage = new Image(imagePath);
        ImageView bookImageView = new ImageView(bookImage);

        bookImageView.setStyle("-fx-background-color: transparent;");
        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        bookImageView.setOnMouseEntered(e -> {
            VBox.setMargin(bookImageView, new Insets(5, 0, 5, 0));
            bookImageView.setStyle("-fx-background-color: #dae7f3;");
        });

        bookImageView.setOnMouseExited(e -> {
            bookImageView.setStyle("-fx-background-color: transparent;");
        });

        return bookImageView;
    }

    private void handleAddToCart(String imagePath, TextField quantityTextField) {
        String quantityText = quantityTextField.getText();

        // Check if quantity is a valid positive integer
        if (isValidQuantity(quantityText)) {
            String currentText = cartLabel.getText();
            String newText = currentText + "\nAdded to Cart " + imagePath + "\nQuantity: " + quantityText;
            cartLabel.setText(newText);
        } else {
            // Display an alert for invalid quantity
            showAlert("Invalid Quantity", "Please enter a valid positive integer for quantity.");
        }
    }

   //method to check if quantity is valid or not
    private boolean isValidQuantity(String quantityText) {
        try {
            int quantity = Integer.parseInt(quantityText);
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }

//method to display the alert,(jo e domosdoshme)
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showLoginScene(Stage primaryStage, Scene scene) {
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
//
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

        Scene loginScene = new Scene(grid, 800, 700);

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
            User user = new User(username,password);

            String type = user.Login();
            // BorderPane layout= new BorderPane();
            // MenuBar menuBar = new MenuBar();

            switch (type) {
                case "Librarian":

                    primaryStage.setTitle("Librarian MENUu");
                    BorderPane layoutL = new BorderPane();

                    MenuBar menuBarL = new MenuBar();
                    Menu fileL = new Menu("BOOKS");
                    Menu requestL = new Menu("Request");
                    Menu showBooksL = new Menu("Show Books");
                    Menu option1L = new Menu("BILLS");
                    Menu option2L = new Menu("Option2");
                    layoutL.setTop(menuBarL);

                    // showBooksL.setOnAction(k -> Librarian.getBooks());
                    // nuk di func q punon , ok bn t njtn gj



                    menuBarL.getMenus().addAll(fileL, requestL, showBooksL, option1L, option2L);
                    MenuItem item1 = new MenuItem("Show Books");
                    MenuItem item2 = new MenuItem("Add Books");//ksaj mund ti bejm disable nga permission i adminit
                    MenuItem item3 = new MenuItem("Test3 L");
                    fileL.getItems().addAll(item1, item2, item3);

                    grid2.add(menuBarL, 0, 0);

                    Button okbutton1 = new Button("OK");
                    //  item1.setOnAction(l-> Methods.getBooks());
                    //   okbutton1.setOnAction(l -> Methods.getBooks());

                    grid2.add(okbutton1, 0, 2);

                    typeLabel.setText("Welcome Back Librariann!");
                    primaryStage.setScene(scene1);
                    break;
                case "Manager":
                    // showManagerView(primaryStage);
                    primaryStage.setTitle("Manager MENU");
                    BorderPane layoutM = new BorderPane();
                    // added menubar to manager view
                    MenuBar menuBarM = new MenuBar();
                    Menu fileM = new Menu("File");
                    Menu requestM = new Menu("Request");
                    Menu showBooksM = new Menu("Show Books");
                    Menu option1M = new Menu("Option1");
                    Menu option2M = new Menu("Option2");

                    layoutM.setTop(menuBarM);

                    menuBarM.getMenus().addAll(fileM, requestM, showBooksM, option1M, option2M);
                    MenuItem item1M = new MenuItem("Test1 M");
                    MenuItem item2M = new MenuItem("Test2 M");
                    MenuItem item3M = new MenuItem("Test3 M");
                    fileM.getItems().addAll(item1M, item2M, item3M);

                    grid2.add(menuBarM, 0, 0);
                    // menuBar.prefHeight().bind(primaryStage.widthProperty());

                    Button okbutton2 = new Button("OK");
                    grid2.add(okbutton2, 0, 2);

                    typeLabel.setText("Welcome Back M!");
                    // typeLabel.setAlignment(Pos.CENTER);
                    primaryStage.setScene(scene1);
                    break;
                case "Administrator":
                    primaryStage.setTitle("Administrator MENU");
                    BorderPane layoutA = new BorderPane();

                    MenuBar menuBarA = new MenuBar();
                    Menu fileA = new Menu("File");
                    Menu requestA = new Menu("Request");
                    Menu showBooksA = new Menu("Show Books");
                    Menu showPerformance = new Menu("Performance");
                    Menu option2A = new Menu("Option2");

                    layoutA.setTop(menuBarA);

                    menuBarA.getMenus().addAll(fileA, requestA, showBooksA, showPerformance, option2A);
                    MenuItem item1A = new MenuItem("Test1 ad");
                    MenuItem item2A = new MenuItem("Test2 ad");
                    MenuItem item3A = new MenuItem("Test3 ad");
                    fileA.getItems().addAll(item1A, item2A, item3A);

                    grid2.add(menuBarA, 0, 0);
                    // should make the menu fit
                    // menuBar.prefHeight().bind(primaryStage.widthProperty());

                    Button okbutton3 = new Button("OK");
                    grid2.add(okbutton3, 0, 2);

                    typeLabel.setText("Welcome Boss!");
                    primaryStage.setScene(scene1);
                    break;
                default:
                    // adding a popup when invalid input entered
                    // System.err.println("Unexpected user type: " + type);
                    JOptionPane.showMessageDialog(null, "Incorrect username or password", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    break;

            }
        });

        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

        backButton.setOnAction(e -> primaryStage.setScene(scene));

        primaryStage.setScene(loginScene);

    }
}