package edu.harvard.cs50.shiftsavior;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        LinearLayout containerView;
        TextView textView;
        SwitchCompat toggleSwitch;

        TaskViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.task_row);
            textView = view.findViewById(R.id.task_row_text);
            toggleSwitch = view.findViewById(R.id.alarm_switch);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task current = (Task) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), TaskActivity.class);
                    intent.putExtra("id", current.id);
                    intent.putExtra("TaskName", current.taskName);
                    intent.putExtra("Location", current.location);
                    intent.putExtra("AlarmInMillis", current.millis);

                    v.getContext().startActivity(intent);
                }
            });

            toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Task current = (Task) containerView.getTag();

                    // Create alarm
                    AlarmManager alarmManager = (AlarmManager)containerView.getContext().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(containerView.getContext(), AlertReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(containerView.getContext(), TaskActivity.broadCastCode, intent, 0);

                    if(isChecked) { // if on, enable alarm
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, current.millis, pendingIntent);
                        Toast.makeText(containerView.getContext(), "Alarm Enabled", Toast.LENGTH_LONG).show();
                    }
                    else { // if off, disable alarm
                        alarmManager.cancel(pendingIntent);
                        Toast.makeText(containerView.getContext(), "Alarm Disabled", Toast.LENGTH_LONG).show();
                    }
                }});
        }
    }

    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task current = tasks.get(position);

        DateFormat formatter = new SimpleDateFormat("H:mm");
        Date date = new Date(current.millis);

        holder.textView.setText(String.format("%s | %s @ %s",
                current.taskName,
                current.location,
                formatter.format(date)));
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {return tasks.size();}

    public void reload() {
        tasks = MainActivity.database.taskDao().getAllTasks();
        notifyDataSetChanged();
    }

}
