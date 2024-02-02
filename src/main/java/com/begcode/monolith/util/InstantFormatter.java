package com.begcode.monolith.util;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.Formatter;

public class InstantFormatter implements Formatter<Instant> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public @NotNull Instant parse(@NotNull String text, Locale locale) {
        return LocalDateTime.parse(text, formatter).atZone(ZoneId.systemDefault()).toInstant();
    }

    @Override
    public @NotNull String print(@NotNull Instant instant, Locale locale) {
        return formatter.format(instant);
    }
}
