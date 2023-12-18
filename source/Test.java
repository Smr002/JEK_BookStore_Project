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
        Methods Methods = new Methods();
        List<Book> books = Methods.readBook();

        for (Book b : books) {
            System.out.println("ISBN: " + b.getISBN());
            System.out.println("Title: " + b.getTitle());
            System.out.println("Category: " + b.getCatogory());
            System.out.println("PurchasedDate: " + b.getPurchasedDate());

            System.out.println();
        }

       // requestBook();
       
        System.out.println("The request book are readen");
        Methods.readRequests();
        System.out.println("The create bill is done");
       Methods.createBill();
    }




}
     

