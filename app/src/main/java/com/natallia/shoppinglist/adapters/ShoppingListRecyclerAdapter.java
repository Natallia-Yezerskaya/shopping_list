package com.natallia.shoppinglist.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
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

        public EditText mEditText_shopping_list_name;
       // public TextView mTextView_number;
        public Button mButton_expand;
        public RecyclerView mRecyclerView;

        public ShoppingListItemRecyclerAdapter mAdapter;

        public ShoppingListHolder(View itemView) {
            super(itemView);
            mEditText_shopping_list_name = (EditText) itemView.findViewById(R.id.et_shopping_list_name);
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
    public void onBindViewHolder(final ShoppingListHolder holder, final int position) {
        final ShoppingList shoppingList = shoppingLists.get(position);

        holder.mEditText_shopping_list_name.setText(shoppingList.getName());
       holder.mEditText_shopping_list_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               DataManager.setNameShoppingList(shoppingList, ((EditText) v).getText().toString());
           }
       });
           // holder.mTextView_number.setText(Integer.toString(position));
            holder.mButton_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (shoppingLists.get(position).isExpanded()) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.height = 0;
                        holder.mRecyclerView.setLayoutParams(params);
                        DataManager.setExpanded(shoppingLists.get(position),false);
                        notifyDataSetChanged();
                    }
                    else {
                        DataManager.setExpanded(shoppingLists.get(position),true);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        holder.mRecyclerView.setLayoutParams(params);
                        notifyDataSetChanged();
                    }*/
                    DataManager.toggleExpanded(shoppingList);
                    notifyDataSetChanged();
                }
            });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
       // holder.mRecyclerView.setNestedScrollingEnabled(false);

        List<ShoppingListItem> values = DataManager.getShoppingListItems(shoppingList);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //holder.mRecyclerView.setMinimumHeight();

        if (shoppingList.isExpanded()) {
            params.height = 170 * values.size();//TODO придумать решение
        } else {
            params.height = 0;
        }


        holder.mRecyclerView.setLayoutParams(params);

        ShoppingListItemRecyclerAdapter adapter = new ShoppingListItemRecyclerAdapter(values);
        holder.mRecyclerView.setAdapter(adapter);
    }



}
