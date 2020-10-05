package co.edu.unal.clockrow.data.Task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import co.edu.unal.clockrow.logic.Task;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository mRepository;
    private LiveData<List<Task>> mAllTasks;


    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getmAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return mAllTasks;
    }

    public void insert (Task task) {
        mRepository.insert(task);
    }

}
