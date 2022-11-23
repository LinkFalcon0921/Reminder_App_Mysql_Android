package com.flintcore.calendar_mysql_android.utils.database;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.flintcore.calendar_mysql_android.utils.database.request.DBRequest;

import java.util.Map;

public class DBObjectRequest {

    /**
     * Build a StringRequest by the params and the error message.
     */
    public static <R extends String> StringRequest createRequest(Query url, int method, Map<String, String> params,
                                              DBRequest.ActionResponse answer,
                                              DBRequest.ErrorResponse<R> error) {
        return new StringRequest(method, url.get(),
                response -> answer.start(response),
                error1 -> error.start((R) error1.getMessage())
        ) {
            @Nullable
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

}
