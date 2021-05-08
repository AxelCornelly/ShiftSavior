package edu.harvard.cs50.shiftsavior;

import android.app.AlarmManager;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;


import java.util.List;

@Dao
public interface TaskDao {
    // Query to insert a new task into database
    @Query("INSERT INTO tasks (TaskName, Location, AlarmInMillis) VALUES ('New Task', 'No Location', 1000)")
    void create();

    // Query to access entire table
    @Transaction
    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    // Query to update a task
    @Query("UPDATE tasks SET TaskName = :taskName, Location = :location, AlarmInMillis = :millis WHERE id = :id")
    void save(String taskName, String location, long millis, int id);

    // Query to delete a task given an id
    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

}
