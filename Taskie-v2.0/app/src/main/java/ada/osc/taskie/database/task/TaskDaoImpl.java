package ada.osc.taskie.database.task;

import java.util.Date;
import java.util.List;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.util.Constants;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;


public class TaskDaoImpl implements TaskDao {

    private Realm db;

    public TaskDaoImpl(Realm database){
        this.db = database;
    }

    @Override
    public void copyToRealm(final Task task) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                db.copyToRealm(task);
            }
        });
    }

    @Override
    public void copyToRealmIfNotExists(Task task) {
        if(!containsTask(task)){
            copyToRealm(task);
        }
    }

    @Override
    public void copyToRealmOrUpdate(final Task task) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                db.copyToRealmOrUpdate(task);
            }
        });
    }

    @Override
    public void deleteFromRealm(final Task task) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.deleteFromRealm();
            }
        });
    }

    @Override
    public List<Task> queryForAll() {
        return db.where(Task.class).findAll();
    }

    @Override
    public Task queryForId(String taskId) {
        return db.where(Task.class).equalTo("id", taskId).findFirst();
    }

    @Override
    public List<Task> queryFor(boolean includeCompleted, TaskPriority priority, boolean sortByPriority) {
        RealmQuery<Task> query = db.where(Task.class);

        if(priority != null){
            query.equalTo(Constants.PRIORITY_TASK_FIELD_NAME, priority.ordinal());
        }
        if(!includeCompleted){
            query.and().equalTo(Constants.COMPLETED_TASK_FIELD_NAME, false);
        }
        if(sortByPriority){
            query.sort(Constants.PRIORITY_TASK_FIELD_NAME, Sort.DESCENDING);
        }

        return query.findAll();
    }

    @Override
    public void editTaskTitle(final Task toEdit, final String newTitle) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.setTitle(newTitle);
            }
        });
    }

    @Override
    public void editTaskDescription(final Task toEdit, final String newDescription) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.setDescription(newDescription);
            }
        });
    }

    @Override
    public void editTaskPriority(final Task toEdit, final TaskPriority newPriority) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.setPriority(newPriority);
            }
        });
    }

    @Override
    public void editTaskDeadline(final Task toEdit, final Date newDeadline) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.setDeadline(newDeadline);
            }
        });
    }

    @Override
    public void editTaskCategory(final Task toEdit, final Category newCategory) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.setCategory(newCategory);
            }
        });
    }

    @Override
    public void toggleTaskCompleted(final Task toEdit) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.toggleCompleted();
            }
        });
    }

    @Override
    public void cycleTaskPriority(final Task toEdit) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                toEdit.cyclePriority();
            }
        });
    }


    private boolean containsTask(Task task){
        Task t = db.where(Task.class).equalTo("id", task.getId()).findFirst();
        return t != null;
    }
}
