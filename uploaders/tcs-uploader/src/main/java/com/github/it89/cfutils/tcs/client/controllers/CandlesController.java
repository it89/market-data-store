package com.github.it89.cfutils.tcs.client.controllers;

import com.github.it89.cfutils.tcs.client.services.CandlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/candles")
@RequiredArgsConstructor
public class CandlesController {
    private final CandlesService candlesService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadCandles(
            @RequestParam String figi, @RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
        candlesService.uploadCandles(figi, dateFrom, dateTo);
    }
}
