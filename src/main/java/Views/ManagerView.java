package Views;

import Controllers.*;
import Model.Admin;
import Model.Librarian;
import Model.Manager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ManagerView {
    private Manager manager;
    private BookController bookController;
    private BillController billController;
    private ManageUsersController manageUsersController;

    public ManagerView(Manager manager, BookController bookController, BillController billController, ManageUsersController manageUsersController) {
        this.manager = manager;
        this.bookController = bookController;
        this.billController = billController;
        this.manageUsersController = manageUsersController;
    }

    public void show(Stage stage) {
        Scene scene = createUserScene("Welcome Manager", createManagerMenu());
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

    private MenuBar createManagerMenu() {
        MenuBar topMenu = new MenuBar();

        Menu home = new Menu("Home");
        MenuItem homeI = new MenuItem("Home Screen");
        MenuItem personalInfo =  new MenuItem("Personal Info");
        home.getItems().addAll(homeI, personalInfo);

        Menu billing = new Menu("Billing");
        MenuItem billingI = new MenuItem("Create Bill");
        billing.getItems().add(billingI);

        Menu books = new Menu("Books");
        MenuItem bookInventory = new MenuItem("Book Inventory");
        MenuItem lowStock = new MenuItem("Low On Stock");
        books.getItems().addAll(bookInventory, lowStock);

        topMenu.getMenus().addAll(home, billing, books);

        personalInfo.setOnAction(a -> personalInfoView(manager));
        billingI.setOnAction(a -> billingView(manager));
        bookInventory.setOnAction(a -> bookInventoryView());
        lowStock.setOnAction(a -> lowBookStockView());

        return topMenu;
    }

    private void billingView(Manager manager) {
        BillingView billingView = new BillingView(bookController, billController);
        billingView.show(manager);
    }

    private void personalInfoView(Manager manager){
        PersonalInfoView personalInfoView = new PersonalInfoView(manager, manageUsersController);
        personalInfoView.show(new Stage());
    }
    private void bookInventoryView(){
        BookInventoryLvl2View bookInventoryLvl2View = new BookInventoryLvl2View(bookController);
        bookInventoryLvl2View.show();
    }
    private void lowBookStockView() {
        LowBookInventoryView lowBookInventoryView = new LowBookInventoryView(bookController);
        lowBookInventoryView.show();
    }
}
