package com.github.it89.cfutils.tcs.client.controllers;

import com.github.it89.cfutils.tcs.client.services.InstrumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instruments")
@RequiredArgsConstructor
public class InstrumentsController {
    private final InstrumentsService instrumentsService;

    @PostMapping("/currencies/upload")
    public void uploadCurrencies() {
        instrumentsService.uploadCurrencies();
    }

    @PostMapping("/shares/upload")
    public void uploadShares() {
        instrumentsService.uploadShares();
    }
}
