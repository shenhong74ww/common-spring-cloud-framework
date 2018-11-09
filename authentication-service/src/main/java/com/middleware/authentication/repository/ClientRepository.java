package com.middleware.authentication.repository;

import com.middleware.authentication.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Client findByClientId(String clientId);
}
