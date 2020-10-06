package co.edu.unal.clockrow.view.activity.Task;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.data.Task.TaskViewModel;
import co.edu.unal.clockrow.logic.Task;
import co.edu.unal.clockrow.view.components.TaskListAdapter;

public class TaskActivity extends AppCompatActivity {

    private TaskViewModel mTaskViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_task);
        final TaskListAdapter adapter = new TaskListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        mTaskViewModel.getAllTasks().observe(this, tasks -> adapter.setTasks(tasks));

        // Getting the fab
        FloatingActionButton fab = findViewById(R.id.fab_task);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Task task = new Task(Objects.requireNonNull(data.getStringExtra(AddTaskActivity.EXTRA_REPLY_NAME)),
                                data.getStringExtra(AddTaskActivity.EXTRA_REPLY_DESCR),
                    (Date) Objects.requireNonNull(data.getSerializableExtra(AddTaskActivity.EXTRA_REPLY_DATE)),
                    Calendar.getInstance().getTime(),
                    data.getFloatExtra(AddTaskActivity.EXTRA_REPLY_DIFFICULTY, 0f),
                    Objects.requireNonNull(data.getStringExtra(AddTaskActivity.EXTRA_REPLY_PRIORITY)));

            mTaskViewModel.insert(task);

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}