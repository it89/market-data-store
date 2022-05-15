package com.github.it89.cfutils.tcs.client.services;

import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import com.github.it89.cfutils.tcs.client.store.feign.InstrumentsFeignClient;
import com.github.it89.cfutils.tcs.client.tcs.TcsInstrumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentsService {
    private final TcsInstrumentsService tcsInstrumentsService;
    private final InstrumentsFeignClient instrumentsFeignClient;

    public void uploadCurrencies() {
        List<Instrument> instruments = tcsInstrumentsService.getIAllCurrencies();
        instrumentsFeignClient.upload(instruments);
    }

    public void uploadShares() {
        List<Instrument> instruments = tcsInstrumentsService.getAllShares();
        instrumentsFeignClient.upload(instruments);
    }
}
