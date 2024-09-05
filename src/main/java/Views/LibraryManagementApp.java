package Views;
import Controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;


public class LibraryManagementApp extends Application{
    private BookController bookController;
    private LibrarianController librarianController;
    private ManagerController managerController;
    private AdminController adminController;
    private BillController billController;
    private UserController userController;
    private ManageUsersController manageUsersController;


    @Override
    public void start(Stage stage) {
        initializeControllers();
        loadInitialData();

        LoginView loginView = new LoginView(userController, bookController, librarianController, managerController, adminController, billController, manageUsersController);
        loginView.show(stage);
    }

    private void initializeControllers() {
        bookController = new BookController();
        librarianController = new LibrarianController();
        managerController = new ManagerController();
        adminController = new AdminController();
        billController = new BillController();
        userController = new UserController();
        manageUsersController = new ManageUsersController(userController,adminController,managerController,librarianController);
    }

    private void loadInitialData() {
        bookController.readBook();
        librarianController.readLibrarian();
        managerController.readManagers();
        adminController.readAdmins();
        billController.readBill();
        userController.readUsers();
    }

    public static void main(String[] args) {
        launch();
    }
}
