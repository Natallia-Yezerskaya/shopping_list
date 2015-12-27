package com.natallia.shoppinglist.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;

import java.util.List;

/**
 */
/*
public class ShoppingListEditRecyclerAdapter extends RecyclerView.Adapter<ShoppingListEditRecyclerAdapter.ShoppingListEditHolder> {


    //будет хранить данные одного item
    public class ShoppingListEditHolder extends RecyclerView.ViewHolder {

        public TextView mTextView_name;
        public Button mButton_add;
        public RecyclerView mRecyclerView;
        public ShoppingListEditHolder(View itemView) {
            super(itemView);
            mTextView_name = (TextView) itemView.findViewById(R.id.tv_shopping_list_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_add = (Button) itemView.findViewById(R.id.btn_add);
            mRecyclerView =  (RecyclerView) itemView.findViewById(R.id.rv_edit_items);

        }
    }

    private List<ShoppingListItem> shoppingListItems;
    private ShoppingList shoppingList;
    private int shoppingListId;
    private Context context;

    // конструктор
    public ShoppingListEditRecyclerAdapter(Context context, int shoppingListId) {
        //this.shoppingListId = shoppingListId;
        this.context = context;
    }

    @Override
    public ShoppingListEditHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // создаем новый view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item_element,parent,false);

        return new ShoppingListEditHolder(v);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(ShoppingListEditHolder holder, final int position) {
            shoppingList = DataManager.getShoppingListById(shoppingListId);
            holder.mTextView_name.setText(shoppingList.getName());
            holder.mButton_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<ShoppingListItem> values = DataManager.getShoppingListItems(shoppingList);

        ShoppingListItemRecyclerAdapter adapter = new ShoppingListItemRecyclerAdapter(values);
        holder.mRecyclerView.setAdapter(adapter);
    }



}
*/