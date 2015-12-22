package com.natallia.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.database.Category;
import com.natallia.shoppinglist.database.DataManager;

import java.util.List;
/**
 */
public class MyCategoryRecyclerAdapter extends RecyclerView.Adapter<MyCategoryRecyclerAdapter.CategoryHolder> {
    //private final Context context;
    //private final List<String> categories;

    //будет хранить данные одного item
    public class CategoryHolder extends RecyclerView.ViewHolder {

        public TextView mTextView_name;
        public TextView mTextView_number;
        public Button mButton_delete;
        public CategoryHolder(View itemView) {
            super(itemView);
            mTextView_name = (TextView) itemView.findViewById(R.id.tv_recycler_item);
            mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_delete = (Button) itemView.findViewById(R.id.button_delete);
        }
    }

    private List<Category> categories;

    // конструктор
    public MyCategoryRecyclerAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // создаем новый view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item,parent,false);

        return new CategoryHolder(v);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, final int position) {
            holder.mTextView_name.setText(categories.get(position).getName());
            holder.mTextView_number.setText(Integer.toString(position));
            holder.mButton_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManager.deleteCategory(categories.get(position));
                    notifyDataSetChanged();
                }
            });
    }



}
