package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.IndexInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndexInfoRepository extends CrudRepository<IndexInfoEntity, Long> {
    Optional<IndexInfoEntity> findByCode(String code);
}
