package com.natallia.shoppinglist.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.UI.ShoppingListItemAdapterCallback;
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
    private ShoppingListItemAdapterCallback mShoppingListItemAdapterCallback;

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
        public LinearLayout mItemLayout;
        private TextView mTotalChecked;


        public ShoppingListHolder(View itemView) {
            super(itemView);
            mItemLayout = (LinearLayout)itemView.findViewById(R.id.linear_layout_shopping_list);
            mEditText_shopping_list_name = (EditText) itemView.findViewById(R.id.et_shopping_list_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_expand = (Button) itemView.findViewById(R.id.btn_shopping_list_expand);
            mRecyclerView =  (RecyclerView) itemView.findViewById(R.id.rv_shopping_list_items);
            mButton_edit = (Button) itemView.findViewById(R.id.btn_edit);
            mTotalChecked = (TextView)itemView.findViewById(R.id.total_checked);
        }
    }
    private List<ShoppingList> shoppingLists;
    private Context context;

    // конструктор
    public ShoppingListRecyclerAdapter(ShoppingListItemAdapterCallback mShoppingListItemAdapterCallback, Context context, List<ShoppingList> shoppingLists) {
        this.mShoppingListItemAdapterCallback = mShoppingListItemAdapterCallback;
        this.shoppingLists = shoppingLists;
        this.context = context;
    }

    public void RefreshTotalChecked(TextView totalCheckedTextView, LinearLayout layout, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        String text = DataManager.shoppingListGetChecked(shoppingList) + "/" + shoppingList.getItems().size();
        totalCheckedTextView.setText(text);
        boolean shoppingListIsChecked = DataManager.shoppingListIsChecked(shoppingList);
        if (shoppingListIsChecked){
            layout.getBackground().setColorFilter(context.getResources().getColor(R.color.itemListBackgroundChecked), PorterDuff.Mode.MULTIPLY);
            //layout.setBackgroundColor(context.getResources().getColor(R.color.itemListBackgroundChecked));
        } else {
            layout.getBackground().setColorFilter(context.getResources().getColor(R.color.itemListBackground), PorterDuff.Mode.MULTIPLY);

            //layout.setBackgroundColor(context.getResources().getColor(R.color.itemListBackground));
        }
    }


    @Override
    public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // создаем новый view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_element, parent, false);
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

        RefreshTotalChecked(holder.mTotalChecked, holder.mItemLayout, position);

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
                //DataManager.createShoppingListItem(shoppingList);

                onShoppingListEdit.onShoppingListEdit(shoppingList.getId());
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
       // holder.mRecyclerView.setNestedScrollingEnabled(false);
        //holder.mRecyclerView.setHasFixedSize(true);
        List<ShoppingListItem> values = shoppingList.getItems().where().findAllSorted("position", Sort.DESCENDING);
               // DataManager.getShoppingListItems(shoppingList);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //holder.mRecyclerView.setMinimumHeight();

        DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //int height = getDPI(64* values.size(), metrics);
        Resources r = context.getResources();
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, r.getDisplayMetrics());

        if (shoppingList.isExpanded()) {
            //params.height = (64* values.size());//TODO придумать решение
            params.height = height * values.size();
        } else {
            params.height = 0;
        }

        holder.mRecyclerView.setLayoutParams(params);
        ShoppingListItemRecyclerAdapter adapter = new ShoppingListItemRecyclerAdapter(values,false,this,holder.mItemLayout, mShoppingListItemAdapterCallback,position,context,null);
        holder.mRecyclerView.setAdapter(adapter);
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
