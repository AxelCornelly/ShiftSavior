package edu.harvard.cs50.shiftsavior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static TaskDatabase database;
    public static TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks")
                .allowMainThreadQueries()
                .build();

        recyclerView = findViewById(R.id.recycler_view);
        emptyText = findViewById(R.id.empty_text);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addTaskButton = findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.taskDao().create();
                emptyText.setText("");
                adapter.reload();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(database.taskDao().getAllTasks().size() == 0) {
            emptyText.setText("Looks like you don't have any tasks!");
        }
        else {
            emptyText.setText("");
        }

        adapter.reload();
    }
}