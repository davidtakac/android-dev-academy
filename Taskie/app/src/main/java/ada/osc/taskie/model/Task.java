package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ada.osc.taskie.database.task.SafeTaskDaoImpl;
import ada.osc.taskie.database.task.TaskDaoImpl;

@DatabaseTable(tableName = Task.TABLE_NAME_TASKS, daoClass = SafeTaskDaoImpl.class)
public class Task implements Serializable {

    public static final String TABLE_NAME_TASKS = "tasks";
    public static final String COMPLETED_FIELD_NAME = "iscompleted";
    public static final String PRIORITY_FIELD_NAME = "priority";

    @Expose
    @SerializedName("id")
    @DatabaseField(id = true)
    private String mId = UUID.randomUUID().toString();

    @Expose
    @SerializedName("title")
    @DatabaseField
    private String mTitle;

    @Expose
    @SerializedName("content")
    @DatabaseField
    private String mDescription;

    @Expose
    @SerializedName("isCompleted")
    @DatabaseField(columnName = COMPLETED_FIELD_NAME)
    private boolean mCompleted = false;

    @Expose
    @SerializedName("taskPriority")
    @DatabaseField(columnName = PRIORITY_FIELD_NAME)
    private int mPriority;

    @Expose
    @SerializedName("dueDate")
    @DatabaseField
    private String mDeadline;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Category mCategory;

    //Required by ORMLite
    public Task() {

    }

    public Task(String title, String description, TaskPriority priority, Date deadline) {
        mTitle = title;
        mDescription = description;
        mCompleted = false;
        mPriority = priority.ordinal();
        mDeadline = DateFormat.getDateInstance().format(deadline);
    }

    public void setCategory(Category category){
        mCategory = category;
    }

    public Category getCategory(){
        return mCategory;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public void toggleCompleted() {
        mCompleted = !mCompleted;
    }

    public TaskPriority getPriority() {
        return TaskPriority.values()[mPriority];
    }

    public void setPriority(TaskPriority priority) {
        mPriority = priority.ordinal();
    }

    public Date getDeadline() {
        try {
            return DateFormat.getDateInstance().parse(mDeadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public void setDeadline(Date deadline) {
        mDeadline = DateFormat.getDateInstance().format(deadline);
    }

    public void cyclePriority() {
        TaskPriority newPriority = TaskPriority.LOW;
        switch (TaskPriority.values()[mPriority]) {
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

    public void updateTask(String title, String description, boolean isCompleted, TaskPriority priority, Date deadline) {
        mTitle = title;
        mDescription = description;
        mCompleted = isCompleted;
        mPriority = priority.ordinal();
        mDeadline = DateFormat.getDateInstance().format(deadline);
    }

    @Override
    public String toString() {
        return "Task{" +
                "Title = " + mTitle + "\n" +
                "Category = " + mCategory;
    }
}
