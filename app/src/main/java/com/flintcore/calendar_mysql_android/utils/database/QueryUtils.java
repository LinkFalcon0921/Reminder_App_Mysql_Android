package com.flintcore.calendar_mysql_android.utils.database;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class QueryUtils {

    public static Query concat(Query... queries) {
        return () -> Arrays.stream(queries).map(Query::get)
                .collect(Collectors.joining("/"))
                .concat(Query.PHP_SUFFIX);
    }

}
