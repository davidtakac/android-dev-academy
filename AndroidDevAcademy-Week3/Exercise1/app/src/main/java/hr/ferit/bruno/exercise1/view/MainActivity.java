package hr.ferit.bruno.exercise1.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.bruno.exercise1.R;
import hr.ferit.bruno.exercise1.TasksRepository;
import hr.ferit.bruno.exercise1.model.Task;

public class MainActivity extends AppCompatActivity {

    TasksRepository mRepository;

    @BindView(R.id.edittext_newtask_title)
    EditText mTitle;
    @BindView(R.id.edittext_newtask_summary)
    EditText mSummary;
    @BindView(R.id.edittext_newtask_importance)
    EditText mImportance;
    @BindView(R.id.textview_newtask_display)
    TextView mDisplayTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRepository = TasksRepository.getInstance();
    }

    @OnClick(R.id.button_newtask_save)
    public void storeTask(View view) {

        String title = mTitle.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(this, "Empty title!", Toast.LENGTH_SHORT).show();
            return;
        }
        String summary = mSummary.getText().toString();
        if (summary.isEmpty()) {
            Toast.makeText(this, "Empty summary!", Toast.LENGTH_SHORT).show();
            return;
        }
        String importance = mImportance.getText().toString();
        if (importance.isEmpty()) {
            Toast.makeText(this, "Empty importance!", Toast.LENGTH_SHORT).show();
            return;
        }
        Task toSave = new Task(Integer.parseInt(importance), title, summary);
        mRepository.save(toSave);

        clearAllEditTexts();

        displayTasks();
    }

    private void clearAllEditTexts() {
        mTitle.setText("");
        mSummary.setText("");
        mImportance.setText("");
    }

    private void displayTasks() {
        List<Task> tasks = mRepository.getTasks();

        String toDisplay = "";
        if (tasks.size() != 1) {
            toDisplay = mDisplayTask.getText().toString();
        }

        toDisplay += "Task #" + tasks.size() + "\n" + tasks.get(tasks.size() - 1) + "\n\n";
        mDisplayTask.setText(toDisplay);
    }
}
