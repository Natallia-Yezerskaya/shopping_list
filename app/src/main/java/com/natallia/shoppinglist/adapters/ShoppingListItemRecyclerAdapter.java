package com.natallia.shoppinglist.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.natallia.shoppinglist.MainActivity;
import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ShoppingListItemAdapterCallback;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.Item;
import com.natallia.shoppinglist.database.ShoppingListItem;
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
    private int mColorBackground;
    private ShoppingListItemAdapterCallback mShoppingListItemAdapterCallback;
    private int mShoppingListPosition;
    private Context mContext;
    private Activity mActivity;
    private TextView.OnEditorActionListener onEditorActionListener;

    public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        this.onEditorActionListener = onEditorActionListener;
    }

    //будет хранить данные одного item
    public class ShoppingListItemHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        public EditText mTextView_name;
       // public TextView mTextView_number;
        public Button mButton_icon;
        public CheckBox mCheckBox;
        public View mItemView;
        public EditText mNumberPicker;
        public Button mNumberPickerPlus;
        public Button mNumberPickerMinus;

        //public ClassLoader mOwnerRecyclerView;

        public ShoppingListItemHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView_name = (EditText) itemView.findViewById(R.id.tv_item_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_icon = (Button) itemView.findViewById(R.id.btn_icon);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox_item);
            mNumberPicker = (EditText) itemView.findViewById(R.id.numberPicker);
            mNumberPickerPlus = (Button) itemView.findViewById(R.id.btn_plus);
            mNumberPickerMinus = (Button) itemView.findViewById(R.id.btn_minus);

        }


        @Override
        public void onItemSelected() {
            Drawable background = itemView.getBackground();
            if (background instanceof ColorDrawable)
                mColorBackground = ((ColorDrawable) background).getColor();

            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(mColorBackground);
        }


    }



    // конструктор
    public ShoppingListItemRecyclerAdapter(List<ShoppingListItem> shoppingListItems, boolean editMode, OnStartDragListener dragStartListener, View mItemLayout, ShoppingListItemAdapterCallback mShoppingListItemAdapterCallback, int position, Context context,Activity activity) {

        this.shoppingListItems = shoppingListItems;
        this.editMode = editMode;
        mDragStartListener = dragStartListener;
        this.mItemLayout = mItemLayout;
        this.mShoppingListItemAdapterCallback = mShoppingListItemAdapterCallback;
        this.mShoppingListPosition = position;
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public ShoppingListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item_element,parent,false);
        return new ShoppingListItemHolder(v);
    }

    public void insertItem(List<ShoppingListItem> items) {
        shoppingListItems = items;
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return shoppingListItems.size();
    }

    @Override
    public void onBindViewHolder(final ShoppingListItemHolder holder, int position) {
        final ShoppingListItem shoppingListItem = shoppingListItems.get(position);
        final Item item = shoppingListItem.getItem();
        if (editMode && position == 0 && shoppingListItems.get(position).getItem().getName() == null) {
            final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            holder.mTextView_name.post(new Runnable() {
                @Override
                public void run() {
                    holder.mTextView_name.requestFocus();
                    imm.showSoftInput(holder.mTextView_name, 0);
                }
            });

        }

        if (shoppingListItem.isChecked()) {
            //holder.mItemView.setBackgroundColor(holder.mItemView.getResources().getColor(R.color.itemBackgroundChecked));
            holder.mItemView.getBackground().setColorFilter(holder.mItemView.getResources().getColor(R.color.itemBackgroundChecked),PorterDuff.Mode.MULTIPLY);
        }else {
            //holder.mItemView.setBackgroundColor(holder.mItemView.getResources().getColor(R.color.itemBackground));
            holder.mItemView.getBackground().setColorFilter(holder.mItemView.getResources().getColor(R.color.itemBackground), PorterDuff.Mode.MULTIPLY);
        }
        holder.mTextView_name.setText(item.getName());
        if (shoppingListItem.isChecked()) {
            holder.mTextView_name.setPaintFlags(holder.mTextView_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.mTextView_name.setPaintFlags(holder.mTextView_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG) );
        }
            holder.mTextView_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DataManager.setNameShoppingListItem(item, ((EditText) v).getText().toString());
                //notifyDataSetChanged();
            }
        });
        holder.mTextView_name.setOnEditorActionListener(onEditorActionListener);


        holder.mNumberPicker.setText(String.valueOf(shoppingListItem.getAmount()));
        holder.mNumberPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DataManager.setAmountShoppingListItem(shoppingListItem, Float.parseFloat(((EditText) v).getText().toString()));
            }
        });
        holder.mNumberPickerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = Float.parseFloat(holder.mNumberPicker.getText().toString());
                holder.mNumberPicker.setText(String.valueOf(value + 0.5f));
                DataManager.setAmountShoppingListItem(shoppingListItem, value + 0.5f);
            }
        });

        holder.mNumberPickerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = Float.parseFloat(holder.mNumberPicker.getText().toString());
                if (value != 0f) {
                    holder.mNumberPicker.setText(String.valueOf(value - 0.5f));
                    DataManager.setAmountShoppingListItem(shoppingListItem, value - 0.5f);
                }
            }
        });

        holder.mButton_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.deleteShoppingListItem(shoppingListItems.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
                }
            });
        holder.mButton_icon.setVisibility(View.GONE);

        holder.mCheckBox.setChecked(shoppingListItems.get(holder.getAdapterPosition()).isChecked());
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataManager.toggleChecked(shoppingListItems.get(holder.getAdapterPosition()));
                notifyItemChanged(holder.getAdapterPosition());
                if (mShoppingListItemAdapterCallback!=null) {
                    mShoppingListItemAdapterCallback.onItemChecked(mShoppingListPosition);
                }
            }

        });


        if (editMode){
           // holder.mButton_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.mButton_icon.setVisibility(View.GONE);
            holder.mNumberPickerMinus.setVisibility(View.GONE);
            holder.mNumberPickerPlus.setVisibility(View.GONE);
            holder.mTextView_name.setEnabled(false);
            holder.mNumberPicker.setEnabled(false);
            //holder.mNumberPicker.setTextColor();
            holder.mTextView_name.setCursorVisible(false);
            holder.mNumberPicker.setCursorVisible(false);

        }

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {//TODO
        DataManager.swapShoppingListItems(shoppingListItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }

    @Override
    public void onItemDismiss(int position) {//TODO
        DataManager.deleteShoppingListItem(shoppingListItems.get(position));
        notifyDataSetChanged();
    }

}
