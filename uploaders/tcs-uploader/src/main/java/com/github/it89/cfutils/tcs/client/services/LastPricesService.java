package com.github.it89.cfutils.tcs.client.services;

import com.github.it89.cfutils.tcs.client.store.dto.LastPrice;
import com.github.it89.cfutils.tcs.client.store.feign.LastPricesFeignClient;
import com.github.it89.cfutils.tcs.client.tcs.TcsMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LastPricesService {
    private final TcsMarketDataService tcsMarketDataService;
    private final LastPricesFeignClient lastPricesFeignClient;

    public void upload(Set<String> figiSet) {
        Map<String, LastPrice> figiLastPriceMap = tcsMarketDataService.getLastPrices(figiSet);
        lastPricesFeignClient.upload(figiLastPriceMap);
    }
}
