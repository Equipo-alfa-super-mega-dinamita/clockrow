package co.edu.unal.clockrow.view.activity.Task;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Date;

import co.edu.unal.clockrow.R;

public class AddTaskActivity extends AppCompatActivity {


    private Date datePicked;


    public static final String EXTRA_REPLY_NAME = "co.edu.unal.clockrow.REPLY_NAME";
    public static final String EXTRA_REPLY_DESCR = "co.edu.unal.clockrow.REPLY_DESCR";
    public static final String EXTRA_REPLY_DATE = "co.edu.unal.clockrow.REPLY_DATE";
    public static final String EXTRA_REPLY_PRIORITY = "co.edu.unal.clockrow.REPLY_PRIORITY";
    public static final String EXTRA_REPLY_DIFFICULTY = "co.edu.unal.clockrow.REPLY_DIFFICULTY";

    private EditText mEditTaskNameView;
    private EditText mEditTaskDescriptionView;
    private TextView DEBUG_DATE;

    private RatingBar mTaskDifficultyRating;

    private Spinner mTaskPrioritySpinner;
    private MaterialButton mButtonCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        mEditTaskNameView = findViewById(R.id.edit_task_name);
        mEditTaskDescriptionView = findViewById(R.id.edit_task_description);
        DEBUG_DATE = findViewById(R.id.DEBUG_DATE);

        mTaskDifficultyRating = findViewById(R.id.task_difficulty_rating);
        mTaskPrioritySpinner = findViewById(R.id.task_priority_spinner);

        mButtonCalendar = findViewById(R.id.task_calendar_button);

        mButtonCalendar.setOnClickListener(view -> {

            handleCalendarShow();
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskPrioritySpinner.setAdapter(adapter);


        final Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(view -> handleSaveTask(view));

    }

    private void handleCalendarShow() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DAY = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {

                    handleTimeShow(year, month, day);

                }, YEAR, MONTH, DAY);

        datePickerDialog.show();
    }

    private void handleTimeShow(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        String s = year + ":" + (month + 1) + ":" + day + " " + hour + ":" + minute;

                        calendar.set(year + 1900, month, day, hour, minute);
                        datePicked = calendar.getTime();

                        DEBUG_DATE.setText(s);
                    }
                }, HOUR, MINUTE, true);

        timePickerDialog.show();

    }


    private void handleSaveTask(View view){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditTaskNameView.getText()) ||
                TextUtils.isEmpty(mEditTaskDescriptionView.getText()) ||
                    datePicked == null)
        {
            setResult(RESULT_CANCELED, replyIntent);
        }
        else
        {
            String name = mEditTaskNameView.getText().toString();
            String description = mEditTaskDescriptionView.getText().toString();
            String priority = mTaskPrioritySpinner.getSelectedItem().toString();
            float difficulty = mTaskDifficultyRating.getRating();

            replyIntent.putExtra(EXTRA_REPLY_NAME, name);
            replyIntent.putExtra(EXTRA_REPLY_DESCR, description);
            replyIntent.putExtra(EXTRA_REPLY_DATE, datePicked);
            replyIntent.putExtra(EXTRA_REPLY_PRIORITY, priority);
            replyIntent.putExtra(EXTRA_REPLY_DIFFICULTY, difficulty);

            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
}

