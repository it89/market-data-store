package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.entities.SourceEntity;
import com.github.it89.cfutils.marketdatastore.repositories.SourcesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class SourcesService {
    private final SourcesRepository sourcesRepository;

    SourceEntity register(String code) {
        if (isBlank(code)) {
            return null;
        }
        final Optional<SourceEntity> currentEntity = sourcesRepository.findFirstByCode(code);
        if (currentEntity.isPresent()) {
            return currentEntity.get();
        } else {
            log.info("Register new source {}", code);
            SourceEntity entity = new SourceEntity();
            entity.setCode(code);
            return sourcesRepository.save(entity);
        }
    }
}
