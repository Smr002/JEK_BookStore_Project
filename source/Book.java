package source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Book {
    private String ISBN;
    private String title;
    private String Catogory;
    private String supplier;
    private double PurchasedPrice;
    private Date PurchasedDate;
    private double OriginalPrice;
    private double SellingPrice;
    private String author;

    public Book(String ISBN, String title, String Catogory, String supplier,
            double PurchasedPrice, Date PurchasedDate, double OriginalPrice, double SellingPrice, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.Catogory = Catogory;
        this.supplier = supplier;
        this.PurchasedPrice = PurchasedPrice;
        this.PurchasedDate = PurchasedDate;
        this.OriginalPrice = OriginalPrice;
        this.SellingPrice = SellingPrice;
        this.author = author;
    }
    public Book(){}

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatogory() {
        return Catogory;
    }

    public void setCatogory(String catogory) {
        Catogory = catogory;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getPurchasedPrice() {
        return PurchasedPrice;
    }

    public void setPurchasedPrice(double purchasedPrice) {
        PurchasedPrice = purchasedPrice;
    }

       public Date getPurchasedDate() {
        return PurchasedDate;
    }

    public void setPurchasedDate(Date purchasedDate) {
        PurchasedDate = purchasedDate;
    }

    public double getOriginalPrice() {
        return OriginalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        OriginalPrice = originalPrice;
    }

    public double getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


  
   public List<Book> readBook() throws ParseException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("files/Books.txt"))) {

            String header = reader.readLine();
            String[] columns = header.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length == columns.length) {
                    String ISBNTemp = values[0].trim();
                    String titleTemp = values[1].trim();
                    String categoryTemp = values[2].trim();
                    String supplierTemp = values[3].trim();
                    double purchasedPriceTemp = Double.parseDouble(values[4].trim());
                    Date purchasedDateTemp = new SimpleDateFormat("yyyy-MM-dd").parse(values[5].trim());
                    double originalPriceTemp = Double.parseDouble(values[6].trim());
                    double sellingPriceTemp = Double.parseDouble(values[7].trim());
                    String authorTemp = values[8].trim();

                    Book book = new Book(ISBNTemp, titleTemp, categoryTemp, supplierTemp,
                            purchasedPriceTemp, purchasedDateTemp, originalPriceTemp, sellingPriceTemp, authorTemp);
                    books.add(book);
                } else {
                    System.err.println("Invalid data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }
}