package co.edu.unal.clockrow.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.logic.Task;
import co.edu.unal.clockrow.logic.clock.TimeListener;
import co.edu.unal.clockrow.view.components.TaskListAdapter;
import co.edu.unal.clockrow.viewmodel.ClockViewModel;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView clockText;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button shiftButton;
    private ClockViewModel clockViewModel;
    private static final String TAG = "ClockActivity";

    private TextView taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        configView();
    }

    private void configView() {
        //view model
        clockViewModel = new ViewModelProvider(this).get(ClockViewModel.class);
        //Buttons
        clockText = findViewById(R.id.timeText);
        startButton = findViewById(R.id.startButtonClock);
        pauseButton = findViewById(R.id.puaseButtonClock);
        resetButton = findViewById(R.id.resetButtonClock);
        shiftButton = findViewById(R.id.shift);

        //listener
        startButton.setOnClickListener((View.OnClickListener) this);
        pauseButton.setOnClickListener((View.OnClickListener) this);
        resetButton.setOnClickListener((View.OnClickListener) this);
        shiftButton.setOnClickListener(this);
        //set Initial time
        clockText.setText(clockViewModel.getCurrentTime());

        //Task
        Task task = (Task) getIntent().getSerializableExtra(TaskListAdapter.TASK_EXTRA_ID);
        taskTitle = findViewById(R.id.task_title);
        taskTitle.setText(task.getName());




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButtonClock:
                clockViewModel.start(new TimeListener<String>() {
                    @Override
                    public void onTimerResponse(String response) {
                        clockText.setText(clockViewModel.getCurrentTime());
                    }

                    @Override
                    public void onTimerFinish(String response) {
                        clockText.setText(clockViewModel.getCurrentTime());
                        Log.d(TAG, clockViewModel.getCurrentTime());
                    }
                });
                break;
            case R.id.puaseButtonClock:
                clockViewModel.pause();
                clockText.setText(clockViewModel.getCurrentTime());
                break;
            case R.id.resetButtonClock:
                clockViewModel.reset();
                clockText.setText(clockViewModel.getCurrentTime());
                break;
            case R.id.shift:
                clockViewModel.shiftTime();
                clockText.setText(clockViewModel.getCurrentTime());
                break;
        }
    }

}