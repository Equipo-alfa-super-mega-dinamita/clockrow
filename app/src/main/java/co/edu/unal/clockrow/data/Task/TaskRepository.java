package co.edu.unal.clockrow.data.Task;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import co.edu.unal.clockrow.logic.Task;

public class TaskRepository {


    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;


    TaskRepository(Application application){
        TaskRoomDB db = TaskRoomDB.getDataBase(application);
        mTaskDao = db.taskDao();
        mAllTasks = mTaskDao.getAlphabetizedTasks();
    }

    LiveData<List<Task>> getmAllTasks(){
        return mAllTasks;
    }

    void insert(Task task){
        TaskRoomDB.databaseWriteExecutor.execute(() -> {mTaskDao.insert(task);});
    }
}
