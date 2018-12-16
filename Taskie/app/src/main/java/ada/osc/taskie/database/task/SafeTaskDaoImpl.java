package ada.osc.taskie.database.task;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;

public class SafeTaskDaoImpl extends TaskDaoImpl {

    protected SafeTaskDaoImpl(Class<Task> dataClass) throws SQLException {
        super(dataClass);
    }

    protected SafeTaskDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Task> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    protected SafeTaskDaoImpl(ConnectionSource connectionSource, Class<Task> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public int create(Task data) {
        int rowsChanged = 0;
        try {
            rowsChanged = super.create(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowsChanged;
    }

    @Override
    public synchronized Task createIfNotExists(Task data) {
        Task task;
        try {
            task = super.createIfNotExists(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public int update(Task data) {
        int rowsUpdated = 0;
        try {
            rowsUpdated = super.update(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Task data) {
        int rowsDeleted = 0;
        try {
            rowsDeleted = super.delete(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowsDeleted;
    }

    @Override
    public List<Task> queryForAll() {
        List<Task> result;

        try {
            result = super.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Task queryForId(String s) {
        Task task;
        try {
            task = super.queryForId(s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public List<Task> queryFor(boolean completed, TaskPriority priority) {
        List<Task> result;

        try {
            result = super.queryFor(completed, priority);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Task> queryForNotCompleted() {
        List<Task> result;

        try {
            result = super.queryForNotCompleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Task> queryForPriority(TaskPriority priority) {
        List<Task> result;

        try {
            result = super.queryForPriority(priority);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
