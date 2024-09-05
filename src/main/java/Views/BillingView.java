package Views;

import Controllers.BillController;
import Controllers.BookController;
import CustomExceptions.EmptyTextFieldException;
import Model.Admin;
import Model.Bill;
import Model.Book;
import Model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class BillingView {

    private BookController bookController;
    private BillController billController;
    private VBox bookListVBox;

    public BillingView(BookController bookController, BillController billController) {
        this.bookController = bookController;
        this.billController = billController;
        this.bookListVBox = new VBox(10);
    }

    public void show(User user) {
        Bill newBill = new Bill();

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        VBox vBox = new VBox(10);
        HBox hbox = new HBox(10);
        VBox rightPane = new VBox(5);
        Label totalPriceLabel = new Label("Total: $0.00");

        rightPane.setPrefWidth(200);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rightPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setStyle("-fx-background-color: white; -fx-border-color: black;");
        scrollPane.setMinHeight(500);

        rightPane.setPadding(new Insets(10));
        rightPane.setStyle("-fx-background-color: white; -fx-border-color: black;");
        rightPane.setMinHeight(450);

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
        borderPane.setRight(scrollPane);
        borderPane.setBottom(totalPriceLabel);

        BorderPane.setMargin(totalPriceLabel, new Insets(10, 10, 10, 10));
        BorderPane.setAlignment(totalPriceLabel, Pos.CENTER_RIGHT);

        Scene scene = new Scene(borderPane, 800, 600);
        Stage billingLibST = new Stage();
        billingLibST.setScene(scene);
        billingLibST.show();

        Alert a = new Alert(Alert.AlertType.ERROR);

        add.setOnMouseClicked(ev -> {
            try {
                if (bookISBNTF.getText().isEmpty() || bookQuantityTF.getText().isEmpty()) {
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

                        book.setStock(book.getStock() - quantity);
                        success.setText("Book added successfully.");
                        bookISBNTF.clear();
                        bookQuantityTF.clear();

                        // Update the book list display
                        HBox bookEntry = new HBox(10);
                        Label bookName = new Label(book.getTitle());
                        Label bookQty = new Label(String.valueOf(quantity));
                        Label bookPrice = new Label(String.format("$%.2f", book.getSellingPrice() * quantity));

                        bookName.setMaxWidth(Double.MAX_VALUE);
                        bookQty.setMaxWidth(Double.MAX_VALUE);
                        bookPrice.setMaxWidth(Double.MAX_VALUE);

                        HBox.setHgrow(bookName, Priority.ALWAYS);
                        HBox.setHgrow(bookQty, Priority.ALWAYS);
                        HBox.setHgrow(bookPrice, Priority.ALWAYS);

                        bookEntry.getChildren().addAll(bookName, bookQty, bookPrice);
                        rightPane.getChildren().add(bookEntry);

                        totalPriceLabel.setText(String.format("Total: $%.2f", newBill.getTotal1()));
                    }
                }
            } catch (EmptyTextFieldException ex) {
                System.out.println(ex.getMessage());
            }

        });

        print.setOnMouseClicked(ev -> {
            File bill;
            bookController.writeBook();

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
                billController.addBill(newBill);

                billingLibST.close();

            } catch (IOException exception) {
                System.out.println("Error in bill printing");
            }

        });

        exit.setOnMouseClicked(e -> billingLibST.close());
    }
}
