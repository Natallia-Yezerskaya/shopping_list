package com.natallia.shoppinglist.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.natallia.shoppinglist.MainActivity;
import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.database.Category;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;

import java.util.List;

/**
 */
public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListHolder> {
    //private final Context context;
    //private final List<String> categories;

    //будет хранить данные одного item
    public class ShoppingListHolder extends RecyclerView.ViewHolder {

        public TextView mTextView_name;
       // public TextView mTextView_number;
        public Button mButton_expand;
        public RecyclerView mRecyclerView;

        public ShoppingListItemRecyclerAdapter mAdapter;

        public ShoppingListHolder(View itemView) {
            super(itemView);
            mTextView_name = (TextView) itemView.findViewById(R.id.tv_shopping_list_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_expand = (Button) itemView.findViewById(R.id.btn_shopping_list_expand);
            mRecyclerView =  (RecyclerView) itemView.findViewById(R.id.rv_shopping_list_items);

        }
    }

    private List<ShoppingList> shoppingLists;
    private Context context;

    // конструктор
    public ShoppingListRecyclerAdapter(Context context, List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
        this.context = context;
    }

    @Override
    public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // создаем новый view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_element,parent,false);

        return new ShoppingListHolder(v);
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    @Override
    public void onBindViewHolder(ShoppingListHolder holder, final int position) {
            holder.mTextView_name.setText(shoppingLists.get(position).getName());
           // holder.mTextView_number.setText(Integer.toString(position));
            holder.mButton_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DataManager.deleteCategory(shoppingLists.get(position));
                    notifyDataSetChanged();
                }
            });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mRecyclerView.setNestedScrollingEnabled(false);



        List<ShoppingListItem> values = DataManager.getShoppingListItems(shoppingLists.get(position));

        holder.mRecyclerView.setMinimumHeight(180 * values.size());
        holder.mRecyclerView.setHasFixedSize(false);
        ShoppingListItemRecyclerAdapter adapter = new ShoppingListItemRecyclerAdapter(values); //TODO MyShoppingListsRecyclerAdapter
        holder.mRecyclerView.setAdapter(adapter);
    }



}
