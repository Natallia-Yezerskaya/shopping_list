package com.natallia.shoppinglist.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.natallia.shoppinglist.adapters.ShoppingListItemRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;

import java.util.List;

import io.realm.Realm;


public class ShoppingListsEditFragment extends Fragment {


    private static String KEA_AAA = "sfafsafa";
    private static int KEY_number ;
    private int keyNumber ;
    public EditText mEditText;
    private ShoppingList mShoppingList;
    private int mShoppingListId;

    private Realm realm;

    private String aaa;

    private ActivityListener mActivityListener;

    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private Button mButton_add;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ShoppingListsEditFragment() {
        this.aaa = aaa;

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

        aaa = getActivity().getIntent().getStringExtra(KEA_AAA);

        // if (KEY_number == 1){
        // прописываем layout фрагмента
        View view = inflater.inflate(R.layout.shopping_list_edit_fragment, container, false);

        mTextView = (TextView) view.findViewById(R.id.tv_shopping_list_name);
        final ShoppingList shoppingList = DataManager.getShoppingListById(mShoppingListId);
        mTextView.setText(shoppingList.getName());

        mButton_add = (Button) view.findViewById(R.id.btn_add);
        mButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.createShoppingListItem(shoppingList);
                mAdapter.notifyDataSetChanged();

            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_edit_items); //отображает все шопинг листы
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//TODO

      // int id = getActivity().getIntent().getIntExtra("ShoppingListId",0);


        List<ShoppingListItem> values = shoppingList.getItems();


        mAdapter = new ShoppingListItemRecyclerAdapter(values); //TODO MyShoppingListsRecyclerAdapter
        mRecyclerView.setAdapter(mAdapter);

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


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_action:
                DataManager.createShoppingList();
                mAdapter.notifyDataSetChanged();

                Log.d("ShoppingList", "onOptionsItemSelected"); //TODO проверить работу кнопок в каждом фрагменте
                break;


        }
        return super.onOptionsItemSelected(item);


    }


}
