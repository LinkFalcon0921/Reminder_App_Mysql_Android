package com.flintcore.calendar_mysql_android.utils.alerts;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flintcore.calendar_mysql_android.R;

public class AlertDefaultSelection {

    @FunctionalInterface
    public interface CallOnClick {
        void doAction();
    }

    public static void createAndShowAlert(Context context, int message, CallOnClick positive, CallOnClick negative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.default_view_alert,
                null, false);

        ((TextView) view.findViewById(R.id.info_tv)).setText(message);

        builder.setView(view)
                .setPositiveButton(R.string.yes_option_alert_txt, (dialogInterface, i) -> {
                    positive.doAction();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.no_option_alert_txt, (dialogInterface, i) -> {
                    negative.doAction();
                    dialogInterface.dismiss();
                })
                .show();
    }

}
