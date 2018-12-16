package ada.osc.taskie.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<Category> mCategories;
    private CategoryClickListener mListener;

    private int lastCheckedPosition = -1;

    public CategoriesAdapter(CategoryClickListener listener) {
        mCategories = new ArrayList<>();
        mListener = listener;
    }

    public void updateCategories(List<Category> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
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
        Category current = mCategories.get(position);

        holder.mNameTextView.setText(current.getName());

        holder.mCategoryRadioButton.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_item_category)
        TextView mNameTextView;
        @BindView(R.id.radiobutton_item_category)
        RadioButton mCategoryRadioButton;

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.radiobutton_item_category)
        public void onRadioClick() {
            int adapterPosition = getAdapterPosition();
            mListener.onCategoryChecked(mCategories.get(adapterPosition));
            lastCheckedPosition = adapterPosition;
            notifyDataSetChanged();
        }
    }
}
