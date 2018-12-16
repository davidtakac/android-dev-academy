package ada.osc.taskie.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewTaskActivity extends AppCompatActivity {

    private static final String TAG = "David";
    private static final int REQUEST_CATEGORY = 10;
    public static final String EXTRA_CATEGORYID = "categoryid";

    @BindView(R.id.edittext_newtask_title)
    EditText mTitleEntry;
    @BindView(R.id.edittext_newtask_description)
    EditText mDescriptionEntry;
    @BindView(R.id.spinner_newtask_priority)
    Spinner mPriorityEntry;
    @BindView(R.id.calendarview_newtask_deadline)
    CalendarView mDeadlineEntry;
    @BindView(R.id.button_newtask_categories)
    Button mStartCategoriesButton;

    private DatabaseHelper mDatabaseHelper = null;

    private Category mSelectedCategory = null;

    private Task taskToUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        setUpSpinnerSource();

        mDeadlineEntry.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);

                mDeadlineEntry.setDate(c.getTimeInMillis());
            }
        });

        if (activityCalledByEditIntent()) {
            Task clickedTask = getHelper().getTaskDao().queryForId(getIntent().getStringExtra(TasksActivity.EXTRA_EDITTASK));
            taskToUpdate = clickedTask;
            //mSelectedCategory = clickedTask.getCategory();
            setViewsToValuesOf(clickedTask);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }

    private void setUpSpinnerSource() {
        mPriorityEntry.setAdapter(
                new ArrayAdapter<TaskPriority>(
                        this, android.R.layout.simple_list_item_1, TaskPriority.values()
                )
        );
        TaskPriority previousPriority = TaskPriority.valueOf(SharedPrefsUtil.getFromPrefs(this, Task.PRIORITY_FIELD_NAME, "LOW"));
        mPriorityEntry.setSelection(previousPriority.ordinal());
    }

    @OnClick(R.id.imagebutton_newtask_savetask)
    public void saveTask() {
        String title = mTitleEntry.getText().toString();
        String description = mDescriptionEntry.getText().toString();

        if (title.isEmpty()) {
            mTitleEntry.setError(getString(R.string.errormsg_edittext_blankInput));
            return;
        }
        if (description.isEmpty()) {
            mDescriptionEntry.setError(getString(R.string.errormsg_edittext_blankInput));
            return;
        }

        TaskPriority priority = (TaskPriority) mPriorityEntry.getSelectedItem();
        SharedPrefsUtil.saveToPrefs(this, Task.PRIORITY_FIELD_NAME, priority.toString());

        Date deadline = new Date(mDeadlineEntry.getDate());
        if (isDateBeforeToday(deadline)) {
            ToastUtil.shortToastWithString(this
                    , getString(R.string.errormsg_calendarview_invalidDeadline));
            return;
        }

        if(activityCalledByEditIntent()){
            taskToUpdate.updateTask(title, description, taskToUpdate.isCompleted(), priority, deadline);
            taskToUpdate.setCategory(mSelectedCategory);
            updateTask(taskToUpdate);
        } else {
            Task newTask = new Task(title, description, priority, deadline);
            newTask.setCategory(mSelectedCategory);
            createNewTask(newTask);
        }
        finish();
    }

    @OnClick(R.id.button_newtask_categories)
    public void startCategoriesActivity() {
        Intent intent = new Intent();
        intent.setClass(this, CategoriesActivity.class);
        if (mSelectedCategory != null) {
            intent.putExtra(EXTRA_CATEGORYID, mSelectedCategory.getId());
        }
        startActivityForResult(intent, REQUEST_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CATEGORY) {
            if (data.hasExtra(CategoriesActivity.EXTRA_CATEGORY)) {
                try {
                    mSelectedCategory = getHelper().getCategoryDao()
                            .queryForId(data.getStringExtra(CategoriesActivity.EXTRA_CATEGORY));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean activityCalledByEditIntent() {
        return (getIntent().getStringExtra(TasksActivity.EXTRA_EDITTASK) != null);
    }

    private void setViewsToValuesOf(Task task) {
        mTitleEntry.setText(task.getTitle());
        mDescriptionEntry.setText(task.getDescription());
        mDeadlineEntry.setDate(task.getDeadline().getTime());
        mPriorityEntry.setSelection(task.getPriority().ordinal());
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }

    private boolean isDateBeforeToday(Date date) {
        /*
         * DateFormat is needed for when the user picks the same date as today.
         * The before() method compares by milliseconds and will always say that
         * the selected date is before current date. But the format() method returns
         * a string based on the day, so by comparing their string representations,
         * we can conclude whether they are the same date or not.
         */
        DateFormat df = DateFormat.getDateInstance();
        return date.before(new Date()) && !df.format(date).equals(df.format(new Date()));
    }

    private void createNewTask(Task taskToCreate){
        getHelper().getTaskDao().create(taskToCreate);

        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call postNewTaskCall = apiService
                .postNewTask(SharedPrefsUtil.getFromPrefs(this
                        , SharedPrefsUtil.TOKEN, null), taskToCreate);

        postNewTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void updateTask(Task taskToUpdate){
        getHelper().getTaskDao().update(taskToUpdate);

        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call updateTaskCall = apiService
                .updateTask(SharedPrefsUtil.getFromPrefs(this
                        , SharedPrefsUtil.TOKEN, null), taskToUpdate);

        updateTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
