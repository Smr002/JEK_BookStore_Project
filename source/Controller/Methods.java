package source.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import source.Model.Book;
import source.Model.User;
import javafx.beans.property.SimpleStringProperty;


public class Methods {

    public void saveBooksToFile(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/Books.dat"))) {
            oos.writeObject(books);
            System.out.println("Books saved to file: Books.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> readBook() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/Books.dat"))) {

            List<Book> books = (List<Book>) ois.readObject();
            System.out.println("Books loaded from file: Books.dat");
            return books;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void getBooks() throws ParseException {
        List<Book> booksList = readBook();

        if (booksList.isEmpty()) {
            System.out.println("No books available.");
        } else {
            Stage booksStage = new Stage();
            booksStage.setTitle("List of Books");

            TableView<Book> table = new TableView<>();

            // isbn column
            TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
            isbnColumn.setMinWidth(100);
            isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

            // title column
            TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
            titleColumn.setMinWidth(200);
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            // category column
            TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
            categoryColumn.setMinWidth(100);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            // sellingPrice column
            TableColumn<Book, Double> priceColumn = new TableColumn<>("Selling Price");
            priceColumn.setMinWidth(100);
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

            // Set the columns to the table
            table.getColumns().addAll(isbnColumn, titleColumn, categoryColumn, priceColumn);

            // add the data to the table
            table.setItems(FXCollections.observableArrayList(booksList));

            VBox booksLayout = new VBox();
            booksLayout.getChildren().add(table);

            Scene booksScene = new Scene(booksLayout, 600, 400);
            booksStage.setScene(booksScene);
            booksStage.show();
        }
    }

    public List<String> readRequests() {

        List<String> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("files/request.txt"))) {
            String header = reader.readLine();
            String[] columns = header.split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                requests.add(line);
                String[] values = line.split(",");
                String orderRqst = values[0].trim();
                String isbnT = values[1].trim();
                System.out.println(orderRqst + " " + isbnT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void saveTransaction(User user) throws ParseException {
        List<Book> booksList = readBook();
        List<String> requests = readRequests();

        String filePath = "files/saveTRansaction.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            Map<String, Integer> isbnQuantityMap = processRequests(requests);

            for (Map.Entry<String, Integer> entry : isbnQuantityMap.entrySet()) {
                String isbn = entry.getKey();
                int quantity = entry.getValue();

                Book matchingBook = findBookByISBN(booksList, isbn);
                if (matchingBook != null) {
                    Date date = new Date();
                    Random orderId = new Random();
                    writer.write(orderId.nextInt() + "," + isbn + "," + matchingBook.getTitle() + ","
                            + matchingBook.getAuthor() + ","
                            + date + "," + matchingBook.getSellingPrice() * quantity + "," + quantity + ","
                            + user.getUsername() + "\n");

                    System.out.println("Content has been written to the file: " + filePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> processRequests(List<String> requests) {
        /*
         * This declares a map where keys are of type String and values are of type
         * Integer.
         * In Java, a Map is a collection that stores key-value pairs. In this case, the
         * keys are ISBNs
         * (which are strings representing book identifiers),
         * and the values are integers representing the quantity of books
         * associated with each ISBN.
         */
        Map<String, Integer> isbnQuantityMap = new HashMap<>();

        for (String request : requests) {
            String[] columns = request.split(",");
            String temp = columns[1].trim();
            isbnQuantityMap.put(temp, isbnQuantityMap.getOrDefault(temp, 0) + 1);
        }

        return isbnQuantityMap;
    }

    private Book findBookByISBN(List<Book> booksList, String isbn) {
        for (Book book : booksList) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public static void requestBook(int quantity) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        try {
            List<Book> booksList = readBook();

            System.out.print("Enter your ISBN for the request:");
            String isbnTemp = sc.nextLine();

            for (Book b : booksList) {
                if (b.getISBN().equals(isbnTemp)) {
                    if (quantity > b.getStock() || quantity <= 0) {
                        System.out.println("Invalid quantity. Available stock: " + b.getStock());
                        return; // Exit without creating request
                    }

                    String filePath = "files/request.txt";
                    try (PrintWriter output = new PrintWriter(new FileWriter(filePath, true))) {
                        Random orderRqst = new Random();
                        output.write(orderRqst.nextInt() + ",");
                        output.write(isbnTemp + ",");
                        output.write(quantity + "\n");
                        System.out.println("The request is done successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return; // Exit the loop after successful request
                }
            }

            System.out.println("Book with ISBN " + isbnTemp + " not found");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    public void createBill(String ISBN, int quantity) throws IOException, ParseException {
        String filePath = "files/createBill.txt";
        String requestsFilePath = "files/request.txt";
        String booksFilePath = "files/Books.txt";

        List<Book> books = readBook();
        Date date = new Date();
        Random orderId = new Random();

        // Read requests
        List<String> requests = readRequests();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true));
                BufferedWriter requestsWriter = new BufferedWriter(new FileWriter(requestsFilePath));
                PrintWriter booksWriter = new PrintWriter(new FileWriter(booksFilePath))) {

            boolean exists = false;
            for (Book b : books) {
                if (ISBN.equals(b.getISBN())) {

                    int newStock = b.getStock() - quantity;
                    b.setStock(newStock);

                    writer.write(orderId.nextInt() + "," + b.getISBN() + "," + b.getTitle() + "," + b.getAuthor() + ","
                            + b.getCategory() + "," + date + "," + quantity + "\n");
                    exists = true;
                    break;
                    // Update stock in Books.txt

                }
            }

            if (!exists) {
                System.out.println("This book doesn't exist");
            }

            // Remove the first instance of the ISBN in requests
            boolean foundAndRemoved = false;
            List<String> updatedRequests = new ArrayList<>();
            for (String request : requests) {
                if (request.contains(ISBN) && !foundAndRemoved) {
                    foundAndRemoved = true;
                } else {
                    updatedRequests.add(request);
                }
            }

            if (!foundAndRemoved) {
                System.out.println("ISBN not found in requests");
            }

            // Rewrite the requests file
            for (String request : updatedRequests) {
                requestsWriter.write(request + "\n");
            }

            // Rewrite the Books.txt file with updated stock
            try (PrintWriter booksWriters = new PrintWriter(new FileWriter(booksFilePath))) {
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    booksWriters.write(book.getISBN() + "," + book.getTitle() + "," + book.getCategory() + ","
                            + book.getSupplier() + "," + book.getPurchasedPrice() + ","
                            + new SimpleDateFormat("yyyy-MM-dd").format(book.getPurchasedDate()) + ","
                            + book.getOriginalPrice() + "," + book.getSellingPrice() + ","
                            + book.getAuthor() + "," + book.getStock() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> filter(TextField startDateField, TextField endDateField, ChoiceBox<String> cb, ChoiceBox<String> cb1) {
        ArrayList<String> filteredTransactions = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader("files/saveTRansaction.txt"))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String dateStr = values[4];
                Date transactionDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    
                // Convert text from TextField to Date objects
                Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse(startDateField.getText());
                Date endDate = new SimpleDateFormat("dd.MM.yyyy").parse(endDateField.getText());
    
                if (transactionDate.after(startDate) && transactionDate.before(endDate)
                        && values[7].equals(cb.getValue())) {
                    if (cb1.getValue().equals("Daily")) {
                        filteredTransactions.add(line);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    
        return filteredTransactions;
    }
    

    public static void Performance(Stage primaryStage, Scene scene) {
        GridPane gridPane = new GridPane();
        Scene scene1 = new Scene(gridPane, 800, 700);
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(25, 25, 25, 25));

        Label label = new Label("Choose librarian and timeframe:");
        gridPane.add(label, 0, 0);
        ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList("librarian1", "Second", "Third"));
        gridPane.add(cb, 0, 1);
        ChoiceBox<String> cb1 = new ChoiceBox<>(FXCollections.observableArrayList("Daily", "Monthly", "Yearly"));
        gridPane.add(cb1, 1, 1);
        Button ok = new Button("OK");
        gridPane.add(ok, 2, 1);

        ok.setOnAction(e -> {
            if (cb.getSelectionModel().isEmpty() || cb1.getSelectionModel().isEmpty()) {
                showAlert("Warning", "Please select both librarian and timeframe.");
            } else {
                buttonOk(primaryStage, scene1, cb, cb1);
            }
        });

        Button back = new Button("Back");
        gridPane.add(back, 1, 2);
        back.setOnAction(e -> primaryStage.setScene(scene));
        primaryStage.setScene(scene1);
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void buttonOk(Stage primaryStage, Scene scene, ChoiceBox<String> cb, ChoiceBox<String> cb1) {
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(25, 25, 25, 25));

        Label startDateLabel = new Label("Start Date:");
        grid.add(startDateLabel, 0, 0);
        TextField startDate = new TextField();
        grid.add(startDate, 1, 0);

        Label endDateLabel = new Label("End Date:");
        grid.add(endDateLabel, 0, 1);
        TextField endDate = new TextField();
       
        grid.add(endDate, 1, 1);

        Button check = new Button("CHECK");
        grid.add(check, 1, 2);
        check.setOnAction(e -> buttonCheck(primaryStage, startDate, endDate, cb, cb1));

        Button back = new Button("Back");
        grid.add(back, 2, 2);
        back.setOnAction(e -> primaryStage.setScene(scene));

        Scene scene2 = new Scene(grid, 800, 700);
        primaryStage.setScene(scene2);
    }

    public static void buttonCheck(Stage primaryStage, TextField startDateField, TextField endDateField,
                                   ChoiceBox<String> cb, ChoiceBox<String> cb1) {
        
            ArrayList<String> filteredTransactions = Methods.filter(startDateField, endDateField, cb, cb1);
            showTransactionTable(primaryStage, filteredTransactions);
        
    }
    private static void showTransactionTable(Stage primaryStage, ArrayList<String> transactions) {
        TableView<String> table = new TableView<>();
    
        TableColumn<String, String> transactionColumn = new TableColumn<>("Transaction");
        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("name")); 
   
        table.getColumns().add(transactionColumn);
    
      
        ObservableList<String> data = FXCollections.observableArrayList(transactions);
        table.setItems(data);
    
        Scene scene3 = new Scene(new VBox(table), 800, 700);
        primaryStage.setScene(scene3);
        primaryStage.show();
    }

}