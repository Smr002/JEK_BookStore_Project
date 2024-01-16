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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import source.Controller.Methods;
import source.Model.Book;
import source.Model.Order;
import javafx.animation.SequentialTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirstWindow {
    private final Label cartLabel = new Label("Cart is Empty");
    private final VBox allBooksVBox = new VBox(40);
    private final VBox cartVBox = new VBox(10); // New VBox for cart items
    private final Label totalPriceLabel = new Label("Total Price:");
    boolean addedToCart = false;
    boolean emptyCart = true;//act as a flag, false when sth is added to cart
    Order order = new Order();
    double total = order.getTotalPrice();
    List<String> isbnListt = order.getIsbnList();
    List<String> quantityListt = order.getQuantityList();
    Date order_date = order.getOrderDate();
    private ScrollPane cartScrollPane;
    private Scene orderConfirmationScene;
    private Button addToCartButton;
    private Button orderButton;
    private ImageView emptyLabel;

    public void showFirstWindow() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JEK-BOOKSTORE");
        primaryStage.setMaximized(true);// e bn stage fullscreen
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #34d5db");//top colour
        Scene scene = new Scene(borderPane, 800, 650);

        Button rightButton = new Button("Login");
        rightButton.setStyle("-fx-background-color: #9fd2f5; -fx-background-radius: 15;-fx-border-radius:15; -fx-border-color:darkblue; -fx-border-width: 2px; -fx-text-fill: #ffffff; -fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-font-weight: bold;");
        rightButton.setOnAction(e -> {
            LoginScene.showLoginScene(primaryStage);
        });
//topHBox at the top
        //topVbox has logoHbox and buttonsHbox
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(10, 10, 10, 10));

        // Wrap logoHBox and buttonsHBox in a VBox
        VBox topVBox = new VBox();

        HBox logoHBox = new HBox();
        createTopLabel(logoHBox);
//

        HBox buttonsHBox = new HBox();
        buttonsHBox.setPrefWidth(1800);

        topVBox.getChildren().addAll(logoHBox, buttonsHBox);
        topHBox.getChildren().add(topVBox);



        borderPane.setTop(topHBox);

        TextField searchBar = new TextField();
        Button searchButton = new Button();

        ChoiceBox<String> searchByBox = new ChoiceBox<>(FXCollections.observableArrayList("Title", "Author", "Isbn"));


        Label searchLabel = new Label("Search By: ");
        HBox searchBox = new HBox(searchLabel, searchByBox, searchBar, searchButton);
        searchBox.setSpacing(5);

        searchLabel.setStyle("-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-text-fill: darkblue; " + "-fx-padding: 2px; " + "-fx-border-width: 2px; " + "-fx-border-radius: 5px; " + "-fx-background-radius: 5px; " + "-fx-alignment: CENTER;");
        searchByBox.setStyle("-fx-font-size: 13px; " + "-fx-font-weight: bold; " + "-fx-text-fill: darkblue; " + "-fx-padding: 0.7px; " + "-fx-background-color: #F0F8FF; " + "-fx-border-radius: 10px; " + "-fx-background-radius: 30px; " + "-fx-alignment: CENTER;");
        searchBar.setStyle("-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-text-fill: darkblue; " + "-fx-padding: 3px; " + "-fx-background-color: #F0F8FF; " + "-fx-border-radius: 5px; " + "-fx-background-radius: 10px; " + "-fx-alignment: CENTER;");
        searchButton.setStyle("-fx-graphic: url('images/search-icon.png');" + "-fx-background-size: contain;" + "-fx-background-repeat: no-repeat;" + "-fx-background-position: center; " + "-fx-padding: 4px; " + "-fx-background-color: #F0F8FF; " + "-fx-background-radius: 5px; ");

        buttonsHBox.getChildren().addAll(searchBox, rightButton);


        HBox.setHgrow(searchBox, Priority.ALWAYS);// to create the space between searchBox and login button

        topVBox.setAlignment(Pos.TOP_LEFT);

        searchByBox.setOnAction(e -> {
            String searchBy = searchByBox.getValue();
            searchBar.setPromptText("Enter " + searchBy);
        });
