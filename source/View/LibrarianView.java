package source.View;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import source.Controller.Methods;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LibrarianView {

    private static final Map<String, String> ISBNToBookNameMap = new HashMap<>();
    private static final Map<String, Integer> ISBNToAvailableStockMap = new HashMap<>();
    private static final Map<String, String> ISBNToCategoryMap = new HashMap<>();
    private static final Map<String, String> ISBNToAuthorMap = new HashMap<>();



    public static void showLibrarianView(Stage primaryStage) {
        primaryStage.setTitle("Librarian MENU");

        BorderPane layout = new BorderPane();
        Scene scene = new Scene(new VBox(), 800, 700);
        scene.setFill(Color.OLDLACE);


        Button showRequestsButton = new Button("Show Requests");
        layout.setCenter(showRequestsButton);

        showRequestsButton.setOnAction(e -> {
            showRequestsScene(primaryStage);
        });


        Button logoutButton = new Button("Logout");
        layout.setBottom(logoutButton);

        logoutButton.setOnAction(e -> {
            LoginScene.showLoginScene(primaryStage);
        });

        Button showBooksButton = new Button("Show Books");
        layout.setTop(showBooksButton);
        showBooksButton.setOnAction(e -> {
            try {
                Methods.getBooks();
            } catch (ParseException e1) {

                e1.printStackTrace();
            }
        });


        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.show();
    }

    private static void showRequestsScene(Stage primaryStage) {

        Stage requestsStage = new Stage();
        requestsStage.setTitle("Book Requests");

        VBox requestsLayout = new VBox(10);
        Scene requestsScene = new Scene(requestsLayout, 600, 400);

        try {
            BufferedReader reader = new BufferedReader(new FileReader("files/Request.txt"));
            String line;

            ObservableList<HBox> hboxList = FXCollections.observableArrayList();

            while ((line = reader.readLine()) != null) {
                final String requestLine = line;

                Button checkButton = new Button("Check");
                checkButton.setOnAction(e -> handleCheck(requestsStage,requestLine,hboxList));

                TextField textField = new TextField(line);
                textField.setPrefColumnCount(17);
                textField.setEditable(false);

                HBox requestBox = new HBox(10, textField, checkButton);
                hboxList.add(requestBox);
            }
            reader.close();

            requestsLayout.getChildren().addAll(hboxList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestsStage.setScene(requestsScene);
        requestsStage.show();
    }

    private static void handleCheck(Stage requestsStage,String requestDetails,ObservableList<HBox> hboxList)  {
        String[] parts = requestDetails.split(",");
        if (parts.length == 3) {
            String orderID = parts[0];
            String ISBN = parts[1];
            int requestedQuantity = Integer.parseInt(parts[2]);

            String bookName = getBookNameFromISBN(ISBN);

            int availableStock = getAvailableStock(ISBN);

            String category = getCategory(ISBN);

            String author = getAuthor(ISBN);

            if (availableStock >= requestedQuantity) {
                displayConfirmationBox(requestsStage,requestDetails,ISBN,bookName, requestedQuantity, orderID,category,author);
            } else {
                displayErrorBox(bookName,requestedQuantity,availableStock);
                deleteRequest(requestDetails);
                hboxList.removeIf(hbox -> {
                    String line = ((TextField) hbox.getChildren().get(0)).getText();
                    return line.equals(requestDetails);
                });
                showRequestsScene(new Stage());
                requestsStage.close();
            }
        }
    }

    private static String getBookNameFromISBN(String ISBN) {
        //System.out.println("aaaa");
        if (ISBNToBookNameMap.isEmpty()) {
            loadISBNToBookNameMapping();
        }
        return ISBNToBookNameMap.getOrDefault(ISBN, "Not Found");
    }

    private static void loadISBNToBookNameMapping() {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String ISBN = parts[0].trim();
                    String bookName = parts[1].trim();
                    ISBNToBookNameMap.put(ISBN, bookName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getAvailableStock(String ISBN) {

        if (ISBNToAvailableStockMap.isEmpty()) {
            loadISBNToAvailableStockMapping();
        }
        return ISBNToAvailableStockMap.getOrDefault(ISBN, 0);
    }

    private static void loadISBNToAvailableStockMapping() {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ISBN")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String ISBN = parts[0].trim();
                    int availableStock = Integer.parseInt(parts[9].trim());
                    ISBNToAvailableStockMap.put(ISBN, availableStock);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static void displayConfirmationBox(Stage requestsStage,String requestDetails,String ISBN,String bookName, int requestedQuantity, String orderID, String getCategory, String getAuthor) {
        Platform.runLater(() -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("The \"" + bookName + "\" is in stock, create bill?");
            confirmationAlert.setContentText("Order ID: " + orderID + "\nRequested Quantity: " + requestedQuantity);

            ButtonType createBillButton = new ButtonType("Create Bill");
            ButtonType cancelButton = new ButtonType("Cancel");

            confirmationAlert.getButtonTypes().setAll(createBillButton, cancelButton);


            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == createBillButton) {
                    writeBillToFile(ISBN, bookName, getAuthor, getCategory, requestedQuantity);
                    System.out.println("Creating bill for order ID: " + orderID);
                    deleteRequest(requestDetails);
                    showRequestsScene(new Stage());
                    requestsStage.close();
                } else {
                    System.out.println("Canceling order ID: " + orderID);
                }
            });
        });
    }

    private static void displayErrorBox(String bookName,int requestedQuantity, int availableStock) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Error, no stock for the requested book: " + '"' + bookName + '"' + "\n" +
                "Requested stock: " + requestedQuantity+ "\n" + "Available stock: " +availableStock );
        errorAlert.showAndWait();
    }

    private static void deleteRequest(String requestDetails) {

        String filePath = "files/Request.txt";

        try {

            File tempFile = new File("files/temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                if (!currentLine.equals(requestDetails)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }

            writer.close();
            reader.close();

            Files.move(tempFile.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCategory(String ISBN) {
        if (ISBNToCategoryMap.isEmpty()) {
            loadISBNToCategoryMapping();
        }
        return ISBNToCategoryMap.getOrDefault(ISBN, "Unknown");
    }

    private static void loadISBNToCategoryMapping() {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ISBN")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String ISBN = parts[0].trim();
                    String category = parts[2].trim();
                    ISBNToCategoryMap.put(ISBN, category);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAuthor(String ISBN) {
        if (ISBNToAuthorMap.isEmpty()) {
            loadISBNToAuthorMapping();
        }
        return ISBNToAuthorMap.getOrDefault(ISBN, "Unknown");
    }

    private static void loadISBNToAuthorMapping() {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ISBN")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    String ISBN = parts[0].trim();
                    String author = parts[8].trim();
                    ISBNToAuthorMap.put(ISBN, author);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBillToFile(String ISBN, String bookName, String author, String category, int quantity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/createBill.txt", true))) {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            writer.write("ISBN: " + ISBN + ", ");
            writer.write("Book Name: " + bookName + ", ");
            writer.write("Author: " + author + ", ");
            writer.write("Category: " + category + ", ");
            writer.write("Date: " + formattedDateTime + ", ");
            writer.write("Quantity: " + quantity);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void showBooksScene(Stage primaryStage) throws ParseException {
//        Stage booksStage = new Stage();
//        booksStage.setTitle("All Books");
//
//        VBox booksLayout = new VBox(10);
//        Scene booksScene = new Scene(booksLayout, 600, 400);
//
//        //TableView<Map<String, String>> tableView = Methods.getBooks();
//
//        booksStage.setScene(booksScene);
//        //booksLayout.getChildren().addAll(tableView);
//        booksStage.show();
//
//    }
    private static TableView<Map<String, String>> createTableView() {
        TableView<Map<String, String>> tableView = new TableView<>();

        TableColumn<Map<String, String>, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("ISBN")));

        TableColumn<Map<String, String>, String> bookNameColumn = new TableColumn<>("Book Name");
        bookNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Book Name")));

        TableColumn<Map<String, String>, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Category")));

        TableColumn<Map<String, String>, String> supplierColumn = new TableColumn<>("Supplier");
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Supplier")));

        TableColumn<Map<String, String>, String> priceBoughtColumn = new TableColumn<>("Price Bought");
        priceBoughtColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Price Bought")));

        TableColumn<Map<String, String>, String> dateBoughtColumn = new TableColumn<>("Date Bought");
        dateBoughtColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Date Bought")));

        TableColumn<Map<String, String>, String> priceSoldColumn = new TableColumn<>("Price Sold");
        priceSoldColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Price Sold")));

        TableColumn<Map<String, String>, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Price")));

        TableColumn<Map<String, String>, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Author")));

        TableColumn<Map<String, String>, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Quantity")));


        tableView.getColumns().addAll(isbnColumn, bookNameColumn, categoryColumn, supplierColumn, priceBoughtColumn, dateBoughtColumn, priceSoldColumn, priceColumn, authorColumn, quantityColumn);

        ObservableList<Map<String, String>> books = loadBooksFromFile();
        tableView.setItems(books);

        return tableView;
    }

    private static ObservableList<Map<String, String>> loadBooksFromFile() {
        ObservableList<Map<String, String>> books = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    Map<String, String> bookMap = new HashMap<>();
                    bookMap.put("ISBN", parts[0].trim());
                    bookMap.put("Book Name", parts[1].trim());
                    bookMap.put("Category", parts[2].trim());
                    bookMap.put("Supplier", parts[3].trim());
                    bookMap.put("Price Bought", parts[4].trim());
                    bookMap.put("Date Bought", parts[5].trim());
                    bookMap.put("Price Sold", parts[6].trim());
                    bookMap.put("Price", parts[7].trim());
                    bookMap.put("Author", parts[8].trim());
                    bookMap.put("Quantity", parts[9].trim());

                    books.add(bookMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

}