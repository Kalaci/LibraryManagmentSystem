package Views;

import CustomExceptions.EmptyTextFieldException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Optional;

import Model.*;
import Controllers.* ;

public class HelloViewApp extends Application {

    UserController userController = new UserController();
    LibrarianController librarianController = new LibrarianController();
    ManagerController managerController = new ManagerController();
    AdminController adminController = new AdminController();
    BookController bookController = new BookController();
    BillController billController = new BillController();

    Manager man1 = new Manager("Manager1", "12345678", "Manager", "12.12.2006", "0****", "mmanager@testing.com", 750, "Manager");
    Librarian lib1 = new Librarian("Librarian1", "87654321", "Librarian", "12.10.2007", "1****", "librarian@testing.com", 550, "Librarian");
    Librarian admin = new Librarian("Admin", "Admin", "Admin", "12.10.2004", "5****", "admin@testing.com",900 , "Admin");
    Librarian librarian;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    @Override
    public void start(Stage stage) throws IOException {
        bookController.readBook();
        librarianController.readLibrarian();
        managerController.readManagers();
        adminController.readAdmins();
        billController.readBill();
        userController.readUsers();


        Bill tester = new Bill();

        VBox vbox = new VBox();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(0,10,0,10));
        pane.setHgap(10);
        pane.setVgap(10);

        //all the information fields
        Label usernameLbl = new Label("Username");
        TextField usernameTF = new TextField();
        Label passwordLbl = new Label("Password");
        PasswordField passwordF = new PasswordField();

        //text
        Label text = new Label("Enter Your Information");
        text.setFont(new Font("Elephant",20));
        text.setPadding(new Insets(0,10,0,10));

        //button
        Button signIn = new Button ("Sign in");
        signIn.setAlignment(Pos.CENTER);
        signIn.setPadding(new Insets(10,10,10,10));

        //error label
        Label error = new Label(" ");
        error.setAlignment(Pos.CENTER);

        // Disable sign-in button if fields are empty
        signIn.disableProperty().bind(
                usernameTF.textProperty().isEmpty().or(passwordF.textProperty().isEmpty())
        );

