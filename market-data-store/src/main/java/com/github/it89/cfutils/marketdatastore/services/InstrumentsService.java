package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.converters.InstrumentConverter;
import com.github.it89.cfutils.marketdatastore.entities.InstrumentEntity;
import com.github.it89.cfutils.marketdatastore.models.Instrument;
import com.github.it89.cfutils.marketdatastore.repositories.InstrumentsRepository;
import com.github.it89.cfutils.marketdatastore.requests.InstrumentsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class InstrumentsService {
    private final InstrumentsRepository instrumentsRepository;

    @Transactional
    public List<Instrument> upload(List<Instrument> instruments) {
        return instruments.stream()
                .map(this::uploadInstrument)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Instrument> search(InstrumentsFilter filter) {
        List<InstrumentEntity> entityList = new ArrayList<>();
        if (!isEmpty(filter.getFigiSet())) {
            entityList = instrumentsRepository.findAllByFigiIn(filter.getFigiSet());
        }
        if (!isEmpty(filter.getIsinSet())) {
            Set<Long> currentIdSet = entityList.stream()
                    .map(InstrumentEntity::getId)
                    .collect(Collectors.toSet());

            entityList.addAll(instrumentsRepository.findAllByIsinIn(filter.getIsinSet()).stream()
                    .filter(it -> !currentIdSet.contains(it.getId()))
                    .collect(Collectors.toList()));

        }
        return entityList.stream()
                .map(InstrumentConverter::entityToDto)
                .collect(Collectors.toList());
    }

    private Instrument uploadInstrument(Instrument instrument) {
        var entity = searchEntity(instrument).orElseGet(InstrumentEntity::new);
        updateEntity(entity, instrument);
        return InstrumentConverter.entityToDto(instrumentsRepository.save(entity));
    }

    private void updateEntity(InstrumentEntity entity, Instrument instrument) {
        entity.setFigi(instrument.getFigi());
        entity.setIsin(instrument.getIsin());
        entity.setTicker(instrument.getTicker());
        entity.setName(instrument.getName());
        entity.setCurrency(instrument.getCurrency());
        entity.setType(instrument.getType());
    }

    private Optional<InstrumentEntity> searchEntity(Instrument instrument) {
        if (isNotBlank(instrument.getFigi())) {
            return instrumentsRepository.findFirstByFigi(instrument.getFigi());
        } else {
            return instrumentsRepository.findFirstByIsinAndCurrency(instrument.getIsin(), instrument.getCurrency());
        }
    }
}
