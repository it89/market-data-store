package com.github.it89.cfutils.marketdatastore.converters;

import com.github.it89.cfutils.marketdatastore.entities.IndexInfoEntity;
import com.github.it89.cfutils.marketdatastore.models.IndexInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IndexInfoConverter {
    public static IndexInfo entityToDto(IndexInfoEntity entity) {
        return IndexInfo.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }
}
