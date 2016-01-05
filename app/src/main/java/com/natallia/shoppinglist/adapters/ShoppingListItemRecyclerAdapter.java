package com.natallia.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.Item;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;

import java.util.List;

/**
 */
public class ShoppingListItemRecyclerAdapter extends RecyclerView.Adapter<ShoppingListItemRecyclerAdapter.ShoppingListItemHolder> {
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    enum Direction {LEFT, RIGHT}

    //будет хранить данные одного item
    public class ShoppingListItemHolder extends RecyclerView.ViewHolder {

        public EditText mTextView_name;
       // public TextView mTextView_number;
        public Button mButton_icon;
        public CheckBox mCheckBox;
        public View mItemView;
        public ShoppingListItemHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView_name = (EditText) itemView.findViewById(R.id.tv_item_name);
           // mTextView_number = (TextView) itemView.findViewById(R.id.tv_number);
            mButton_icon = (Button) itemView.findViewById(R.id.btn_icon);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox_item);


        }
    }

    private List<ShoppingListItem> shoppingListItems;
    private boolean editMode;

    // конструктор
    public ShoppingListItemRecyclerAdapter(List<ShoppingListItem> shoppingListItems, boolean editMode) {
        this.shoppingListItems = shoppingListItems;
        this.editMode = editMode;
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
        final Item item = shoppingListItems.get(position).getItem();
            holder.mTextView_name.setText(item.getName());
            holder.mTextView_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    DataManager.setNameShoppingListItem(item, ((EditText) v).getText().toString());
                    //notifyDataSetChanged();
                }
            });
           // holder.mTextView_number.setText(Integer.toString(position));
            holder.mButton_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManager.deleteShoppingListItem(shoppingListItems.get(position));
                    notifyDataSetChanged();
                }
            });

        holder.mCheckBox.setChecked(shoppingListItems.get(position).isChecked());
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.toggleChecked(shoppingListItems.get(position));
                notifyDataSetChanged();
            }
        });


        if (editMode){
            holder.mButton_icon.setVisibility(View.VISIBLE);
        }
        else{
            holder.mButton_icon.setVisibility(View.GONE);
        }

        holder.mItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        historicX = event.getX();
                        historicY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX < -DELTA) {
                            Log.d("влево","влево");
                            return true;
                        } else if (event.getX() - historicX > DELTA) {
                            Log.d("вправо", "вправо");
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });





    }



}
