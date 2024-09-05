package Controllers;

import Model.Librarian;
import Model.User;

import java.io.*;
import java.util.ArrayList;

public class LibrarianController extends UserController{
    private ArrayList<Librarian> librarians;

    public LibrarianController(){
        super();
        librarians = new ArrayList<>();
    }

    public ArrayList<Librarian> getLibrarians() {
        return librarians;
    }

    public void setLibrarians(ArrayList<Librarian> librarians) {
        this.librarians = librarians;
    }

    public void addLibrarian (Librarian librarian){
        librarians.add(librarian);
        writeLibrarians();
    }

    public void removeLibrarian(Librarian librarian) {
        librarians.remove(librarian);
        removeUser(librarian);
        writeLibrarians();
    }

    public void writeLibrarians(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("Librarian.dat"));
            output.writeObject(getLibrarians());
            output.close();
        }
        catch (IOException e){
            System.out.println("Error occurred when writing librarians.");
        }
    }

    public void updateLibrarian(Librarian updatedLibrarian) {
        for (int i = 0; i < librarians.size(); i++) {
            Librarian librarian = librarians.get(i);
            if (librarian.getUsername().equals(updatedLibrarian.getUsername())) {
                librarians.set(i, updatedLibrarian);
                writeLibrarians();
                return;
            }
        }
    }

    public void readLibrarian(){
        File file = new File("Librarian.dat");
        if (!file.exists() || file.length() == 0) {
            System.out.println("Librarian data file is empty or doesn't exist.");
            return;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            setLibrarians((ArrayList<Librarian>) input.readObject());
        } catch (IOException e) {
            System.out.println("Error in librarian data reading: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found during librarian data reading: " + e.getMessage());
        }
    }
}
