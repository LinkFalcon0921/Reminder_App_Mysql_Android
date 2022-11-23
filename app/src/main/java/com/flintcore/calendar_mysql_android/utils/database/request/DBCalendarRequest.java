package com.flintcore.calendar_mysql_android.utils.database.request;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.flintcore.calendar_mysql_android.R;
import com.flintcore.calendar_mysql_android.adapaters.CalendarRecyclerAdapter;
import com.flintcore.calendar_mysql_android.models.Calendar;
import com.flintcore.calendar_mysql_android.utils.database.Query;
import com.flintcore.calendar_mysql_android.utils.database.QueryUtils;
import com.flintcore.calendar_mysql_android.utils.database.mapper.JsonMapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DATABASE REQUEST FOR PHP API REST.
 */
public class DBCalendarRequest extends DBRequest<Calendar, Long, LocalDate, String> {

    public static final String API_REST_MYSQL_CALENDAR = "/api_rest_msql/calendar";
    //    Change the ip/host here
    private static final Query URL_LOCATION = () ->
            String.join("", Configurations.PROTOCOL, Configurations.URL_IP,
                    API_REST_MYSQL_CALENDAR);

    private static DBCalendarRequest calendarRequest;

    private boolean flagUpdated;
    private final List<Calendar> notes;

    private DBCalendarRequest() {
        super();
        this.notes = new ArrayList<>();
        this.flagUpdated = false;
    }

    public static DBCalendarRequest getInstance(Context context) {
        if (Objects.isNull(calendarRequest)) {
            calendarRequest = new DBCalendarRequest();
            calendarRequest.loadInDatabase(context);
        }

        return calendarRequest;
    }

