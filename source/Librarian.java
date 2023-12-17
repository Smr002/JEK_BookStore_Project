package source;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.text.ParseException;

public class Librarian extends User {

    public Librarian(String username, String password) {
        super(username, password);
    }

    public static void getBooks() {
        try {
            Book book = new Book();
            List<Book> booksList = book.readBook();

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
        } catch (ParseException e) {
            e.printStackTrace();
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

    public void createBill() throws ParseException {
        Book book = new Book();
        Librarian librarian = new Librarian("librarian1", "password123");
        List<Book> booksList = book.readBook();
        List<String> requests = librarian.readRequests();

        String filePath = "files/createBill.txt";

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
                            + date + "," + matchingBook.getSellingPrice() * quantity + "," + quantity + "\n");

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

    public void SaveTransaction() {
        // Implement the method logic here
    }
}
