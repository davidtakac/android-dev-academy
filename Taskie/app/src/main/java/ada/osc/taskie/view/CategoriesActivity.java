package ada.osc.taskie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "category";

    private DatabaseHelper mDatabaseHelper = null;

    private CategoriesAdapter mCategoriesAdapter;

    @BindView(R.id.recycler_categories)
    RecyclerView mCategoriesRecycler;
    @BindView(R.id.edittext_categories_addCategory)
    EditText mAddCategoryEditText;
    @BindView(R.id.button_categories_addCategory)
    Button mAddCategoryButton;
    @BindView(R.id.button_categories_save)
    Button mSaveCategoriesButton;

    private Category mSelectedCategory = null;

    CategoryClickListener mCategoryClickListener = new CategoryClickListener() {
        @Override
        public void onCategoryChecked(Category category) {
            mSelectedCategory = category;
            Log.d("David", mSelectedCategory.toString());

        }
    };

    @OnClick(R.id.button_categories_addCategory)
    public void onAddClick() {
        if (mAddCategoryEditText.getText().toString().isEmpty()) {
            mAddCategoryEditText.setError(getString(R.string.errormsg_edittext_blankInput));
            return;
        }
        Category newCategory = new Category(mAddCategoryEditText.getText().toString());
        getHelper().getCategoryDao().create(newCategory);
        mAddCategoryEditText.setText(null);
        updateCategoriesDisplay();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        setUpRecyclerView();
        if (getIntent().hasExtra(NewTaskActivity.EXTRA_CATEGORYID)) {
            String previousCategoryId = getIntent().getStringExtra(NewTaskActivity.EXTRA_CATEGORYID);

            try {
                mCategoriesAdapter.setCheckedPosition(getHelper().getCategoryDao().getCategoryIndex(previousCategoryId));
                mSelectedCategory = getHelper().getCategoryDao().queryForId(previousCategoryId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateCategoriesDisplay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
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

        mCategoriesAdapter = new CategoriesAdapter(mCategoryClickListener);

        mCategoriesRecycler.setLayoutManager(layoutManager);
        mCategoriesRecycler.addItemDecoration(decoration);
        mCategoriesRecycler.setItemAnimator(animator);
        mCategoriesRecycler.setAdapter(mCategoriesAdapter);
    }

    @OnClick(R.id.button_categories_save)
    public void onSaveClick() {
        if (mSelectedCategory == null) {
            ToastUtil.shortToastWithString(this
                    , getResources().getString(R.string.errormsg_categories_noCategoriesSelected));
            return;
        }

        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.putExtra(EXTRA_CATEGORY, mSelectedCategory.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateCategoriesDisplay() {
        mCategoriesAdapter.updateCategories(getHelper().getCategoryDao().queryForAll());
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }
}
