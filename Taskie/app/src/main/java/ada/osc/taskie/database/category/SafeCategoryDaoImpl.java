package ada.osc.taskie.database.category;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.model.Category;

public class SafeCategoryDaoImpl extends CategoryDaoImpl {
    protected SafeCategoryDaoImpl(Class<Category> dataClass) throws SQLException {
        super(dataClass);
    }

    protected SafeCategoryDaoImpl(ConnectionSource connectionSource, Class<Category> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    protected SafeCategoryDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Category> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public int create(Category data) {
        int rowsChanged = 0;
        try {
            rowsChanged = super.create(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowsChanged;
    }

    @Override
    public List<Category> queryForAll() {
        List<Category> result;

        try {
            result = super.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int getCategoryIndex(String categoryId) {
        int index = -1;
        try {
            index = super.getCategoryIndex(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }
}
