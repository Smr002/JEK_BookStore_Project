package source;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException, IOException {

        Users Librarian = new Users();
        Librarian.addPermission(Permission.CHECK_BOOK);
        Librarian.addPermission(Permission.CREATE_BILL);
       List <Book> books = new ArrayList<>();
        Methods mthd = new Methods();

         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Adding books
            Book book1 = new Book("978-0132350884", "The Pragmatic Programmer", "Programming",
                    "Bookstore XYZ", 25.0, dateFormat.parse("2022-01-01"), 30.0, 40.0, "Andrew Hunt", 5);

            Book book2 = new Book("978-0596007126", "Clean Code", "Software Development",
                    "Books R Us", 20.0, dateFormat.parse("2022-02-15"), 28.0, 35.0, "Robert C. Martin", 5);

            Book book3 = new Book("978-0451524935", "1984", "Dystopian Fiction",
                    "Libro World", 15.0, dateFormat.parse("2022-04-20"), 18.0, 25.0, "George Orwell", 4);

            Book book4 = new Book("978-0061120084", "To Kill a Mockingbird", "Classic Fiction",
                    "Page Turner Books", 12.0, dateFormat.parse("2022-05-05"), 15.0, 20.0, "Harper Lee", 5);

            // Adding books to the list
            books.add(book1);
            books.add(book2);
            books.add(book3);
            books.add(book4);

            // Saving books to file
            mthd.saveBooksToFile(books);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Book> retrievedBooks = mthd.readBook();
        for (Book book : retrievedBooks) {
            System.out.println("ISBN: " + book.getISBN());
            System.out.println("Title: " + book.getTitle());
            
            // Add more details as needed
            System.out.println();
        }
    }
}

