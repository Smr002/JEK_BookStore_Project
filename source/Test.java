package source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws ParseException, FileNotFoundException {
        Book book = new Book();
        List<Book> books = book.readBook();

        for (Book b : books) {
            System.out.println("ISBN: " + b.getISBN());
            System.out.println("Title: " + b.getTitle());
            System.out.println("Category: " + b.getCatogory());
            System.out.println("PurchasedDate: " + b.getPurchasedDate());

            System.out.println();
        }

       // requestBook();
        Librarian librarian = new Librarian("librarian1","password123");
        System.out.println("The request book are readen");
        librarian.readRequests();
        System.out.println("The create bill is done");
        librarian.createBill();
    }

   public static  void requestBook() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        try {
            Book book = new Book();
            List<Book> booksList = book.readBook();

            System.out.print("Enter your ISBN for the request:");
            String isbnTemp = sc.nextLine();

          
            for (Book b : booksList) {
                if (b.getISBN().equals(isbnTemp)) {
                    String filePath = "files/Request.txt";
                    File file = new File(filePath);

            
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try (PrintWriter output = new PrintWriter(new FileWriter(file, true))) {
                        Random orderRqst = new Random();
                        output.print(orderRqst.nextInt() + ",");
                        output.println(isbnTemp);
                        System.out.println("The request is done succesful");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break; 
                }
                    else{
                    System.out.println("This book doesntr exist");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close(); 
        }
    }


}
     

