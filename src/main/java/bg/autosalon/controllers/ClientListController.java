package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.services.ClientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add client");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClients() {
        clientsTable.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
    }
}