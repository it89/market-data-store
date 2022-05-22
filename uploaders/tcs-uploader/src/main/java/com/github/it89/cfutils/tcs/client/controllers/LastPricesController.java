package com.github.it89.cfutils.tcs.client.controllers;

import com.github.it89.cfutils.tcs.client.requests.InstrumentsFilter;
import com.github.it89.cfutils.tcs.client.services.LastPricesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lastPrices")
@RequiredArgsConstructor
public class LastPricesController {
    private final LastPricesService lastPricesService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestBody InstrumentsFilter filter) {
        lastPricesService.upload(filter);
    }
}
