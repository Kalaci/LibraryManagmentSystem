package Controllers;

import Model.*;

import java.io.*;
import java.util.ArrayList;

public class UserController implements java.io.Serializable {
    private static final long serialVersionUID = 15000002354454L;
    private ArrayList<User> users;

    public UserController() {
        users = new ArrayList<>();
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser (User user){
        users.add(user);
        writeUsers();
    }

    public void removeUser(User user) {
        System.out.println("Removing user: " + user.getUsername());

        users.remove(user);
        writeUsers();
    }
    public User searchForUser(String username, String password) {
        for (User i : users) {
            //System.out.println("Checking user: " + i.getUsernameN());
            if (i.getPasswordN().equals(password) && i.getUsernameN().equals(username)) {
                System.out.println("User found: " + i.getName());
                return i;
            }
        }
        System.out.println("No matching user found for username: " + username);
        return null;
    }



    public void writeUsers(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("Users.dat"));
            output.writeObject(getUsers());
            output.close();
        }
        catch (IOException e){
            System.out.println("Error occurred when writing users.");
        }
    }

    public void readUsers(){
        try {
            File file = new File("Users.dat");
            file.createNewFile();
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            setUsers((ArrayList<User>) input.readObject());
            //System.out.println("Users loaded: " + users.size());
        }
        catch (IOException e){
            System.out.println("Error in Admin data reading");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

}