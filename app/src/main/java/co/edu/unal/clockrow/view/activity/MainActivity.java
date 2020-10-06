package co.edu.unal.clockrow.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.logic.clock.Clock;
import co.edu.unal.clockrow.view.activity.Task.TaskActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MESSAGE = "co.edu.unal.clockrow.MESSAGE";
    private Button buttonClock;
    private Button buttonTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configView();
    }

    private void configView(){

        buttonClock = findViewById(R.id.buttonClock);
        buttonTask = findViewById(R.id.buttonTask);
        buttonClock.setOnClickListener(this);
        buttonTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case(R.id.buttonClock):
                intent = new Intent(this, ClockActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "holo");
                startActivity(intent);
                break;

            case(R.id.buttonTask):
                intent = new Intent(this, TaskActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "holo");
                startActivity(intent);
                break;
        }
    }
}