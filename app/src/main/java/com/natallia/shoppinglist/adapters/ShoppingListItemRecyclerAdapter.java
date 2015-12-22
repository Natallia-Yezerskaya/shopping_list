package com.natallia.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;

import java.util.List;

/**
 */
public class ShoppingListItemRecyclerAdapter extends RecyclerView.Adapter<ShoppingListItemRecyclerAdapter.ShoppingListItemHolder> {


    //будет хранить данные одного item
    public class ShoppingListItemHolder extends RecyclerView.ViewHolder {

        public TextView mTextView_name;
       // public TextView mTextView_number;
        public Button mButton_icon;
        public CheckBox mCheckBox;
        public ShoppingListItemHolder(View itemView) {
            super(itemView);
            mTextView_name = (TextView) itemView.findViewById(R.id.tv_item_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_icon = (Button) itemView.findViewById(R.id.btn_icon);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox_item);

        }
    }

    private List<ShoppingListItem> shoppingListItems;

    // конструктор
    public ShoppingListItemRecyclerAdapter(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    @Override
    public ShoppingListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // создаем новый view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item_element,parent,false);

        return new ShoppingListItemHolder(v);
    }

    @Override
    public int getItemCount() {
        return shoppingListItems.size();
    }

    @Override
    public void onBindViewHolder(ShoppingListItemHolder holder, final int position) {
            holder.mTextView_name.setText(shoppingListItems.get(position).getItem().getName());
           // holder.mTextView_number.setText(Integer.toString(position));
            holder.mButton_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // DataManager.deleteCategory(shoppingLists.get(position));
                    notifyDataSetChanged();
                }
            });
    }



}
