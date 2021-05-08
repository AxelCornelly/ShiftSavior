package edu.harvard.cs50.shiftsavior;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskActivity extends AppCompatActivity{
    private EditText taskName;
    private EditText taskLocation;
    private TimePicker timePicker;
    private int id;
    public static int broadCastCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskName = findViewById(R.id.task_name);
        taskLocation = findViewById(R.id.task_location);
        timePicker = findViewById(R.id.time_picker);


        String task = getIntent().getStringExtra("TaskName");
        String location = getIntent().getStringExtra("Location");
        boolean status = getIntent().getExtras().getBoolean("toggleButtonStatus");
        id = getIntent().getIntExtra("id",0);

        taskName.setText(task);
        taskLocation.setText(location);
        timePicker.setIs24HourView(false);

        // Functionality for Set Alarm button
        Button setAlarmButton = findViewById(R.id.set_alarm_button);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);

                long timeInMillis = calendar.getTimeInMillis();

                MainActivity.database.taskDao().save(taskName.getText().toString(), taskLocation.getText().toString(), timeInMillis, id);

                startAlarm(calendar);
                finish();
            }
        });

        // Functionality for Delete Alarm button
        Button deleteAlarmButton = findViewById(R.id.delete_alarm_button);
        deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm();
                MainActivity.database.taskDao().delete(id);
                // Checks to see if Task list is empty, if so, display empty message
                if(MainActivity.database.taskDao().getAllTasks().size() == 0)
                {
                    MainActivity.emptyText.setText("Looks like you don't have any tasks!");
                }

                finish();
            }
        });
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        broadCastCode++;
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, broadCastCode, intent,0);

        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, MainActivity.database.taskDao().getAllTasks().get(id - 1).millis, pendingIntent);
    }

    private void deleteAlarm() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        broadCastCode--;
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, broadCastCode, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Task Alarm Deleted!", Toast.LENGTH_LONG).show();
    }
}
