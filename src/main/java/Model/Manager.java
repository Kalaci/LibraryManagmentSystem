package Model;

import javafx.beans.property.SimpleStringProperty;

public class Manager extends User{
    private static final long serialVersionUID = 15000002354458L;

    public Manager(String username, String password, String name, String birthday, String phone, String email, double salary,String accessLvl){
        super(username, password, name, birthday, phone, email, salary,accessLvl);
    }


}

