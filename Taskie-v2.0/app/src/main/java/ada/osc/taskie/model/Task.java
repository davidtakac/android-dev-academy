package ada.osc.taskie.model;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ada.osc.taskie.util.Constants;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject implements Serializable {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String title;
    private String description;
    private boolean completed;
    private int priority;
    private Date deadline;

    private Category category;

    public Task() {
        title = "";
        description = "";
        completed = false;
        priority = 0;
        deadline = new Date();
        category = new Category();
    }

    public Task(String title, String description, TaskPriority priority, Date deadline) {
        this.title = title;
        this.description = description;
        completed = false;
        this.priority = priority.ordinal();
        this.deadline = deadline;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void toggleCompleted() {
        Log.d(Constants.LOG_TAG, "toggling in task");
        completed = !completed;
    }

    public TaskPriority getPriority() {
        return TaskPriority.values()[priority];
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority.ordinal();
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void cyclePriority() {
        TaskPriority newPriority = TaskPriority.LOW;
        switch (getPriority()) {
            case LOW:
                newPriority = TaskPriority.MEDIUM;
                break;
            case MEDIUM:
                newPriority = TaskPriority.HIGH;
                break;
            case HIGH:
                newPriority = TaskPriority.LOW;
                break;
        }

        this.setPriority(newPriority);
    }

    @Override
    public String toString() {
        return "Task{" +
                "Title = " + title + "\n" +
                "Category = " + category;
    }
}
