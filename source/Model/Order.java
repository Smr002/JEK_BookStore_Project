package source.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private double totalPrice;
    private List<String> isbnList;
    private List<String> quantityList;
    private Date orderDate;



    public Order(String name, String surname, String phone, String email, double totalPrice, List<String> isbnList, List<String> quantityList, Date orderDate) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.totalPrice = totalPrice;
        this.isbnList = isbnList;
        this.isbnList=quantityList;
        if (orderDate == null) {
            this.orderDate = new Date();
        } else {
            this.orderDate = orderDate;
        }
    }

    public Order() {
        // Default constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getIsbnList() {
        if (isbnList == null) {
            isbnList = new ArrayList<>();
        }
        return isbnList;
    }

    public void setIsbnList(List<String> isbnList) {
        this.isbnList = isbnList;
    }
    public List<String> getQuantityList() {
        if (quantityList == null) {
            quantityList = new ArrayList<>();
        }
        return quantityList;
    }

    public void setQuantityList(List<String> quantityList) {
        this.quantityList = quantityList;
    }
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
