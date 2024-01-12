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

    public TransactionData(int orderId, String isbn, String date,
            double totalPrice,
            int quantity, String seller) {
        this.orderId = orderId;
        this.isbn = isbn;

        this.date = date;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.seller = seller;
    }

    public TransactionData(int orderId2, String isbn2, String date2, double totalPrice2, String seller2) {
        this.orderId = orderId2;
        this.isbn = isbn2;
        this.date = date2;
        this.totalPrice = totalPrice2;
        this.seller = seller2;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "orderId=" + orderId +
                ", isbn='" + isbn + '\'' +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                ", seller='" + seller + '\'' +
                '}';
    }
}
