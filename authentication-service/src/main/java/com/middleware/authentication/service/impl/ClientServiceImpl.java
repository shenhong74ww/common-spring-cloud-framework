package com.middleware.authentication.service.impl;

import com.middleware.authentication.model.Client;
import com.middleware.authentication.repository.ClientRepository;
import com.middleware.authentication.service.ClientService;
import com.middleware.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl extends BaseServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> getClientList() {
        ArrayList<Client> clientList = new ArrayList<>();
        clientRepository.findAll().forEach((client) -> {
            clientList.add(client);
        });
        return clientList;
    }

    @Override
    public Client getByClientId(String clientId) {
        return clientRepository.findByClientId(clientId);
    }

    @Override
    public Client createOrUpdateClient(Client client) {
        return clientRepository.save(client);
    }


}
