package com.epam.esm.dao.util;

import javax.persistence.AttributeConverter;
import java.time.Duration;

public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return duration.toDays();
    }

    @Override
    public Duration convertToEntityAttribute(Long aLong) {
        return Duration.ofDays(aLong);
    }
}
