package com.github.it89.cfutils.tcs.client.services;

import com.github.it89.cfutils.tcs.client.store.dto.BondInfo;
import com.github.it89.cfutils.tcs.client.store.dto.BondInstrument;
import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import com.github.it89.cfutils.tcs.client.store.feign.BondsFeignClient;
import com.github.it89.cfutils.tcs.client.store.feign.InstrumentsFeignClient;
import com.github.it89.cfutils.tcs.client.tcs.TcsInstrumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentsService {
    private final TcsInstrumentsService tcsInstrumentsService;
    private final InstrumentsFeignClient instrumentsFeignClient;
    private final BondsFeignClient bondsFeignClient;

    public void uploadCurrencies() {
        List<Instrument> instruments = tcsInstrumentsService.getIAllCurrencies();
        instrumentsFeignClient.upload(instruments);
    }

    public void uploadShares() {
        List<Instrument> instruments = tcsInstrumentsService.getAllShares();
        instrumentsFeignClient.upload(instruments);
    }

    public void uploadBonds() {
        List<BondInstrument> bondInstruments = tcsInstrumentsService.getAllBonds();
        instrumentsFeignClient.upload(bondInstruments.stream()
                .map(BondInstrument::getInstrument)
                .collect(Collectors.toList()));

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
