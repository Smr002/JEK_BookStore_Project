package source;

import java.text.ParseException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException {
        Book book = new Book();
        List<Book> books = book.readBook();

        for (Book b : books) {
            System.out.println("ISBN: " + b.getISBN());
            System.out.println("Title: " + b.getTitle());
            System.out.println("Category: " + b.getCatogory());
            System.out.println("PurchasedDate: " + b.getPurchasedDate());    
    
            System.out.println();
        }
    }
}
