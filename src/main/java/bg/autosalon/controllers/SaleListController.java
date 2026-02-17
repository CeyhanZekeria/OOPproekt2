package bg.autosalon.controllers;

import bg.autosalon.entities.Sale;
import bg.autosalon.services.SaleService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SaleListController {

    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private TableColumn<Sale, String> colCar;
    @FXML
    private TableColumn<Sale, String> colClient;
    @FXML
    private TableColumn<Sale, String> colEmployee;
    @FXML
    private TableColumn<Sale, String> colDate;
    @FXML
    private TableColumn<Sale, String> colPrice;

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
        openSaleForm(null);
    }

    @FXML
    public void onEditSale() {
        Sale selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a sale to edit!");
            return;
        }
        openSaleForm(selected);
    }

    @FXML
    public void onDeleteSale() {
        Sale selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a sale to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Delete this sale? The car status will revert to AVAILABLE.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                saleService.deleteSale(selected.getId());
                loadSales();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        }
    }

    private void openSaleForm(Sale saleToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_sale.fxml"));
            Parent root = loader.load();

            if (saleToEdit != null) {
                AddSaleController controller = loader.getController();
                controller.setSaleToEdit(saleToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(saleToEdit == null ? "New Sale" : "Edit Sale");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}