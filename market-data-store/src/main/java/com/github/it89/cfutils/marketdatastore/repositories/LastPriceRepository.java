package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.LastPriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LastPriceRepository extends CrudRepository<LastPriceEntity, Long> {

    @Query(value = "select p.id, p.instrument_id, p.price, p.time " +
            "from (select p.*, row_number() over (partition by p.instrument_id order by p.time desc) rn " +
            "      from last_prices p " +
            "      where p.instrument_id in (:instrumentIds)) p " +
            "where p.rn = 1",
            nativeQuery = true)
    List<LastPriceEntity> getLastPrices(Set<Long> instrumentIds);
}
