package ada.osc.taskie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ada.osc.taskie.R;
import ada.osc.taskie.database.category.CategoryDaoImpl;
import ada.osc.taskie.database.category.SafeCategoryDaoImpl;
import ada.osc.taskie.database.task.SafeTaskDaoImpl;
import ada.osc.taskie.database.task.TaskDaoImpl;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Task.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SafeTaskDaoImpl getTaskDao() {
        SafeTaskDaoImpl taskDao;
        try {
            taskDao = DaoManager.createDao(connectionSource, Task.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskDao;
    }

    public SafeCategoryDaoImpl getCategoryDao() {
        SafeCategoryDaoImpl categoryDao;
        try {
            categoryDao = DaoManager.createDao(connectionSource, Category.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryDao;
    }
}
