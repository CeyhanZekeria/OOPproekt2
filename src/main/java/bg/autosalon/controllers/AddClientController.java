package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddClientController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final ClientService clientService = new ClientService();
    private Client clientToEdit = null;

    public void setClientToEdit(Client client) {
        this.clientToEdit = client;
        firstNameField.setText(client.getFirstName());
        lastNameField.setText(client.getLastName());
        emailField.setText(client.getEmail());
        phoneField.setText(client.getPhone());
        passwordField.setText(client.getPassword());
    }

    @FXML
    public void onSave() {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("Email and password are required!");
            return;
        }

        try {
            if (clientToEdit != null) {
                clientToEdit.setFirstName(firstNameField.getText());
                clientToEdit.setLastName(lastNameField.getText());
                clientToEdit.setEmail(emailField.getText());
                clientToEdit.setPhone(phoneField.getText());
                clientToEdit.setPassword(passwordField.getText());

                clientService.updateClient(clientToEdit);
            } else {
                Client client = new Client();
                client.setFirstName(firstNameField.getText());
                client.setLastName(lastNameField.getText());
                client.setEmail(emailField.getText());
                client.setPhone(phoneField.getText());
                client.setPassword(passwordField.getText());
                client.setRole(UserRole.CLIENT);

                clientService.addClient(client);
            }
            closeWindow();
        } catch (Exception e) {
            errorLabel.setText("Error saving: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}