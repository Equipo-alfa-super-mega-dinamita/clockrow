package co.edu.unal.clockrow.view.components;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.logic.Task;
import co.edu.unal.clockrow.view.activity.ClockActivity;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private static final String TAG = "TaskListAdapter";
    public static final String TASK_EXTRA_ID = "TaskListAdapter.TASK";

    private final LayoutInflater mInflater;
    private List<Task> mTasks;
    private Context context;

    public TaskListAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public TaskListAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskViewHolder holder, int position) {
        if (mTasks != null) {
            Task current = mTasks.get(position);
            holder.taskItemName.setText(current.getName());
            holder.taskItemDesc.setText(current.getDescription());
            holder.taskItemPriority.setText(current.getPriority());
            holder.taskRatingBarDifficulty.setRating(current.getDifficulty());


            FloatingActionButton fab = holder.taskFAB;
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(context, ClockActivity.class);
                intent.putExtra(TASK_EXTRA_ID, current);
                context.startActivity(intent);
            });

            Log.i(TAG, current.toString());
        } else {
            holder.taskItemName.setText(R.string.no_task);
            holder.taskItemDesc.setText(R.string.no_desc);
            holder.taskItemPriority.setText(R.string.no_priority);
            holder.taskRatingBarDifficulty.setRating(0);
        }
    }

    public void setTasks(List<Task> tasks){
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTasks != null) return mTasks.size();
        return 0;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskItemName;
        private final TextView taskItemDesc;
        private final TextView taskItemPriority;
        private final RatingBar taskRatingBarDifficulty;
        private final FloatingActionButton taskFAB;


        public TaskViewHolder(View itemView) {
            super(itemView);
            this.taskItemName = itemView.findViewById(R.id.task_name);
            this.taskItemDesc = itemView.findViewById(R.id.task_desc);
            this.taskItemPriority = itemView.findViewById(R.id.task_priority);
            this.taskRatingBarDifficulty = itemView.findViewById(R.id.task_rating_bar_difficulty);
            this.taskFAB = itemView.findViewById(R.id.workButton);

        }
    }
}

