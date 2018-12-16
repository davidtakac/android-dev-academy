package ada.osc.taskie.database.category;

import java.util.List;

import ada.osc.taskie.model.Category;

public interface CategoryDao {
    void copyToRealm(Category category);
    List<Category> queryForAll();
    int getCategoryIndex(Category category);
    Category getCategoryById(String categoryId);

    Category getNoneCategory();
}
