package Views;

import Controllers.*;
import Model.Librarian;
import Model.Manager;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;


public class ManageUsersViewLvl3 {
    private UserController userController;
    private AdminController adminController;
    private ManagerController managerController;
    private LibrarianController librarianController;
    private ManageUsersController manageUsersController;



    public ManageUsersViewLvl3(UserController userController, AdminController adminController, ManagerController managerController, LibrarianController librarianController, ManageUsersController manageUsersController){
        this.userController = userController;
        this.adminController = adminController;
        this.managerController = managerController;
        this.librarianController = librarianController;
        this.manageUsersController = manageUsersController;


    }
    public void show() {
        TableView<User> usersTable = new TableView<>();
        ObservableList<User> userData = FXCollections.observableArrayList(userController.getUsers());
        ObservableList<User> filteredData = FXCollections.observableArrayList(userData);
        usersTable.setItems(userData);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by username...");
        Button searchButton = new Button("Search");
        searchButton.setPrefSize(80, 10);

        HBox searchBox = new HBox();
        searchBox.getChildren().addAll(searchField,searchButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(5);

        searchButton.setOnMouseClicked(e -> {
            filteredData.clear();
            String filterTxt = searchField.getText().toLowerCase();
            if (filterTxt == null || filterTxt.isEmpty()) {
                filteredData.addAll(userData);
            } else {
                for (User user : userData) {
                    if (user.getUsername().toLowerCase().contains(filterTxt)) {
                        filteredData.add(user);
                    }
                }
            }
            usersTable.setItems(filteredData);
        });

        usersTable.setEditable(true);

        TableColumn<User, String> username = new TableColumn<>("Username");
        TableColumn<User, String> password = new TableColumn<>("Password");
        TableColumn<User, String> name = new TableColumn<>("Name");
        TableColumn<User, String> birthday = new TableColumn<>("Birthday");
        TableColumn<User, String> phone = new TableColumn<>("Phone");
        TableColumn<User, String> email = new TableColumn<>("Email");
        TableColumn<User, Double> salary = new TableColumn<>("Salary (€)");
        TableColumn<User, String> accessLvl = new TableColumn<>("Access");


        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        birthday.setCellValueFactory(new PropertyValueFactory<User, String>("birthday"));
        phone.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        salary.setCellValueFactory(new PropertyValueFactory<User, Double>("salary"));
        salary.setCellFactory(TextFieldTableCell.<User, Double>forTableColumn(new DoubleStringConverter()));
        salary.setOnEditCommit(e -> {
            User editedUser = e.getTableView().getItems().get(e.getTablePosition().getRow());
            editedUser.setSalary(e.getNewValue());
            manageUsersController.updateUser(editedUser);
        });
        accessLvl.setCellValueFactory(new PropertyValueFactory<User, String>("accessLvl"));

        usersTable.getColumns().addAll(username, name,password,phone, email, salary, birthday, accessLvl);



        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Button delete = new Button("Delete");
        delete.setPrefSize(80, 10);
        Button exit = new Button("Exit");
        exit.setPrefSize(80, 10);

        hBox.getChildren().addAll(exit, delete);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(7, 5, 10, 5));
        vBox.getChildren().addAll(searchBox, usersTable, hBox);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        StackPane stackPane = new StackPane(vBox);

        Scene scene = new Scene(stackPane, 800, 600);
        Stage userManagementView = new Stage();
        usersTable.prefHeightProperty().bind(userManagementView.heightProperty());
        usersTable.prefWidthProperty().bind(userManagementView.widthProperty());
        userManagementView.setScene(scene);
        userManagementView.show();

        exit.setOnMouseClicked(e -> userManagementView.close());

        delete.setOnMouseClicked(e -> {
            manageUsersController.deleteUser(usersTable.getSelectionModel().getSelectedItem());
            usersTable.getItems().remove(usersTable.getSelectionModel().getSelectedItem()); //this will remove the selected row
        });
    }
}
