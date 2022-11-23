package com.flintcore.calendar_mysql_android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

public class DateFormatter {

    private StringJoiner dateJoiner;

    public enum FormatterType {
        ENGLISH,
        LATIN_AMERICA;
    }

    public static final String DEFAULT_INT_DATE_STRING_FORMAT = "yyyyMMdd";
    public static final String DEFAULT_STRING_ENGLISH_FORMAT = "yyyy/MM/dd";
    public static final String DEFAULT_STRING_DOMINICAN_FORMAT = "dd/MM/yyyy";
    private static DateFormatter formatter;

    private final SimpleDateFormat dateFormatInt, dateFormatDefault;
    private final DateTimeFormatter dateDominicanFormat;

    private DateFormatter() {
        this.dateFormatInt = new SimpleDateFormat(DEFAULT_INT_DATE_STRING_FORMAT, Locale.getDefault());
        this.dateFormatDefault = new SimpleDateFormat(DEFAULT_STRING_ENGLISH_FORMAT, Locale.getDefault());
        this.dateDominicanFormat = DateTimeFormatter.ofPattern(DEFAULT_STRING_DOMINICAN_FORMAT);
    }

    public static synchronized DateFormatter getFormatter() {
        if (Objects.isNull(formatter)) {
            formatter = new DateFormatter();
        }

        return formatter;
    }

    public LocalDate getLocalDate(int year, int month, int day) {
        Month of = Month.values()[month];
        return LocalDate.of(year, of, day);
    }

    public synchronized String getStringLocalDateDominican(int year, int month, int day) {
        Month of = Month.values()[month];
        return LocalDate.of(year, of, day).format(this.dateDominicanFormat);
    }

    public synchronized String getStringLocalDateDominican(LocalDate localDate) {
        return localDate.format(this.dateDominicanFormat);
    }

    public synchronized LocalDate getLocalDateDominican(String stringLocalDate) {
        return LocalDate.parse(stringLocalDate, this.dateDominicanFormat);
    }

    public LocalDate getLocalDateIntegerString(String intDate) {
        try {
            Date date = this.dateFormatInt.parse(intDate);

            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException | NullPointerException e) {
            return LocalDate.now();
        }
    }

    public LocalDate getLocalDateString(String date) {
        return LocalDate.parse(date);
    }
}
