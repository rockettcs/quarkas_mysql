package com.integra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MyService {

    @Inject
    DataRepository dataRepository;

    @Transactional
    public void performDatabaseOperations(String key, String value) {
        // Save to MySQL
        DataEntity dataEntity = new DataEntity();
        dataEntity.key = key;
        dataEntity.value = value;
        dataRepository.persist(dataEntity);
    }
}