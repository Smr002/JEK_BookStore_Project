package source;
import java.text.ParseException;
import java.util.List;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Librarian extends User{

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

                //add the data to the table
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

    public void CreateBill(){
        int orderId;
        Book book = new Book();


        String filePath = "createBill.txt";
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



      


       

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("");

            System.out.println("Content has been written to the file: " + filePath);
        } catch (IOException e) {
       
            e.printStackTrace();
        }
    


    };
    public void SaveTransacation(){
        //hidhi ne file
    };

}
