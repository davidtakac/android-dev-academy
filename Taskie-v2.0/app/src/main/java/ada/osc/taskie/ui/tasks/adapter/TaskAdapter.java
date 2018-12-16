package ada.osc.taskie.ui.tasks.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.tasks.listener.TaskClickListener;
import ada.osc.taskie.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private TaskClickListener taskClickListener;

    public TaskAdapter(TaskClickListener listener) {
        taskClickListener = listener;
        tasks = new ArrayList<>();
    }

    public void updateTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView, taskClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        Task current = tasks.get(position);
        holder.tvTitle.setText(current.getTitle());
        holder.tvDescription.setText(current.getDescription());

        int color = R.color.taskPriority_Unknown;
        switch (current.getPriority()) {
            case LOW:
                color = R.color.taskpriority_low;
                break;
            case MEDIUM:
                color = R.color.taskpriority_medium;
                break;
            case HIGH:
                color = R.color.taskpriority_high;
                break;
        }

        holder.ivPriority.setImageResource(color);

        DateFormat df = DateFormat.getDateInstance();
        holder.tvDeadline.setText(df.format(current.getDeadline()));

        holder.tbToggleCompleted.setChecked(current.isCompleted());

        holder.tvCategory.setText(current.getCategory() == null ? "" : current.getCategory().getName());

        //Dims the task when completed
        holder.itemView.setAlpha(current.isCompleted() ? 0.5f : 1f);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_task_title)
        TextView tvTitle;
        @BindView(R.id.textview_task_description)
        TextView tvDescription;
        @BindView(R.id.imageview_task_priority)
        ImageView ivPriority;
        @BindView(R.id.textview_task_deadline)
        TextView tvDeadline;
        @BindView(R.id.togglebtn_task_completed)
        ToggleButton tbToggleCompleted;
        @BindView(R.id.textview_task_category)
        TextView tvCategory;

        public TaskViewHolder(View itemView, TaskClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        public void onTaskClick() {
            taskClickListener.onClick(tasks.get(getAdapterPosition()));
        }

        @OnLongClick
        public boolean onTaskLongClick() {
            taskClickListener.onLongClick(tasks.get(getAdapterPosition()));
            return true;
        }

        @OnClick(R.id.togglebtn_task_completed)
        public void onToggleButtonClick() {
            taskClickListener.onToggleButtonClick(tasks.get(getAdapterPosition()));
        }

        @OnClick(R.id.imageview_task_priority)
        public void onPriorityClick() {
            taskClickListener.onPriorityClick(tasks.get(getAdapterPosition()));
        }
    }
}
