package source.View;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Main.Main;
import source.Model.Book;

public class FirstWindow {
    private ScrollPane sx;
    private Label cartLabel = new Label("Cart is Empty");
    private VBox allBooksVBox = new VBox(40);
    private VBox cartVBox = new VBox(10); // New VBox for cart items
    private Scene orderConfirmationScene;
    private Label totalPriceLabel = new Label("Total Price: $0.00");

    public void showFirstWindow() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JEK-BOOKSTORE");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: red");
        Scene scene = new Scene(borderPane, 800, 650);

        Button rightButton = new Button("Login");
        GridPane labelGrid = new GridPane();
        GridPane gp2 = new GridPane();
        borderPane.setRight(gp2);
        borderPane.setTop(labelGrid);
        gp2.add(rightButton, 0, 0);
        createTopLabel(borderPane);

        List<Book> books = Methods.readBook();
        int booksPerRow = 3;

        for (int i = 0; i < books.size(); i += booksPerRow) {
            int endIndex = Math.min(i + booksPerRow, books.size());
            List<Book> rowBooks = books.subList(i, endIndex);

            HBox bookRow = createBookRow(rowBooks);
            bookRow.setAlignment(Pos.BASELINE_LEFT);
            bookRow.setStyle("-fx-border-color: blue");
            allBooksVBox.getChildren().add(bookRow);
        }

        ScrollPane scrollPane = new ScrollPane(allBooksVBox);
        scrollPane.setPrefViewportWidth(200);
        sx = new ScrollPane();
        sx.setPrefViewportWidth(300);

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(sx);

        sx.setContent(cartVBox);
        Button orderButton = new Button("Proceed to order");
        VBox orderButtonVBox = new VBox(orderButton);
        orderButtonVBox.setVisible(false);
        sx.setContent(new VBox(cartVBox, orderButtonVBox));

        rightButton.setOnAction(e -> {
            LoginScene.showLoginScene(primaryStage);
        });

        orderConfirmationScene = createOrderConfirmationScene(primaryStage);

        orderButton.setOnAction(e -> {
            primaryStage.setScene(orderConfirmationScene);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createTopLabel(BorderPane borderPane) {
        Image logoImage = new Image("images/jek_logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);
        borderPane.setTop(logoImageView);
    }

    private HBox createBookRow(List<Book> rowBooks) {
        HBox bookRow = new HBox(30);

        for (Book book : rowBooks) {
            VBox bookContainer = createBookContainer(book);
            bookRow.getChildren().add(bookContainer);
        }

        return bookRow;
    }

    private VBox createBookContainer(Book book) {
        ImageView bookImageView = createBookImageView(book.getImagePath());

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: red");
        addToCartButton.setStyle("-fx-background-radius: 6");

        Label textLabel = new Label(book.getTitle() + "\n" +
                "Description: " + book.getCategory() + "\n" +
                "ISBN: " + book.getISBN());
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
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

        // Pass quantityTextField to handleAddToCart method
        addToCartButton.setOnAction(e -> handleAddToCart(book, quantityTextField));

        return bookContainer;
    }

    private ImageView createBookImageView(String imagePath) {
        Image bookImage = new Image("file:" + imagePath);
        ImageView bookImageView = new ImageView(bookImage);

        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        bookImageView.setOnMouseEntered(e -> {
            VBox.setMargin(bookImageView, new Insets(5, 0, 5, 0));
            //bookImageView.setStyle("-fx-background-color: #dae7f3;");
            bookImageView.setFitHeight(320);
            //.setFitWidth(100);
        });

        bookImageView.setOnMouseExited(e -> {
            bookImageView.setFitHeight(300);
            //bookImageView.setFitWidth(95);
            //bookImageView.setStyle("-fx-background-color: transparent;");
        });

        bookImageView.setOnMouseClicked(e->{
            //new stage
        });

        return bookImageView;
    }

    // Create a delete button to delete certain book in the cart
    private void handleAddToCart(Book book, TextField quantityTextField) {
        String quantityText = quantityTextField.getText();

        if (isValidQuantity(quantityText)) {
            HBox cartItemBox = createCartItem(book, quantityText);
            cartVBox.getChildren().add(cartItemBox);

            VBox orderButtonVBox = (VBox) ((VBox)sx.getContent()).getChildren().get(1);//e ben visible orderButton kur behet add to cart
            orderButtonVBox.setVisible(true);
        } else {
            showAlert("Invalid Quantity", "Please enter a valid positive integer for quantity.");
        }
    }

    private HBox createCartItem(Book book, String quantityText) {
        Label cartItemLabel = new Label("Added to Cart " + "\nTitle:" + book.getTitle() + "\nPrice:"
                + book.getSellingPrice() +
                "\nQuantity: " + quantityText + "\n-----------------------------------");

        Button deleteButton = new Button("Remove from cart");
        deleteButton.setStyle("-fx-background-color: #ff3333"); // Red color for delete button
        deleteButton.setStyle("-fx-background-radius: 6");

        HBox cartItemBox = new HBox(10);
        cartItemBox.getChildren().addAll(cartItemLabel,deleteButton);
        deleteButton.setAlignment(Pos.BOTTOM_RIGHT);
        cartItemBox.setAlignment(Pos.CENTER_LEFT);

        // Pass cartItemBox to handleDeleteFromCart method
        deleteButton.setOnAction(e -> handleDeleteFromCart(cartItemBox));

        return cartItemBox;
    }

    private void handleDeleteFromCart(HBox cartItemBox) {
        cartVBox.getChildren().remove(cartItemBox);//heq nga cart
    }

    private boolean isValidQuantity(String quantityText) {
        try {
            int quantity = Integer.parseInt(quantityText);
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //////////////////////////////////////////////////
    private Scene createOrderConfirmationScene(Stage primaryStage) {
        // Create a GridPane for the order confirmation scene
        GridPane orderConfirmationGrid = new GridPane();
        orderConfirmationGrid.setAlignment(Pos.TOP_LEFT);
        orderConfirmationGrid.setVgap(20);

        Label orderLabel = new Label("Please fill in to order");
        orderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        orderLabel.setStyle("-fx-text-fill: green;");
        orderConfirmationGrid.add(orderLabel, 0, 0);
        orderConfirmationGrid.add(new Label(" Enter your name:"), 0, 1);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        orderConfirmationGrid.add(nameField, 1, 1);

        orderConfirmationGrid.add(new Label(" Enter your surname:"), 0, 2);
        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");
        orderConfirmationGrid.add(surnameField, 1, 2);

        orderConfirmationGrid.add(new Label(" Enter your email:"), 0, 3);
        TextField emailField = new TextField();
        emailField.setPromptText("email");
        orderConfirmationGrid.add(emailField, 1, 3);

        orderConfirmationGrid.add(new Label(" Enter your phone number:"), 0, 4);
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone number");
        orderConfirmationGrid.add(phoneNumberField, 1, 4);

        Label totalPricewV= new Label("Price without VAT: ");
        orderConfirmationGrid.add(totalPricewV,0,5);
Label vatPrice = new Label("VAT: ");
orderConfirmationGrid.add(vatPrice,0,6);
Label totalPrice = new Label("Total: ");
orderConfirmationGrid.add(totalPrice,0,7);
//create a confirmation order button
        Button confirmOrder= new Button("Confirm Order");

        // createe a back button to return to the main scene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Main.showMainScene(primaryStage);
            primaryStage.close();
        });


        orderConfirmationGrid.add(backButton, 0,10);
orderConfirmationGrid.add(confirmOrder,1,10);
        return new Scene(orderConfirmationGrid, 450, 450);
    }




}
