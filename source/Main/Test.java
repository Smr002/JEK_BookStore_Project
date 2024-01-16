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
                    "Bookstore XYZ", 25.0, dateFormat.parse("2022-01-01"), 30.0, 40.0, "Andrew Hunt", 100,
                    "images/pragmatic_programmer.png",0);

            Book book2 = new Book("978-0596007126", "Clean Code", "Software Development",
                    "Books R Us", 20.0, dateFormat.parse("2022-02-15"), 28.0, 35.0, "Robert C. Martin", 100,
                    "images/clean_code.png",0);

            Book book3 = new Book("978-0451524935", "1984", "Dystopian Fiction",
                    "Libro World", 15.0, dateFormat.parse("2022-04-20"), 18.0, 25.0, "George Orwell", 100,
                    "images/1984.png",0);

            Book book4 = new Book("978-0061120084", "To Kill a Mockingbird", "Classic Fiction",
                    "Page Turner Books", 12.0, dateFormat.parse("2022-05-05"), 15.0, 20.0, "Harper Lee", 100,
                    "images/To_Kill_A_Mockingbird.jpeg",0);
            Book book5 = new Book("978-1234567890", "The Divine Comedy", "Italian",
                    "Adrion Ltd", 30.0, dateFormat.parse("2022-11-29"), 40.0, 45.0, "Dante Aligheri", 100,
                    "images/dante_book.png",0);
            Book book6 = new Book("978-1212345674", "Don Kishoti", "Art",
                    "Libra.com", 12.0, dateFormat.parse("2023-01-20"), 15.0, 20.0, "Servantes", 100,
                    "images/dante_book.png",0);
            Book book7 = new Book("978-4356723451", "Me Ty Pa ty", "Shqip",
                    "Adrioni", 9.0, dateFormat.parse("2022-12-4"), 12.0, 16.0, "OSHO", 100, "images/Me_ty_Pa_ty.png",0);
            Book book8 = new Book("978-4351409439", "Shoku Zylo", "Shqip",
                    "Shtepia e Librit", 3.0, dateFormat.parse("2022-06-10"), 5.0, 10.0, "Dritero Agolli", 100,
                    "images/Shoku_Zylo.png",0);
            Book book9 = new Book("978-9808616490", "Kodi i DaVincit", "Novel",
                    "Book R Us", 21.0, dateFormat.parse("2022-04-26"), 25.0, 30.0, "Dan Brown", 100,
                    "images/Kodi_i_Da_Vicit.png",0);
            Book book10 = new Book("978-123098127", "Kronike ne Gure", "Shqip",
                    "Adrion", 5.0, dateFormat.parse("2022-12-2"), 7.0, 10.0, "Ismail Kadare", 100,
                    "images/Kronika_ne_gure.png",0);
////////////////////////////////
            Book book11 = new Book("978-0140449136", "Crime and Punishment", "Fiction",
                    "Money Bookstore", 18.0, dateFormat.parse("2024-01-14"), 22.0, 28.0, "Fyodor Dostoevsky", 100, "images/crimeandpunishment.png", 0);

            Book book12 = new Book("978-0140449334", "Meditations", "Philosophy",
                    "Books4Sale", 15.0, dateFormat.parse("2024-01-14"), 20.0, 25.0, "Marcus Aurelius", 100, "images/meditions.png", 0);

            Book book13 = new Book("978-0553213690", "Metamorphosis", "Fiction",
                    "Libra me shumice", 10.0, dateFormat.parse("2024-01-14"), 12.0, 18.0, "Franz Kafka", 100, "images/metamorphosis.png", 0);

            Book book14 = new Book("978-0375702242", "The Idiot", "Fiction",
                    "Book", 14.0,dateFormat.parse("2024-01-14"), 16.0, 22.0, "Fyodor Dostoevsky", 100, "images/theidiot.png", 0);

            Book book15 = new Book("978-0374528379", "The Brothers Karamazov", "Fiction",
                    "Adrion", 20.0, dateFormat.parse("2024-01-14"), 25.0, 32.0, "Fyodor Dostoevsky", 100, "images/thebrotherskaramazov.png", 0);

            Book book16 = new Book("978-0747532743", "Harry Potter and the Philosopher's Stone", "Fantasy",
                    "Adrion", 25.0, dateFormat.parse("2024-01-14"), 30.0, 40.0, "J.K. Rowling", 100, "images/harrypoter1.png", 0);

            Book book17 = new Book("978-0747538493", "Harry Potter and the Chamber of Secrets", "Fantasy",
                    "Adrion", 25.0, dateFormat.parse("2024-01-14"), 30.0, 40.0, "J.K. Rowling", 100, "images/harrypoter2.png", 0);

            Book book18 = new Book("978-0747546290", "Harry Potter and the Prisoner of Azkaban", "Fantasy",
                    "Adrion", 25.0,dateFormat.parse("2024-01-14") , 30.0, 40.0, "J.K. Rowling", 100, "images/harrypoter3.png", 0);



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
            books.add(book11);
            books.add(book12);
            books.add(book13);
            books.add(book14);
            books.add(book15);
            books.add(book16);
            books.add(book17);
            books.add(book18);


            // Saving books to file
            mthd.saveBooksToFile(books);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Book> retrievedBooks = mthd.readBook();
        for (Book book : retrievedBooks) {
            System.out.println("ISBN: " + book.getISBN());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Sold: " + book.getBooksSold());

            // Add more details as needed
            System.out.println();
        }
    }
}
