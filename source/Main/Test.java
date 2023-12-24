package source.Main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import source.Controller.Methods;
import source.Model.Book;
import source.Model.Permission;
import source.Model.Users;

public class Test {

    public static void main(String[] args) throws ParseException, IOException {

        Users Librarian = new Users();
        Librarian.addPermission(Permission.CHECK_BOOK);
        Librarian.addPermission(Permission.CREATE_BILL);
        List<Book> books = new ArrayList<>();
        Methods mthd = new Methods();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Adding books
            Book book1 = new Book("978-0132350884", "The Pragmatic Programmer", "Programming",
                    "Bookstore XYZ", 25.0, dateFormat.parse("2022-01-01"), 30.0, 40.0, "Andrew Hunt", 5,
                    "images/pragmatic_programmer.png");

            Book book2 = new Book("978-0596007126", "Clean Code", "Software Development",
                    "Books R Us", 20.0, dateFormat.parse("2022-02-15"), 28.0, 35.0, "Robert C. Martin", 5,
                    "images/clean_code.png");

            Book book3 = new Book("978-0451524935", "1984", "Dystopian Fiction",
                    "Libro World", 15.0, dateFormat.parse("2022-04-20"), 18.0, 25.0, "George Orwell", 4,
                    "images/1984.png");

            Book book4 = new Book("978-0061120084", "To Kill a Mockingbird", "Classic Fiction",
                    "Page Turner Books", 12.0, dateFormat.parse("2022-05-05"), 15.0, 20.0, "Harper Lee", 5,
                    "images/To_Kill_A_Mockingbird.jpeg");
            Book book5 = new Book("978-1234567890", "The Divine Comedy", "Italian",
                    "Adrion Ltd", 30.0, dateFormat.parse("2022-11-29"), 40.0, 45.0, "Dante Aligheri", 6,
                    "images/dante_book.png");
            Book book6 = new Book("978-1212345674", "Don Kishoti", "Art",
                    "Libra.com", 12.0, dateFormat.parse("2023-01-20"), 15.0, 20.0, "Servantes", 3,
                    "images/dante_book.png");
            Book book7 = new Book("978-4356723451", "Me Ty Pa ty", "Shqip",
                    "Adrioni", 9.0, dateFormat.parse("2022-12-4"), 12.0, 16.0, "OSHO", 8, "images/Me_ty_Pa_ty.png");
            Book book8 = new Book("978-4351409439", "Shoku Zylo", "Shqip",
                    "Shtepia e Librit", 3.0, dateFormat.parse("2022-06-10"), 5.0, 10.0, "Dritero Agolli", 5,
                    "images/Shoku_Zylo.png");
            Book book9 = new Book("978-9808616490", "Kodi i DaVincit", "Novel",
                    "Book R Us", 21.0, dateFormat.parse("2022-04-26"), 25.0, 30.0, "Dan Brown", 5,
                    "images/Kodi_i_Da_Vicit.png");
            Book book10 = new Book("978-123098127", "Kronike ne Gure", "Shqip",
                    "Adrion", 5.0, dateFormat.parse("2022-12-2"), 7.0, 10.0, "Ismail Kadare", 10,
                    "images/Kronika_ne_gure.png");
            // Adding books to the list
            books.add(book1);
            books.add(book2);
            books.add(book3);
            books.add(book4);
            books.add(book5);
            books.add(book6);
            books.add(book7);
            books.add(book8);
            books.add(book9);
            books.add(book10);

            // Saving books to file
            mthd.saveBooksToFile(books);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Book> retrievedBooks = mthd.readBook();
        for (Book book : retrievedBooks) {
            System.out.println("ISBN: " + book.getISBN());
            System.out.println("Title: " + book.getTitle());
            System.out.println("ImagePath: " + book.getImagePath());

            // Add more details as needed
            System.out.println();
        }
    }
}
