package com.github.it89.cfutils.csv.uploader.parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.it89.cfutils.csv.uploader.ParseException;
import com.github.it89.cfutils.csv.uploader.store.Candle;
import com.github.it89.cfutils.csv.uploader.store.IndexValue;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@UtilityClass
@Slf4j
public class IndexValuesCsvParser {
    public static List<IndexValue> parse(byte[] fileBytes, ZoneId zoneId) {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        mapper.findAndRegisterModules();
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);

        try (MappingIterator<IndexValueCsvDto> mappingIterator = mapper
                .readerFor(IndexValueCsvDto.class)
                .with(bootstrapSchema)
                .readValues(fileBytes)) {

            List<IndexValueCsvDto> csvValues = mappingIterator.readAll();
            return csvValues.stream()
                    .map(it -> IndexValuesCsvParser.convert(it, zoneId))
                    .toList();
        } catch (IOException e) {
            log.error("Failed to parse CSV", e);
            throw new ParseException("Failed to parse CSV", e);
        }
    }

    private static IndexValue convert(IndexValueCsvDto csv, ZoneId zoneId) {
        return IndexValue.builder()
                .instant(getOpenTime(csv.getDate(), zoneId))
                .value(csv.getValue())
                .build();
    }

    private Instant getOpenTime(String value, ZoneId zoneId) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atStartOfDay().atZone(zoneId).toInstant();
    }
}
