package com.github.it89.cfutils.marketdatastore.services;

import com.github.it89.cfutils.marketdatastore.converters.IndexInfoConverter;
import com.github.it89.cfutils.marketdatastore.converters.IndexValueConverter;
import com.github.it89.cfutils.marketdatastore.entities.IndexInfoEntity;
import com.github.it89.cfutils.marketdatastore.entities.IndexValueEntity;
import com.github.it89.cfutils.marketdatastore.entities.SourceEntity;
import com.github.it89.cfutils.marketdatastore.exceptions.IndexInfoNotFoundException;
import com.github.it89.cfutils.marketdatastore.models.IndexInfo;
import com.github.it89.cfutils.marketdatastore.models.IndexValue;
import com.github.it89.cfutils.marketdatastore.repositories.IndexInfoRepository;
import com.github.it89.cfutils.marketdatastore.repositories.IndexValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndexesService {
    private final SourcesService sourcesService;
    private final IndexInfoRepository indexInfoRepository;
    private final IndexValueRepository indexValueRepository;

    @Transactional
    public IndexInfo createIndex(IndexInfo indexInfo) {
        IndexInfoEntity entity = new IndexInfoEntity();
        entity.setCode(indexInfo.getCode());
        entity.setName(indexInfo.getName());

        return IndexInfoConverter.entityToDto(indexInfoRepository.save(entity));
    }

    @Transactional
    public List<IndexValue> getValues(String indexCode) {
        IndexInfoEntity indexInfoEntity = indexInfoRepository.findByCode(indexCode)
                .orElseThrow(IndexInfoNotFoundException::new);

        return indexValueRepository.findAllByIndexInfoOrderByInstant(indexInfoEntity).stream()
                .map(IndexValueConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void uploadValues(String indexCode, List<IndexValue> values, String source) {
        IndexInfoEntity indexInfoEntity = indexInfoRepository.findByCode(indexCode)
                .orElseThrow(IndexInfoNotFoundException::new);

        SourceEntity sourceEntity = sourcesService.register(source);

        Map<Instant, IndexValueEntity> entityMap = indexValueRepository
                .findAllByIndexInfoOrderByInstant(indexInfoEntity)
                .stream().collect(Collectors.toMap(IndexValueEntity::getInstant, v -> v));

        updateValues(values, entityMap, indexInfoEntity, sourceEntity);
    }

    private void updateValues(List<IndexValue> values,
                              Map<Instant, IndexValueEntity> entityMap,
                              IndexInfoEntity indexInfoEntity,
                              SourceEntity sourceEntity) {
        List<IndexValueEntity> changedValues = new ArrayList<>();

        for (IndexValue value : values) {
            if (entityMap.containsKey(value.getInstant())) {
                IndexValueEntity entity = entityMap.get(value.getInstant());
                if (!entity.getValue().equals(value.getValue())) {
                    entity.setValue(value.getValue());
                    entity.setSource(sourceEntity);

                    changedValues.add(entity);
                }
            } else {
                IndexValueEntity entity = new IndexValueEntity();
                entity.setIndexInfo(indexInfoEntity);
                entity.setInstant(value.getInstant());
                entity.setValue(value.getValue());
                entity.setSource(sourceEntity);

                changedValues.add(entity);
            }
        }

        indexValueRepository.saveAll(changedValues);
    }
}
