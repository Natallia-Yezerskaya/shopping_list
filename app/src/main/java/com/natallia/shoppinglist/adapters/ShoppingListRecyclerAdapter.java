package com.natallia.shoppinglist.adapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.UI.ShoppingListItemAdapterCallback;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;
import com.natallia.shoppinglist.fragments.ShoppingListRenameDialog;
import com.natallia.shoppinglist.helper.OnStartDragListener;

import java.util.List;

import io.realm.Sort;

/**
 * Адаптер для отображения всех списков покупок (используется в главном фрагменте)
 */
public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListHolder> implements OnStartDragListener {

    public OnShoppingListEdit onShoppingListEdit;
    private ItemTouchHelper mItemTouchHelper;
    private ShoppingListItemAdapterCallback mShoppingListItemAdapterCallback;
    private List<ShoppingList> shoppingLists;
    private Context context;

    @Override
    public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_element, parent, false);
        return new ShoppingListHolder(v);
    }

    public class ShoppingListHolder extends RecyclerView.ViewHolder {

        public TextView mTextView_shopping_list_name;
        public ImageButton mButton_favorite;
        public ImageButton mButton_edit;
        public RecyclerView mRecyclerView;
        public LinearLayout mItemLayout;
        private TextView mTotalChecked;
        public LinearLayout mLayoutName;

        public ShoppingListHolder(View itemView) {
            super(itemView);
            mItemLayout = (LinearLayout)itemView.findViewById(R.id.linear_layout_shopping_list);
            mLayoutName = (LinearLayout)itemView.findViewById(R.id.ll_shopping_list_name);
            mTextView_shopping_list_name = (TextView) itemView.findViewById(R.id.tv_shopping_list_name);
            mButton_favorite = (ImageButton) itemView.findViewById(R.id.btn_favorite);
            mRecyclerView =  (RecyclerView) itemView.findViewById(R.id.rv_shopping_list_items);
            mButton_edit = (ImageButton) itemView.findViewById(R.id.btn_edit);
            mTotalChecked = (TextView)itemView.findViewById(R.id.total_checked);
        }
    }


    public ShoppingListRecyclerAdapter(ShoppingListItemAdapterCallback mShoppingListItemAdapterCallback, Context context, List<ShoppingList> shoppingLists) {
        this.mShoppingListItemAdapterCallback = mShoppingListItemAdapterCallback;
        this.shoppingLists = shoppingLists;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final ShoppingListHolder holder, final int position) {
        final ShoppingList shoppingList = shoppingLists.get(position);

        holder.mTextView_shopping_list_name.setText(shoppingList.getName());
        holder.mLayoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.toggleExpanded(shoppingList);
                notifyDataSetChanged();
            }
        });
        holder.mLayoutName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final ShoppingListRenameDialog myDialogFragment = new ShoppingListRenameDialog();
                myDialogFragment.mOldName = holder.mTextView_shopping_list_name.getText().toString();
                myDialogFragment.show(((Activity) context).getFragmentManager(), "ShoppingListRenameDialog");
                myDialogFragment.mListener = new ShoppingListRenameDialog.ShoppingListRenameDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog, String text) {
                        if (!text.equals("")) {
                            holder.mTextView_shopping_list_name.setText(text);
                            DataManager.setNameShoppingList(shoppingList, holder.mTextView_shopping_list_name.getText().toString());
                        }
                    }
                };
                return false;
            }
        });
// устанавливаем background
        if (shoppingList.isExpanded()){
            holder.mItemLayout.setBackgroundResource(R.drawable.rectangle_rounded_some);
        } else {
            holder.mItemLayout.setBackgroundResource(R.drawable.rectangle_rounded_all);
        }
        RefreshTotalChecked(holder.mTotalChecked, holder.mTextView_shopping_list_name, holder.mItemLayout, position);

        if (shoppingList.isFavorite()){
            holder.mButton_favorite.setBackgroundResource(R.drawable.ic_action_favorite);
        }
        else{
            holder.mButton_favorite.setBackgroundResource(R.drawable.ic_action_favorite_out);
        }

        holder.mButton_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.toggleFavorite(shoppingList);
                notifyItemChanged(position);
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
        List<ShoppingListItem> values = shoppingList.getItems().where().findAllSorted("position", Sort.DESCENDING);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Resources r = context.getResources();
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 54, r.getDisplayMetrics());

        if (shoppingList.isExpanded()) {
            params.height = height * values.size();
        } else {
            params.height = 0;
        }
        holder.mRecyclerView.setLayoutParams(params);
        ShoppingListItemRecyclerAdapter adapter = new ShoppingListItemRecyclerAdapter(values,false,this,holder.mItemLayout, mShoppingListItemAdapterCallback,position,context,null);
        holder.mRecyclerView.setAdapter(adapter);
    }

    public void RefreshTotalChecked(TextView totalCheckedTextView, TextView tvName, LinearLayout layout, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        String text = DataManager.shoppingListGetChecked(shoppingList) + "/" + shoppingList.getItems().size();
        totalCheckedTextView.setText(text);

        boolean shoppingListIsChecked = DataManager.shoppingListIsChecked(shoppingList);
        if (shoppingListIsChecked){
            DataManager.setShoppingListIsChecked(shoppingList,true);
            layout.getBackground().setColorFilter(context.getResources().getColor(R.color.itemListBackgroundChecked), PorterDuff.Mode.MULTIPLY);
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            DataManager.setShoppingListIsChecked(shoppingList,false);
            layout.getBackground().setColorFilter(context.getResources().getColor(R.color.itemListBackground), PorterDuff.Mode.MULTIPLY);
            tvName.setPaintFlags(tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }



    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
