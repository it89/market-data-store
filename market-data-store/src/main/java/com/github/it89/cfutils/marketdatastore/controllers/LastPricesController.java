package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.LastPrice;
import com.github.it89.cfutils.marketdatastore.models.MonetaryAmount;
import com.github.it89.cfutils.marketdatastore.services.LastPriceAmountService;
import com.github.it89.cfutils.marketdatastore.services.LastPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/lastPrices")
@RequiredArgsConstructor
public class LastPricesController {
    private final LastPriceService lastPriceService;
    private final LastPriceAmountService lastPriceAmountService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestBody Map<String, LastPrice> figiLastPriceMap) {
        lastPriceService.upload(figiLastPriceMap);
    }

    @PostMapping("/getAmount")
    public Map<String, MonetaryAmount> getAmount(@RequestBody Set<String> figiSet) {
        return lastPriceAmountService.getAmount(figiSet);
    }
}
