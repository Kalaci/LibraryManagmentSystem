package Views;
import Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Controllers.*;

import java.util.Optional;


public class LoginView {
    private UserController userController;
    private BookController bookController;
    private LibrarianController librarianController;
    private ManagerController managerController;
    private AdminController adminController;
    private BillController billController;
    private ManageUsersController manageUsersController;
    private TextField usernameTF;
    private PasswordField passwordF;
    public LoginView(UserController userController, BookController bookController, LibrarianController librarianController, ManagerController managerController, AdminController adminController, BillController billController, ManageUsersController manageUsersController) {
        this.userController = userController;
        this.bookController = bookController;
        this.librarianController = librarianController;
        this.managerController = managerController;
        this.adminController = adminController;
        this.billController = billController;
        this.manageUsersController = manageUsersController;

    }
    Manager man1 = new Manager("Manager", "password", "Manager", "12.12.2006", "0****", "mmanager@testing.com", 750, "Manager");
    Librarian lib1 = new Librarian("Librarian", "password", "Librarian", "12.10.2007", "1****", "librarian@testing.com", 550, "Librarian");
    Admin admin = new Admin("Admin", "Admin", "Admin", "12.10.2004", "5****", "admin@testing.com",900 , "Admin");

    public void show (Stage stage){
        /*userController.addUser(admin);*/
        VBox vbox = new VBox();
        GridPane pane = createLoginForm();
        Label error = new Label(" ");
        error.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(createTitle(), pane, createSignInButton(stage, pane, error), error);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 600, 300);

        stage.setScene(scene);
        stage.show();
    }
    private GridPane createLoginForm () {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(0, 10, 0, 10));
        pane.setHgap(10);
        pane.setVgap(10);

        Label usernameLbl = new Label("Username");
        usernameTF = new TextField();
        Label passwordLbl = new Label("Password");
        passwordF = new PasswordField();

        pane.addColumn(0, usernameLbl, passwordLbl);
        pane.addColumn(1, usernameTF, passwordF);
        return pane;
    }

    private Label createTitle() {
        Label text = new Label("Enter Your Information");
        text.setFont(new Font("Elephant", 20));
        text.setPadding(new Insets(0, 10, 0, 10));
        return text;
    }

    private Button createSignInButton(Stage stage, GridPane pane, Label error) {
        Button signIn = new Button("Sign in");
        signIn.setAlignment(Pos.CENTER);
        signIn.setPadding(new Insets(10, 10, 10, 10));

        signIn.disableProperty().bind(
                usernameTF.textProperty().isEmpty()
                        .or(passwordF.textProperty().isEmpty())
        );

        signIn.setOnMouseClicked(e -> handleLogin(stage, pane, error));
        return signIn;
    }
    private void handleLogin(Stage stage, GridPane pane, Label error) {
        String username = usernameTF.getText();
        String password = passwordF.getText();

        User user = userController.searchForUser(username, password);
        if (user instanceof Admin) {
            AdminView adminView = new AdminView((Admin) user, bookController,billController, userController, manageUsersController, adminController, managerController, librarianController);
            adminView.show(new Stage());
        } else if (user instanceof Manager) {
            ManagerView managerView = new ManagerView((Manager) user, bookController, billController, manageUsersController);
            managerView.show(new Stage());
        } else if (user instanceof Librarian) {
            LibrarianView librarianView = new LibrarianView((Librarian) user, bookController, billController, librarianController, manageUsersController);
            librarianView.show(new Stage());
        } else {
            error.setText("Invalid username or password.");
        }

        checkLowStockWarning().ifPresent(message -> {
            Alert lowStockWarning = new Alert(Alert.AlertType.WARNING);
            lowStockWarning.setContentText(message);
            lowStockWarning.show();
        });
    }

    private Optional<String> checkLowStockWarning() {
        for (Book book : bookController.getBooks()) {
            if (book.getStock() <= 5) {
                return Optional.of("It seems we are running low on some books, please check stock.");
            }
        }
        return Optional.empty();
    }
}
