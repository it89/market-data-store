package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.BondNominalValueEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BondNominalRepository extends CrudRepository<BondNominalValueEntity, Long> {

    @Query(value = "select t.id, t.instrument_id, t.nominal_value, t.currency, t.time " +
            "from (select t.*, row_number() over (partition by t.instrument_id order by t.time desc) rn " +
            "      from bond_nominal_values t " +
            "      where t.instrument_id in (:instrumentIds)) t " +
            "where t.rn = 1",
            nativeQuery = true)
    List<BondNominalValueEntity> getLastNominalValues(Set<Long> instrumentIds);

    @Query(value = """
            select v.*
            from bond_nominal_values v
            join instruments i on v.instrument_id = i.id
            where i.isin = :isin
            order by v.time desc
            limit 1
            """,
            nativeQuery = true)
    Optional<BondNominalValueEntity> findByLastByIsin(String isin);
}
