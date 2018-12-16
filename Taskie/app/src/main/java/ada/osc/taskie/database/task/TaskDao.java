package ada.osc.taskie.database.task;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;

public interface TaskDao extends Dao<Task, String> {
    List<Task> queryForNotCompleted() throws SQLException;
    List<Task> queryForPriority(TaskPriority priority) throws SQLException;
    List<Task> queryFor(boolean completed, TaskPriority priority) throws SQLException;
}