    @Override
    public void addObject(Context context, Calendar calendarObj) {
        try {
            ActionResponse response = (result) -> {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    if (jsonArray.length() == 1) {
                        throw new JSONException("Fail by not insertion");
                    }

                    this.requestUpdate();
                    this.loadInDatabase(context)
                    ;
                    this.activateListener();

                    Toast.makeText(context, R.string.reminder_added_msg, Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                } catch (JSONException e) {
                    Toast.makeText(context, R.string.error_send_reminder_msg, Toast.LENGTH_LONG).show();
                }
            };
            ErrorResponse<String> error = fail -> {
//                Toast
                Toast.makeText(context, R.string.error_send_reminder_msg, Toast.LENGTH_LONG).show();
            };

            Map<String, String> params = new HashMap<>();
            params.put(Calendar.Fields.SUBJECT.asString(), calendarObj.getSubject());
            params.put(Calendar.Fields.DATE_EXPECTED.asString(), calendarObj.getDateExpected().toString());

            RequestQueue requestQueue = getVolley(context);
            Query url = QueryUtils.concat(URL_LOCATION, Query.create);
            StringRequest request = this.buildParams(url, POST, params, response, error);

            requestQueue.add(request);
            requestQueue.start();

        } catch (Exception e) {
            Toast.makeText(context, R.string.fail_request_code_reminder, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void editObject(Context context, Long id, Calendar objRequested) {

        ActionResponse response = (result) -> {
            try {
                JSONArray resultArray = new JSONArray(result);

                if (resultArray.length() == 1) {
                    Toast.makeText(context, "No fue posible editar al usuario", Toast.LENGTH_LONG).show();
                    return;
                }
                this.requestUpdate();
                this.loadInDatabase(context);

                this.activateListener();
                Toast.makeText(context, R.string.reminder_edited_msg, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
//                Toast.
                e.printStackTrace();
            }
        };
        ErrorResponse<String> error = System.out::println;

        Map<String, String> params = new HashMap<>();
        params.put(Calendar.Fields.ID.asString(), Long.toString(id));
        params.put(Calendar.Fields.SUBJECT.asString(), objRequested.getSubject());
        params.put(Calendar.Fields.DATE_EXPECTED.asString(), objRequested.getDateExpected().toString());

        RequestQueue requestQueue = getVolley(context);
        Query url = QueryUtils.concat(URL_LOCATION, Query.edit);
        StringRequest request = this.buildParams(url, POST, params, response, error);

        requestQueue.add(request);
        requestQueue.start();
    }

    @Override
    public List<Calendar> getAllObjects(Context context) {
        this.loadInDatabase(context);

        return this.notes;
    }

    //    Method get all : Used for all get request for get all values
    @Override
    protected synchronized void loadInDatabase(Context context) {
        if (!this.notes.isEmpty() && this.flagUpdated) {
            return;
        }

        this.notes.clear();
        ActionResponse response = (result) -> {
            try {
                JSONArray expectedCalendars = new JSONArray(result);

                for (int index = 0; index < expectedCalendars.length(); index++) {
                    JSONObject calendarJson = expectedCalendars.getJSONObject(index);

                    Calendar calendar = JsonMapper.getCalendarJson(calendarJson);

                    this.notes.add(calendar);
                }

                this.flagUpdated = true;

                this.activateListener();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        ErrorResponse<String> error = System.out::println;

        RequestQueue volley = getVolley(context);
        Query url = QueryUtils.concat(URL_LOCATION, Query.get);
        StringRequest request = this.buildParams(url, GET, null, response, error);

        volley.add(request);
        volley.start();
    }

    @Override
    public List<Calendar> getAllObjects(@NotNull Context context, LocalDate localDate) {
        this.loadInDatabase(context);

        return getListByLocalDate(localDate);
    }

    @Override
    public Calendar getObject(Context context, Long id) {
        return this.getAllObjects(context).stream()
                .filter(cld -> cld.getId() == id)
                .findFirst().orElse(new Calendar());
    }

    @Override
    public Calendar getObject(Context context, Long id, @NonNull LocalDate localDate) {
        return this.getAllObjects(context, localDate).stream()
                .filter(cld -> cld.getId() == id)
                .findFirst().orElse(new Calendar());
    }

    @Override
    public void removeObject(Context context, Long id) {
        ActionResponse response = (result) -> {
            if (Long.parseLong(result) == -1) {
                Toast.makeText(context, R.string.no_deleted_calendar_msg, Toast.LENGTH_SHORT)
                        .show();
            }

            this.requestUpdate();
            this.loadInDatabase(context);

            if (context instanceof Activity) {
                ((Activity) context).finish();
            }

            this.activateListener();
            Toast.makeText(context, R.string.deleted_calendar_msg, Toast.LENGTH_SHORT)
                    .show();
        };
        ErrorResponse<String> error = System.out::println;

        Map<String, String> params = new HashMap<>();
        params.put(Calendar.Fields.ID.asString(), id.toString());

        RequestQueue requestQueue = getVolley(context);
        Query url = QueryUtils.concat(URL_LOCATION, Query.remove);
        StringRequest request = this.buildParams(url, POST, params, response, error);

        requestQueue.add(request);
        requestQueue.start();
    }

    public int getLength(CalendarRecyclerAdapter.RecyclerType type, @Nullable LocalDate date) {

        switch (type) {
            case TODAY_NOTES:
                return getListByLocalDate(date).size();

            default:
                return this.notes.size();
        }
    }

    public Calendar getObject(int position) {
        return this.notes.get(position);
    }

    public Calendar getObject(int position, LocalDate localDate) {
        List<Calendar> byLocalDate = this.getListByLocalDate(localDate);
        return byLocalDate.get(position);
    }

    public void requestUpdate() {
        this.flagUpdated = false;
    }

    private List<Calendar> getListByLocalDate(LocalDate localDate) {
        return this.notes.stream()
                .filter(cld -> cld.getDateExpected().equals(localDate))
                .collect(Collectors.toList());
    }

    @Deprecated
    private void updateValuesInPosition(Long id, Calendar obj) {
        this.notes.stream()
                .filter(cld -> cld.getId() == id)
                .findFirst()
                .ifPresent(calendar -> {
                    calendar.setSubject(obj.getSubject());
                    calendar.setDateExpected(obj.getDateExpected());
                });
    }
}
