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
import javafx.scene.Node;
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
    private Button addToCartButton;
    private double totalPrice=0.0;

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



        orderButton.setOnAction(e -> primaryStage.setScene(Methods.createOrderConfirmationScene(primaryStage,this)));
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
        ///testing sth
        addToCartButton.setUserData(book);
        ///
        addToCartButton.setStyle("-fx-background-color: red");
        addToCartButton.setStyle("-fx-background-radius: 6");

        Label textLabel = new Label(book.getTitle() + "\n" +
                "Category: " + book.getCategory() + "\n" +
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
        addToCartButton.setOnAction(e -> handleAddToCart(book, quantityTextField,addToCartButton));

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
    private void handleAddToCart(Book book, TextField quantityTextField, Button addToCartButton) {
        String quantityText = quantityTextField.getText();

        boolean addedToCart = false;

        if (isValidQuantity(quantityText)) {
            int quantity = Integer.parseInt(quantityText);

            if (quantity <= book.getStock()) {
                HBox cartItemBox = createCartItem(book, quantityText, addToCartButton);
                cartVBox.getChildren().add(cartItemBox);

                VBox orderButtonVBox = (VBox) ((VBox) sx.getContent()).getChildren().get(1);
                orderButtonVBox.setVisible(true);
//calculating the price
                double itemPrice=book.getSellingPrice()*quantity;
                totalPrice+=itemPrice;
//totalPriceLabel.setText("Total Price: $"+String.format("%.2f",totalPrice));//update the total
                addedToCart = true;//true only when that particular book is added to cart successfully
            } else {
                showAlert("Invalid Quantity", "Quantity more than available stock.");
            }
        } else {
            showAlert("Invalid Quantity", "Please enter a valid positive integer for quantity.");
        }

        // Disable the button only if the book is added successfully
        if (addedToCart) {
            addToCartButton.setDisable(true);
        }
    }
    //adding a getter so i can access in the controller
    public double getTotalPrice(){
        return totalPrice;
    }

    private HBox createCartItem(Book book, String quantityText,Button addToCartButton) {
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
        deleteButton.setOnAction(e -> handleDeleteFromCart(cartItemBox,addToCartButton));

        return cartItemBox;
    }

    private void handleDeleteFromCart(HBox cartItemBox,Button addToCartButton) {
        cartVBox.getChildren().remove(cartItemBox);//heq nga cart
        addToCartButton.setDisable(false);//when removed from cart the addToCart button is active back again
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



}
