package Controllers;

import Model.Bill;
import Model.Librarian;

import java.io.*;
import java.util.ArrayList;

public class BillController implements Serializable {

    private ArrayList<Bill> bills;

    public BillController(){
        bills = new ArrayList<>();
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }
    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    public void addBill(Bill bill) {
        bills.add(bill);
        writeBill();
    }

    public void removeBill(Bill bill) {
        bills.remove(bill);
        writeBill();
    }

    public void readBill() {
        File file = new File("BillData.dat");
        if (!file.exists() || file.length() == 0) {
            System.out.println("Bill data file is empty or doesn't exist.");
            return;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            setBills((ArrayList<Bill>) input.readObject());
        } catch (IOException ex) {
            System.out.println("Error in bill data reading: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found during deserialization: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void writeBill(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("BillData.dat"));
            output.writeObject(getBills());
            output.close();
        }
        catch (IOException ex){
            System.out.println("Error occurred in writing bills.");
        }
    }

}
