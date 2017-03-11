package com.github.aleksandermielczarek.objectboxexample.domain.converter;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import io.objectbox.converter.PropertyConverter;

/**
 * Created by Aleksander Mielczarek on 11.03.2017.
 */

public class LocalDateTimeConverter implements PropertyConverter<LocalDateTime, String> {

    @Override
    public LocalDateTime convertToEntityProperty(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String convertToDatabaseValue(LocalDateTime localDateTime) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);
    }
}
