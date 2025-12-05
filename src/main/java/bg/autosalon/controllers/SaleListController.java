package bg.autosalon.controllers;

import bg.autosalon.entities.Sale;
import bg.autosalon.services.SaleService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SaleListController {

    @FXML private TableView<Sale> salesTable;
    @FXML private TableColumn<Sale, String> colCar;
    @FXML private TableColumn<Sale, String> colClient;
    @FXML private TableColumn<Sale, String> colEmployee;
    @FXML private TableColumn<Sale, String> colDate;
    @FXML private TableColumn<Sale, String> colPrice;

    private final SaleService saleService = new SaleService();

    @FXML
    public void initialize() {

        colCar.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getCar().getBrand() + " " + cell.getValue().getCar().getModel()));

        colClient.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getClient().getFirstName() + " " + cell.getValue().getClient().getLastName()));

        colEmployee.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getEmployee().getFirstName() + " " + cell.getValue().getEmployee().getLastName()));

        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSaleDate().toString()));
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getFinalPrice()) + " BGN."));

        loadSales();
    }

    private void loadSales() {
        salesTable.setItems(FXCollections.observableArrayList(saleService.getAllSales()));
    }
}