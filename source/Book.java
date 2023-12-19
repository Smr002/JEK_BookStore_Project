package source;
import java.util.Date;


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
    private double stock;

    public Book(String ISBN, String title, String Catogory, String supplier,
            double PurchasedPrice, Date PurchasedDate, double OriginalPrice, double SellingPrice, String author,double stock) {
        this.ISBN = ISBN;
        this.title = title;
        this.Catogory = Catogory;
        this.supplier = supplier;
        this.PurchasedPrice = PurchasedPrice;
        this.PurchasedDate = PurchasedDate;
        this.OriginalPrice = OriginalPrice;
        this.SellingPrice = SellingPrice;
        this.author = author;
        this.stock = stock;
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
    
    public double getStock() {
        return stock;
    }
    public void setStock(double stock) {
        this.stock = stock;
    }
  


}