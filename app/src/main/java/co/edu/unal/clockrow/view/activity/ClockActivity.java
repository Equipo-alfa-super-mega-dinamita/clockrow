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
        //listener
        startButton.setOnClickListener((View.OnClickListener)this);
        pauseButton.setOnClickListener((View.OnClickListener)this);
        resetButton.setOnClickListener((View.OnClickListener)this);
        clockText.setText(clockViewModel.getTime());

        //Task
        Task task = (Task) getIntent().getSerializableExtra(TaskListAdapter.TASK_EXTRA_ID);
        taskTitle = findViewById(R.id.task_title);
        taskTitle.setText(task.getName());




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButtonClock:
                Log.d(TAG, "onClick: start");
                clockViewModel.start(new TimeListener<String>() {
                    @Override
                    public void onTimerResponse(String response) {
                        clockText.setText(clockViewModel.getTime());
                    }

                    @Override
                    public void onTimerFinish(String response) {
                        clockText.setText(clockViewModel.getTime());
                        Log.d(TAG,clockViewModel.getTime());
                    }
                });
                break;
            case R.id.puaseButtonClock:
                clockViewModel.pause();
                clockText.setText(clockViewModel.getTime());
                break;
            case R.id.resetButtonClock:
                clockViewModel.reset();
                clockText.setText(clockViewModel.getTime());
                break;
        }
    }
}