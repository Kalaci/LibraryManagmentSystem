package Model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Book implements Serializable {
    private static final long serialVersionUID = 5296705482940410483L;

    private transient SimpleStringProperty ISBN, title, genre, supplier, author;
    private transient DoubleProperty purchasedPrice,sellingPrice;
    private transient SimpleIntegerProperty stock;

    private String ISBN1,title1,genre1,supplier1,author1;
    private Double purchasedPrice1,sellingPrice1;
    private Integer stock1;


    public Book(String ISBN, String title, String genre, String supplier, double purchasedPrice, double sellingPrice, String author, int stock){
        this.ISBN = new SimpleStringProperty(ISBN);
        this.title= new SimpleStringProperty(title);
        this.genre= new SimpleStringProperty(genre);
        this.supplier= new SimpleStringProperty(supplier);
        this.author = new SimpleStringProperty(author);
        this.purchasedPrice = new SimpleDoubleProperty(purchasedPrice);
        this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
        this.stock = new SimpleIntegerProperty(stock);


        ISBN1=ISBN;
        title1=title;
        genre1=genre;
        supplier1=supplier;
        purchasedPrice1=purchasedPrice;
        sellingPrice1=sellingPrice;
        stock1=stock;
        author1=author;
    }

    public void setISBN(String ISBN){
        this.ISBN = new SimpleStringProperty(ISBN);
        ISBN1 = ISBN;
    }
    public void setTitle (String title){
        this.title = new SimpleStringProperty(title);
        title1 = title;
    }
    public void setGenres (String genre){
        this.genre = new SimpleStringProperty(genre);
        genre1 = genre;
    }
    public void setSupplier (String supplier){
        this.supplier = new SimpleStringProperty(supplier);
        supplier1 = supplier;
    }
    public void setPurchasedPrice(double purchasedPrice){
        this.purchasedPrice = new SimpleDoubleProperty(purchasedPrice);
        purchasedPrice1 = purchasedPrice;
    }
    public void setSellingPrice (double sellingPrice){
        this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
        sellingPrice1 = sellingPrice;
    }
    public void setAuthor (String author){
        this.author = new SimpleStringProperty(author);
        author1 = author;
    }
    public void setStock (int stock){
        this.stock = new SimpleIntegerProperty(stock);
        stock1 = stock;
    }

    public String getISBN (){
        if (this.ISBN == null){
            setISBN(ISBN1);
        }
        return ISBN.get();
    }
    public String getTitle (){
        if (this.title == null){
            setTitle(title1);
        }
        return title.get();
    }
    public String getGenres (){
        if (this.genre == null){
            setGenres(genre1);
        }
        return genre.get();
    }
    public StringProperty getGenresP() {
        return this.genre;
    }
    public String getSupplier (){
        if (this.supplier == null){
            setSupplier(supplier1);
        }
        return supplier.get();
    }
    public Double getPurchasedPrice (){
        if (this.purchasedPrice == null){
            setPurchasedPrice(purchasedPrice1);
        }
        return purchasedPrice.get();
    }
    public Double getSellingPrice (){
        if (this.sellingPrice == null){
            setSellingPrice(sellingPrice1);
        }
        return sellingPrice.get();
    }
    public Integer getStock (){
        if (this.stock == null){
            setStock(stock1);
        }
        return stock.get();
    }
    public String getAuthor(){
        if(this.author == null){
            setAuthor(author1);
        }
        return author.get();
    }

    public String getFormattedPurchasedPrice() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY); //this is to set euro as default currency
        return currencyFormat.format(this.purchasedPrice);
    }

    public String getFormattedSellingPrice() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return currencyFormat.format(this.sellingPrice);
    }
}