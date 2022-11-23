package com.flintcore.calendar_mysql_android.models;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Locale;


public class Calendar {

    public enum Fields {
        ID,
        SUBJECT,
        DATE_EXPECTED;

        @NonNull
        public String asString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }
    }

    private long id;

    private String subject;
    private LocalDate dateExpected;

    //    Getters Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getDateExpected() {
        return dateExpected;
    }

    public void setDateExpected(LocalDate dateExpected) {
        this.dateExpected = dateExpected;
    }
}
