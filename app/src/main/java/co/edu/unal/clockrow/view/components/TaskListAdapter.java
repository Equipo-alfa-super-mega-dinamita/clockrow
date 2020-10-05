package co.edu.unal.clockrow.view.components;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.logic.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private static final String TAG = "TaskListAdapter";

    private final LayoutInflater mInflater;
    private List<Task> mTasks;

    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
            holder.taskItemView.setText(current.getName());
            Log.i(TAG, current.toString());

        } else {
            holder.taskItemView.setText(R.string.no_task);        }
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
        private final TextView taskItemView;
        public TaskViewHolder(View itemView) {
            super(itemView);
            this.taskItemView = itemView.findViewById(R.id.textTaskName);
        }
    }
}
