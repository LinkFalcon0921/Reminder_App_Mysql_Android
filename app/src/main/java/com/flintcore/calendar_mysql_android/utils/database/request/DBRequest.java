package com.flintcore.calendar_mysql_android.utils.database.request;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flintcore.calendar_mysql_android.utils.ActionListenerConsumer;
import com.flintcore.calendar_mysql_android.utils.database.DBObjectRequest;
import com.flintcore.calendar_mysql_android.utils.database.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Use a @{@link Query} to save the url
 */
public abstract class DBRequest<Type, IdType, Param, ErrorParam extends String> {

    protected interface Configurations {
        String URL_DB = "jdbc:mysql://localhost:3306/android_calendar",
        // TODO : Change to the IP here
        PROTOCOL = "http://",
                URL_IP = "192.168.100.150",
                DRIVER = "com.mysql.cj.jdbc.Driver",
                TABLE = "calendar",
                USER = "root",
                PASS = "Mosquea42510";

    }

    /**
     * Listeners for the changesValues
     */

    private final Set<ActionListenerConsumer> actionsListeners;
    private boolean flagFirstRequest;

    protected DBRequest() {
        this.actionsListeners = new HashSet<>();
    }

    //    Crud methods
    public abstract void addObject(@NonNull Context context, Type entity);

    public abstract void editObject(@NonNull Context context, IdType param, Type entity);

    public abstract List<Type> getAllObjects(@NonNull Context context);

    protected abstract void loadInDatabase(@NonNull Context context);

    public abstract List<Type> getAllObjects(@NonNull Context context, Param param);

    public abstract Type getObject(@NonNull Context context, IdType id);

    public abstract Type getObject(@NonNull Context context, IdType id, Param param);

    public abstract void removeObject(@NonNull Context context, IdType id);

    protected RequestQueue getVolley(@NonNull Context context) {
        return Volley.newRequestQueue(context);
    }

    protected StringRequest buildParams(Query url, int method, Map<String, String> values,
                                        ActionResponse answer, ErrorResponse<ErrorParam> error) {
        return DBObjectRequest.createRequest(url, method, values, answer, error);
    }

    public final void activateListener() {
        this.actionsListeners.forEach(ActionListenerConsumer::consume);
    }

    public final void addListener(@NonNull ActionListenerConsumer action) {
        this.actionsListeners.add(action);
    }

    public final void removeListener(@NonNull ActionListenerConsumer action) {
        this.actionsListeners.remove(action);
    }

    @FunctionalInterface
    public interface ActionResponse {
        void start(String response);
    }

    @FunctionalInterface
    public interface ErrorResponse<T> {
        void start(T t);
    }

}
