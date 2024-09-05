package Views;

import Controllers.BookController;
import CustomExceptions.EmptyTextFieldException;
import Model.Book;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class BookInventoryLvl2View {
    private BookController bookController;

    public BookInventoryLvl2View(BookController bookController){
        this.bookController = bookController;
    }

    public void show (){
        TableView<Book> bookTable = new TableView<>();
        ObservableList<Book> bookData = FXCollections.observableArrayList(bookController.getBooks());
        ObservableList<Book> filteredData = FXCollections.observableArrayList(bookData);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title...");
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
                filteredData.addAll(bookData);
            } else {
                for (Book book : bookData) {
                    if (book.getTitle().toLowerCase().contains(filterTxt)) {
                        filteredData.add(book);
                    }
                }
            }
            bookTable.setItems(filteredData);
        });

        bookTable.setItems(bookData);
        bookTable.setEditable(true);

        TableColumn<Book, String> ISBN = new TableColumn<>("ISBN");
        TableColumn<Book, String> title = new TableColumn<>("Title");
        TableColumn<Book, String> genreT = new TableColumn<>("Genre");
        TableColumn<Book, String> supplier = new TableColumn<>("Supplier");
        TableColumn<Book, Double> purchasePrice = new TableColumn<>("Purchase Price (€)");
        TableColumn<Book, Double> sellingPrice = new TableColumn<>("Selling Price (€)");
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
        vBox.getChildren().addAll(searchBox, bookTable, hBox);
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


}
