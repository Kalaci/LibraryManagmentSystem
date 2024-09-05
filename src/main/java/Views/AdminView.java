package Views;
import Controllers.*;
import Model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminView {
    private Admin admin;
    private BookController bookController;
    private BillController billController;
    private UserController userController;
    private ManageUsersController manageUsersController;
    private AdminController adminController;
    private ManagerController managerController;
    private LibrarianController librarianController;


    public AdminView(Admin admin, BookController bookController, BillController billController, UserController userController, ManageUsersController manageUsersController, AdminController adminController, ManagerController managerController, LibrarianController librarianController) {
        this.admin = admin;
        this.bookController = bookController;
        this.billController = billController;
        this.userController = userController;
        this.manageUsersController = manageUsersController;
        this.adminController = adminController;
        this.managerController = managerController;
        this.librarianController = librarianController;
    }

    public void show(Stage stage) {
        Scene scene = createUserScene("Welcome Admin", createAdminMenu());
        stage.setScene(scene);
        stage.show();
    }

    private Scene createUserScene(String welcomeMessage, MenuBar menuBar) {
        StackPane paneLib = new StackPane();
        BorderPane borderPane = new BorderPane();

        Label welcometxt = new Label(welcomeMessage);
        welcometxt.setFont(new Font("Elephant", 20));
        welcometxt.setAlignment(Pos.CENTER);
        borderPane.setCenter(welcometxt);

        borderPane.setTop(menuBar);
        paneLib.getChildren().add(borderPane);
        Scene mainLibSc = new Scene(paneLib, 800, 600);
        menuBar.prefWidthProperty().bind(mainLibSc.widthProperty());

        return mainLibSc;
    }

    private MenuBar createAdminMenu() {
        MenuBar topMenu = new MenuBar();

        Menu home = new Menu("Home");
        MenuItem homeI = new MenuItem("Home Screen");
        home.getItems().add(homeI);

        Menu billing = new Menu("Billing");
        MenuItem billingI = new MenuItem("Create Bill");
        billing.getItems().add(billingI);

        Menu books = new Menu("Books");
        MenuItem bookInventory = new MenuItem("Book Inventory");
        MenuItem lowStock = new MenuItem("Low On Stock");
        books.getItems().addAll(bookInventory, lowStock);

        Menu users = new Menu("Users");
        MenuItem manageUsers = new MenuItem("Manage Users");
        MenuItem addUsers = new MenuItem("Add users");
        users.getItems().addAll(manageUsers, addUsers);

        Menu statistics = new Menu("Stats");
        MenuItem expenses = new MenuItem("Monthly Expenses");
        statistics.getItems().addAll(expenses);

        topMenu.getMenus().addAll(home, billing, books, users, statistics);

        billingI.setOnAction(a -> billingView(admin));
        bookInventory.setOnAction(a -> bookInventoryView());
        lowStock.setOnAction(a -> lowBookStockView());
        manageUsers.setOnAction(a -> manageUsersViewAdmin());
        addUsers.setOnAction(a -> addUserView());
        expenses.setOnAction(a -> monthlyExpensesView());

        return topMenu;
    }

    private void billingView(Admin admin) {
        BillingView billingView = new BillingView(bookController, billController);
        billingView.show(admin);
    }
    private void bookInventoryView(){
        BookInventoryLvl2View bookInventoryLvl2View = new BookInventoryLvl2View(bookController);
        bookInventoryLvl2View.show();
    }
    private void lowBookStockView() {
        LowBookInventoryView lowBookInventoryView = new LowBookInventoryView(bookController);
        lowBookInventoryView.show();
    }

    private void manageUsersViewAdmin() {
        ManageUsersViewLvl3 manageUsersViewLvl3 = new ManageUsersViewLvl3(userController, adminController, managerController, librarianController, manageUsersController);
        manageUsersViewLvl3.show();
    }

    private void addUserView() {
    AddUserView addUserView = new AddUserView(userController, managerController, librarianController);
    addUserView.show();
    }

    private void monthlyExpensesView(){
        MonthlyExpensesView monthlyExpensesView = new MonthlyExpensesView(billController);
        monthlyExpensesView.show();
    }
}
