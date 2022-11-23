package com.flintcore.calendar_mysql_android.adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.flintcore.calendar_mysql_android.FormCalendarActivity;
import com.flintcore.calendar_mysql_android.R;
import com.flintcore.calendar_mysql_android.databinding.CalendarItemPreviewBinding;
import com.flintcore.calendar_mysql_android.listeners.OnClickIntent;
import com.flintcore.calendar_mysql_android.models.Calendar;
import com.flintcore.calendar_mysql_android.utils.DateFormatter;
import com.flintcore.calendar_mysql_android.utils.database.request.DBCalendarRequest;

import java.time.LocalDate;

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.CalendarViewHolder> {

    private final DateFormatter dateFormatter;

    public enum RecyclerType {
        ALL_NOTES,
        TODAY_NOTES
    }

    private final RecyclerType type;
    private final DBCalendarRequest connection;

    public CalendarRecyclerAdapter(Context context, RecyclerType type) {
        this.type = type;
        this.connection = DBCalendarRequest.getInstance(context);
        dateFormatter = DateFormatter.getFormatter();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Calendar_item_view adapter
        View holder = CalendarItemPreviewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false).getRoot();

        return new CalendarViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        Calendar calendar;

        switch (this.type) {
            case TODAY_NOTES:
                calendar = this.connection.getObject(position, LocalDate.now());

                holder.vSubject.setText(calendar.getSubject());
                break;

            default:
                calendar = this.connection.getObject(position);
                holder.vSubject.setText(calendar.getSubject());

                String dateExpected = this.dateFormatter
                        .getStringLocalDateDominican(calendar.getDateExpected());

                holder.vDate.setText(dateExpected);
        }

        holder.itemView.setOnClickListener(
                OnClickIntent.startIntentListener(
                        FormCalendarActivity.createEditCalendarForm(
                                holder.itemView.getContext(),
                                calendar.getId()))
        );

    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public synchronized int getItemCount() {
        ;
        LocalDate localDate = setParamByType();
        return this.connection.getLength(this.type, localDate);
    }

    @Nullable
    private LocalDate setParamByType() {
        return this.type.equals(RecyclerType.TODAY_NOTES) ? LocalDate.now() : null;
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {

        protected TextView vSubject, vDate;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            this.vSubject = itemView.findViewById(R.id.subject_calendar);
            this.vDate = itemView.findViewById(R.id.date_calendar);

        }
    }
}
