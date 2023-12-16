package source;
import java.text.ParseException;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
                                // Handle the case where no books are available
                                System.out.println("No books available.");
                            } else {
                                Stage booksStage = new Stage();
                                booksStage.setTitle("List of Books");
                    
                                VBox booksLayout = new VBox(10);
                                booksLayout.setAlignment(Pos.CENTER);
                    
                                for (Book b : booksList) {
                                    Label isbnLabel = new Label("ISBN: " + b.getISBN());
                                    Label titleLabel = new Label("Title: " + b.getTitle());
                                    Label categoryLabel = new Label("Category: " + b.getCatogory());
                    
                                    booksLayout.getChildren().addAll(isbnLabel, titleLabel, categoryLabel, new Separator());
                                }
                    
                                Scene booksScene = new Scene(booksLayout, 400, 300);
                                booksStage.setScene(booksScene);
                                booksStage.show();
                            }
                        } catch (ParseException k) {
                 
                            k.printStackTrace();
                        }
                    };
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
