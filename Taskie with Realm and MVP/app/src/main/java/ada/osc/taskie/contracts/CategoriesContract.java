package ada.osc.taskie.contracts;

import java.util.List;

import ada.osc.taskie.model.Category;

public interface CategoriesContract {
    interface View{
        void updateCategoriesDisplay(List<Category> categories);
        void setAddCategoryError(String errorMessage);
        void clearAddCategory();
        void setCheckmarkPosition(int position);
        void showToastWith(String str);
        void onCategorySaved(Category savedCategory);
    }

    interface Presenter{
        void setView(CategoriesContract.View categoriesView);
        void saveCategory(String categoryName);
        void setCheckedCategory(Category category);
        void loadCategories();
        void handlePreviouslySelectedCategory(String categoryId);
        void onSaveClick();
    }
}
