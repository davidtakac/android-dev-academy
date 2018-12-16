package ada.osc.taskie.presentation.categories;

import android.util.Log;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.contracts.CategoriesContract;
import ada.osc.taskie.database.category.CategoryDao;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.util.Constants;

public class CategoriesPresenter implements CategoriesContract.Presenter {

    private CategoriesContract.View view;
    private Category selectedCategory;
    private CategoryDao categoryDao;

    public CategoriesPresenter(CategoryDao categoryDao){
        this.categoryDao = categoryDao;
    }

    @Override
    public void setView(CategoriesContract.View categoriesView) {
        view = categoriesView;
    }

    @Override
    public void saveCategory(String categoryName) {
        if (categoryName.isEmpty()) {
            view.setAddCategoryError(getStringFromRes(R.string.errormsg_edittext_blankInput));
            return;
        }

        categoryDao.copyToRealm(new Category(categoryName));
        view.clearAddCategory();
    }

    @Override
    public void setCheckedCategory(Category category) {
        selectedCategory = category;
    }

    @Override
    public void loadCategories() {
        view.updateCategoriesDisplay(categoryDao.queryForAll());
    }

    @Override
    public void handlePreviouslySelectedCategory(String categoryId) {
        Log.d(Constants.LOG_TAG, "category id: " + categoryId);
        Category category = categoryDao.getCategoryById(categoryId);
        if(category == null || category.getName().isEmpty()){
            return;
        }

        setCheckedCategory(category);
        view.setCheckmarkPosition(categoryDao.getCategoryIndex(selectedCategory));
    }

    @Override
    public void onSaveClick() {
        if(selectedCategory == null){
            view.showToastWith(getStringFromRes(R.string.errormsg_categories_noCategoriesSelected));
            return;
        }

        view.onCategorySaved(selectedCategory);
    }

    private String getStringFromRes(int stringId){
        return App.getContext().getResources().getString(stringId);
    }
}
