package co.edu.unal.clockrow.view.activity.Task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.edu.unal.clockrow.R;

public class AddTaskActivity extends AppCompatActivity {


    public static final String EXTRA_REPLY = "co.edu.unal.clockrow.REPLY";
    private EditText mEditTaskNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        mEditTaskNameView = findViewById(R.id.edit_task_name);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Intent replyIntent = new Intent();
                        if (TextUtils.isEmpty(mEditTaskNameView.getText())){
                            setResult(RESULT_CANCELED, replyIntent);
                        }
                        else
                        {
                            String name = mEditTaskNameView.getText().toString();
                            replyIntent.putExtra(EXTRA_REPLY, name);
                            setResult(RESULT_OK, replyIntent);
                        }
                        finish();
                    }
                }
        );

    }
}