package co.edu.unal.clockrow.data.Task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import co.edu.unal.clockrow.logic.Task



class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: TaskRepository = TaskRepository(application)
    val allTasks: LiveData<List<Task>>

    fun insert(task: Task?) {
        mRepository.insert(task)
    }



    init {
        allTasks = mRepository.getmAllTasks()
    }
}