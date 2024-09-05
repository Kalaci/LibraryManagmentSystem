package Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.ArrayList;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = 15000002354454L;
    private transient SimpleStringProperty username, password,accessLvl, name, birthday, phone, email;
    private transient SimpleDoubleProperty salary;

    private String usernameN, passwordN, accessLvlN, nameN, birthdayN, phoneN, emailN;
    private double salaryN;

    private ArrayList<Bill> bills;


    public User(String username, String password, String name, String birthday, String phone, String email, double salary, String accessLvl) {
        this.username= new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.name= new SimpleStringProperty(name);
        this.birthday= new SimpleStringProperty(birthday);
        this.phone= new SimpleStringProperty(phone);
        this.email= new SimpleStringProperty(email);
        this.salary= new SimpleDoubleProperty(salary);
        this.accessLvl = new SimpleStringProperty(accessLvl);

        this.usernameN = username;
        this.passwordN = password;
        this.accessLvlN = accessLvl;
        this.nameN = name;
        this.birthdayN = birthday;
        this.phoneN = phone;
        this.emailN = email;
        this.salaryN = salary;

        bills = new ArrayList<>();
    }

    // Add a bill
    public void addBill(Bill bill) {
        bills.add(bill);
    }

    // Getters and Setters
    public ArrayList<Bill> getBills() {
        return bills;
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    public String getAccessLvlN() {
        return accessLvlN;
    }

    public void setAccessLvlN(String accessLvlN) {
        this.accessLvlN = accessLvlN;
    }


    public String getUsernameN() {
        return usernameN;
    }


    public void setUsernameN(String usernameN) {
        this.usernameN = usernameN;
    }

    public String getBirthdayN() {
        return birthdayN;
    }

    public String getNameN() {
        return nameN;
    }

    public void setNameN(String nameN) {
        this.nameN = nameN;
    }

    public String getPhoneN() {
        return phoneN;
    }

    public void setPhoneN(String phoneN) {
        this.phoneN = phoneN;
    }

    public String getEmailN() {
        return emailN;
    }

    public void setEmailN(String emailN) {
        this.emailN = emailN;
    }

    public double getSalaryN() {
        return salaryN;
    }

    public void setSalaryN(double salaryN) {
        this.salaryN = salaryN;
    }

    public void setBirthdayN(String birthdayN) {
        this.birthdayN = birthdayN;
    }

    public String getPasswordN() {
        return passwordN;
    }

    public void setPasswordN(String passwordN) {
        this.passwordN = passwordN;
    }

    public String getName() {
        if (this.name == null) {
            this.name = new SimpleStringProperty(nameN);
        }
        return name.get();
    }

    public void setName(String name) {
        this.nameN = name;
        if (this.name == null) {
            this.name = new SimpleStringProperty(name);
        } else {
            this.name.set(name);
        }
    }

    public String getAccessLvl() {
        if (this.accessLvl == null) {
            this.accessLvl = new SimpleStringProperty(accessLvlN);
        }
        return accessLvl.get();
    }

    public void setAccessLvl(String accessLvl) {
        this.accessLvlN = accessLvl;
        if (this.accessLvl == null) {
            this.accessLvl = new SimpleStringProperty(accessLvl);
        } else {
            this.accessLvl.set(accessLvl);
        }
    }

    public String getUsername() {
        if (this.username == null) {
            this.username = new SimpleStringProperty(usernameN);
        }
        return username.get();
    }

    public void setUsername(String username) {
        this.usernameN = username;
        if (this.username == null) {
            this.username = new SimpleStringProperty(username);
        } else {
            this.username.set(username);
        }
    }

    public String getPassword() {
        if (this.password == null) {
            this.password = new SimpleStringProperty(passwordN);
        }
        return password.get();
    }

    public void setPassword(String password) {
        this.passwordN = password;
        if (this.password == null) {
            this.password = new SimpleStringProperty(password);
        } else {
            this.password.set(password);
        }
    }

    public String getPhone() {
        if (this.phone == null) {
            this.phone = new SimpleStringProperty(phoneN);
        }
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phoneN = phone;
        if (this.phone == null) {
            this.phone = new SimpleStringProperty(phone);
        } else {
            this.phone.set(phone);
        }
    }

    public String getEmail() {
        if (this.email == null) {
            this.email = new SimpleStringProperty(emailN);
        }
        return email.get();
    }

    public void setEmail(String email) {
        this.emailN = email;
        if (this.email == null) {
            this.email = new SimpleStringProperty(email);
        } else {
            this.email.set(email);
        }
    }

    public Double getSalary() {
        if (this.salary == null) {
            this.salary = new SimpleDoubleProperty(salaryN);
        }
        return salary.get();
    }

    public void setSalary(Double salary) {
        this.salaryN = salary;
        if (this.salary == null) {
            this.salary = new SimpleDoubleProperty(salary);
        } else {
            this.salary.set(salary);
        }
    }

    public String getBirthday() {
        if (this.birthday == null) {
            setBirthday(birthdayN);
        }
        return birthday.get();
    }

    public void setBirthday(String birthday) {
        this.birthday = new SimpleStringProperty(birthday);
        birthdayN = birthday;
    }

    public void readBill(){
        try {
            File file = new File("BookDataAdm.dat");
            file.createNewFile();
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            bills = ((ArrayList<Bill>) input.readObject());
            input.close();
        }
        catch (IOException ex){
            System.out.println("Error in bill data reading.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBill(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("BillDataAdm.dat"));
            output.writeObject(getBills());
            output.close();
        }
        catch (IOException ex){
            System.out.println("Error occurred in writing bills.");
        }
    }
}
