package ada.osc.taskie.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.contracts.CategoriesContract;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.ui.categories.adapter.CategoriesAdapter;
import ada.osc.taskie.presentation.categories.CategoriesPresenter;
import ada.osc.taskie.ui.categories.listener.CategoryClickListener;
import ada.osc.taskie.ui.newtask.NewTaskActivity;
import ada.osc.taskie.util.Constants;
import ada.osc.taskie.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesActivity extends AppCompatActivity implements CategoriesContract.View {

    @BindView(R.id.recycler_categories)
    RecyclerView rvCategories;
    @BindView(R.id.edittext_categories_addCategory)
    EditText etAddCategory;
    @BindView(R.id.button_categories_addCategory)
    Button btnAddCategory;
    @BindView(R.id.button_categories_save)
    Button btnApplyCategories;

    private CategoriesContract.Presenter presenter;
    private CategoriesAdapter categoriesAdapter;

    CategoryClickListener categoryClickListener = new CategoryClickListener() {
        @Override
        public void onCategoryChecked(Category category) {
            presenter.setCheckedCategory(category);
        }
    };

    @OnClick(R.id.button_categories_addCategory)
    public void onAddClick() {
        presenter.saveCategory(etAddCategory.getText().toString());
        presenter.loadCategories();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        setUpRecyclerView();
        presenter = new CategoriesPresenter(App.getCategoryDao());
        presenter.setView(this);

        presenter.handlePreviouslySelectedCategory(getIntent().getStringExtra(Constants.EXTRA_CATEGORYID));
        presenter.loadCategories();
    }

    private void setUpRecyclerView() {

        int orientation = LinearLayoutManager.VERTICAL;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this,
                orientation,
                false
        );

        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(this, orientation);

        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

        categoriesAdapter = new CategoriesAdapter(categoryClickListener);

        rvCategories.setLayoutManager(layoutManager);
        rvCategories.addItemDecoration(decoration);
        rvCategories.setItemAnimator(animator);
        rvCategories.setAdapter(categoriesAdapter);
    }

    @OnClick(R.id.button_categories_save)
    public void onSaveClick() {
        presenter.onSaveClick();
    }

    @Override
    public void updateCategoriesDisplay(List<Category> categories) {
        categoriesAdapter.updateCategories(categories);
    }

    @Override
    public void setAddCategoryError(String errorMessage) {
        etAddCategory.setError(errorMessage);
    }

    @Override
    public void clearAddCategory() {
        etAddCategory.setText(null);
    }

    @Override
    public void setCheckmarkPosition(int position) {
        categoriesAdapter.setCheckedPosition(position);
    }

    @Override
    public void showToastWith(String str) {
        ToastUtil.shortToastWithString(this, str);
    }

    public void startNewTaskActivity(Category selectedCategory){
        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.putExtra(Constants.EXTRA_CATEGORY, selectedCategory.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCategorySaved(Category savedCategory) {
        startNewTaskActivity(savedCategory);
    }
}
