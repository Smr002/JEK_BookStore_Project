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

    public static void main(String[] args) throws ParseException, IOException {
      
        Users Librarian = new Users();
        Librarian.addPermission(Permission.CHECK_BOOK);
        Librarian.addPermission(Permission.CREATE_BILL);
 
        if(Librarian.hasPermission(Permission.CHECK_BOOK)) {
            for (Book book : source.Methods.readBook()) {
                System.out.println(book.getISBN());
                System.out.println(book.getCategory());
            }
        } else{
            System.out.println("You have not permission");
        }

        /*String username;
        String pass;

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Username");
        username = sc.nextLine();

        System.out.println("Enter the Password");
        pass = sc.nextLine();
 
        User user = new User(username,pass);

        user.Login();
        Methods methd = new Methods();

        methd.saveTransaction(user);  // fq e par kur duhet me u fut tek */
            Scanner sc = new Scanner(System.in);
            Methods methd = new Methods();  
          /*  System.out.println("Enter the isbn");
            String isbn = sc.nextLine();

            methd.createBill(isbn);*/
            System.out.println("Enter the quantity of books");
           int quant = sc.nextInt();
            methd.requestBook(quant);
        
    }




}
     

