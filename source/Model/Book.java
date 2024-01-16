package source.Model;
import java.io.Serializable;
import java.util.Date;


public class Book implements Serializable {
    private String ISBN;
    private String title;
    private String Category;
    private String supplier;
    private double PurchasedPrice;
    private Date PurchasedDate;
    private double OriginalPrice;
    private double SellingPrice;
    private String author;
    private int stock;
    private String imagePath;
    private int booksSold;

    public Book(String ISBN, String title, String Category, String supplier,
            double PurchasedPrice, Date PurchasedDate, double OriginalPrice, double SellingPrice, String author,int stock,String imagePath,int booksSold) {
        this.ISBN = ISBN;
        this.title = title;
        this.Category = Category;
        this.supplier = supplier;
        this.PurchasedPrice = PurchasedPrice;
        this.PurchasedDate = PurchasedDate;
        this.OriginalPrice = OriginalPrice;
        this.SellingPrice = SellingPrice;
        this.author = author;
        this.stock = stock;
        this.imagePath=imagePath;
        this.booksSold=0;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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
    
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getImagePath(){
        return imagePath;
    }
 public void setImagePath(String imagePath){
        this.imagePath=imagePath;
 }
    public int getBooksSold() {
        return booksSold;
    }

    public void setBooksSold(int booksSold) {
        this.booksSold = booksSold;
    }



}