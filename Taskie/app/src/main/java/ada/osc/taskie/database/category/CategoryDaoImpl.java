package ada.osc.taskie.database.category;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.database.category.CategoryDao;
import ada.osc.taskie.model.Category;

public class CategoryDaoImpl extends BaseDaoImpl<Category, String> implements CategoryDao {

    protected CategoryDaoImpl(Class<Category> dataClass) throws SQLException {
        super(dataClass);
    }

    protected CategoryDaoImpl(ConnectionSource connectionSource, Class<Category> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    protected CategoryDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Category> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    public int getCategoryIndex(String categoryId) throws SQLException {
        List<Category> categories = new ArrayList<>();
        categories = queryForAll();

        for (Category c : categories) {
            if (c.getId().equals(categoryId)) {
                return categories.indexOf(c);
            }
        }

        return -1;
    }

}
