package Model;


import javafx.beans.property.SimpleStringProperty;

public class Admin extends User{
    private static final long serialVersionUID = 15000002354457L;
    public Admin(String username, String password, String name, String birthday, String phone, String email, double salary, String accessLvl){
        super(username, password, name, birthday, phone, email, salary, accessLvl);
    }

}

