package bg.autosalon.controllers;

import bg.autosalon.entities.ServiceRecord;
import bg.autosalon.services.ServiceRecordService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceListController {

    @FXML private TableView<ServiceRecord> serviceTable;
    @FXML private TableColumn<ServiceRecord, String> colCar;
    @FXML private TableColumn<ServiceRecord, String> colType;
    @FXML private TableColumn<ServiceRecord, String> colDate;
    @FXML private TableColumn<ServiceRecord, String> colDesc;

    private final ServiceRecordService serviceService = new ServiceRecordService();

    @FXML
    public void initialize() {
        colCar.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getCar().getBrand() + " " + cell.getValue().getCar().getModel()));

        colType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType().toString()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate().toString()));
        colDesc.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));

        loadRecords();
    }

    private void loadRecords() {
        serviceTable.setItems(FXCollections.observableArrayList(serviceService.getAllRecords()));
    }
}