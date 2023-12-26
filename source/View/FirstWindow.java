package source.View;

import java.util.List;
import javafx.application.Platform;
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
import source.Model.Book;

public class FirstWindow {
    static double total = 0;
    private Label cartLabel = new Label("Cart is Empty");
    private VBox allBooksVBox = new VBox(40);

    public void showFirstWindow() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JEK-BOOKSTORE");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: red");
        Scene scene = new Scene(borderPane, 1000, 850);

        Button rightButton = new Button("Login");
        GridPane labelGrid = new GridPane();
        GridPane gp2 = new GridPane();
        borderPane.setRight(gp2);
        borderPane.setTop(labelGrid);
        gp2.add(rightButton, 0, 0);
        labelGrid.add(createTopLabel(), 0, 0);
        labelGrid.setAlignment(Pos.TOP_LEFT);

        List<Book> books = Methods.readBook();

        int booksPerRow = 3;
        // creating bookrow with 3 books per roww
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
        ScrollPane sx = new ScrollPane();// kjo eshte per pj add to cart
        sx.setPrefViewportWidth(300);

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(sx);

        sx.setContent(cartLabel);

        rightButton.setOnAction(e -> {
            LoginScene.showLoginScene(primaryStage);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createTopLabel() {
        Label labelTop = new Label("JEK-BOOKSTORE");
        labelTop.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        labelTop.setStyle("-fx-text-fill: darkblue;");
        return labelTop;
    }

    private HBox createBookRow(List<Book> rowBooks) {
        HBox bookRow = new HBox(20);

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
            bookImageView.setStyle("-fx-background-color: #dae7f3;");
        });

        bookImageView.setOnMouseExited(e -> {
            bookImageView.setStyle("-fx-background-color: transparent;");
        });

        return bookImageView;
    }

    private void handleAddToCart(Book book, TextField quantityTextField) {
        String quantityText = quantityTextField.getText();
        int qua = Integer.parseInt(quantityText);


        if (isValidQuantity(quantityText)) {
            String currentText = cartLabel.getText();
            total+=qua*book.getSellingPrice();
            String newText = currentText + "\nAdded to Cart " + "\nTitle:" + book.getTitle() + "\nPrice per book:"
                    + book.getSellingPrice() +
                    "\nQuantity: " + quantityText + "\nTotal Price for Book: " + qua*book.getSellingPrice() + "\nCurrent Price for all: " + total;

            cartLabel.setText(newText);
        } else {
            showAlert("Invalid Quantity", "Please enter a valid positive integer for quantity.");
        }
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
}

