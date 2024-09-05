package Controllers;

import Model.Book;

import java.io.*;
import java.util.ArrayList;

public class BookController implements Serializable{
    private ArrayList<Book> books;

    public BookController(){
        books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> booksObj) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
        writeBook();
    }

    public void removeBook(Book book) {
        books.remove(book);
        writeBook();
    }

    //method will be used for creating bill
    public Book findBook (String ISBN){
        for(Book i: books){
            if(i.getISBN().equals(ISBN)){
                return i;
            }
        }
        return null;
    }

    //Low stock warning
    public ArrayList<Book> lowStock(){
        ArrayList<Book> list = new ArrayList<>();
        for(Book i : books){
            if(i.getStock()<=5){
                list.add(i);
            }
        }
        return list;
    }

    public void writeBook(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("BookData.dat"));
            output.writeObject(getBooks());
            output.close();
        }
        catch (IOException ex){
            System.out.println("Error occurred when writing books.");
        }
    }

    public void readBook(){
        File file = new File("BookData.dat");
        if (!file.exists() || file.length() == 0) {
            System.out.println("Book data file is empty or doesn't exist.");
            return;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            books = (ArrayList<Book>) input.readObject();
            if (books.isEmpty()) {
                System.out.println("No books found in the data file.");
            } else {
                System.out.println("Books successfully read from file.");
            }
        } catch (IOException ex) {
            System.out.println("Error in book data reading: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found during book data reading: " + e.getMessage());
        }
    }

}
