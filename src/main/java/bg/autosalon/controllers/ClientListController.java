package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.services.ClientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private void loadClients() {
        clientsTable.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
    }
}