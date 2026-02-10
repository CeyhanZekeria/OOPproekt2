package bg.autosalon.controllers;

import bg.autosalon.entities.ServiceRecord;
import bg.autosalon.services.ServiceRecordService;
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

public class ServiceListController {

    @FXML private TableView<ServiceRecord> serviceTable;
    @FXML private TableColumn<ServiceRecord, String> colCar;
    @FXML private TableColumn<ServiceRecord, String> colType;
    @FXML private TableColumn<ServiceRecord, String> colDate;
    @FXML private TableColumn<ServiceRecord, String> colPrice;
    @FXML private TableColumn<ServiceRecord, String> colDesc;

    private final ServiceRecordService serviceService = new ServiceRecordService();

    @FXML
    public void initialize() {
        colCar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getBrand() + " " + cell.getValue().getCar().getModel()));
        colType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType().toString()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate().toString()));


        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f BGN", cell.getValue().getPrice())));

        colDesc.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        loadRecords();
    }

    private void loadRecords() {
        serviceTable.setItems(FXCollections.observableArrayList(serviceService.getAllRecords()));
    }

    @FXML public void onAddService() { openServiceForm(null); }

    @FXML public void onEditService() {
        ServiceRecord selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a record!");
            return;
        }
        openServiceForm(selected);
    }

    @FXML public void onDeleteService() {
        ServiceRecord selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a record!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this record?", ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                serviceService.deleteRecord(selected.getId());
                loadRecords();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        }
    }

    private void openServiceForm(ServiceRecord recordToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_service.fxml"));
            Parent root = loader.load();
            if (recordToEdit != null) {
                loader.<AddServiceController>getController().setRecordToEdit(recordToEdit);
            }
            Stage stage = new Stage();
            stage.setTitle(recordToEdit == null ? "New Service" : "Edit Service");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadRecords();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}