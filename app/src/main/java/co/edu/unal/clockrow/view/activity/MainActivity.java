package co.edu.unal.clockrow.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.view.activity.Task.TaskActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "co.edu.unal.clockrow.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void sendMessage(View view){
        Intent intent = new Intent(this, TaskActivity.class);

        intent.putExtra(EXTRA_MESSAGE, "holo");
        startActivity(intent);

    }
}