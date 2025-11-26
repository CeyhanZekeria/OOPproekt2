package bg.autosalon.dao.impl;

import bg.autosalon.entities.Client;

public class ClientDao extends GenericDao<Client> {
    public ClientDao() {
        super(Client.class);
    }
}
