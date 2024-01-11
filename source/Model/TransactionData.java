package source.Model;

import java.text.SimpleDateFormat;


public class TransactionData {
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    private String isbn;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private double totalPrice;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String seller;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public TransactionData(int orderId, String isbn, String title, String author, String date,
                           double totalPrice,
                           int quantity, String seller) {
        this.orderId = orderId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.date = date;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "orderId=" + orderId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                ", seller='" + seller + '\'' +
                '}';
    }
}
