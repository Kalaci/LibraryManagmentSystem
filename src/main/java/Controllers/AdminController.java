package Controllers;

import Model.Admin;
import Model.User;

import java.io.*;
import java.util.ArrayList;

public class AdminController extends UserController {
    private ArrayList<Admin> admins;

    public AdminController() {
        super();
        admins = new ArrayList<>();
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }
    public void setAdmins(ArrayList<Admin> Admins) {

        this.admins = Admins;
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
        addUser(admin);
        writeAdmins();
    }

    public void removeAdmin(Admin admin) {
        admins.remove(admin);
        removeUser(admin);
        writeAdmins();
    }

    public void updateAdmin(Admin updatedAdmin) {
        for (int i = 0; i < admins.size(); i++) {
            Admin admin = admins.get(i);
            if (admin.getUsername().equals(updatedAdmin.getUsername())) {
                admins.set(i, updatedAdmin);
                writeAdmins();
            }
        }
    }
    public void writeAdmins(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("Admin.dat"));
            output.writeObject(getAdmins());
            output.close();
        }
        catch (IOException e){
            System.out.println("Error occurred when writing Admins.");
        }
    }

    public void readAdmins(){
        try{
            File file = new File("Admin.dat");
            file.createNewFile();
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            setAdmins((ArrayList<Admin>) input.readObject());
        }
        catch (IOException e){
            System.out.println("Error in Admin data reading");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

}
