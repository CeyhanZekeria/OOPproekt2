package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.services.ClientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientListController {

    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, String> colFirstName;
    @FXML private TableColumn<Client, String> colLastName;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colPhone;
    @FXML private TableColumn<Client, Integer> colPoints;

    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("loyaltyPoints"));

        loadClients();
    }

    @FXML
    public void onAddClient() {
        openClientForm(null);
    }

    @FXML
    public void onEditClient() {
        Client selected = clientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a client to edit!");
            return;
        }
        openClientForm(selected);
    }

    @FXML
    public void onDeleteClient() {
        Client selected = clientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a client to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Delete client: " + selected.getFirstName() + " " + selected.getLastName() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                clientService.deleteClient(selected.getId());
                loadClients();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        }
    }

    private void openClientForm(Client clientToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_client.fxml"));
            Parent root = loader.load();

            if (clientToEdit != null) {
                AddClientController controller = loader.getController();
                controller.setClientToEdit(clientToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(clientToEdit == null ? "Add Client" : "Edit Client");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadClients() {
        clientsTable.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
    }
}