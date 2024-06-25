package com.integra;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataRepository implements PanacheRepository<DataEntity> {

    public DataEntity findByKey(String key) {
        return find("key", key).firstResult();
    }
}