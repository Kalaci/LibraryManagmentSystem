package Views;

import Controllers.LibrarianController;
import Controllers.ManagerController;
import Controllers.UserController;
import CustomExceptions.EmptyTextFieldException;
import Model.Librarian;
import Model.Manager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddUserView {
    ManagerController managerController;
    UserController userController;
    LibrarianController librarianController;

    public AddUserView(UserController userController, ManagerController managerController, LibrarianController librarianController){
        this.userController = userController;
        this.managerController = managerController;
        this.librarianController = librarianController;
    }

    public void show(){  GridPane grid = new GridPane();
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        BorderPane borderPane = new BorderPane();

        Label name = new Label("Name");
        TextField nameTF = new TextField();
        Label username = new Label("Username");
        TextField usernameTF = new TextField();
        Label password = new Label("Password");
        TextField passwordTF = new TextField();
        Label phone = new Label("Phone n.o.");
        TextField phoneTF = new TextField();
        Label email = new Label("Email");
        TextField emailTf = new TextField();
        Label salary = new Label("Salary");
        TextField salaryTF = new TextField();
        Label bDay = new Label("Birthday");
        TextField bDayTF = new TextField();
        Label access = new Label("Access Type");
        RadioButton librarianRB = new RadioButton("Librarian");
        RadioButton managerRB = new RadioButton("Manager");
        ToggleGroup RBGroup = new ToggleGroup();
        librarianRB.setToggleGroup(RBGroup);
        managerRB.setToggleGroup(RBGroup);
        HBox RBPane = new HBox();
        RBPane.setSpacing(8);
        RBPane.getChildren().addAll(librarianRB, managerRB);

        grid.addRow(0, name, nameTF);
        grid.addRow(1, username, usernameTF);
        grid.addRow(2, password, passwordTF);
        grid.addRow(3, phone, phoneTF);
        grid.addRow(4, email, emailTf);
        grid.addRow(5, salary, salaryTF);
        grid.addRow(6, bDay, bDayTF);
        grid.addRow(7, access, RBPane);

        grid.setVgap(8);
        grid.setHgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(60, 0, 0, 0));

        Text error = new Text("");

        Button add = new Button("Add");
        add.setPrefSize(80, 10);
        Button back = new Button("Exit");
        back.setPrefSize(80, 10);

        hBox.getChildren().addAll(add, back);
        hBox.setPadding(new Insets(40, 0, 10, 0));
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(grid, hBox, error);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        borderPane.setCenter(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER);


        Scene scene = new Scene(borderPane, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        Alert a = new Alert(Alert.AlertType.ERROR);

        back.setOnMouseClicked(e -> {
            stage.close();

        });

        add.setOnMouseClicked(e -> {
            try {
                if (nameTF.getText().isEmpty() || usernameTF.getText().isEmpty() || passwordTF.getText().isEmpty() || emailTf.getText().isEmpty() || phoneTF.getText().isEmpty() || salaryTF.getText().isEmpty() || bDayTF.getText().isEmpty() || (librarianRB.isSelected() && managerRB.isSelected())) {

                    a.setContentText("Fill all fields");
                    a.show();
                    error.setText("Fill all fields.");
                    throw new EmptyTextFieldException();

                }
            } catch (EmptyTextFieldException ex) {
                throw new RuntimeException(ex);
            }
            String nameS = nameTF.getText();
            String usernameS = usernameTF.getText();
            String passwordS = passwordTF.getText();
            String phoneS = phoneTF.getText();
            String emailS = (String) emailTf.getText();
            double salaryS = Double.parseDouble(salaryTF.getText());
            String bDayS = bDayTF.getText();

            if (userController.searchForUser(usernameS, passwordS) != null) {
                error.setText("User already existent.");
            }
            else{
                if(librarianRB.isSelected()) {
                    Librarian librarian = new Librarian(usernameS, passwordS, nameS, bDayS, phoneS, emailS, (Double) salaryS, "Librarian");
                    librarian.setSalaryN(salaryS);
                    librarian.setEmailN(emailS);
                    librarianController.addLibrarian(librarian);
                    librarianController.writeLibrarians();
                    userController.addUser(librarian);
                    userController.writeUsers();
                    nameTF.clear();
                    usernameTF.clear();
                    passwordTF.clear();
                    phoneTF.clear();
                    emailTf.clear();
                    salaryTF.clear();
                    bDayTF.clear();
                    librarianRB.setSelected(false);
                } else {
                    Manager manager = new Manager(usernameS, passwordS, nameS, bDayS, phoneS, emailS, salaryS, "Manager");
                    manager.setSalaryN(salaryS);
                    manager.setEmailN(emailS);
                    managerController.addManagers(manager);
                    managerController.writeManagers();
                    userController.addUser(manager);
                    userController.writeUsers();
                    nameTF.clear();
                    usernameTF.clear();
                    passwordTF.clear();
                    phoneTF.clear();
                    emailTf.clear();
                    salaryTF.clear();
                    bDayTF.clear();
                    managerRB.setSelected(false);
                }
                userController.writeUsers();
                error.setText("User added.");
            }
        });}

}
