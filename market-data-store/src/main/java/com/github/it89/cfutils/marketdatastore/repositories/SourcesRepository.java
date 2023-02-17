package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.SourceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SourcesRepository extends CrudRepository<SourceEntity, Long> {
    Optional<SourceEntity> findFirstByCode(String code);
}
