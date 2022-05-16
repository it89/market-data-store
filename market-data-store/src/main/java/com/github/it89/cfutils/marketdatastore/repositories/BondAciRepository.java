package com.github.it89.cfutils.marketdatastore.repositories;

import com.github.it89.cfutils.marketdatastore.entities.BondAciValuesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BondAciRepository extends CrudRepository<BondAciValuesEntity, Long> {

    @Query(value = "select t.id, t.figi, t.aci_value, t.currency, t.time " +
            "from (select t.*, row_number() over (partition by t.figi order by t.time desc) rn " +
            "      from bond_aci_values t " +
            "      where t.figi in (:figiSet)) t " +
            "where t.rn = 1",
            nativeQuery = true)
    List<BondAciValuesEntity> getLastAciValues(Set<String> figiSet);
}
