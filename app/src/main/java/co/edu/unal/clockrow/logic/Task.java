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


    @NonNull
    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @NonNull
    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "creation_date")
    private Date creationDate;

    @NonNull
    @ColumnInfo(name = "difference")
    private Float difficulty;

    @NonNull
    @ColumnInfo(name = "priority")
    private String priority;


    public Task(@NonNull String name, String description, @NonNull Date dueDate, @NonNull Date creationDate, @NonNull Float difficulty, @NonNull String priority) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.creationDate = creationDate;
        this.difficulty = difficulty;
        this.priority = priority;
    }


    // Setters & Getters
    // --- // --- // --- //

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

    @NonNull
    public Float getDifficulty() { return difficulty; }
    public void setDifficulty(@NonNull Float difficulty) { this.difficulty = difficulty; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    @NonNull
    public String getPriority() { return priority; }
    public void setPriority(@NonNull String priority) { this.priority = priority;}



    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", creationDate=" + creationDate +
                ", difficulty=" + difficulty +
                ", priority='" + priority + '\'' +
                '}';
    }
}
