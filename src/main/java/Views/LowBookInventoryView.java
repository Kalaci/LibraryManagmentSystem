package Views;

import Controllers.BookController;
import Model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class LowBookInventoryView {
    private BookController bookController;

    public LowBookInventoryView(BookController bookController){
        this.bookController = bookController;
    }

    public void show() {
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
}
