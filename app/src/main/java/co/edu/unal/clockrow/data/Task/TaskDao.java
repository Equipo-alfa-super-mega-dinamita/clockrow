package co.edu.unal.clockrow.data.Task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import co.edu.unal.clockrow.logic.Task;

@Dao
public interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("DELETE FROM tasks")
    void deleteAll();

    @Query("SELECT * from tasks ORDER BY name ASC")
    LiveData<List<Task>> getAlphabetizedTasks();
}
