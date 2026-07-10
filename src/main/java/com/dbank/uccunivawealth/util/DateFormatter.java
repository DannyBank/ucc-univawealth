package com.dbank.uccunivawealth.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class DateFormatter {

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    // Case-insensitive month/year formatters
    private static final DateTimeFormatter MONTH_YEAR_4 =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM yyyy")
                    .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter MONTH_YEAR_2 =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM yy")
                    .toFormatter(Locale.ENGLISH);

    private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE
    );

    private static final List<DateTimeFormatter> YEAR_MONTH_FORMATS = List.of(
            MONTH_YEAR_4,
            MONTH_YEAR_2
    );

    public static String formatDate(String input) {

        if (input == null || input.isBlank()) {
            return "";
        }

        input = input.trim();

        // Try ZonedDateTime / LocalDateTime
        for (DateTimeFormatter formatter : DATE_TIME_FORMATS) {

            try {
                ZonedDateTime zoned = ZonedDateTime.parse(input, formatter);
                return zoned.format(OUTPUT_FORMAT);
            } catch (DateTimeParseException ignored) {
            }

            try {
                LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
                return dateTime.format(OUTPUT_FORMAT);
            } catch (DateTimeParseException ignored) {
            }
        }

        // Try LocalDate
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                return date.format(OUTPUT_FORMAT);
            } catch (DateTimeParseException ignored) {
            }
        }

        // Try YearMonth
        for (DateTimeFormatter formatter : YEAR_MONTH_FORMATS) {
            try {
                YearMonth yearMonth = YearMonth.parse(input, formatter);

                // Default to the first day of the month
                return yearMonth.atDay(1).format(OUTPUT_FORMAT);

                // Or, if you prefer "Oct 2026", use:
                // return yearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy"));

            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException("Unsupported date format: " + input);
    }
}