        pane.addColumn(0,usernameLbl,passwordLbl);
        pane.addColumn(1,usernameTF,passwordF);
        vbox.getChildren().addAll(text,pane,signIn,error);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox,600,300);


        stage.setScene(scene);
        stage.show();


        signIn.setOnMouseClicked(e-> {
            String username = usernameTF.getText();
            String password = passwordF.getText();

            User user = userController.searchForUser(username,password);

            if (user instanceof Admin) {
                Stage adminStage = new Stage();
                Scene adminScene = createUserScene("Welcome Admin", createAdminMenu((Admin) user));
                adminStage.setScene(adminScene);
                adminStage.show();
            } else if (user instanceof Manager) {
                Stage managerStage = new Stage();
                Scene managerScene = createUserScene("Welcome Manager", createManagerMenu((Manager) user));
                managerStage.setScene(managerScene);
                managerStage.show();
            } else if (user instanceof Librarian) {
                Stage librarianStage = new Stage();
                Scene librarianScene = createUserScene("Welcome Librarian", createLibrarianMenu((Librarian) user));
                librarianStage.setScene(librarianScene);
                librarianStage.show();
            } else {
                error.setText("Invalid username or password.");
            }

            // Check for low stock warning after login
            checkLowStockWarning().ifPresent(message -> {
                Alert lowStockWarning = new Alert(Alert.AlertType.WARNING);
                lowStockWarning.setContentText(message);
                lowStockWarning.show();
            });
        });
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

    private MenuBar createAdminMenu(Admin admin) {
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

        // Set up action handlers
        billingI.setOnAction(a -> billingView(admin));
        bookInventory.setOnAction(a -> bookInventoryView());
        lowStock.setOnAction(a -> lowBookStockView());
        manageUsers.setOnAction(a -> manageUsersViewAdmin());
        addUsers.setOnAction(a -> addUserView());

        return topMenu;
    }

    private MenuBar createManagerMenu(Manager manager) {
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
        MenuItem manageUsersM = new MenuItem("Manage Users");
        MenuItem addUsers = new MenuItem("Add users");
        users.getItems().addAll(manageUsersM, addUsers);

        topMenu.getMenus().addAll(home, billing, books, users);

        // Set up action handlers
        billingI.setOnAction(a -> billingView(manager));
        bookInventory.setOnAction(a -> bookInventoryView());
        lowStock.setOnAction(a -> lowBookStockView());
        manageUsersM.setOnAction(a -> manageUsersViewLibrarians());
        addUsers.setOnAction(a -> addUserView());

        return topMenu;
    }

    private MenuBar createLibrarianMenu(Librarian librarian) {
        MenuBar topMenu = new MenuBar();

        Menu home = new Menu("Home");
        MenuItem homeI = new MenuItem("Home Screen");
        home.getItems().add(homeI);

        Menu billing = new Menu("Billing");
        MenuItem billingI = new MenuItem("Create Bill");
        billing.getItems().add(billingI);

        topMenu.getMenus().addAll(home, billing);

        // Set up action handlers
        billingI.setOnAction(a -> billingView(librarian));

        return topMenu;
    }

    private Optional<String> checkLowStockWarning() {
        for (Book book : bookController.getBooks()) {
            if (book.getStock() <= 5) {
                return Optional.of("It seems we are running low on some books, please check stock.");
            }
        }
        return Optional.empty();
    }

    public void billingView(User user) {
        Bill newBill = new Bill();

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        VBox vBox = new VBox(10);
        HBox hbox = new HBox(10);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label bookISBN = new Label("ISBN");
        TextField bookISBNTF = new TextField();

        Label bookQuantity = new Label("Quantity");
        TextField bookQuantityTF = new TextField();

        gridPane.addColumn(0, bookISBN, bookQuantity);
        gridPane.addColumn(1, bookISBNTF, bookQuantityTF);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(60, 0, 0, 0));

        Button add = new Button("Add book");
        add.setPrefSize(80, 10);
        Button print = new Button("Print bill");
        print.setPrefSize(80, 10);
        Button exit = new Button("Exit");
        exit.setPrefSize(80, 10);

        Text success = new Text("");

        hbox.getChildren().addAll(add, print, exit);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(40, 0, 0, 0));

        vBox.getChildren().addAll(gridPane, hbox, success);
        vBox.setAlignment(Pos.CENTER);

        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 800, 600);
        Stage billingLibST = new Stage();
        billingLibST.setScene(scene);
        billingLibST.show();

        Alert a = new Alert(Alert.AlertType.ERROR);

        add.setOnMouseClicked(ev -> {
            try {

                if ((bookISBNTF.getText().isEmpty() && bookQuantityTF.getText().isEmpty()) || bookISBNTF.getText().isEmpty() || bookQuantityTF.getText().isEmpty()) {
                    a.setContentText("Fill all fields");
                    a.show();
                    throw new EmptyTextFieldException();

                } else {
                    String ISBN = bookISBNTF.getText();
                    int quantity = Integer.parseInt(bookQuantityTF.getText());

                    if (bookController.findBook(ISBN) == null) {
                        success.setText("Book does not exist.");
                    }

                    Book book = bookController.findBook(ISBN);

                    if (book.getStock() < quantity) {
                        success.setText("Not enough books in stock");
                    } else {
                        newBill.addBookToBill(book, quantity, bookController);

                        book.setStock((book.getStock() - quantity));
                        bookController.writeBook();
                        success.setText("Book added successfully.");
                        bookISBNTF.clear();
                        bookQuantityTF.clear();
                    }

                }
            } catch (EmptyTextFieldException ex) {
                System.out.println(ex.getMessage());
            }

        });

        print.setOnMouseClicked(ev -> {
            File bill;
            user.addBill(newBill);
            billController.writeBill();

            try {
                bill = new File("Bill.txt");

                PrintWriter clear = new PrintWriter(bill);
                clear.print(" ");
                clear.close();

                PrintWriter writer = new PrintWriter(bill);
                writer.write("\t\t\t\t" + newBill.getDate1() + "\n");
                writer.write("\t\t\t\t  " + user.getUsernameN() + "\n\n");
                writer.write("Purchase\t\t Quantity\t\tPrice\n");
                int j = 0;
                for (Book i : newBill.getBooksInBill()) {
                    writer.write(i.getTitle() + "\t\t\t\t\t" + newBill.getQuantity().get(j) + "x\t\t\t" + i.getSellingPrice() + "\n");
                    System.out.println(i.getTitle());
                    j++;
                }
                writer.write("\n");
                writer.write("Total\t\t\t\t\t\t\t" + String.valueOf(newBill.getTotal1()));

                writer.flush();
                writer.close();
                billingLibST.close();

            } catch (IOException exception) {
                System.out.println("Error in bill printing");
            }

        });

        exit.setOnMouseClicked(e -> billingLibST.close());
    }


    public void bookInventoryView() {
        TableView<Book> bookTable = new TableView<>();
        ObservableList<Book> bookData = FXCollections.observableArrayList(bookController.getBooks());

        bookTable.setItems(bookData);
        bookTable.setEditable(true);

        TableColumn<Book, String> ISBN = new TableColumn<>("ISBN");
        TableColumn<Book, String> title = new TableColumn<>("Title");
        TableColumn<Book, String> genreT = new TableColumn<>("Genre");
        TableColumn<Book, String> supplier = new TableColumn<>("Supplier");
        TableColumn<Book, Double> purchasePrice = new TableColumn<>("Purchase Price");
        TableColumn<Book, Double> sellingPrice = new TableColumn<>("Selling Price");
        TableColumn<Book, String> author = new TableColumn<>("Author");
        TableColumn<Book, Integer> stock = new TableColumn<>("Stock");
        TableColumn<Book, String> date = new TableColumn<>("Date");


        ISBN.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
        ISBN.setCellFactory(TextFieldTableCell.forTableColumn()); //reimplements the table cell as text field
        ISBN.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setISBN(e.getNewValue());
            bookController.writeBook();
        }); //assignment of the new values to the corresponding table cell


        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setOnEditCommit(e -> {
            System.out.println((e.getTableView().getItems().get(e.getTablePosition().getRow()).getTitle())); //stock idea implementation, REMOVE THIS LATER!!!
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setTitle(e.getNewValue());
            bookController.writeBook();
        });


        genreT.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
        genreT.setCellFactory(TextFieldTableCell.forTableColumn());
        genreT.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setGenres(e.getNewValue());
            bookController.writeBook();
        });


        purchasePrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("purchasedPrice"));
        purchasePrice.setCellFactory(TextFieldTableCell.<Book, Double>forTableColumn(new DoubleStringConverter()));
        // TextFieldTableCell.forTableColumn() is by default StringConverter thus we use a DoubleStringConverter method
        purchasePrice.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setPurchasedPrice(e.getNewValue());
            bookController.writeBook();
        });


        supplier.setCellValueFactory(new PropertyValueFactory<Book, String>("supplier"));
        supplier.setCellFactory(TextFieldTableCell.forTableColumn());
        supplier.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setSupplier(e.getNewValue());
            bookController.writeBook();
        });


        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        author.setCellFactory(TextFieldTableCell.forTableColumn());
        author.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setAuthor(e.getNewValue());
            bookController.writeBook();
        });


        stock.setCellValueFactory(new PropertyValueFactory<Book, Integer>("stock"));
        stock.setCellFactory(TextFieldTableCell.<Book, Integer>forTableColumn(new IntegerStringConverter()));
        stock.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setStock(e.getNewValue());
            bookController.writeBook();
        });


        sellingPrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("sellingPrice"));
        sellingPrice.setCellFactory(TextFieldTableCell.<Book, Double>forTableColumn(new DoubleStringConverter()));
        sellingPrice.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setSellingPrice(e.getNewValue());
            bookController.writeBook();
        });


        bookTable.getColumns().addAll(ISBN, title, author, supplier, purchasePrice, sellingPrice, stock);

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Button add = new Button("Add");
        add.setPrefSize(80, 10);
        Button exit = new Button("Exit");
        exit.setPrefSize(80, 10);
        Button delete = new Button("Delete");
        delete.setPrefSize(80, 10);


        hBox.getChildren().addAll(exit, add,delete);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(7, 5, 10, 5));
        vBox.getChildren().addAll(bookTable, hBox);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        StackPane stackPane = new StackPane(vBox);

        Scene scene = new Scene(stackPane, 800, 600);
        Stage bookInventory = new Stage();
        bookTable.prefHeightProperty().bind(bookInventory.heightProperty());
        bookTable.prefWidthProperty().bind(bookInventory.widthProperty());
        bookInventory.setScene(scene);
        bookInventory.show();

        delete.setOnMouseClicked(e -> {
            bookController.removeBook(bookTable.getSelectionModel().getSelectedItem());
            bookTable.getItems().removeAll(bookTable.getSelectionModel().getSelectedItem()); //this will remove the selected row
        });
        exit.setOnMouseClicked(e -> bookInventory.close());
        add.setOnMouseClicked(e -> addBookView(bookInventory));
    }
    public void lowBookStockView() {
        TableView<Book> bookTable = new TableView<>();
        ObservableList<Book> lowS = FXCollections.observableArrayList(bookController.lowStock());

        bookTable.setItems(lowS);
        bookTable.setEditable(true);

        TableColumn<Book, String> ISBN = new TableColumn<>("ISBN");
        TableColumn<Book, String> title = new TableColumn<>("Title");
        TableColumn<Book, String> genreT = new TableColumn<>("Genre");
        TableColumn<Book, String> supplier = new TableColumn<>("Supplier");
        TableColumn<Book, Double> purchasePrice = new TableColumn<>("Purchase Price");
        TableColumn<Book, Double> sellingPrice = new TableColumn<>("Selling Price");
        TableColumn<Book, String> author = new TableColumn<>("Author");
        TableColumn<Book, Integer> stock = new TableColumn<>("Stock");


        ISBN.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));

        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

        genreT.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));

        purchasePrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("purchasedPrice"));

        supplier.setCellValueFactory(new PropertyValueFactory<Book, String>("supplier"));

        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));

        stock.setCellValueFactory(new PropertyValueFactory<Book, Integer>("stock"));
        stock.setCellFactory(TextFieldTableCell.<Book, Integer>forTableColumn(new IntegerStringConverter()));
        stock.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setStock(e.getNewValue());
            bookController.writeBook();
        });

        sellingPrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("sellingPrice"));

        bookTable.getColumns().addAll(ISBN, title, author, supplier, purchasePrice, sellingPrice, stock);

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Button exit = new Button("Exit");
        exit.setPrefSize(80, 10);


        hBox.getChildren().addAll(exit);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(7, 5, 10, 5));
        vBox.getChildren().addAll(bookTable, hBox);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        StackPane stackPane = new StackPane(vBox);

        Scene scene = new Scene(stackPane, 800, 600);
        Stage bookInventory = new Stage();
        bookTable.prefHeightProperty().bind(bookInventory.heightProperty());
        bookTable.prefWidthProperty().bind(bookInventory.widthProperty());
        bookInventory.setScene(scene);
        bookInventory.show();

        exit.setOnMouseClicked(e -> bookInventory.close());
    }
    public void addBookView(Stage stage) {
        GridPane grid = new GridPane();
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        BorderPane borderPane = new BorderPane();

        Label bookISBN = new Label("ISBN");
        TextField bookISBNTF = new TextField();
        Label title = new Label("Title");
        TextField titleTF = new TextField();
        Label author = new Label("Author");
        TextField authorTF = new TextField();
        Label genre = new Label("Genre");
        TextField genreTF = new TextField();
        Label supplier = new Label("Supplier");
        TextField supplierTF = new TextField();
        Label purchaseP = new Label("Purchase Price");
        TextField purchasePTF = new TextField();
        Label sellingP = new Label("Selling Price");
        TextField sellingPTF = new TextField();
        Label stock = new Label("Stock");
        TextField stockTF = new TextField();

        grid.addRow(0, bookISBN, bookISBNTF);
        grid.addRow(1, title, titleTF);
        grid.addRow(2, author, authorTF);
        grid.addRow(3, genre, genreTF);
        grid.addRow(4, supplier, supplierTF);
        grid.addRow(5, purchaseP, purchasePTF);
        grid.addRow(6, sellingP, sellingPTF);
        grid.addRow(7, stock, stockTF);

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
        stage.setScene(scene);
        stage.show();

        Alert a = new Alert(Alert.AlertType.ERROR);

        back.setOnMouseClicked(e -> {
            stage.close();
            bookInventoryView();
        });
        add.setOnMouseClicked(e -> {
            try {
                if (bookISBNTF.getText().isEmpty() || titleTF.getText().isEmpty() || authorTF.getText().isEmpty() || genreTF.getText().isEmpty() || supplierTF.getText().isEmpty() || purchasePTF.getText().isEmpty() || sellingPTF.getText().isEmpty() || stockTF.getText().isEmpty()) {
                    a.setContentText("Fill all fields");
                    a.show();
                    error.setText("Fill all fields.");
                    throw new EmptyTextFieldException();

                }
                else if (((bookISBNTF.getText()).matches("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$"))) {
                    String ISBNS = bookISBNTF.getText();
                    String titleS = titleTF.getText();
                    String authorS = authorTF.getText();
                    String genreS = genreTF.getText();
                    String supplierS = supplierTF.getText();
                    double purchaseS = Double.parseDouble(purchasePTF.getText());
                    double sellingS = Double.parseDouble(sellingPTF.getText());
                    int stockS = Integer.parseInt(stockTF.getText());

                    if (bookController.findBook(ISBNS) != null) {
                        error.setText("Book already existent.");
                    } else {
                        Book newBook = new Book(ISBNS, titleS, genreS, supplierS, purchaseS, sellingS, authorS, stockS);
                        bookController.addBook(newBook);
                        bookISBNTF.clear();
                        titleTF.clear();
                        authorTF.clear();
                        genreTF.clear();
                        supplierTF.clear();
                        purchasePTF.clear();
                        sellingPTF.clear();
                        stockTF.clear();

                        bookController.writeBook();
                        error.setText("Book added.");

                    }
                }
                else {
                    a.setContentText("Incorrect ISBN format.");
                    a.show();
                    error.setText("Fill the ISBN code according to format.");
                }
            } catch (EmptyTextFieldException ex) {
                System.out.println(ex.getMessage());
            }

        });

    }


    public void manageUsersViewLibrarians() {
        TableView<Librarian> usersTable = new TableView<>();
        ObservableList<Librarian> librarianData = FXCollections.observableArrayList(librarianController.getLibrarians());
        usersTable.setItems(librarianData);

        usersTable.setEditable(true);

        TableColumn<Librarian, String> username = new TableColumn<>("Username");
        TableColumn<Librarian, String> password = new TableColumn<>("Password");
        TableColumn<Librarian, String> name = new TableColumn<>("Name");
        TableColumn<Librarian, String> birthday = new TableColumn<>("Birthday");
        TableColumn<Librarian, String> phone = new TableColumn<>("Phone n.o.");
        TableColumn<Librarian, String> email = new TableColumn<>("Email");
        TableColumn<Librarian, Double> salary = new TableColumn<>("Salary");
        TableColumn<Librarian, String> accessLvl = new TableColumn<>("Access");


        username.setCellValueFactory(new PropertyValueFactory<Librarian, String>("username"));
        username.setCellFactory(TextFieldTableCell.forTableColumn()); //reimplements the table cell as text field
        username.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setUsernameN(e.getNewValue());
            librarianController.writeLibrarians();
        }); //assignment of the new values to the corresponding table cell

        password.setCellValueFactory(new PropertyValueFactory<Librarian, String>("password"));
        password.setCellFactory(TextFieldTableCell.forTableColumn());
        password.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setPasswordN(e.getNewValue());
            librarianController.writeLibrarians();
        });

        name.setCellValueFactory(new PropertyValueFactory<Librarian, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setNameN(e.getNewValue());
            librarianController.writeLibrarians();
        });

        birthday.setCellValueFactory(new PropertyValueFactory<Librarian, String>("birthday"));
        birthday.setCellFactory(TextFieldTableCell.forTableColumn());
        birthday.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setBirthdayN(e.getNewValue());
            librarianController.writeLibrarians();
        });

        phone.setCellValueFactory(new PropertyValueFactory<Librarian, String>("phone"));
        phone.setCellFactory(TextFieldTableCell.forTableColumn());
        phone.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setPhoneN(e.getNewValue());
            librarianController.writeLibrarians();
        });

        email.setCellValueFactory(new PropertyValueFactory<Librarian, String>("email"));
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setEmailN(e.getNewValue());
            librarianController.writeLibrarians();
        });

        salary.setCellValueFactory(new PropertyValueFactory<Librarian, Double>("salary"));
        salary.setCellFactory(TextFieldTableCell.<Librarian, Double>forTableColumn(new DoubleStringConverter()));
        salary.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setSalaryN(e.getNewValue());
            librarianController.writeLibrarians();
        });

        accessLvl.setCellValueFactory(new PropertyValueFactory<Librarian, String>("accessLvl"));
        accessLvl.setCellFactory(TextFieldTableCell.forTableColumn());


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
        vBox.getChildren().addAll(usersTable, hBox);
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
            librarianController.removeLibrarian(usersTable.getSelectionModel().getSelectedItem());
            librarianController.removeUser(usersTable.getSelectionModel().getSelectedItem());
            usersTable.getItems().removeAll(usersTable.getSelectionModel().getSelectedItem()); //this will remove the selected row

        });
    }
    /*public void manageUsersViewLibrarian() {
        TableView<Librarian> librarianTable = new TableView<>();
        ObservableList<Librarian> userData;
        userData = FXCollections.observableArrayList(librarianController.getLibrarians());

        librarianTable.setItems(userData);


        TableColumn<Librarian, String> username = new TableColumn<>("Username");
        TableColumn<Librarian, String> password = new TableColumn<>("Password");
        TableColumn<Librarian, String> accessLvl = new TableColumn<>("Access");


        username.setCellValueFactory(new PropertyValueFactory<Librarian, String>("username"));
        username.setCellFactory(TextFieldTableCell.forTableColumn());

        password.setCellValueFactory(new PropertyValueFactory<Librarian, String>("password"));
        password.setCellFactory(TextFieldTableCell.forTableColumn());

        accessLvl.setCellValueFactory(new PropertyValueFactory<Librarian, String>("accessLvl"));
        accessLvl.setCellFactory(TextFieldTableCell.forTableColumn());

        librarianTable.getColumns().addAll(username, password, accessLvl);
        username.prefWidthProperty().bind(librarianTable.widthProperty().multiply(0.333));
        password.prefWidthProperty().bind(librarianTable.widthProperty().multiply(0.333));
        accessLvl.prefWidthProperty().bind(librarianTable.widthProperty().multiply(0.328));


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
        vBox.getChildren().addAll(librarianTable, hBox);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        StackPane stackPane = new StackPane(vBox);

        Scene scene = new Scene(stackPane, 800, 600);
        Stage userManagementView = new Stage();
        librarianTable.prefHeightProperty().bind(userManagementView.heightProperty());
        librarianTable.prefWidthProperty().bind(userManagementView.widthProperty());
        userManagementView.setScene(scene);
        userManagementView.show();

        exit.setOnMouseClicked(e -> userManagementView.close());

        delete.setOnMouseClicked(e -> {
            librarianController.removeLibrarian((Librarian) librarianTable.getSelectionModel().getSelectedItem());
            librarianTable.getItems().removeAll(librarianTable.getSelectionModel().getSelectedItem()); //this will remove the selected row
        });

        librarianTable.setRowFactory( e -> {
            TableRow <Librarian> row = new TableRow<>();
            row.setOnMouseClicked( click -> {
                if(click.getClickCount() == 2 && (!row.isEmpty())){
                    userStatsView(userManagementView,librarianTable.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        });

    }*/

    public void manageUsersViewAdmin() {
        TableView<User> usersTable = new TableView<>();
        ObservableList<User> userData;
        userData = FXCollections.observableArrayList(userController.getUsers());

        usersTable.setItems(userData);


        TableColumn<User, String> username = new TableColumn<>("Username");
        TableColumn<User, String> password = new TableColumn<>("Password");
        TableColumn<User, String> accessLvl = new TableColumn<>("Access");


        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        username.setCellFactory(TextFieldTableCell.forTableColumn());

        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        password.setCellFactory(TextFieldTableCell.forTableColumn());

        accessLvl.setCellValueFactory(new PropertyValueFactory<User, String>("accessLvl"));
        accessLvl.setCellFactory(TextFieldTableCell.forTableColumn());

        usersTable.getColumns().addAll(username, password, accessLvl);
        username.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.333));
        password.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.333));
        accessLvl.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.328));


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
        vBox.getChildren().addAll(usersTable, hBox);
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
            userController.removeUser(usersTable.getSelectionModel().getSelectedItem());
            usersTable.getItems().removeAll(usersTable.getSelectionModel().getSelectedItem()); //this will remove the selected row

        });

        usersTable.setRowFactory( e -> {
            TableRow <User> row = new TableRow<>();
            row.setOnMouseClicked( click -> {
                if(click.getClickCount() == 2 && (!row.isEmpty())){
                    userStatsView(userManagementView,usersTable.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        });

    }
    public void addUserView () {
        GridPane grid = new GridPane();
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
                    librarian = new Librarian(usernameS, passwordS, nameS, bDayS, phoneS, emailS, (Double) salaryS, "Librarian");
                    librarian.setSalaryN(salaryS);
                    librarian.setEmailN(emailS);
                    librarianController.addLibrarian(librarian);
                    librarianController.writeLibrarians();
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
        });
    }
    public void userStatsView (Stage stage, User user) {
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

        /*if (user instanceof Librarian) {
            nameTF.setText(((Librarian) user).getName());
            usernameTF.setText(user.getUsername());
            passwordTF.setText(user.getPassword());
            phoneTF.setText(((Librarian) user).getPhone());
            emailTf.setText(((Librarian) user).getEmail());
            salaryTF.setText(String.valueOf(((Librarian) user).getSalary()));
            bDayTF.setText(((Librarian) user).getBirthday());
            accessTF.setText("Librarian");

        } else if (user instanceof Manager) {
            nameTF.setText(((Manager) user).getName());
            usernameTF.setText(user.getUsername());
            passwordTF.setText(user.getPassword());
            phoneTF.setText(((Manager) user).getPhoneN());
            emailTf.setText(((Manager) user).getEmailN());
            salaryTF.setText(String.valueOf((((Manager) user).getSalaryN())));
            bDayTF.setText(((Manager) user).getBirthdayN());
            accessTF.setText("Manager");
        }
        else{
            nameTF.setEditable(false);
            usernameTF.setEditable(false);
            passwordTF.setEditable(false);
            phoneTF.setEditable(false);
            emailTf.setEditable(false);
            salaryTF.setEditable(false);
            bDayTF.setEditable(false);
            accessTF.setText("Admin");
        }

        HBox hBox = new HBox();
        hBox.setSpacing(60);
        hBox.getChildren().addAll(gridPaneLeft, gridPaneRight);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(40, 0, 0, 0));


        HBox fromDate = new HBox();
        fromDate.setSpacing(10);
        Label from = new Label("From");
        TextField fromTF = new TextField();
        HBox toDate = new HBox();
        toDate.setSpacing(10);
        Label to = new Label("To");
        TextField toTF = new TextField();

        fromDate.getChildren().addAll(from, fromTF);
        toDate.getChildren().addAll(to, toTF);

        HBox fullDate = new HBox();
        fullDate.getChildren().addAll(fromDate, toDate);
        fullDate.setSpacing(60);
        fullDate.setAlignment(Pos.CENTER);

        BorderPane report = new BorderPane();
        HBox reportH = new HBox();
        Label bookTotal = new Label("Total books: ");
        Label billTotal = new Label("Total bills: ");
        Label revenueTotal = new Label("Total revenue: ");
        reportH.getChildren().addAll(bookTotal, billTotal, revenueTotal);
        reportH.setAlignment(Pos.CENTER);
        reportH.setSpacing(100);
        report.setCenter(reportH);


        Button generate = new Button("Generate");
        generate.setPrefSize(80, 10);
        Button save = new Button("Save");
        save.setPrefSize(80, 10);
        Button back = new Button("Exit");
        back.setPrefSize(80, 10);
        HBox buttons = new HBox();
        buttons.setSpacing(30);
        buttons.getChildren().addAll(generate, save, back);
        buttons.setAlignment(Pos.CENTER);

        Label error = new Label("");
        error.setAlignment(Pos.CENTER);

        VBox vBox = new VBox();
        vBox.setSpacing(90);
        vBox.getChildren().addAll(hBox, fullDate, report, buttons, error);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setTop(vBox);


        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();

        Alert a = new Alert(Alert.AlertType.ERROR);
        generate.setOnMouseClicked(e -> {
        });
        back.setOnMouseClicked(e -> {
            stage.close();
        });
        save.setOnMouseClicked(e -> {

            if (user.getAccessLvl()== "Librarian") {
                try {
                    if (nameTF.getText().isEmpty() || usernameTF.getText().isEmpty() || passwordTF.getText().isEmpty() || phoneTF.getText().isEmpty() || emailTf.getText().isEmpty() || bDayTF.getText().isEmpty() || salaryTF.getText().isEmpty()) {
                        a.setContentText("Fill all fields");
                        a.show();
                        error.setText("Fill all fields.");

                        throw new EmptyTextFieldException();
                    }
                    else if (((emailTf.getText()).matches("^(.+)@(\\S+) $")) && ((bDayTF.getText()).matches(String.valueOf(dateFormat)))&&((phoneTF.getText()).matches("[0-9]{10}"))) {
                        librarianController.removeLibrarian((Librarian) user);
                        Librarian newLib = new Librarian((usernameTF).getText(),(passwordTF).getText(),(nameTF).getText(),(bDayTF).getText(),(phoneTF).getText(),(emailTf).getText(),Double.parseDouble((salaryTF).getText()),"Librarian");
                        System.out.println(((Librarian)newLib).getUsername());
                        librarianController.addLibrarian((Librarian) newLib);
                        librarianController.writeLibrarians();
                    }
                    else {
                        error.setText("Pleas enter the details in the correct format.");

                    }
                } catch (EmptyTextFieldException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (user.getAccessLvl() == "Manager") {
                try {
                    if (nameTF.getText().isEmpty() || usernameTF.getText().isEmpty() || passwordTF.getText().isEmpty() || phoneTF.getText().isEmpty() || emailTf.getText().isEmpty() || bDayTF.getText().isEmpty() || salaryTF.getText().isEmpty()) {
                        a.setContentText("Fill all fields");
                        a.show();
                        error.setText("Fill all fields.");
                        throw new EmptyTextFieldException();

                    }
                    else if (((emailTf.getText()).matches("^(.+)@(\\S+) $")) && ((bDayTF.getText()).matches(String.valueOf(dateFormat)))&&((phoneTF.getText()).matches("[0-9]{10}"))) {
                        managerController.removeManager((Manager) user);
                        Manager newMan = new Manager((usernameTF).getText(),(passwordTF).getText(),(nameTF).getText(),(bDayTF).getText(),(phoneTF).getText(),(emailTf).getText(),Double.parseDouble((salaryTF).getText()),"Librarian");
                        managerController.addManagers((Manager) newMan);
                        managerController.writeManagers();
                    }
                    else {
                        error.setText("Pleas enter the details in the correct format.");

                    }


                } catch (EmptyTextFieldException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else{
                System.out.println("Admin cannot be changed.");
            }
            stage.close();
        });

         */
    }

   /* public void librarianManagmentView() {
        TableView<Librarian> bookTable = new TableView<>();
        ObservableList<Librarian> bookData = FXCollections.observableArrayList(librarianController.getLibrarians());

        bookTable.setItems(bookData);
        bookTable.setEditable(true);

        TableColumn<Librarian, String> username = new TableColumn<>("Username");
        TableColumn<Librarian, String>  name = new TableColumn<>("Name");
        TableColumn<Librarian, String> password  = new TableColumn<>("Password");
        TableColumn<Librarian, String> phone = new TableColumn<>("Phone");
        TableColumn<Librarian, String> email = new TableColumn<>("Email");
        TableColumn<Librarian, String> birthday = new TableColumn<>("Birthday");
        TableColumn<Librarian, Double> salary = new TableColumn<>("Salary");
        TableColumn<Librarian, String> acsesslvl = new TableColumn<>("Access");


        username.setCellValueFactory(new PropertyValueFactory<Librarian, String>("username"));
        username.setCellFactory(TextFieldTableCell.forTableColumn()); //reimplements the table cell as text field
        username.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setUsername(e.getNewValue());
            userController.writeUsers();
        }); //assignment of the new values to the corresponding table cell

        name.setCellValueFactory(new PropertyValueFactory<Librarian, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setName(e.getNewValue());
            userController.writeUsers();
        });
        password.setCellValueFactory(new PropertyValueFactory<Librarian, String>("password"));
        password.setCellFactory(TextFieldTableCell.forTableColumn());
        password.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setPassword(e.getNewValue());
            userController.writeUsers();
        });
        phone.setCellValueFactory(new PropertyValueFactory<Librarian, String>("phone"));
        phone.setCellFactory(TextFieldTableCell.forTableColumn());
        phone.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setPhone(e.getNewValue());
            userController.writeUsers();
        });
        email.setCellValueFactory(new PropertyValueFactory<Librarian, String>("email"));
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setEmail(e.getNewValue());
            userController.writeUsers();
        });
        birthday.setCellValueFactory(new PropertyValueFactory<Librarian, String>("birthday"));
        birthday.setCellFactory(TextFieldTableCell.forTableColumn());
        birthday.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setBirthday(e.getNewValue());
            userController.writeUsers();
        });
        salary.setCellValueFactory(new PropertyValueFactory<Librarian, Double>("salary"));
        salary.setCellFactory(TextFieldTableCell.<Librarian, Double>forTableColumn(new DoubleStringConverter()));
        salary.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setSalary(e.getNewValue());
            userController.writeUsers();
        });
        acsesslvl.setCellValueFactory(new PropertyValueFactory<Librarian, String>("accessLvl"));
        acsesslvl.setCellFactory(TextFieldTableCell.forTableColumn());
        acsesslvl.setOnEditCommit(e -> {
            (e.getTableView().getItems().get(e.getTablePosition().getRow())).setAccessLvl(e.getNewValue());
            userController.writeUsers();
        });


        bookTable.getColumns().addAll(username, name, password,phone,email,birthday,salary,acsesslvl);

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Button add = new Button("Add");
        Button exit = new Button("Exit");
        add.setPrefSize(80, 10);

        exit.setPrefSize(80, 10);


        hBox.getChildren().addAll(exit, add);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(7, 5, 10, 5));
        vBox.getChildren().addAll(bookTable, hBox);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 0, 10));

        StackPane stackPane = new StackPane(vBox);

        Scene scene = new Scene(stackPane, 800, 600);
        Stage bookInventory = new Stage();
        bookTable.prefHeightProperty().bind(bookInventory.heightProperty());
        bookTable.prefWidthProperty().bind(bookInventory.widthProperty());
        bookInventory.setScene(scene);
        bookInventory.show();

        exit.setOnMouseClicked(e -> bookInventory.close());

        add.setOnMouseClicked(e -> addUserView(bookInventory));

    }*/

    public static void main(String[] args) {
        launch();
    }}
