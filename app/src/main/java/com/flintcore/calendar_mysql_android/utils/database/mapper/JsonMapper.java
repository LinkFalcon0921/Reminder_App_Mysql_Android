package com.flintcore.calendar_mysql_android.utils.database.mapper;

import com.flintcore.calendar_mysql_android.models.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class JsonMapper {

    public static Calendar getCalendarJson(JSONObject calendarJson) throws JSONException {
        Calendar calendar = new Calendar();

        calendar.setId(calendarJson.getInt(Calendar.Fields.ID.asString()));

        calendar.setSubject(calendarJson.getString(Calendar.Fields.SUBJECT.asString()));

        String dateString = calendarJson.getString(Calendar.Fields.DATE_EXPECTED.asString());
        LocalDate dateExpected = LocalDate.parse(dateString);
        calendar.setDateExpected(dateExpected);

        return calendar;
    }

}
