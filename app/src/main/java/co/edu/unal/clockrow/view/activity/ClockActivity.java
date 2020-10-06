package co.edu.unal.clockrow.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.logic.Task;
import co.edu.unal.clockrow.logic.clock.Pomodoro;
import co.edu.unal.clockrow.logic.clock.TimeControlMethod;
import co.edu.unal.clockrow.logic.clock.TimeListener;
import co.edu.unal.clockrow.view.components.TaskListAdapter;
import co.edu.unal.clockrow.viewmodel.ClockViewModel;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView clockText;
    private TextView methodText;
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
        clockViewModel.setCtx(getApplicationContext());
        //Buttons & text
        clockText = findViewById(R.id.timeText);
        methodText = findViewById(R.id.methodText);
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
        methodText.setText(clockViewModel.getMethod().toString());

        //Task
        Task task = (Task) getIntent().getSerializableExtra(TaskListAdapter.TASK_EXTRA_ID);
        taskTitle = findViewById(R.id.task_title);
        taskTitle.setText(task.getName());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.method_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.method_pomododoro:
                clockViewModel.setMethod(TimeControlMethod.POMODORO);
                break;

            case R.id.method_saving:
                clockViewModel.setMethod(TimeControlMethod.SAVING_METHOD);
                break;

            case R.id.method_marathon:
                clockViewModel.setMethod(TimeControlMethod.MARATHON);
                break;

        }
        clockText.setText(clockViewModel.getCurrentTime());
        methodText.setText(clockViewModel.getMethod().toString());
        return true;
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