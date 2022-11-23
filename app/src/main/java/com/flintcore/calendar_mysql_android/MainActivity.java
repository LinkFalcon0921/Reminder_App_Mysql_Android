package com.flintcore.calendar_mysql_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flintcore.calendar_mysql_android.databinding.ActivityMainBinding;
import com.flintcore.calendar_mysql_android.listeners.OnClickIntent;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.mainBinding.getRoot());

        this.mainBinding.btnAllSeeList.setOnClickListener(
                OnClickIntent.startIntentListener(
                        getTabListIntent())
        );

        this.mainBinding.btnAddReminder.setOnClickListener(
                OnClickIntent.startIntentListener(
                        FormCalendarActivity.createNewCalendarForm(this))
        );

        this.setRemindersActionsLayout();
    }

    private Intent getTabListIntent() {
        return new Intent(this, TabsReminderListActivity.class);
    }

    private void setRemindersActionsLayout() {
        final int DEFAULT_BOTTOM_MARGIN = 40;
        for (int childIndx = 0;
             childIndx < this.mainBinding.remindersMainActions.getChildCount() - 1;
             childIndx++) {
            ((LinearLayout.LayoutParams) this.mainBinding.remindersMainActions
                    .getChildAt(childIndx).getLayoutParams())
                    .bottomMargin = DEFAULT_BOTTOM_MARGIN;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}