package com.flintcore.calendar_mysql_android.listeners;

import android.content.Intent;
import android.view.View;

public interface OnClickIntent extends View.OnClickListener {

    static OnClickIntent startIntentListener(Intent intent){
       return (view) -> view.getContext().startActivity(intent);
    }
}