/////////////////////////////////////////////
        //////////////////////////////////////////////

        searchButton.setOnAction(e -> {
            String searchTerm = searchBar.getText();
            String searchBy = searchByBox.getValue();

            if (searchBy == null || searchBy.equals("Search By")) {
                showAlert("Invalid Search", "Please select a search option.");
                return;
            }

            List<Book> searchResults = Methods.searchBooks(searchBy, searchTerm);

            allBooksVBox.getChildren().clear();
//show searched books

            int booksPerRow = 3;
            for (int i = 0; i < searchResults.size(); i += booksPerRow) {
                int endIndex = Math.min(i + booksPerRow, searchResults.size());
                List<Book> rowBooks = searchResults.subList(i, endIndex);

                HBox bookRow = createBookRow(rowBooks);
                bookRow.setAlignment(Pos.BASELINE_LEFT);
                bookRow.setStyle("-fx-background-color: #c9d4d6");
                allBooksVBox.getChildren().add(bookRow);
            }
        });


      ///show all books
        List<Book> books = Methods.readBook();
        int booksPerRow = 3;

        for (int i = 0; i < books.size(); i += booksPerRow) {
            int endIndex = Math.min(i + booksPerRow, books.size());
            List<Book> rowBooks = books.subList(i, endIndex);

            HBox bookRow = createBookRow(rowBooks);

            bookRow.setAlignment(Pos.BASELINE_LEFT);
            bookRow.setStyle("-fx-background-color: #ecf0f1");

            allBooksVBox.getChildren().add(bookRow);
        }
        allBooksVBox.setStyle("-fx-background-color: #ecf0f1");//color of empty space between b. rows

        ScrollPane booksScrollPane = new ScrollPane(allBooksVBox);
        booksScrollPane.setPrefViewportWidth(200);

        cartScrollPane = new ScrollPane();


        borderPane.setCenter(booksScrollPane);
        borderPane.setLeft(cartScrollPane);
        emptyLabel = new ImageView("images/emptycart.png");

        cartScrollPane.setPrefViewportWidth(300);
        cartScrollPane.setContent(cartVBox);
        orderButton = new Button("Proceed to order");
        VBox orderButtonVBox = new VBox(orderButton);
        orderButtonVBox.setVisible(false);//visible only when a book is order
        cartScrollPane.setContent(new VBox(cartVBox, orderButtonVBox, emptyLabel));


        orderButton.setVisible(emptyCart);
        orderButton.setStyle("-fx-background-color: #e74c3c; " + "-fx-background-radius: 8px; " + "-fx-text-fill: #ffffff; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-font-size: 14px; " + "-fx-padding: 8px 16px; " + "-fx-effect: innershadow(gaussian, #b42e1f, 10, 0, 0, 0);");
        orderButton.setOnAction(e -> primaryStage.setScene(Methods.createOrderConfirmationScene(primaryStage, this, total, isbnListt, quantityListt, order_date)));
        primaryStage.setScene(scene);
        primaryStage.show();


        if (order.getTotalPrice() == 0) {
            System.out.println("cart is empty");
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
        HBox bookRow = new HBox(80);

        for (Book book : rowBooks) {
            VBox bookContainer = createBookContainer(book);
            bookContainer.setStyle("-fx-background-color: #e0e5eb");
            bookContainer.setAlignment(Pos.CENTER);
            bookRow.getChildren().add(bookContainer);
        }

        return bookRow;
    }

    private ImageView createBookImageView(Book book, String imagePath) {
        Image bookImage = new Image("file:" + imagePath);
        ImageView bookImageView = new ImageView(bookImage);

        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), bookImageView);//zoom effect
        scaleIn.setToY(1.09);
        scaleIn.setToX(1.09);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(190), bookImageView);
        scaleOut.setToY(1.0);
        scaleOut.setToX(1.0);


        Tooltip tooltip = new Tooltip("Click image for more info!");
        tooltip.setShowDelay(Duration.millis(150));
        Tooltip.install(bookImageView, tooltip);


        bookImageView.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            scaleIn.play();
        });

        bookImageView.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            scaleOut.play();
        });
        bookImageView.setOnMouseClicked(e -> {
            showBookInformationPopup(book);
        });
        return bookImageView;
    }

    private void showBookInformationPopup(Book book) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Book Information: " + book.getTitle());

        Label bookInfo = new Label("Title: " + book.getTitle() + "\n" + "Category: " + book.getCategory() + "\n" + "Author: " + book.getAuthor() + "\n" + "ISBN: " + book.getISBN() + "\n" + "Price: $" + book.getSellingPrice());
        bookInfo.setStyle("-fx-font-family: 'Century'; -fx-font-size: 14px; -fx-font-weight: bold;");


        VBox labelsVBox = new VBox(10);
        labelsVBox.getChildren().addAll(bookInfo);
        labelsVBox.setAlignment(Pos.CENTER_LEFT);


        Image bookImage = new Image(book.getImagePath());
        ImageView imageView = new ImageView(bookImage);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.add(imageView, 0, 0);
        gridPane.add(labelsVBox, 1, 0);
        gridPane.setStyle("-fx-background-color: #f4f4f4;");

        Scene scene = new Scene(gridPane, 500, 300);

        popupStage.setScene(scene);
        popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);//****

        popupStage.showAndWait();
    }


    private VBox createBookContainer(Book book) {
        ImageView bookImageView = createBookImageView(book, book.getImagePath());

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-graphic: url('images/cart-icon.png');" + "-fx-background-size: contain;" + "-fx-background-repeat: no-repeat;" + "-fx-background-position: center;" + "-fx-background-radius: 6");
        addToCartButton.setUserData(book);

        Text titleText = new Text(book.getTitle());
        titleText.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        titleText.setWrappingWidth(230);//when title is too long its splited in rows

        Label titleLabel = new Label();
        titleLabel.setGraphic(titleText);
        titleLabel.setAlignment(Pos.CENTER);

        Label infoLabel = new Label("ISBN: " + book.getISBN() + "\nPrice: $" + book.getSellingPrice());
        infoLabel.setStyle("-fx-font-family: Georgia; -fx-font-weight: bold; -fx-font-size: 19; ");


        TextField quantityTextField = new TextField();
        quantityTextField.setPromptText("Quantity");
        HBox quantityAndButtonBox = new HBox(10);
        quantityAndButtonBox.getChildren().addAll(quantityTextField, addToCartButton);

        VBox bookContainer = new VBox(10);
        bookContainer.getChildren().addAll(bookImageView, titleLabel, infoLabel, quantityAndButtonBox);
        bookContainer.setAlignment(Pos.CENTER_LEFT);

        addToCartButton.setOnAction(e -> handleAddToCart(book, quantityTextField, addToCartButton));

        return bookContainer;
    }

    private void handleAddToCart(Book book, TextField quantityTextField, Button addToCartButton) {
        String quantityText = quantityTextField.getText();

        if (isValidQuantity(quantityText)) {
            if (Integer.parseInt(quantityText) <= book.getStock()) {
                HBox cartItemBox = createCartItem(book, quantityText, addToCartButton);
                cartVBox.getChildren().add(cartItemBox);

                VBox orderButtonVBox = (VBox) ((VBox) cartScrollPane.getContent()).getChildren().get(1);

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
                    System.out.println("item added to cart");
                    emptyCart = false;
                }

                orderButton.setVisible(!emptyCart);
                emptyLabel.setVisible(emptyCart);


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String formattedDate = dateFormat.format(order.getOrderDate());
                addedToCart = true;
                if (addedToCart) {
                    addToCartButton.setDisable(true);
                }
            } else {
                showAlert("Invalid Quantity", "Quantity more than available stock.");
            }
        } else {
            showAlert("Invalid Quantity", "Please enter a valid positive integer for quantity.");
        }


    }
    private void handleDeleteFromCart(HBox cartItemBox, Button addToCartButton, Book book, String quantityText) {
        cartVBox.getChildren().remove(cartItemBox);

        double removedItemPrice = book.getSellingPrice() * Integer.parseInt(quantityText);

        total -= removedItemPrice;
        isbnListt.remove(book.getISBN());
        quantityListt.remove(quantityText);
        order.setTotalPrice(total);

        if (order.getTotalPrice() == 0) {
            System.out.println("cart is empty");
            emptyCart = true;
        }
        orderButton.setVisible(!emptyCart);
        emptyLabel.setVisible(emptyCart);

        addToCartButton.setDisable(false);
    }

    private HBox createCartItem(Book book, String quantityText, Button addToCartButton) {
        Label cartItemLabel = new Label("   Added to Cart " + "\n   Title:" + book.getTitle() + "\n   Price:" + book.getSellingPrice() + "\n   Quantity: " + quantityText + "\n");

        Button deleteButton = new Button();
        deleteButton.setStyle("-fx-background-color: red;" + "-fx-background-radius: 6;" + "-fx-graphic: url('images/delete-icon.png');" + "-fx-background-size: contain;" + "-fx-background-repeat: no-repeat;" + "-fx-background-position: center;" + "-fx-border-radius: 6;" + "-fx-border-color:black;");

        HBox cartItemBox = new HBox(10);
        HBox.setHgrow(cartItemLabel, Priority.ALWAYS);

        // to push the deleteButton to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        cartItemBox.getChildren().addAll(cartItemLabel, spacer, deleteButton);
        cartItemBox.setAlignment(Pos.TOP_LEFT);
        cartItemBox.setMaxWidth(300);
        cartItemBox.setStyle("-fx-background-color: #ffdddd; -fx-background-radius: 13; -fx-border-radius: 17; -fx-border-color: #ff9999; -fx-border-width: 2px; -fx-font-family: 'Cursive', cursive, sans-serif; -fx-fill: #cc0000;");

        deleteButton.setOnAction(e -> handleDeleteFromCart(cartItemBox, addToCartButton, book, quantityText));

        return cartItemBox;
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
