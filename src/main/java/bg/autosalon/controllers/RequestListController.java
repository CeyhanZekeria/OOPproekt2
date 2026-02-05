package bg.autosalon.controllers;

import bg.autosalon.entities.TestDriveRequest;
import bg.autosalon.enums.RequestStatus;
import bg.autosalon.services.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class RequestListController {

    @FXML private TableView<TestDriveRequest> requestsTable;
    @FXML private TableColumn<TestDriveRequest, String> colClient;
    @FXML private TableColumn<TestDriveRequest, String> colCar;
    @FXML private TableColumn<TestDriveRequest, String> colDate;
    @FXML private TableColumn<TestDriveRequest, String> colStatus;

    private final RequestService requestService = new RequestService();

    @FXML
    public void initialize() {

        colClient.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getClient().getFirstName() + " " + cell.getValue().getClient().getLastName()
        ));

        colCar.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getCar().getBrand() + " " + cell.getValue().getCar().getModel()
        ));

        colDate.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getRequestDate().toString().replace("T", " ")
        ));

        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getStatus().toString()
        ));


        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    if (item.equals("PENDING")) setTextFill(Color.ORANGE);
                    else if (item.equals("APPROVED")) setTextFill(Color.GREEN);
                    else setTextFill(Color.RED);
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });

        loadRequests();
    }

    private void loadRequests() {
        requestsTable.setItems(FXCollections.observableArrayList(requestService.getAllRequests()));
    }

    @FXML
    public void onApprove() {
        TestDriveRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a request!");
            return;
        }

        if (selected.getStatus() == RequestStatus.PENDING) {
            requestService.updateStatus(selected.getId(), RequestStatus.APPROVED);
            loadRequests();
            showAlert("Request Approved!");
        } else {
            showAlert("Request is already processed!");
        }
    }

    @FXML
    public void onCancel() {
        TestDriveRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a request!");
            return;
        }

        requestService.updateStatus(selected.getId(), RequestStatus.CANCELLED);
        loadRequests();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}