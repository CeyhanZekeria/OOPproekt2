package bg.autosalon.services;

import bg.autosalon.dao.impl.ClientDao;
import bg.autosalon.entities.Client;

import java.util.*;

public class ClientService {

    private final ClientDao clientDao = new ClientDao();

    public void addClient(Client client) {
        clientDao.save(client);
    }

    public void updateClient(Client client) {
        clientDao.update(client);
    }

    public Client getClient(Long id) {
        return clientDao.findById(id);
    }

    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    public void deleteClient(Long id) {
        Client client = clientDao.findById(id);
        if (client != null) {
            clientDao.delete(client);
        }
    }
}
