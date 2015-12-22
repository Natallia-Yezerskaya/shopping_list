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
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.adapters.ShoppingListRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;

import java.util.List;

import io.realm.Realm;


public class ShoppingListsItemFragment extends Fragment {


    private static String KEA_AAA = "sfafsafa";
    private static int KEY_number ;
    private int keyNumber ;
    public TextView mTextView;

    private Realm realm;

    private String aaa;

    private ActivityListener mActivityListener;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ShoppingListsItemFragment() {
        this.aaa = aaa;

    }

    public static ShoppingListsItemFragment getInstance (String aaa,Intent intent){
        ShoppingListsItemFragment fragment = new ShoppingListsItemFragment();
        intent.putExtra(KEA_AAA,aaa);
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
        View view = inflater.inflate(R.layout.shopping_list_item_element,container,false);

       // mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_shopping_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        List<ShoppingList> values = DataManager.getShoppingLists();  //TODO getShoppingLists





       // mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShoppingListRecyclerAdapter(getContext(),values); //TODO MyShoppingListsRecyclerAdapter
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

                Log.d("ShoppingList", "onOptionsItemSelected"); //TODO проверить работу кнопок в каждом фрагменте
                break;


        }
        return super.onOptionsItemSelected(item);


    }


}
