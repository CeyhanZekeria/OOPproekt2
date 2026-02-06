package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.entities.Notification;
import bg.autosalon.entities.User;
import bg.autosalon.services.NotificationService;
import bg.autosalon.utils.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyNotificationsController {

    @FXML private TableView<Notification> notifTable;
    @FXML private TableColumn<Notification, String> colDate;
    @FXML private TableColumn<Notification, String> colMessage;

    private final NotificationService service = new NotificationService();

    @FXML
    public void initialize() {

        colDate.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));


        colMessage.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMessage()));

        loadMyNotifications();
    }

    private void loadMyNotifications() {
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser instanceof Client) {
            Client client = (Client) currentUser;

            List<Notification> myList = service.getClientNotifications(client.getId());


            notifTable.setItems(FXCollections.observableArrayList(myList));
        }
    }
}