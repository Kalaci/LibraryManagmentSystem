package Views;

import Controllers.BillController;
import Controllers.BookController;
import Controllers.LibrarianController;
import Controllers.ManageUsersController;
import Model.Admin;
import Model.Book;
import Model.Librarian;
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

public class LibrarianView {
    private Librarian librarian;
    private BookController bookController;
    private BillController billController;
    private LibrarianController librarianController;
    private ManageUsersController manageUsersController;

    public LibrarianView(Librarian librarian,BookController bookController, BillController billController, LibrarianController librarianController, ManageUsersController manageUsersController){
        this.bookController = bookController;
        this.billController = billController;
        this.librarianController = librarianController;
        this.librarian = librarian;
        this.manageUsersController = manageUsersController;
    }

    public void show(Stage stage){
        Scene scene = createUserScene("Welcome Librarian", createLibrarianMenu());
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

    private MenuBar createLibrarianMenu() {
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
        books.getItems().addAll(bookInventory);

        topMenu.getMenus().addAll(home, billing, books);

        billingI.setOnAction(a -> billingView((Librarian) librarian));
        bookInventory.setOnAction(a -> bookInventoryView());
        personalInfo.setOnAction(a -> personalInfoView(librarian));

        return topMenu;
    }

    private void billingView(Librarian librarian) {
        BillingView billingView = new BillingView(bookController, billController);
        billingView.show(librarian);
    }

    private void bookInventoryView(){
        BookInventoryLvl3View bookInventoryLvl3View = new BookInventoryLvl3View(bookController);
        bookInventoryLvl3View.show();
    }

    private void personalInfoView(Librarian librarian){
    PersonalInfoView personalInfoView = new PersonalInfoView(librarian, manageUsersController);
    personalInfoView.show(new Stage());
    }

}
