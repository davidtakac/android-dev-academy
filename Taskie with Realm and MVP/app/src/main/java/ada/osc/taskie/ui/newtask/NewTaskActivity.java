package ada.osc.taskie.ui.newtask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

import ada.osc.taskie.App;
import ada.osc.taskie.contracts.NewTaskContract;
import ada.osc.taskie.presentation.newtask.NewTaskPresenter;
import ada.osc.taskie.ui.categories.CategoriesActivity;
import ada.osc.taskie.util.Constants;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTaskActivity extends AppCompatActivity implements NewTaskContract.View {

    @BindView(R.id.edittext_newtask_title)
    EditText etTitleEntry;
    @BindView(R.id.edittext_newtask_description)
    EditText etDescriptionEntry;
    @BindView(R.id.spinner_newtask_priority)
    Spinner spnPriorityEntry;
    @BindView(R.id.calendarview_newtask_deadline)
    CalendarView cvDeadlineEntry;
    @BindView(R.id.button_newtask_categories)
    Button btnStartCategories;

    private NewTaskContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        setUpSpinnerSource();

        cvDeadlineEntry.setOnDateChangeListener(getOnDateChangeListener());

        presenter = new NewTaskPresenter(App.getTaskDao(), App.getCategoryDao(), this);
        presenter.setView(this);

        if (activityCalledByEditIntent()) {
            presenter.initializeToValuesOf(getIntent().getStringExtra(Constants.EXTRA_EDITTASK));
        } else {
            //when creating new task, set up the spinner source according to last selected priority.
            setSpinnerSelectionToSharedPrefs();
        }
    }

    private void setSpinnerSelectionToSharedPrefs(){
        TaskPriority previousPriority = TaskPriority.valueOf(SharedPrefsUtil.getFromPrefs(this, Constants.PRIORITY_TASK_FIELD_NAME, "LOW"));
        spnPriorityEntry.setSelection(previousPriority.ordinal());
    }

    @OnClick(R.id.imagebutton_newtask_savetask)
    public void onSaveClick() {
        presenter.saveTask();
    }

    @OnClick(R.id.button_newtask_categories)
    public void startCategoriesActivity() {
        Intent intent = new Intent();
        intent.setClass(this, CategoriesActivity.class);
        if (presenter.getSelectedCategory() != null) {
            intent.putExtra(Constants.EXTRA_CATEGORYID, presenter.getSelectedCategory().getId());
        }
        startActivityForResult(intent, Constants.REQUEST_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CATEGORY) {
            if (data.hasExtra(Constants.EXTRA_CATEGORY)) {
                String categoryId = data.getStringExtra(Constants.EXTRA_CATEGORY);
                presenter.setSelectedCategory(categoryId);
            }
        }
    }

    private boolean activityCalledByEditIntent() {
        return (getIntent().getStringExtra(Constants.EXTRA_EDITTASK) != null);
    }

    @Override
    public void showToastWithString(String str) {
        ToastUtil.shortToastWithString(this, str);
    }

    @Override
    public void setViewsToValuesOf(Task task) {
        etTitleEntry.setText(task.getTitle());
        etDescriptionEntry.setText(task.getDescription());
        cvDeadlineEntry.setDate(task.getDeadline().getTime());
        spnPriorityEntry.setSelection(task.getPriority().ordinal());
    }

    @Override
    public String getTitleEntry() {
        return etTitleEntry.getText().toString();
    }

    @Override
    public String getDescriptionEntry() {
        return etDescriptionEntry.getText().toString();
    }

    @Override
    public TaskPriority getPriorityEntry() {
        return (TaskPriority) spnPriorityEntry.getSelectedItem();
    }

    @Override
    public Date getDateEntry() {
        return new Date(cvDeadlineEntry.getDate());
    }

    @Override
    public void setTitleError(String errorMessage) {
        etTitleEntry.setError(errorMessage);
    }

    @Override
    public void setDescriptionError(String errorMessage) {
        etDescriptionEntry.setError(errorMessage);
    }

    @Override
    public void onTaskSaved() {
        finish();
    }

    public CalendarView.OnDateChangeListener getOnDateChangeListener(){
        return new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);

                cvDeadlineEntry.setDate(c.getTimeInMillis());
            }
        };
    }

    private void setUpSpinnerSource() {
        spnPriorityEntry.setAdapter(
                new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_1, TaskPriority.values()
                )
        );
    }
}
