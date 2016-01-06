package com.natallia.shoppinglist.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.SendSMS;
import com.natallia.shoppinglist.adapters.ShoppingListItemRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;
import com.natallia.shoppinglist.helper.ItemTouchHelperAdapter;
import com.natallia.shoppinglist.helper.OnStartDragListener;
import com.natallia.shoppinglist.helper.SimpleItemTouchHelperCallback;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;


public class ShoppingListsEditFragment extends Fragment implements OnStartDragListener {

    private int mShoppingListId;
    public SendSMS sendSMS;


    private ActivityListener mActivityListener;
    private View mItemLayout;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private Button mButton_add;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    public static ShoppingListsEditFragment getInstance (String aaa,Intent intent){
        ShoppingListsEditFragment fragment = new ShoppingListsEditFragment();
        fragment.mShoppingListId = intent.getIntExtra("ShoppingListId",0);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // прописываем layout фрагмента
        View view = inflater.inflate(R.layout.shopping_list_edit_fragment, container, false);

        if (mShoppingListId == 0){
            mShoppingListId = savedInstanceState.getInt("ShoppingListId",0);
        }
        final  ShoppingList mShoppingList = DataManager.getShoppingListById(mShoppingListId);

        mTextView = (TextView) view.findViewById(R.id.tv_shopping_list_name);
        mTextView.setText(mShoppingList.getName());
        mButton_add = (Button) view.findViewById(R.id.btn_add);
        mButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.createShoppingListItem(mShoppingList);
                mAdapter.notifyDataSetChanged();

            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_edit_items); //отображает все шопинг листы
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//TODO

      // int id = getActivity().getIntent().getIntExtra("ShoppingListId",0);

        List<ShoppingListItem> values = mShoppingList.getItems().where().findAllSorted("id", Sort.DESCENDING);
        mAdapter = new ShoppingListItemRecyclerAdapter(values,true,this, mItemLayout);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityListener){
            mActivityListener = (ActivityListener)context;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivityListener!=null) {mActivityListener.setTitle("Hello");}
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }//TODO разобраться с меню

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_action:
               // DataManager.createShoppingList();
                //mAdapter.notifyDataSetChanged();

                Log.d("editFragment", "onOptionsItemSelected"); //TODO проверить работу кнопок в каждом фрагменте
                break;
            case R.id.my_action1:
                DataManager.deleteShoppingList(DataManager.getShoppingListById(mShoppingListId));
                mAdapter.notifyDataSetChanged();
                getActivity().onBackPressed();
                Log.d("editFragment", "onOptionsItemSelectedShoppingList"); //TODO проверить работу кнопок в каждом фрагменте
                break;
            case R.id.send_sms:

                RealmList<ShoppingListItem> items = DataManager.getShoppingListById(mShoppingListId).getItems();
                String txtsms = "";
                int i = 0;
                while (i < items.size()) {
                    String  itemName = items.get(i).getItem().getName() == null? "": items.get(i).getItem().getName().toString();
                    txtsms+= itemName + ", ";
                    i++;
                }
                sendSMS.sendSMS(txtsms);
                Log.d("editFragment", "send sms");

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ShoppingListId",mShoppingListId);
    }



}
