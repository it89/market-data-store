package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.IndexInfoEntity;
import com.github.it89.cfutils.marketdatastore.entities.IndexValueEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IndexValueRepository extends CrudRepository<IndexValueEntity, Long> {
    List<IndexValueEntity> findAllByIndexInfoOrderByInstant(IndexInfoEntity indexInfoEntity);
}
