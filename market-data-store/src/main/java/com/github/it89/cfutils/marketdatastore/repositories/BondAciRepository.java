package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.BondAciValueEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BondAciRepository extends CrudRepository<BondAciValueEntity, Long> {

    @Query(value = "select t.id, t.instrument_id, t.aci_value, t.currency, t.time " +
            "from (select t.*, row_number() over (partition by t.instrument_id order by t.time desc) rn " +
            "      from bond_aci_values t " +
            "      where t.instrument_id in (:instrumentIds)) t " +
            "where t.rn = 1",
            nativeQuery = true)
    List<BondAciValueEntity> getLastAciValues(Set<Long> instrumentIds);

    @Query(value = """
            select v.*
            from bond_aci_values v
            join instruments i on v.instrument_id = i.id
            where i.isin = :isin
            order by v.time desc
            limit 1
            """,
            nativeQuery = true)
    Optional<BondAciValueEntity> findByLastByIsin(String isin);
}
