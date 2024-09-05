package Model;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Librarian extends User{

    private static final long serialVersionUID = 15000002354455L;
    public Librarian(String username, String password, String name, String birthday, String phone, String email, double salary, String accessLvl){
        super(username, password, name, birthday, phone, email, salary, accessLvl);

    }
}