package source.View;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.text.SimpleDateFormat;
import java.time.Duration;

import javafx.stage.Stage;
import javafx.scene.Node;
import source.Controller.Methods;
import source.Main.Main;
import source.Model.Book;
import source.Model.Order;
import source.Model.User;

public class FirstWindow {
    private ScrollPane sx;
    private Label cartLabel = new Label("Cart is Empty");
    private VBox allBooksVBox = new VBox(40);
    private VBox cartVBox = new VBox(10); // New VBox for cart items
    private Scene orderConfirmationScene;
    private Label totalPriceLabel = new Label("Total Price:");
    private Button addToCartButton;

    Order order = new Order();
    double total = order.getTotalPrice();
    List<String> isbnListt = order.getIsbnList();
    List<String> quantityListt = order.getQuantityList();
    Date order_date = order.getOrderDate();

    public void showFirstWindow() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JEK-BOOKSTORE");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: red");
        Scene scene = new Scene(borderPane, 800, 650);
        TextField searchBar = new TextField();
        Button searchButton = new Button("Search");
        ChoiceBox<String> searchByBox = new ChoiceBox<>(
                FXCollections.observableArrayList("Title", "Author", "Isbn"));
        Button rightButton = new Button("Login");
        GridPane labelGrid = new GridPane();
        GridPane gp2 = new GridPane();
        borderPane.setRight(gp2);
        borderPane.setTop(labelGrid);
        gp2.add(rightButton, 0, 0);
        createTopLabel(borderPane);
        // welcomeView(borderPane);
        ///// test
        Button showOrders = new Button("orders");
        gp2.add(showOrders, 0, 1);
        gp2.add(searchButton, 0, 2);
        gp2.add(searchBar, 0, 3);
        gp2.add(searchByBox, 0, 4);

        //
        searchButton.setOnAction(e -> {
            String searchTerm = searchBar.getText();
            String searchBy = searchByBox.getValue();

            if (searchBy == null || searchBy.equals("Search By")) {

                showAlert("Invalid Search", "Please select a search option.");
                return;
            }

            List<Book> searchResults = new ArrayList<>();

            switch (searchBy) {
                case "Title":
                    searchResults = Methods.searchBooksByTitle(searchTerm);
                    break;
                case "Author":
                    searchResults = Methods.searchBooksByAuthor(searchTerm);
                    break;
                case "Isbn":
                    searchResults = Methods.searchBooksByIsbn(searchTerm);
                    break;
                default:

                    break;
            }

            allBooksVBox.getChildren().clear();

            // row i ri
            int booksPerRow = 3;
            for (int i = 0; i < searchResults.size(); i += booksPerRow) {
                int endIndex = Math.min(i + booksPerRow, searchResults.size());
                List<Book> rowBooks = searchResults.subList(i, endIndex);

                HBox bookRow = createBookRow(rowBooks);
                bookRow.setAlignment(Pos.BASELINE_LEFT);
                bookRow.setStyle("-fx-border-color: green");
                allBooksVBox.getChildren().add(bookRow);
            }
        });

        /////

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

        orderButton.setOnAction(e -> primaryStage.setScene(
                Methods.createOrderConfirmationScene(primaryStage, this, total, isbnListt, quantityListt, order_date)));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createTopLabel(BorderPane borderPane) {
        Image logoImage = new Image("images/jek_logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        HBox hbox = new HBox(logoImageView);
        borderPane.setTop(hbox);

        javafx.util.Duration duration = javafx.util.Duration.seconds(5);

        TranslateTransition translateTransition = new TranslateTransition(duration, hbox);
        translateTransition.setFromX(-50);
        translateTransition.setToX(1550);

        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

        javafx.animation.SequentialTransition sequentialTransition = new SequentialTransition(translateTransition);

        sequentialTransition.play();
    }

    private void welcomeView(BorderPane borderPane) {
        Label label = new Label("Welcome in JEK !!!");

        HBox hbox = new HBox(label);
        borderPane.setTop(hbox);

        javafx.util.Duration duration = javafx.util.Duration.seconds(1);

        TranslateTransition translateTransition = new TranslateTransition(duration, hbox);
        translateTransition.setFromX(-100);
        translateTransition.setToX(0);

        translateTransition.play();
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
        /// testing sth
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
        addToCartButton.setOnAction(e -> handleAddToCart(book, quantityTextField, addToCartButton));

        return bookContainer;
    }

    private ImageView createBookImageView(String imagePath) {
        Image bookImage = new Image("file:" + imagePath);
        ImageView bookImageView = new ImageView(bookImage);

        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        bookImageView.setOnMouseEntered(e -> {
            VBox.setMargin(bookImageView, new Insets(5, 0, 5, 0));
            // bookImageView.setStyle("-fx-background-color: #dae7f3;");
            bookImageView.setFitHeight(320);
            // .setFitWidth(100);
        });

        bookImageView.setOnMouseExited(e -> {
            bookImageView.setFitHeight(300);
            // bookImageView.setFitWidth(95);
            // bookImageView.setStyle("-fx-background-color: transparent;");
        });

        bookImageView.setOnMouseClicked(e -> {
            // new stage
        });

        return bookImageView;
    }

    private void handleAddToCart(Book book, TextField quantityTextField, Button addToCartButton) {
        String quantityText = quantityTextField.getText();

        boolean addedToCart = false;

        if (isValidQuantity(quantityText)) {

            if (Integer.parseInt(quantityText) <= book.getStock()) {
                HBox cartItemBox = createCartItem(book, quantityText, addToCartButton);
                cartVBox.getChildren().add(cartItemBox);

                VBox orderButtonVBox = (VBox) ((VBox) sx.getContent()).getChildren().get(1);
                orderButtonVBox.setVisible(true);

                isbnListt.add(book.getISBN());
                quantityListt.add(quantityText); // Parse quantity as an integer

                // calculating the price
                double itemPrice = book.getSellingPrice() * Integer.parseInt(quantityText);
                ;
                total += itemPrice;
                order.setTotalPrice(total);
                System.out.println("total is" + order.getTotalPrice());
                order.setIsbnList(new ArrayList<>(isbnListt)); // Make a copy
                order.setQuantityList(new ArrayList<>(quantityListt)); // Make a copy
                // add the date
                // Date order_date = order.getOrderDate();
                order.setOrderDate(order_date != null ? order_date : new Date());

                // Format the date as dd.MM.yyyy HH:mm:ss if needed
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String formattedDate = dateFormat.format(order.getOrderDate());
                addedToCart = true;
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

    private HBox createCartItem(Book book, String quantityText, Button addToCartButton) {
        Label cartItemLabel = new Label("Added to Cart " + "\nTitle:" + book.getTitle() + "\nPrice:"
                + book.getSellingPrice() +
                "\nQuantity: " + quantityText + "\n-----------------------------------");

        Button deleteButton = new Button("Remove from cart");
        deleteButton.setStyle("-fx-background-color: #ff3333"); // Red color for delete button
        deleteButton.setStyle("-fx-background-radius: 6");

        HBox cartItemBox = new HBox(10);
        cartItemBox.getChildren().addAll(cartItemLabel, deleteButton);
        deleteButton.setAlignment(Pos.BOTTOM_RIGHT);
        cartItemBox.setAlignment(Pos.CENTER_LEFT);

        // Pass cartItemBox to handleDeleteFromCart method
        deleteButton.setOnAction(e -> handleDeleteFromCart(cartItemBox, addToCartButton, book, quantityText));

        return cartItemBox;
    }

    private void handleDeleteFromCart(HBox cartItemBox, Button addToCartButton, Book book, String quantityText) {
        cartVBox.getChildren().remove(cartItemBox); // Remove from cart

        // Calculate the price of the removed item
        double removedItemPrice = book.getSellingPrice() * Integer.parseInt(quantityText);

        total -= removedItemPrice;
        isbnListt.remove(book.getISBN());
        quantityListt.remove(quantityText);
        order.setTotalPrice(total);

        addToCartButton.setDisable(false); // Enable the addToCart button
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
