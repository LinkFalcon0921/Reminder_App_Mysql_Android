package com.flintcore.calendar_mysql_android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flintcore.calendar_mysql_android.databinding.ActivityFormCalendarBinding;
import com.flintcore.calendar_mysql_android.listeners.OnClickActivityManager;
import com.flintcore.calendar_mysql_android.models.Calendar;
import com.flintcore.calendar_mysql_android.utils.DateFormatter;
import com.flintcore.calendar_mysql_android.utils.alerts.AlertDefaultSelection;
import com.flintcore.calendar_mysql_android.utils.database.request.DBCalendarRequest;
import com.flintcore.calendar_mysql_android.utils.refs.IntentKey;

import java.time.LocalDate;
import java.util.Objects;

public class FormCalendarActivity extends AppCompatActivity {

    private DateFormatter dateFormatter;
    private ActivityFormCalendarBinding viewDataBinding;
    private DBCalendarRequest calendarRequester;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewDataBinding = ActivityFormCalendarBinding
                .inflate(getLayoutInflater());

        setContentView(this.viewDataBinding.getRoot());

        calendarRequester = DBCalendarRequest.getInstance(this);
        this.dateFormatter = DateFormatter.getFormatter();


        builtButtons();
        setInfoReminderSelected();

    }

    private void setInfoReminderSelected() {
        String key = Calendar.Fields.ID.asString();
        boolean containsKey = getIntent().getExtras()
                .containsKey(key);

        this.viewDataBinding.datePickerTv.setOnClickListener(this::showDatePicker);

        if (!containsKey) {
            return;
        }

        try {
            Calendar calendarSelected = this.calendarRequester.getObject(this,
                    getIntent().getExtras().getLong(key));

            this.viewDataBinding.subjectTxt.setText(calendarSelected.getSubject());
            String localDateExpected = dateFormatter.getStringLocalDateDominican(calendarSelected.getDateExpected());
            this.viewDataBinding.datePickerTv.setText(localDateExpected);
        } catch (RuntimeException e) {
            this.finish();
        }

    }

    private void showDatePicker(View view) {

        java.util.Calendar calendarObj = java.util.Calendar.getInstance();

        if (Objects.isNull(this.datePickerDialog)) {
            this.datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    new DateCalendarPickerListener(),
                    calendarObj.get(java.util.Calendar.YEAR),
                    calendarObj.get(java.util.Calendar.MONTH),
                    calendarObj.get(java.util.Calendar.DAY_OF_MONTH)
            );
            this.datePickerDialog.setTitle(getString(R.string.add_date_calendar_msg));
        }
        this.datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        this.datePickerDialog.show();
    }

    private void showDatePicker(View view, LocalDate localDate) {

//      Set date picker
        if (Objects.isNull(this.datePickerDialog)) {
            datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    new DateCalendarPickerListener(),
                    localDate.getYear(),
                    localDate.getMonthValue(),
                    localDate.getDayOfMonth()
            );
            datePickerDialog.setTitle(getString(R.string.add_date_calendar_msg));
        }

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void builtButtons() {
        //        Button texts
        int btn_form_title = getIntent().getExtras()
                .getInt(IntentKey.BTN_SAVE_TXT.asText());

        this.viewDataBinding.btnSend.setText(btn_form_title);

        //        Button texts
        btn_form_title = getIntent().getExtras()
                .getInt(IntentKey.BTN_CANCEL_TXT.asText());

        this.viewDataBinding.btnCancel.setText(btn_form_title);

        setButtonsListeners();
    }

    private void setButtonsListeners() {
        boolean containsKeySave = getIntent().getExtras()
                .getInt(IntentKey.BTN_SAVE_TXT.asText()) == R.string.save_txt;

        if (containsKeySave) {
            this.viewDataBinding.btnCancel
                    .setOnClickListener(OnClickActivityManager.getOnCloseListener(this));

            View.OnClickListener saveClickListener = (view) -> {
                String trimInfoDate = this.viewDataBinding.datePickerTv.getText().
                        toString().trim();

                if (trimInfoDate.isEmpty()) {
                    Toast.makeText(this, R.string.invalid_date_msg, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                String trimInfoSubject = this.viewDataBinding.subjectTxt.getText().
                        toString().trim();
                if (trimInfoSubject.isEmpty()) {
                    Toast.makeText(this, R.string.invalid_subject_msg, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Calendar calendar = new Calendar();

                calendar.setSubject(trimInfoSubject);
                calendar.setDateExpected(this.dateFormatter.getLocalDateDominican(trimInfoDate));

                calendarRequester.addObject(this, calendar);
            };
            this.viewDataBinding.btnSend
                    .setOnClickListener(saveClickListener);

        } else {
            View.OnClickListener removeClickListener = (view) -> {
                long idObject = getIntent().getExtras()
                        .getLong(Calendar.Fields.ID.asString());

//                Alert handle
                AlertDefaultSelection.createAndShowAlert(this, R.string.validate_delete_calendar_msg,
                        () -> DBCalendarRequest.getInstance(this).removeObject(this, idObject),
                        () -> {
                        });
            };
            this.viewDataBinding.btnCancel
                    .setOnClickListener(removeClickListener);

            View.OnClickListener editClickListener = (view) -> {
//                Date validation
                String trimInfoDate = this.viewDataBinding.datePickerTv.getText().
                        toString().trim();

                if (trimInfoDate.isEmpty()) {
                    Toast.makeText(this, R.string.invalid_date_msg, Toast.LENGTH_SHORT)
                            .show();
                }

//                Subject validation
                String trimInfoSubject = this.viewDataBinding.subjectTxt.getText().
                        toString().trim();

                if (trimInfoSubject.isEmpty()) {
                    Toast.makeText(this, R.string.invalid_subject_msg, Toast.LENGTH_SHORT)
                            .show();
                }

                Calendar calendar = new Calendar();

                long idObject = getIntent().getExtras()
                        .getLong(Calendar.Fields.ID.asString());

                calendar.setId(idObject);
                calendar.setSubject(trimInfoSubject);
                calendar.setDateExpected(this.dateFormatter.getLocalDateDominican(trimInfoDate));

                calendarRequester.editObject(this, idObject, calendar);
            };
            this.viewDataBinding.btnSend
                    .setOnClickListener(editClickListener);
        }
    }

    public static Intent createNewCalendarForm(Context context) {
        return getFormIntent(context, R.string.save_txt, R.string.cancel_txt);
    }

    public static Intent createEditCalendarForm(Context context, long id) {
        Intent formIntent = getFormIntent(context, R.string.edit_txt, R.string.delete_txt);
        formIntent.putExtra(Calendar.Fields.ID.asString(), id);

        return formIntent;
    }

    @NonNull
    private static Intent getFormIntent(Context context, int id_save_string, int id_cancel_string) {
        Intent formIntent = new Intent(context, FormCalendarActivity.class);

        formIntent.putExtra(IntentKey.BTN_SAVE_TXT.asText(), id_save_string);
        formIntent.putExtra(IntentKey.BTN_CANCEL_TXT.asText(), id_cancel_string);
        return formIntent;
    }


    //Date picker listener
    private class DateCalendarPickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String dateJoining = FormCalendarActivity.this.dateFormatter
                    .getStringLocalDateDominican(year, month, day);

            FormCalendarActivity.this.viewDataBinding
                    .datePickerTv
                    .setText(dateJoining);
        }
    }

}