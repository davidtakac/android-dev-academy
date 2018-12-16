package ada.osc.taskie.database.category;

import java.util.Collections;
import java.util.List;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.util.Constants;
import io.realm.Realm;

public class CategoryDaoImpl implements CategoryDao {

    private Realm db;

    public CategoryDaoImpl(Realm database){
        this.db = database;
    }

    @Override
    public void copyToRealm(final Category category) {
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                db.copyToRealm(category);
            }
        });
    }

    @Override
    public List<Category> queryForAll() {
        return db.where(Category.class).findAll();
    }

    @Override
    public int getCategoryIndex(Category category) {
        List<Category> categories = queryForAll();
        String categoryId = category.getId();

        for (Category c : categories) {
            if (c.getId().equals(categoryId)) {
                return categories.indexOf(c);
            }
        }

        return -1;
    }

    @Override
    public Category getCategoryById(String categoryId) {
        return db.where(Category.class).equalTo("id", categoryId).findFirst();
    }

    @Override
    public Category getNoneCategory() {
        return db.where(Category.class).equalTo("name", Constants.NONE_CATEGORY_NAME).findFirst();
    }

}
