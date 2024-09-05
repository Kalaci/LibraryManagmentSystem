package Controllers;

import Model.Manager;
import Model.User;

import java.io.*;
import java.util.ArrayList;

public class ManagerController extends UserController{

    private ArrayList<Manager> managers;

    public ManagerController() {
        super();
        managers = new ArrayList<>();
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }
    public void setManagers(ArrayList<Manager> managers) {
        this.managers = managers;
    }

    public void addManagers (Manager manager){
        managers.add(manager);
        addUser(manager);
        writeManagers();
    }

    public void removeManager(Manager manager) {
        managers.remove(manager);
        removeUser(manager);
        writeManagers();
    }

    public void updateManager(Manager updatedManager) {
        for (int i = 0; i < managers.size(); i++) {
            Manager manager = managers.get(i);
            if (manager.getUsername().equals(updatedManager.getUsername())) {
                managers.set(i, updatedManager);
                writeManagers();
                return;
            }
        }
    }

    public void writeManagers(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("Manager.dat"));
            output.writeObject(getManagers());
            output.close();
        }
        catch (IOException e){
            System.out.println("Error occurred when writing managers.");
        }
    }

    public void readManagers(){

        File file = new File("Manager.dat");
        if (!file.exists() || file.length() == 0) {
            System.out.println("Manager data file is empty or doesn't exist.");
            return;
        }
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            setManagers((ArrayList<Manager>) input.readObject());
        }catch (IOException e){
            System.out.println("Error in manager data reading");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
