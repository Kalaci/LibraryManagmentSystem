package Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import Controllers.BookController;
import javafx.beans.property.SimpleStringProperty;

public class Bill implements Serializable {
    private static final long serialVersionUID = 355L;
    private static int nextOrderID = 100000;
    private transient SimpleDoubleProperty total;
    private transient SimpleStringProperty date, orderID;

    private String orderID1, date1;
    private double total1;
    private ArrayList<Book> booksInBill;
    private ArrayList<Integer> quantity;

    public Bill() {
        this.orderID1 = String.valueOf(nextOrderID++);
        this.date1 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.booksInBill = new ArrayList<>();
        this.quantity = new ArrayList<>();
        this.total1 = 0.0;

        this.orderID = new SimpleStringProperty(this.orderID1);
        this.total = new SimpleDoubleProperty(this.total1);
        this.date = new SimpleStringProperty(this.date1);
    }

    public ArrayList<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(ArrayList<Integer> quantity) {
        this.quantity = quantity;
    }

    public String getOrderID1() {
        return orderID1;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public ArrayList<Book> getBooksInBill() {
        return booksInBill;
    }

    public void setBooks(ArrayList<Book> booksInBill) {
        this.booksInBill = booksInBill;
    }

    public double getTotal1() {
        return total1;
    }

    public void setTotal1(double total1) {
        this.total1 = total1;
        if (this.total != null) {
            this.total.set(total1);
        }
    }
    public double getTotal() {
        return total != null ? total.get() : total1;
    }
    public void setTotal(double total) {
        this.total.set(total);
        this.total1 = total;
    }

    public String getDate() {
        return date != null ? date.get() : date1;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getOrderID() {
        return orderID != null ? orderID.get() : orderID1;
    }

    public void setOrderID(String orderID) {
        this.orderID.set(orderID);
    }

    public void addBookToBill(Book book, int quantity, BookController bookController) {
        booksInBill.add(book);
        this.quantity.add(quantity);
        double price = book.getSellingPrice();
        double newTotal = getTotal() + (price * quantity);
        setTotal(newTotal);
    }
}
