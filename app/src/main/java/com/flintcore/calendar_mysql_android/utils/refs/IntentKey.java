package com.flintcore.calendar_mysql_android.utils.refs;

import java.util.Locale;

public enum IntentKey {

    BTN_SAVE_TXT,
    BTN_CANCEL_TXT;

    public String asText() {
        return this.toString()
                .replace('_', '\0')
                .toLowerCase(Locale.ROOT);
    }
}
