package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.AmountInfo;
import com.github.it89.cfutils.marketdatastore.models.Instrument;
import com.github.it89.cfutils.marketdatastore.models.PriceInfo;
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
    public void upload(@RequestBody Map<String, PriceInfo> figiLastPriceMap) {
        lastPriceService.upload(figiLastPriceMap);
    }

    @PostMapping("/getAmount")
    public Map<Instrument, AmountInfo> getAmount(@RequestBody Set<String> figiSet) {
        return lastPriceAmountService.getAmount(figiSet);
    }
}
