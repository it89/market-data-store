package com.github.it89.cfutils.tcs.client.tcs;

import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Currency;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InstrumentsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TcsInstrumentsService {
    private final InstrumentsService investApiInstrumentsService;

    public List<Instrument> getIAllCurrencies() {
        log.info("Get currencies from TCS");
        List<Currency> allCurrencies = investApiInstrumentsService.getAllCurrenciesSync();
        return allCurrencies.stream()
                .map(InstrumentsConverter::currencyToInstrument)
                .collect(Collectors.toList());
    }

    public List<Instrument> getAllShares() {
        log.info("Get shares from TCS");
        List<Share> allShares = investApiInstrumentsService.getAllSharesSync();
        return allShares.stream()
                .map(InstrumentsConverter::shareToInstrument)
                .collect(Collectors.toList());
    }
}
