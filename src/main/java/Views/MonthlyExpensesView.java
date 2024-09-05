package Views;

import Controllers.BillController;
import Model.Bill;
import Model.Book;
import Model.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

public class MonthlyExpensesView {

    private BillController billController;

    public MonthlyExpensesView(BillController billController) {
        this.billController = billController;
    }

    public void show() {
        TableView<Bill> billTable = new TableView<>();
        ObservableList<Bill> bills = FXCollections.observableArrayList(billController.getBills());
        ObservableList<Bill> filteredData = FXCollections.observableArrayList(bills);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by bill Id...");
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
                filteredData.addAll(bills);
            } else {
                for (Bill bill : bills) {
                    if (bill.getOrderID1().toLowerCase().contains(filterTxt)) {
                        filteredData.add(bill);
                    }
                }
            }
            billTable.setItems(filteredData);
        });

        Label totalLabel = new Label("Total: 0.0€");
        totalLabel.setFont(new Font("Arial", 24));

        HBox totalLabelBox = new HBox(totalLabel);
        totalLabelBox.setAlignment(Pos.CENTER);
        totalLabelBox.setPadding(new Insets(20));

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button filterButton = new Button("Filter");
        filterButton.setPrefSize(80, 10);

        HBox filterDate = new HBox();
        filterDate.getChildren().addAll(new Label("From: "), startDatePicker, new Label("To: "), endDatePicker, filterButton);
        filterDate.setAlignment(Pos.CENTER);
        filterDate.setSpacing(10);

        filterButton.setOnMouseClicked(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate != null && endDate != null) {
                filteredData.clear();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filteredData.addAll(bills.stream()
                        .filter(bill -> {
                            LocalDate billDate = LocalDate.parse(bill.getDate1(), formatter);
                            return (billDate.isEqual(startDate) || billDate.isAfter(startDate)) &&
                                    (billDate.isEqual(endDate) || billDate.isBefore(endDate));
                        })
                        .collect(Collectors.toList()));
            } else {
                filteredData.clear();
                filteredData.addAll(bills);
            }
            double totalEarnings = filteredData.stream().mapToDouble(Bill::getTotal1).sum();
            totalLabel.setText("Total: "+ totalEarnings + "€");
            billTable.setItems(filteredData);
        });

        HBox filterData = new HBox();
        filterData.getChildren().addAll(searchBox,filterDate);
        filterData.setAlignment(Pos.CENTER);
        filterData.setSpacing(5);

        billTable.setItems(bills);

        TableColumn<Bill, String> orderIDColumn = new TableColumn<>("Order ID");
        TableColumn<Bill, String> dateColumn = new TableColumn<>("Date");
        TableColumn<Bill, Double> totalColumn = new TableColumn<>("Total");

        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        orderIDColumn.setPrefWidth(260);
        dateColumn.setPrefWidth(260);
        totalColumn.setPrefWidth(260);

        billTable.getColumns().addAll( orderIDColumn, dateColumn, totalColumn);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(filterData, billTable, totalLabelBox);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("All Bills");
        stage.setScene(scene);
        stage.show();
    }
}
