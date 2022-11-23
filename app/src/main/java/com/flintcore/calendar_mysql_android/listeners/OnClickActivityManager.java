package com.flintcore.calendar_mysql_android.listeners;

import android.app.Activity;
import android.view.View;

public interface OnClickActivityManager extends View.OnClickListener {
    static OnClickActivityManager getOnCloseListener(Activity activity){
        return (view) -> activity.finish();
    }
}
