package com.github.it89.cfutils.marketdatastore.controllers;

import com.github.it89.cfutils.marketdatastore.models.Instrument;
import com.github.it89.cfutils.marketdatastore.requests.InstrumentsFilter;
import com.github.it89.cfutils.marketdatastore.services.InstrumentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instruments")
@RequiredArgsConstructor
public class InstrumentsController {
    private final InstrumentsService instrumentsService;

    @PostMapping("/upload")
    public List<Instrument> upload(@RequestBody List<Instrument> instruments) {
        return instrumentsService.upload(instruments);
    }

    @PostMapping("/search")
    public List<Instrument> search(@RequestBody InstrumentsFilter filter) {
        return instrumentsService.search(filter);
    }
}
