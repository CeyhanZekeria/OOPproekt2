package bg.autosalon.controllers;

import bg.autosalon.entities.Sale;
import bg.autosalon.services.SaleService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
                cell.getValue().getCar().getBrand() + " " + cell.getValue().getCar().getModel()
        ));

        colClient.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getClient().getFirstName() + " " + cell.getValue().getClient().getLastName()
        ));

        colEmployee.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getEmployee().getFirstName() + " " + cell.getValue().getEmployee().getLastName()
        ));

        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSaleDate().toString()));

        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(
                String.format("%.2f BGN", cell.getValue().getFinalPrice())
        ));

        loadSales();
    }

    private void loadSales() {
        salesTable.setItems(FXCollections.observableArrayList(saleService.getAllSales()));
    }

    @FXML
    public void onAddSale() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_sale.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("New Sale");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}