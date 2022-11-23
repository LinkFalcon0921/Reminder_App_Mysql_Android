package com.flintcore.calendar_mysql_android.tabs.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flintcore.calendar_mysql_android.R;
import com.flintcore.calendar_mysql_android.adapaters.CalendarRecyclerAdapter;
import com.flintcore.calendar_mysql_android.utils.Action;
import com.flintcore.calendar_mysql_android.utils.ActionListenerConsumer;
import com.flintcore.calendar_mysql_android.utils.database.request.DBCalendarRequest;

import java.util.Objects;

public class ListTab extends Tab {

    private TextView listTitle;
    private RecyclerView recyclerView;
    private final CalendarRecyclerAdapter.RecyclerType recyclerType;
    private CalendarRecyclerAdapter adapter;
    private Action<Integer> actionAssignTitle;
    //    private DBCalendarRequest calendarRequest;
    private ActionListenerConsumer actionListenerConsumer;

    public ListTab(CalendarRecyclerAdapter.RecyclerType type) {
        this.recyclerType = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_tab, container, false);

        this.listTitle = view.findViewById(R.id.reminder_list_title);

        this.recyclerView = view.findViewById(R.id.calendar_recycler_view);

        this.actionAssignTitle = (count) -> {
            if (count > 0) {
                this.listTitle.setText(R.string.reminders_txt);
            } else {
                this.listTitle.setText(R.string.no_reminders_txt);
            }
        };

        adapter = new CalendarRecyclerAdapter(getContext(), this.recyclerType);

        LinearLayoutManager layout = new LinearLayoutManager(this.getContext());
        layout.setOrientation(RecyclerView.VERTICAL);

        this.recyclerView.setLayoutManager(layout);
        this.recyclerView.setAdapter(adapter);

//       Set notifier to databaseRequest
        ActionListenerConsumer getDataChangedConsumer = this.getDataChangedConsumer();
        DBCalendarRequest.getInstance(this.getContext())
                .addListener(getDataChangedConsumer);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.getDataChangedConsumer().consume();
    }

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        DBCalendarRequest.getInstance(this.getContext())
//                .removeListener(this.actionListenerConsumer);
//    }

    @SuppressLint("NotifyDataSetChanged")
    public ActionListenerConsumer getDataChangedConsumer() {
        if (Objects.isNull(this.actionListenerConsumer)) {
            this.actionListenerConsumer = () -> {
                int adapterItemCount = this.adapter.getItemCount();
                this.actionAssignTitle.start(adapterItemCount);
                this.adapter.notifyDataSetChanged();
            };
        }
        return actionListenerConsumer;
    }

}