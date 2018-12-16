package ada.osc.taskie.ui.categories.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.ui.categories.listener.CategoryClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<Category> categories;
    private CategoryClickListener categoryClickListener;

    private int lastCheckedPosition = -1;

    public CategoriesAdapter(CategoryClickListener listener) {
        categories = new ArrayList<>();
        categoryClickListener = listener;
    }

    public void updateCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public void setCheckedPosition(int position) {
        lastCheckedPosition = position;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category current = categories.get(position);

        holder.tvName.setText(current.getName());
        holder.rbCategory.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_item_category)
        TextView tvName;
        @BindView(R.id.radiobutton_item_category)
        RadioButton rbCategory;

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.radiobutton_item_category)
        public void onRadioClick() {
            int adapterPosition = getAdapterPosition();
            categoryClickListener.onCategoryChecked(categories.get(adapterPosition));
            lastCheckedPosition = adapterPosition;
            notifyDataSetChanged();
        }
    }
}
