package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.converters.InstrumentConverter;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.exceptions.InstrumentNotFoundException;
import com.github.it89.cfutils.marketdatastore.models.Instrument;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class InstrumentsService {
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public Instrument findByFigi(String figi) {
        var entity = instrumentsRepository.findById(figi)
                .orElseThrow(InstrumentNotFoundException::new);
        return InstrumentConverter.entityToDto(entity);
    }

    @Transactional
    public List<Instrument> upload(List<Instrument> instruments) {
        var entityList = StreamSupport.stream(instrumentsRepository.findAllById(
                        instruments.stream()
                                .map(Instrument::getFigi)
                                .collect(Collectors.toSet())).spliterator(), false)
                .collect(Collectors.toList());

        instruments.forEach(it -> uploadInstrument(it, entityList));

        return entityList.stream()
                .map(InstrumentConverter::entityToDto)
                .collect(Collectors.toList());
    }

    private void uploadInstrument(Instrument instrument, List<InstrumentEntity> entityList) {
        var entity = entityList.stream()
                .filter(it -> instrument.getFigi().equals(it.getFigi()))
                .findAny()
                .orElse(createEntity(instrument.getFigi()));
        updateEntity(entity, instrument);
        instrumentsRepository.save(entity);
    }

    private InstrumentEntity createEntity(String figi) {
        var entity = new InstrumentEntity();
        entity.setFigi(figi);
        return entity;
    }

    private void updateEntity(InstrumentEntity entity, Instrument instrument) {
        entity.setIsin(instrument.getIsin());
        entity.setTicker(instrument.getTicker());
        entity.setName(instrument.getName());
        entity.setCurrency(instrument.getCurrency());
    }
}
