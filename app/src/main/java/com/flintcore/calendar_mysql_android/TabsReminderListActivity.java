package com.flintcore.calendar_mysql_android;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.flintcore.calendar_mysql_android.adapaters.CalendarRecyclerAdapter;
import com.flintcore.calendar_mysql_android.databinding.ActivityTabReminderListBinding;
import com.flintcore.calendar_mysql_android.tabs.TabManager;
import com.flintcore.calendar_mysql_android.tabs.view.ListTab;
import com.flintcore.calendar_mysql_android.utils.database.request.DBCalendarRequest;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.HashSet;

public class TabsReminderListActivity extends AppCompatActivity {

    private TabManager tabsManager;
    private ActivityTabReminderListBinding reminderListBinding;
    private HashSet<ListTab> tabHashSet;
    private DBCalendarRequest calendarRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendarRequest = DBCalendarRequest.getInstance(this.getBaseContext());

        this.reminderListBinding = ActivityTabReminderListBinding.inflate(getLayoutInflater());
        setContentView(this.reminderListBinding.getRoot());

        setTabManager(this.reminderListBinding.tabsPager,
                this.reminderListBinding.tabReminderLayout);
    }

    private void setTabManager(ViewPager2 pager2, TabLayout tab_layout_manager) {

        this.tabsManager = new TabManager(getSupportFragmentManager(),
                getLifecycle(),
                pager2,
                tab_layout_manager);

        ListTab listTabToday = new ListTab(CalendarRecyclerAdapter.RecyclerType.TODAY_NOTES);
        ListTab listTabAll = new ListTab(CalendarRecyclerAdapter.RecyclerType.ALL_NOTES);
        this.tabsManager.createTab("Hoy", listTabToday);
        this.tabsManager.createTab("Todos", listTabAll);

        this.tabsManager.setListeners();
        setViewTabConsumer(listTabToday, listTabAll);
    }

    private void setViewTabConsumer(ListTab... listTabs) {
        tabHashSet = new HashSet<>(Arrays.asList(listTabs));
//        this.tabHashSet.stream().map(ListTab::getDataChangedConsumer)
//                .forEach(this.calendarRequest::addListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //    Validate after view is destroyed, start a request to database.
        DBCalendarRequest.getInstance(this).requestUpdate();
        this.tabHashSet.stream().map(ListTab::getDataChangedConsumer)
                .forEach(this.calendarRequest::removeListener);
    }
}