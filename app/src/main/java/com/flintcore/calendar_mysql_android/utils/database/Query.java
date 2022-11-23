package com.flintcore.calendar_mysql_android.utils.database;

@FunctionalInterface
public interface Query {
    String PHP_SUFFIX = ".php";

    Query remove = () -> "remove",
            create = () -> "create",
            get = () -> "get",
            edit = () -> "edit";

    String get();
}
