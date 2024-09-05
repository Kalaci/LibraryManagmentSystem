package Views;

import Controllers.BookController;
import CustomExceptions.EmptyTextFieldException;
import Model.Book;
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

public class BookInventoryLvl3View {
    private BookController bookController;

    public BookInventoryLvl3View(BookController bookController){
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
        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        genreT.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
        purchasePrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("purchasedPrice"));
        supplier.setCellValueFactory(new PropertyValueFactory<Book, String>("supplier"));
        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        stock.setCellValueFactory(new PropertyValueFactory<Book, Integer>("stock"));
        sellingPrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("sellingPrice"));

        bookTable.getColumns().addAll(ISBN, title, author, supplier, purchasePrice, sellingPrice, stock);

        VBox vBox = new VBox();
        HBox hBox = new HBox();


        Button exit = new Button("Exit");
        exit.setPrefSize(100, 10);

        hBox.getChildren().addAll(exit);
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

        exit.setOnMouseClicked(e -> bookInventory.close());
    }

}
