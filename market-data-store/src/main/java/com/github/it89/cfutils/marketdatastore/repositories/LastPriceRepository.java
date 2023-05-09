package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.LastPriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

    @Query(value = """
            select p.*
            from last_prices p
            join instruments i on p.instrument_id = i.id
            where i.isin = :isin
            order by p.time desc
            limit 1
            """,
            nativeQuery = true)
    Optional<LastPriceEntity> findByLastByIsin(String isin);
}
