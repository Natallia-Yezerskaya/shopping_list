package com.natallia.shoppinglist.adapters;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.Item;
import com.natallia.shoppinglist.database.ShoppingListItem;
import com.natallia.shoppinglist.fragments.ShoppingListsFragment;
import com.natallia.shoppinglist.helper.ItemTouchHelperAdapter;
import com.natallia.shoppinglist.helper.ItemTouchHelperViewHolder;
import com.natallia.shoppinglist.helper.OnStartDragListener;
import java.util.List;

/**
 */
public class ShoppingListItemRecyclerAdapter extends RecyclerView.Adapter<ShoppingListItemRecyclerAdapter.ShoppingListItemHolder>
        implements ItemTouchHelperAdapter {

    private final OnStartDragListener mDragStartListener;
    private List<ShoppingListItem> shoppingListItems;
    private boolean editMode;
    public View mItemLayout;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {//TODO
        //Collections.swap(shoppingListItems, fromPosition, toPosition);
        DataManager.swapShoppingListItems(shoppingListItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        //notifyDataSetChanged();
        return true;

    }

    @Override
    public void onItemDismiss(int position) {//TODO
        DataManager.deleteShoppingListItem(shoppingListItems.get(position));
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    //будет хранить данные одного item
    public class ShoppingListItemHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        public EditText mTextView_name;
       // public TextView mTextView_number;
        public Button mButton_icon;
        public CheckBox mCheckBox;
        public View mItemView;

        //public ClassLoader mOwnerRecyclerView;

        public ShoppingListItemHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView_name = (EditText) itemView.findViewById(R.id.tv_item_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_icon = (Button) itemView.findViewById(R.id.btn_icon);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox_item);
        }


        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(0);//TODO здесь вернуть первоначальный цвет
        }
    }



    // конструктор
    public ShoppingListItemRecyclerAdapter(List<ShoppingListItem> shoppingListItems, boolean editMode, OnStartDragListener dragStartListener, View mItemLayout) {

        this.shoppingListItems = shoppingListItems;
        this.editMode = editMode;
        mDragStartListener = dragStartListener;
        this.mItemLayout = mItemLayout;
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
    public void onBindViewHolder(final ShoppingListItemHolder holder, final int position) {
        final Item item = shoppingListItems.get(position).getItem();
            holder.mTextView_name.setText(item.getName());
            holder.mTextView_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    DataManager.setNameShoppingListItem(item, ((EditText) v).getText().toString());
                    //notifyDataSetChanged();
                }
            });
        /*
        holder.mTextView_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Hide keyboard

                    if (getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        yourActivity.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
        */
       // holder.mTextView_name.requestFocus(0);
           // holder.mTextView_number.setText(Integer.toString(position));
            holder.mButton_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManager.deleteShoppingListItem(shoppingListItems.get(position));


                    notifyDataSetChanged();
                }
            });
        holder.mButton_icon.setVisibility(View.GONE);

        holder.mCheckBox.setChecked(shoppingListItems.get(position).isChecked());
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.toggleChecked(shoppingListItems.get(position));
                notifyDataSetChanged();
                //mItemLayout.notify();
               // notifyAll();

               // holder.getParent().notify();//TODO
                ((RecyclerView)v.getParent().getParent()).getAdapter().notifyDataSetChanged();
                //((RecyclerView)v.getParent().getParent()).mAdapter.notifyDataSetChanged();
                //final ShoppingListsFragment parent = (ShoppingListsFragment) v.getParent();
                //parent.notify();
            }

        });


        if (editMode){
           // holder.mButton_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.mButton_icon.setVisibility(View.GONE);
        }

        /*holder.mItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });*/

    }



}
