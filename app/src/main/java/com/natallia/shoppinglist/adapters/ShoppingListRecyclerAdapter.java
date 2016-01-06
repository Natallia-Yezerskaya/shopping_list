package com.natallia.shoppinglist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;
import com.natallia.shoppinglist.helper.OnStartDragListener;

import java.util.List;

import io.realm.Sort;

/**
 */
public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListHolder> implements OnStartDragListener {

    public OnShoppingListEdit onShoppingListEdit;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    //будет хранить данные одного item
    public class ShoppingListHolder extends RecyclerView.ViewHolder {

        public EditText mEditText_shopping_list_name;
        public Button mButton_expand;
        public Button mButton_edit;
        public RecyclerView mRecyclerView;
        public View mItemLayout;

        public ShoppingListHolder(View itemView) {
            super(itemView);
            mItemLayout = itemView.findViewById(R.id.linear_layout_shopping_list);
            mEditText_shopping_list_name = (EditText) itemView.findViewById(R.id.et_shopping_list_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_expand = (Button) itemView.findViewById(R.id.btn_shopping_list_expand);
            mRecyclerView =  (RecyclerView) itemView.findViewById(R.id.rv_shopping_list_items);
            mButton_edit = (Button) itemView.findViewById(R.id.btn_edit);
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
               if (hasFocus == false) {
                   DataManager.setNameShoppingList(shoppingList, ((EditText) v).getText().toString());
               }
           }
       });

        holder.mButton_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManager.toggleExpanded(shoppingList);
                    notifyDataSetChanged();
                }
            });
        holder.mButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onShoppingListEdit.onShoppingListEdit(shoppingList.getId());
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
       // holder.mRecyclerView.setNestedScrollingEnabled(false);

        List<ShoppingListItem> values = shoppingList.getItems().where().findAllSorted("id", Sort.DESCENDING);
               // DataManager.getShoppingListItems(shoppingList);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //holder.mRecyclerView.setMinimumHeight();

        if (shoppingList.isExpanded()) {
            params.height = 170 * values.size();//TODO придумать решение
        } else {
            params.height = 0;
        }

        if (DataManager.shoppingListIsChecked(shoppingList)) {
            holder.mItemLayout.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.mItemLayout.setBackgroundColor(Color.GREEN);//TODO поставить свой цвет
        }

        holder.mRecyclerView.setLayoutParams(params);
        ShoppingListItemRecyclerAdapter adapter = new ShoppingListItemRecyclerAdapter(values,false,this,holder.mItemLayout);
        holder.mRecyclerView.setAdapter(adapter);
    }

}
