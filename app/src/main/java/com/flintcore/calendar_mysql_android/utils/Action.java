package com.flintcore.calendar_mysql_android.utils;

@FunctionalInterface
public interface Action<T> {
    void start(T value);
}
