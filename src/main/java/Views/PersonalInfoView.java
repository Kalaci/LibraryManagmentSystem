package Views;

import Controllers.LibrarianController;
import Controllers.ManageUsersController;
import Controllers.ManagerController;
import Model.Librarian;
import Model.Manager;
import Model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PersonalInfoView {
    private User user;

    private ManageUsersController manageUsersController;

    public PersonalInfoView(User user, ManageUsersController manageUsersController) {
        this.user = user;
        this.manageUsersController = manageUsersController;
    }

    public void show(Stage stage){
        GridPane gridPaneLeft = new GridPane();
        GridPane gridPaneRight = new GridPane();
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
        TextField accessTF = new TextField();
        accessTF.setEditable(false);

        gridPaneLeft.addRow(0, name, nameTF);
        gridPaneLeft.addRow(1, username, usernameTF);
        gridPaneLeft.addRow(2, password, passwordTF);
        gridPaneLeft.addRow(3, phone, phoneTF);
        gridPaneLeft.setHgap(10);
        gridPaneLeft.setVgap(20);

        gridPaneRight.addRow(0, email, emailTf);
        gridPaneRight.addRow(1, salary, salaryTF);
        gridPaneRight.addRow(2, bDay, bDayTF);
        gridPaneRight.addRow(3, access, accessTF);
        gridPaneRight.setHgap(10);
        gridPaneRight.setVgap(20);

        if (user instanceof Librarian) {
            nameTF.setText(user.getName());
            usernameTF.setText(user.getUsername());
            passwordTF.setText(user.getPassword());
            phoneTF.setText(user.getPhone());
            emailTf.setText(user.getEmail());
            salaryTF.setText(String.valueOf(user.getSalary()));
            bDayTF.setText(user.getBirthday());
            accessTF.setText("Librarian");

        } else if (user instanceof Manager) {
            nameTF.setText(user.getName());
            usernameTF.setText(user.getUsername());
            passwordTF.setText(user.getPassword());
            phoneTF.setText(user.getPhoneN());
            emailTf.setText(user.getEmailN());
            salaryTF.setText(String.valueOf((user.getSalaryN())));
            bDayTF.setText(user.getBirthdayN());
            accessTF.setText("Manager");
        }

        HBox hBox = new HBox();
        hBox.setSpacing(60);
        hBox.getChildren().addAll(gridPaneLeft, gridPaneRight);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(40, 0, 0, 0));


        Button saveButton = new Button("Save");
        saveButton.setPrefSize(100,10);
        Button backButton = new Button("Exit");
        backButton.setPrefSize(100,10);
        HBox buttons = new HBox(30, saveButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(90, hBox, buttons);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setTop(vBox);

        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();

        saveButton.setOnMouseClicked(e -> {
            try {
                user.setName(nameTF.getText());
                user.setUsername(usernameTF.getText());
                user.setPassword(passwordTF.getText());

                if (user instanceof Librarian) {
                    (user).setPhone(phoneTF.getText());
                    (user).setEmail(emailTf.getText());
                    (user).setSalary(Double.parseDouble(salaryTF.getText()));
                    (user).setBirthday(bDayTF.getText());
                } else if (user instanceof Manager) {
                    ( user).setPhoneN(phoneTF.getText());
                    (user).setEmailN(emailTf.getText());
                    (user).setSalaryN(Double.parseDouble(salaryTF.getText()));
                    (user).setBirthdayN(bDayTF.getText());
                }

                manageUsersController.updateUser(user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User information updated successfully!");
                alert.show();
                stage.close();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating user information.");
                alert.show();
            }
        });

        backButton.setOnMouseClicked(e -> stage.close());
    }
}

