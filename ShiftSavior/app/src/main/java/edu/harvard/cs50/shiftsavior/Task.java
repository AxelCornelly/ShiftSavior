package edu.harvard.cs50.shiftsavior;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "TaskName")
    public String taskName;

    @ColumnInfo(name = "Location")
    public String location;

    @ColumnInfo(name = "AlarmInMillis")
    public long millis;
}