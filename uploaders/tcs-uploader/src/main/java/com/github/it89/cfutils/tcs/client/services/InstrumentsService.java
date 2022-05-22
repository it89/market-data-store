package com.github.it89.cfutils.tcs.client.services;

import com.github.it89.cfutils.tcs.client.requests.InstrumentsFilter;
import com.github.it89.cfutils.tcs.client.store.dto.BondInfo;
import com.github.it89.cfutils.tcs.client.store.dto.BondInstrument;
import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import com.github.it89.cfutils.tcs.client.store.feign.BondsFeignClient;
import com.github.it89.cfutils.tcs.client.store.feign.InstrumentsFeignClient;
import com.github.it89.cfutils.tcs.client.tcs.TcsInstrumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class InstrumentsService {
    private final TcsInstrumentsService tcsInstrumentsService;
    private final InstrumentsFeignClient instrumentsFeignClient;
    private final BondsFeignClient bondsFeignClient;

    public List<Instrument> upload(InstrumentsFilter filter) {
        List<Instrument> instruments = getCurrencies(filter.getFigiSet());
        instruments.addAll(getShares(filter.getFigiSet(), filter.getIsinSet()));
        instruments.addAll(getEtfs(filter.getFigiSet(), filter.getIsinSet()));

        List<BondInstrument> bondInstruments = getBonds(filter.getFigiSet(), filter.getIsinSet());
        instruments.addAll(bondInstruments.stream()
                .map(BondInstrument::getInstrument)
                .collect(Collectors.toList()));

        instrumentsFeignClient.upload(instruments);
        uploadBondInfo(bondInstruments);
        return instruments;
    }

    private List<Instrument> getCurrencies(Set<String> figiSet) {
        return tcsInstrumentsService.getIAllCurrencies().stream()
                .filter(it -> isInstrumentFromTheRequest(it, figiSet, Collections.emptySet()))
                .collect(Collectors.toList());
    }

    private List<Instrument> getShares(Set<String> figiSet, Set<String> isinSet) {
        return tcsInstrumentsService.getAllShares().stream()
                .filter(it -> isInstrumentFromTheRequest(it, figiSet, isinSet))
                .collect(Collectors.toList());
    }

    private List<Instrument> getEtfs(Set<String> figiSet, Set<String> isinSet) {
        return tcsInstrumentsService.getAllEtfs().stream()
                .filter(it -> isInstrumentFromTheRequest(it, figiSet, isinSet))
                .collect(Collectors.toList());
    }

    private List<BondInstrument> getBonds(Set<String> figiSet, Set<String> isinSet) {
        return tcsInstrumentsService.getAllBonds().stream()
                .filter(it -> isInstrumentFromTheRequest(it.getInstrument(), figiSet, isinSet))
                .collect(Collectors.toList());
    }

    private boolean isInstrumentFromTheRequest(Instrument instrument, Set<String> figiSet, Set<String> isinSet) {
        if (instrument.getFigi() != null && !isEmpty(figiSet) && figiSet.contains(instrument.getFigi())) {
            return true;
        }
        if (instrument.getIsin() != null && !isEmpty(isinSet)) {
            return isinSet.contains(instrument.getIsin());
        }
        return false;
    }

    private void uploadBondInfo(List<BondInstrument> bondInstruments) {
        Instant time = Instant.now();

        Map<String, BondInfo> figiBondInfo = bondInstruments.stream()
                .collect(Collectors.toMap(
                        k -> k.getInstrument().getFigi(),
                        v -> BondInfo.builder()
                                .nominalValue(v.getNominalValue())
                                .nominalCurrency(v.getNominalCurrency())
                                .aciValue(v.getAciValue())
                                .aciCurrency(v.getAciCurrency())
                                .time(time)
                                .build()));

        bondsFeignClient.upload(figiBondInfo);
    }
}
