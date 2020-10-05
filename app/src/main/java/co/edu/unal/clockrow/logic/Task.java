package co.edu.unal.clockrow.logic;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Instant;
import java.util.Date;

import co.edu.unal.clockrow.util.TimestampConverter;

@Entity(tableName = "tasks")
public class Task {



    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    private String description;

    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "dueDate")
    public Date dueDate;

    public Task(@NonNull String name, String description, Date dueDate) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
    }

// Setters & Getters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }
}
