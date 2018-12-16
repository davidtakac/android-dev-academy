package ada.osc.taskie.database.task;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.database.task.TaskDao;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;


public class TaskDaoImpl extends BaseDaoImpl<Task, String> implements TaskDao {

    protected TaskDaoImpl(Class<Task> dataClass) throws SQLException {
        super(dataClass);
    }

    protected TaskDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Task> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    protected TaskDaoImpl(ConnectionSource connectionSource, Class<Task> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public List<Task> queryForNotCompleted() throws SQLException {
        QueryBuilder<Task, String> builder = queryBuilder();
        builder.where().eq(Task.COMPLETED_FIELD_NAME, false);

        return query(builder.prepare());
    }

    @Override
    public List<Task> queryForPriority(TaskPriority priority) throws SQLException {
        QueryBuilder<Task, String> builder = queryBuilder();
        builder.where().eq(Task.PRIORITY_FIELD_NAME, priority);

        return query(builder.prepare());
    }

    @Override
    /* Queries database according to picked attributes.
      If completed is true, the database returns tasks that are completed or otherwise.
      If its false, it only returns not completed tasks.
      If the priority is null, it returns tasks of all priorities. If the priority is specified,
      it returns tasks with the specified priority.
     */
    public List<Task> queryFor(boolean completed, TaskPriority priority) throws SQLException {
        QueryBuilder<Task, String> queryBuilder = queryBuilder();

        if (completed) {
            if (priority == null) {
                return queryForAll();
            }
            return queryForPriority(priority);
        }

        if (priority == null) {
            return queryForNotCompleted();
        }

        queryBuilder.where()
                .eq(Task.COMPLETED_FIELD_NAME, false)
                .and()
                .eq(Task.PRIORITY_FIELD_NAME, priority);

        return query(queryBuilder.prepare());
    }
}
