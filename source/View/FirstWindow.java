package source.View;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Model.Book;
import source.Model.Order;
import javafx.animation.SequentialTransition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirstWindow {
    private ScrollPane sx;
    private Label cartLabel = new Label("Cart is Empty");
    private VBox allBooksVBox = new VBox(40);
    private VBox cartVBox = new VBox(10); // New VBox for cart items
    private Scene orderConfirmationScene;
    private Label totalPriceLabel = new Label("Total Price:");
    private Button addToCartButton;
    boolean addedToCart = false;
    boolean emptyCart= true;
    private Button orderButton;
    private ImageView emptyLabel;

    Order order = new Order();
    double total = order.getTotalPrice();
    List<String> isbnListt = order.getIsbnList();
    List<String> quantityListt = order.getQuantityList();
    Date order_date = order.getOrderDate();

    public void showFirstWindow() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JEK-BOOKSTORE");
        primaryStage.setMaximized(true);// e bn stage fullscreen
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #00ff00");// #ffb3b3
        Scene scene = new Scene(borderPane, 800, 650);

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(10, 10, 10, 10));

        // Wrap logoHBox and buttonsHBox in a VBox
        VBox topVBox = new VBox();

        HBox logoHBox = new HBox();
        createTopLabel(logoHBox);

        HBox buttonsHBox = new HBox();
        buttonsHBox.setPrefWidth(1300);
        // topVBox.setStyle("-fx-background-color: red");
        topVBox.getChildren().addAll(logoHBox, buttonsHBox);
        topHBox.getChildren().add(topVBox);

        borderPane.setTop(topHBox);

        TextField searchBar = new TextField();
        Button searchButton = new Button();
        searchButton.setStyle(
                "-fx-font-family: 'FontAwesome';" +
                        "-fx-font-size: 14px;" +
                        "-fx-content: '\uf002';" // Unicode character for the search icon in Font Awesome
        );
        searchButton.setStyle(
                "-fx-graphic: url('images/search-icon.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;"
        );
        ChoiceBox<String> searchByBox = new ChoiceBox<>(
                FXCollections.observableArrayList("Title", "Author", "Isbn"));
        Button rightButton = new Button("Login");

        buttonsHBox.getChildren().addAll(searchByBox, searchBar, searchButton, rightButton);
        buttonsHBox.setAlignment(Pos.BASELINE_RIGHT);
        topVBox.setAlignment(Pos.TOP_LEFT);

        searchByBox.setOnAction(e -> {
            String searchBy = searchByBox.getValue();
            searchBar.setPromptText("Enter " + searchBy);
        });

        searchButton.setOnAction(e -> {
            String searchTerm = searchBar.getText();
            String searchBy = searchByBox.getValue();

            if (searchBy == null || searchBy.equals("Search By")) {
                showAlert("Invalid Search", "Please select a search option.");
                return;
            }

            List<Book> searchResults = Methods.searchBooks(searchBy, searchTerm);

            allBooksVBox.getChildren().clear();

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
        emptyLabel=new ImageView("images/emptycart.png");

        sx.setContent(cartVBox);
        orderButton = new Button("Proceed to order");
        VBox orderButtonVBox = new VBox(orderButton);
        orderButtonVBox.setVisible(false);
        sx.setContent(new VBox(cartVBox, orderButtonVBox,emptyLabel));

        rightButton.setOnAction(e -> {
            LoginScene.showLoginScene(primaryStage);
        });
        orderButton.setVisible(emptyCart);

        orderButton.setOnAction(e -> primaryStage.setScene(
                Methods.createOrderConfirmationScene(primaryStage, this, total, isbnListt, quantityListt, order_date)));
        primaryStage.setScene(scene);
        primaryStage.show();



        if(order.getTotalPrice()==0) {
            System.out.println("cart is empt");
            emptyCart = true;
        }
    }

    private void createTopLabel(HBox logoHBox) {
        Image logoImage = new Image("images/jek_logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        HBox hbox = new HBox(logoImageView);
        logoHBox.getChildren().add(hbox);

        javafx.util.Duration duration = javafx.util.Duration.seconds(9);

        TranslateTransition translateTransition = new TranslateTransition(duration, hbox);
        translateTransition.setFromX(-50);
        translateTransition.setToX(1550);

        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

        javafx.animation.SequentialTransition sequentialTransition = new SequentialTransition(translateTransition);

        sequentialTransition.play();
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
        addToCartButton.setUserData(book);
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
            bookImageView.setFitHeight(320);
        });

        bookImageView.setOnMouseExited(e -> {
            bookImageView.setFitHeight(300);
        });

        bookImageView.setOnMouseClicked(e -> {
            // new stage
        });

        return bookImageView;
    }

    private void handleAddToCart(Book book, TextField quantityTextField, Button addToCartButton) {
        String quantityText = quantityTextField.getText();

        //boolean addedToCart = false;

        if (isValidQuantity(quantityText)) {
            if (Integer.parseInt(quantityText) <= book.getStock()) {
                HBox cartItemBox = createCartItem(book, quantityText, addToCartButton);
                cartVBox.getChildren().add(cartItemBox);

                VBox orderButtonVBox = (VBox) ((VBox) sx.getContent()).getChildren().get(1);
                orderButtonVBox.setVisible(true);

                isbnListt.add(book.getISBN());
                quantityListt.add(quantityText);

                double itemPrice = book.getSellingPrice() * Integer.parseInt(quantityText);
                total += itemPrice;
                order.setTotalPrice(total);
                order.setIsbnList(new ArrayList<>(isbnListt));
                order.setQuantityList(new ArrayList<>(quantityListt));
                order.setOrderDate(order_date != null ? order_date : new Date());
                if (order.getTotalPrice() != 0) {
                    System.out.println("cart is add");
                    emptyCart = false;
                }

                orderButton.setVisible(!emptyCart);
                emptyLabel.setVisible(emptyCart);


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String formattedDate = dateFormat.format(order.getOrderDate());
                addedToCart = true;
            } else {
                showAlert("Invalid Quantity", "Quantity more than available stock.");
            }
        } else {
            showAlert("Invalid Quantity", "Please enter a valid positive integer for quantity.");
        }

        if (addedToCart) {
            addToCartButton.setDisable(true);
        }
    }

    private HBox createCartItem(Book book, String quantityText, Button addToCartButton) {
        Label cartItemLabel = new Label("Added to Cart " + "\nTitle:" + book.getTitle() + "\nPrice:"
                + book.getSellingPrice() +
                "\nQuantity: " + quantityText + "\n-----------------------------------");

        Button deleteButton = new Button("Remove from cart");
        deleteButton.setStyle("-fx-background-color: #ff3333");
        deleteButton.setStyle("-fx-background-radius: 6");

        HBox cartItemBox = new HBox(10);
        cartItemBox.getChildren().addAll(cartItemLabel, deleteButton);
        deleteButton.setAlignment(Pos.BOTTOM_RIGHT);
        cartItemBox.setAlignment(Pos.CENTER_LEFT);

        deleteButton.setOnAction(e -> handleDeleteFromCart(cartItemBox, addToCartButton, book, quantityText));

        return cartItemBox;
    }

    private void handleDeleteFromCart(HBox cartItemBox, Button addToCartButton, Book book, String quantityText) {
        cartVBox.getChildren().remove(cartItemBox);

        double removedItemPrice = book.getSellingPrice() * Integer.parseInt(quantityText);

        total -= removedItemPrice;
        isbnListt.remove(book.getISBN());
        quantityListt.remove(quantityText);
        order.setTotalPrice(total);

        if(order.getTotalPrice()==0) {
            System.out.println("cart is empt");
            emptyCart = true;
        }
orderButton.setVisible(!emptyCart);
        emptyLabel.setVisible(emptyCart);

        addToCartButton.setDisable(false);
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
