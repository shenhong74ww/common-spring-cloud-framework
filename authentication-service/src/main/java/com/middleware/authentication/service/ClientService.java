package com.middleware.authentication.service;

import com.middleware.authentication.model.Client;

import java.util.List;

public interface ClientService {

    List<Client> getClientList();

    Client getByClientId(String clientId);

    Client createOrUpdateClient(Client client);
}
