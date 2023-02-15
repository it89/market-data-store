package com.github.it89.cfutils.tcs.client.services;

import com.github.it89.cfutils.tcs.client.requests.InstrumentsFilter;
import com.github.it89.cfutils.tcs.client.store.dto.Instrument;
import com.github.it89.cfutils.tcs.client.store.dto.LastPrice;
import com.github.it89.cfutils.tcs.client.store.feign.InstrumentsFeignClient;
import com.github.it89.cfutils.tcs.client.store.feign.LastPricesFeignClient;
import com.github.it89.cfutils.tcs.client.tcs.TcsMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LastPricesService {
    private final TcsMarketDataService tcsMarketDataService;
    private final LastPricesFeignClient lastPricesFeignClient;
    private final InstrumentsFeignClient instrumentsFeignClient;

    public void upload(InstrumentsFilter filter) {
        final List<Instrument> instruments = instrumentsFeignClient.search(filter);
        Set<String> figiSet = instruments.stream()
                .map(Instrument::getFigi)
                .collect(Collectors.toSet());
        upload(figiSet);
    }

    private void upload(Set<String> figiSet) {
        Map<String, LastPrice> figiLastPriceMap = tcsMarketDataService.getLastPrices(figiSet);
        lastPricesFeignClient.upload(figiLastPriceMap);
    }
}
