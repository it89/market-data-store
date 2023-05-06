package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.InstrumentAmountInfo;
import com.github.it89.cfutils.marketdatastore.services.LastAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LastAmountController {
    private final LastAmountService lastAmountService;

    @GetMapping("/instrument/{isin}/lastAmount")
    public InstrumentAmountInfo getAmountByIsin(@PathVariable String isin) {
        return lastAmountService.getLastAmountByIsin(isin);
    }
}
